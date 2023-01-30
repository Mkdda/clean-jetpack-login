package com.maylbus.collectives.cleanarchitecturelogin.repository.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.maylbus.collectives.cleanarchitecturelogin.model.User
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirestoreRepository(private val firestore: FirebaseFirestore): FirestoreProvider {

    override suspend fun insertUser(user: User): Boolean {

        return suspendCoroutine { continuation ->

            val genre: String = if (user.genre!!.male) {

                "Male"
            } else {

                "Female"
            }

            val userToInsert = hashMapOf(
                "name" to user.name,
                "genre" to genre,
                "age" to user.age
            )

            val documentId: String = UUID.randomUUID().toString()

            this.firestore.collection("Users")
                .document(documentId)
                .set(userToInsert)
                .addOnSuccessListener { continuation.resume(true) }
                .addOnFailureListener { continuation.resumeWithException(it)}
        }
    }

    override suspend fun updateName(name: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateAge(age: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccountData(): Boolean {
        TODO("Not yet implemented")
    }
}