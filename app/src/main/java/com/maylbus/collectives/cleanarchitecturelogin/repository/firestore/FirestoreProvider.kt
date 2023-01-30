package com.maylbus.collectives.cleanarchitecturelogin.repository.firestore

import com.maylbus.collectives.cleanarchitecturelogin.model.User

interface FirestoreProvider {

    suspend fun insertUser(user: User): Boolean

    suspend fun updateName(name: String): Boolean

    suspend fun updateAge(age: Int): Boolean

    suspend fun deleteAccountData(): Boolean
}