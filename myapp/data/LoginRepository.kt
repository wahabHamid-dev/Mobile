package com.wahab.myapp.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class LoginRepository {

    private val firebaseAuth: FirebaseAuth = Firebase.auth
    private val firestore: FirebaseFirestore = Firebase.firestore


    suspend fun signup(
        username: String,
        fullName: String,
        email: String,
        idCard: String,
        cellNumber: String,
        password: String
    ): AppResult<LoggedInUser> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = authResult.user ?: return AppResult.Error(Exception("User creation failed"))

            val userData = mapOf(
                "fullName" to fullName,
                "email" to email,
                "username" to username,
                "idCard" to idCard,
                "cellNumber" to cellNumber
            )

            firestore.collection("users").document(user.uid).set(userData).await()

            AppResult.Success(LoggedInUser(user.uid, fullName, email))
        } catch (e: Exception) {
            AppResult.Error(e)
        }
    }

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
