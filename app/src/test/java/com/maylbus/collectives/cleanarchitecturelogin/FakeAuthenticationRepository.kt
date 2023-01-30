package com.maylbus.collectives.cleanarchitecturelogin

import com.google.firebase.FirebaseException
import com.maylbus.collectives.cleanarchitecturelogin.repository.auth.AuthenticationProvider

class FakeAuthenticationRepository : AuthenticationProvider {

    private var loginResponse = false
    private var logOutResponse = false

    private var shouldReturnErrorLogin = false
    private lateinit var loginException: FirebaseException

    private var shouldReturnErrorSignUp = false
    private lateinit var signUpException: FirebaseException

    fun setResponseLogin(response: Boolean) {

        this.loginResponse = response
    }

    fun setShouldReturnErrorLogin(value: Boolean, exception: FirebaseException? = null) {

        this.shouldReturnErrorLogin = value

        if (value && exception != null) {

            this.loginException = exception
        }
    }

    override suspend fun logIn(email: String, password: String): Boolean {

        return if (this.shouldReturnErrorLogin) {

            throw this.loginException
        } else {

            true
        }
    }

    override fun logOut(): Boolean = this.logOutResponse

    override suspend fun signUp(email: String, password: String): Boolean {

        return if (this.shouldReturnErrorSignUp) {

            throw this.signUpException
        } else {

            true
        }
    }
}