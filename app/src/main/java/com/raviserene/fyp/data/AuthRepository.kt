package com.raviserene.fyp.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class UserProfile(
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val role: String = "rider", // or "driver"
    val preferences: Map<String, Any> = emptyMap()
)

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun currentUserUid(): String? = auth.currentUser?.uid

    suspend fun signUpWithEmail(email: String, password: String, profile: UserProfile): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("No UID")
            val profileToSave = profile.copy(uid = uid, email = email)
            store.collection("users").document(uid).set(profileToSave).await()
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signInWithEmail(email: String, password: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("No UID")
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signInWithGoogle(idToken: String, onComplete: (Result<String>) -> Unit) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    if (uid != null) {
                        // ensure user doc exists
                        val ref = store.collection("users").document(uid)
                        ref.get().addOnSuccessListener { doc ->
                            if (!doc.exists()) {
                                val profile = UserProfile(
                                    uid = uid,
                                    displayName = auth.currentUser?.displayName ?: "",
                                    email = auth.currentUser?.email ?: "",
                                    role = "rider"
                                )
                                ref.set(profile)
                            }
                            onComplete(Result.success(uid))
                        }.addOnFailureListener { e -> onComplete(Result.failure(e)) }
                    } else onComplete(Result.failure(Exception("No UID after Google sign-in")))
                } else {
                    onComplete(Result.failure(task.exception ?: Exception("Google sign-in failed")))
                }
            }
    }

    fun signOut() {
        auth.signOut()
    }
}