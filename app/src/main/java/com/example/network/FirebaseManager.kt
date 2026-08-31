package com.example.network

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

/**
 * FirebaseManager — Production backend wiring for Soulmate Matrimony.
 *
 * 1. Firebase Authentication
 *    - Phone Auth (SMS OTP) for login & destructive-account-action verification
 *    - Email Link verification ("email OTP")
 *    - Google Sign-In via Credential Manager + Google ID Token exchange
 * 2. Firebase Realtime Database (soulmate-a511d-default-rtdb.asia-southeast1...)
 *    - users, profiles, privacy_settings, notifications, interests,
 *      chats/{thread}/messages, subscriptions, transactions, verifications,
 *      reports, blocks, support_tickets, success_stories
 * 3. Firebase Cloud Storage (soulmate-a511d.firebasestorage.app)
 *    - profile_photos, govt_id_proofs, chat_media
 *
 * All write operations degrade gracefully: callers catch exceptions and fall
 * back to local demo state so the app remains usable offline / pre-provision.
 */
object FirebaseManager {

    private const val TAG = "FirebaseManager"

    // Google OAuth Web Client ID (client_type 3 in google-services.json).
    // The google-services plugin also generates `default_web_client_id`;
    // we resolve it dynamically and fall back to this constant.
    private const val FALLBACK_WEB_CLIENT_ID =
        "288850011637-3lh2kjika6tqlv5icnm896ojde4on7av.apps.googleusercontent.com"

    // Realtime Database node names
    const val NODE_USERS = "users"
    const val NODE_PROFILES = "profiles"
    const val NODE_PRIVACY = "privacy_settings"
    const val NODE_NOTIFICATIONS = "notifications"
    const val NODE_INTERESTS = "interests"
    const val NODE_CHATS = "chats"
    const val NODE_MESSAGES = "messages"
    const val NODE_SUBSCRIPTIONS = "subscriptions"
    const val NODE_TRANSACTIONS = "transactions"
    const val NODE_VERIFICATIONS = "verifications"
    const val NODE_REPORTS = "reports"
    const val NODE_BLOCKS = "blocks"
    const val NODE_SUPPORT_TICKETS = "support_tickets"
    const val NODE_SUCCESS_STORIES = "success_stories"

    // Storage paths
    const val STORAGE_PHOTOS_PATH = "profile_photos"
    const val STORAGE_GOVT_IDS_PATH = "govt_id_proofs"
    const val STORAGE_CHAT_MEDIA_PATH = "chat_media"

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val database: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance().apply { setPersistenceEnabled(true) }
    }
    private val storage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }

    // ---------------- Session / user state ----------------

    val currentUid: String? get() = auth.currentUser?.uid
    val currentUser: FirebaseUser? get() = auth.currentUser
    fun isSignedIn(): Boolean = auth.currentUser != null
    val isEmailVerified: Boolean get() = auth.currentUser?.isEmailVerified ?: false
    val currentUserPhone: String? get() = auth.currentUser?.phoneNumber

    fun serverClientId(context: Context): String {
        val resId = context.resources.getIdentifier(
            "default_web_client_id", "string", context.packageName
        )
        return if (resId != 0) context.getString(resId) else FALLBACK_WEB_CLIENT_ID
    }

    fun signOut() {
        auth.signOut()
        Log.d(TAG, "User signed out")
    }

    // ---------------- Phone Auth (SMS OTP) ----------------

    /**
     * Sends an SMS OTP to [phoneNumber]. Reports back through callbacks:
     *  - [onCodeSent]: verificationId needed later for manual code entry
     *  - [onAutoVerified]: instant SMS-retention verification, no code needed
     *  - [onFailed]: error message for the user
     */
    fun sendPhoneOtp(
        activity: Activity,
        phoneNumber: String,
        onCodeSent: (String) -> Unit,
        onAutoVerified: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(TAG, "Phone auto-verified (SMS retention)")
                signInWithPhoneCredential(credential, { onAutoVerified() }, onFailed)
            }

            override fun onVerificationFailed(e: com.google.firebase.FirebaseException) {
                Log.e(TAG, "Phone verification failed: ${e.message}")
                onFailed(e.localizedMessage ?: "Verification failed. Check your number and connection.")
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d(TAG, "OTP code sent, verificationId received")
                onCodeSent(verificationId)
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    /** Signs in using the SMS code the user typed. */
    fun verifyPhoneOtp(
        verificationId: String,
        code: String,
        onSuccess: (FirebaseUser) -> Unit,
        onFailed: (String) -> Unit
    ) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneCredential(credential, { onSuccess(auth.currentUser!!) }, onFailed)
    }

    private fun signInWithPhoneCredential(
        credential: PhoneAuthCredential,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        auth.signInWithCredential(credential)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e ->
                onFailed(e.localizedMessage ?: "Invalid OTP code. Please try again.")
            }
    }

    // ---------------- Email Link ("email OTP") verification ----------------

    /** Adds/updates the user's e-mail and triggers Firebase's verification link. */
    fun updateUserEmail(
        email: String,
        onResult: (success: Boolean, message: String) -> Unit
    ) {
        val user = auth.currentUser
        if (user == null) {
            onResult(false, "Not signed in")
            return
        }
        user.verifyBeforeUpdateEmail(email)
            .addOnSuccessListener {
                onResult(true, "Verification link sent to $email. Tap it to confirm.")
            }
            .addOnFailureListener { e ->
                // Older path: update then send verification separately
                user.updateEmail(email)
                    .addOnSuccessListener {
                        user.sendEmailVerification()
                            .addOnSuccessListener {
                                onResult(true, "Verification link sent to $email.")
                            }
                            .addOnFailureListener { e2 ->
                                onResult(false, e2.localizedMessage ?: "Could not send verification e-mail.")
                            }
                    }
                    .addOnFailureListener { e3 ->
                        onResult(false, e3.localizedMessage ?: "Could not update e-mail.")
                    }
            }
    }

    fun sendEmailVerification(onResult: (Boolean, String) -> Unit) {
        val user = auth.currentUser
        if (user == null) {
            onResult(false, "Not signed in")
            return
        }
        user.sendEmailVerification()
            .addOnSuccessListener { onResult(true, "Verification e-mail sent to ${user.email}.") }
            .addOnFailureListener { e -> onResult(false, e.localizedMessage ?: "Could not send e-mail.") }
    }

    /** Reloads the Firebase user so isEmailVerified reflects the latest state. */
    fun reloadUser(onResult: (Boolean, String) -> Unit) {
        val user = auth.currentUser ?: run {
            onResult(false, "Not signed in")
            return
        }
        user.reload()
            .addOnSuccessListener { onResult(true, if (user.isEmailVerified) "VERIFIED" else "PENDING") }
            .addOnFailureListener { e -> onResult(false, e.localizedMessage ?: "Reload failed") }
    }

    // ---------------- Google Sign-In (Credential Manager) ----------------

    /**
     * Exchanges a Google ID Token (obtained by Credential Manager in the UI layer)
     * for a Firebase session. Returns the signed-in FirebaseUser.
     */
    suspend fun signInWithGoogleIdToken(idToken: String): FirebaseUser {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(firebaseCredential).await()
        return result.user ?: throw IllegalStateException("Google sign-in returned no user")
    }

    // ---------------- Destructive actions ----------------

    /** Permanently deletes the Firebase Auth account. Requires a fresh login (OTP). */
    suspend fun deleteAuthAccount(): Result<Unit> = try {
        auth.currentUser?.delete()?.await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // ---------------- Users & Profiles ----------------

    suspend fun upsertUser(uid: String, map: Map<String, Any>) {
        database.getReference(NODE_USERS).child(uid).updateChildren(map).await()
    }

    suspend fun fetchUser(uid: String): Map<String, Any?>? {
        val snap = database.getReference(NODE_USERS).child(uid).get().await()
        return snap.value as? Map<String, Any?>
    }

    suspend fun setUserStatus(uid: String, status: String) {
        database.getReference(NODE_USERS).child(uid).child("status").setValue(status).await()
    }

    suspend fun saveProfile(uid: String, map: Map<String, Any>) {
        database.getReference(NODE_PROFILES).child(uid).setValue(map).await()
    }

    suspend fun fetchProfile(uid: String): Map<String, Any?>? {
        val snap = database.getReference(NODE_PROFILES).child(uid).get().await()
        return snap.value as? Map<String, Any?>
    }

    suspend fun savePrivacySettings(uid: String, map: Map<String, Any>) {
        database.getReference(NODE_PRIVACY).child(uid).setValue(map).await()
    }

    suspend fun fetchPrivacySettings(uid: String): Map<String, Any?>? {
        val snap = database.getReference(NODE_PRIVACY).child(uid).get().await()
        return snap.value as? Map<String, Any?>
    }

    // ---------------- Notifications ----------------

    suspend fun pushNotification(uid: String, map: Map<String, Any>) {
        val ref = database.getReference(NODE_NOTIFICATIONS).child(uid).push()
        ref.setValue(map + mapOf("id" to ref.key)).await()
    }

    fun listenNotifications(uid: String): Flow<List<Map<String, Any?>>> = callbackFlow {
        val ref = database.getReference(NODE_NOTIFICATIONS).child(uid).limitToLast(50)
        val listener = ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children
                    .mapNotNull { it.value as? Map<String, Any?> }
                    .sortedByDescending { (it["createdAt"] as? Long) ?: 0L }
                trySend(items)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        })
        awaitClose { ref.removeEventListener(listener) }
    }

    suspend fun markNotificationsRead(uid: String) {
        val ref = database.getReference(NODE_NOTIFICATIONS).child(uid)
        val snap = ref.get().await()
        snap.children.forEach { child ->
            if (child.child("isRead").value == false) {
                child.ref.child("isRead").setValue(true)
            }
        }
    }

    // ---------------- Interests ----------------

    suspend fun sendInterest(fromUid: String, toUid: String, note: String) {
        val ref = database.getReference(NODE_INTERESTS).push()
        ref.setValue(
            mapOf(
                "id" to ref.key,
                "fromUid" to fromUid,
                "toUid" to toUid,
                "note" to note,
                "status" to "SENT",
                "createdAt" to System.currentTimeMillis()
            )
        ).await()
    }

    // ---------------- Real-time Chat ----------------

    /** Deterministic thread id for two participants (order independent). */
    fun chatThreadId(a: String, b: String): String =
        listOf(a, b).sorted().joinToString("_")

    suspend fun sendChatMessage(
        threadId: String,
        senderId: String,
        text: String,
        type: String,
        mediaUrl: String?
    ): String {
        val ref = database.getReference(NODE_CHATS).child(threadId).child(NODE_MESSAGES).push()
        val message = mapOf(
            "id" to ref.key,
            "senderId" to senderId,
            "text" to text,
            "type" to type,                       // TEXT / IMAGE / AUDIO
            "mediaUrl" to (mediaUrl ?: ""),
            "timestamp" to System.currentTimeMillis(),
            "isRead" to false
        )
        ref.setValue(message).await()
        database.getReference(NODE_CHATS).child(threadId).child("meta")
            .updateChildren(
                mapOf(
                    "lastMessage" to if (type == "TEXT") text else "📎 Attachment",
                    "lastTimestamp" to System.currentTimeMillis(),
                    "lastSenderId" to senderId
                )
            )
        return ref.key ?: ""
    }

    fun listenChatMessages(threadId: String): Flow<List<Map<String, Any?>>> = callbackFlow {
        val ref = database.getReference(NODE_CHATS).child(threadId).child(NODE_MESSAGES)
        val listener = ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children
                    .mapNotNull { it.value as? Map<String, Any?> }
                    .sortedBy { (it["timestamp"] as? Long) ?: 0L }
                trySend(items)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        })
        awaitClose { ref.removeEventListener(listener) }
    }

    // ---------------- Subscriptions & Transactions ----------------

    suspend fun saveSubscription(uid: String, map: Map<String, Any>) {
        val ref = database.getReference(NODE_SUBSCRIPTIONS).child(uid).push()
        ref.setValue(map + mapOf("id" to ref.key, "createdAt" to System.currentTimeMillis())).await()
    }

    suspend fun saveTransaction(uid: String, map: Map<String, Any>) {
        val ref = database.getReference(NODE_TRANSACTIONS).child(uid).push()
        ref.setValue(map + mapOf("id" to ref.key, "createdAt" to System.currentTimeMillis())).await()
    }

    suspend fun fetchTransactions(uid: String): List<Map<String, Any?>> {
        return try {
            val snap = database.getReference(NODE_TRANSACTIONS).child(uid).get().await()
            snap.children.mapNotNull { it.value as? Map<String, Any?> }
                .sortedByDescending { (it["createdAt"] as? Long) ?: 0L }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun saveVerifications(uid: String, map: Map<String, Any>) {
        database.getReference(NODE_VERIFICATIONS).child(uid).updateChildren(map).await()
    }

    // ---------------- Safety: reports & blocks ----------------

    suspend fun reportUser(
        reporterUid: String,
        reportedUid: String,
        reportedName: String,
        reason: String,
        details: String
    ) {
        val ref = database.getReference(NODE_REPORTS).push()
        ref.setValue(
            mapOf(
                "id" to ref.key,
                "reporterUid" to reporterUid,
                "reportedUid" to reportedUid,
                "reportedName" to reportedName,
                "reason" to reason,
                "details" to details,
                "status" to "OPEN",
                "createdAt" to System.currentTimeMillis()
            )
        ).await()
    }

    suspend fun blockUser(blockerUid: String, blockedUid: String, reason: String) {
        database.getReference(NODE_BLOCKS).child("${blockerUid}_$blockedUid")
            .setValue(
                mapOf(
                    "blockerUid" to blockerUid,
                    "blockedUid" to blockedUid,
                    "reason" to reason,
                    "createdAt" to System.currentTimeMillis()
                )
            ).await()
    }

    suspend fun unblockUser(blockerUid: String, blockedUid: String) {
        database.getReference(NODE_BLOCKS).child("${blockerUid}_$blockedUid").removeValue().await()
    }

    suspend fun fetchBlockedUsers(blockerUid: String): List<String> {
        return try {
            val snap = database.getReference(NODE_BLOCKS)
                .orderByKey()
                .startAt("${blockerUid}_")
                .endAt("${blockerUid}_\uf8ff")
                .get().await()
            snap.children.mapNotNull { (it.value as? Map<String, Any?>)?.get("blockedUid") as? String }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ---------------- Support tickets ----------------

    suspend fun createSupportTicket(uid: String, map: Map<String, Any>) {
        val ref = database.getReference(NODE_SUPPORT_TICKETS).push()
        ref.setValue(map + mapOf("id" to ref.key, "createdAt" to System.currentTimeMillis())).await()
    }

    // ---------------- Success stories ----------------

    suspend fun fetchSuccessStories(): List<Map<String, Any?>> {
        return try {
            val snap = database.getReference(NODE_SUCCESS_STORIES).limitToFirst(20).get().await()
            snap.children.mapNotNull { it.value as? Map<String, Any?> }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ---------------- Cloud Storage ----------------

    /**
     * Uploads an image to Firebase Cloud Storage and returns its public
     * download URL. Paths look like `profile_photos/{uid}/1690000000.jpg`.
     */
    suspend fun uploadImage(uri: Uri, storagePath: String): Result<String> {
        return try {
            val imageRef = storage.reference.child(storagePath)
            imageRef.putFile(uri).await()
            val url = imageRef.downloadUrl.await().toString()
            Log.d(TAG, "Uploaded $storagePath -> $url")
            Result.success(url)
        } catch (e: Exception) {
            Log.e(TAG, "Upload failed for $storagePath: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun uploadProfilePhoto(uid: String, imageUri: Uri): Result<String> =
        uploadImage(imageUri, "$STORAGE_PHOTOS_PATH/$uid/${System.currentTimeMillis()}.jpg")

    suspend fun uploadGovtIdDocument(uid: String, docType: String, imageUri: Uri): Result<String> =
        uploadImage(imageUri, "$STORAGE_GOVT_IDS_PATH/$uid/${docType}_${System.currentTimeMillis()}.jpg")

    suspend fun uploadChatMedia(threadId: String, imageUri: Uri): Result<String> =
        uploadImage(imageUri, "$STORAGE_CHAT_MEDIA_PATH/$threadId/${System.currentTimeMillis()}.jpg")

    // ---------------- Account data erasure ----------------

    /** Removes all Realtime Database records belonging to [uid]. */
    suspend fun eraseUserData(uid: String) {
        try {
            database.getReference(NODE_PROFILES).child(uid).removeValue().await()
        } catch (_: Exception) {}
        try {
            database.getReference(NODE_USERS).child(uid).removeValue().await()
        } catch (_: Exception) {}
        try {
            database.getReference(NODE_NOTIFICATIONS).child(uid).removeValue().await()
        } catch (_: Exception) {}
        try {
            database.getReference(NODE_PRIVACY).child(uid).removeValue().await()
        } catch (_: Exception) {}
        try {
            database.getReference(NODE_SUBSCRIPTIONS).child(uid).removeValue().await()
        } catch (_: Exception) {}
        try {
            database.getReference(NODE_TRANSACTIONS).child(uid).removeValue().await()
        } catch (_: Exception) {}
        try {
            database.getReference(NODE_VERIFICATIONS).child(uid).removeValue().await()
        } catch (_: Exception) {}
        Log.d(TAG, "User data erased for $uid")
    }
}
