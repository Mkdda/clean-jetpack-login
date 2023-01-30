package com.maylbus.collectives.cleanarchitecturelogin.repository.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthenticationRepository(private val firebaseAuth: FirebaseAuth): AuthenticationProvider {

    override suspend fun logIn(email: String, password: String): Boolean {

        return suspendCoroutine { continuation ->

            this.firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult> ->

                if (task.isSuccessful) {

                    continuation.resume(true)
                } else {

                    val taskException: FirebaseAuthException = task.exception as FirebaseAuthException

                    continuation.resumeWithException(taskException)
                }
            }
        }
    }

    override fun logOut(): Boolean {

       return try {

            this.firebaseAuth.signOut()

            true
        } catch (e: Exception) {

            false
       }
    }

    override suspend fun signUp(email: String, password: String): Boolean {

        return suspendCoroutine { continuation ->

            this.firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult> ->

                if (task.isSuccessful) {

                    continuation.resume(true)
                } else {

                    val taskException: FirebaseAuthException = task.exception as FirebaseAuthException

                    continuation.resumeWithException(taskException)
                }
            }
        }
    }
}