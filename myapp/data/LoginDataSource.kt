package com.wahab.myapp.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class LoginDataSource {

    private val firebaseAuth: FirebaseAuth = Firebase.auth
    private val firestore: FirebaseFirestore = Firebase.firestore

    suspend fun login(email: String, password: String): AppResult<LoggedInUser> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user ?: return AppResult.Error(Exception("User not found"))

            val doc = firestore.collection("users").document(user.uid).get().await()

            val displayName = doc.getString("fullName") ?: user.email ?: "Unknown"
            val userEmail = doc.getString("email") ?: user.email ?: email

            AppResult.Success(LoggedInUser(user.uid, displayName, userEmail))

        } catch (e: Exception) {
            AppResult.Error(e)
        }
    }
}
