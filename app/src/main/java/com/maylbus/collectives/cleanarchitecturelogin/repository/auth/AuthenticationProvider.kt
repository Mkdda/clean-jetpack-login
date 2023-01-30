package com.maylbus.collectives.cleanarchitecturelogin.repository.auth

interface AuthenticationProvider {

    suspend fun logIn(email: String, password: String): Boolean

    fun logOut(): Boolean

    suspend fun signUp(email: String, password: String): Boolean
}