package com.maylbus.collectives.cleanarchitecturelogin

import com.google.common.truth.Truth.assertThat
import com.google.firebase.FirebaseException
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class AuthenticationRepositoryTest {

    @get:Rule
    val authRule = AuthenticationRepositoryTestRule()

    // region log - in - out

    @Test
    fun `if we mock login task as success then response must be true`() = runBlocking {

        authRule.mockkTaskAsSuccess()

        coEvery {
            authRule.mockAuthentication.signInWithEmailAndPassword("","")
        } returns authRule.successTask

        val result: Boolean = authRule.authenticationRepository.logIn("","")

        assertThat(result).isTrue()
    }

    @Test(expected = FirebaseException::class)
    fun `if we mock login task as failure then response must be a firebaseException`(): Unit = runBlocking {

        authRule.mockkTaskAsFailure()

        coEvery {
            authRule.mockAuthentication.signInWithEmailAndPassword("","")
        } returns authRule.failureTask

        authRule.authenticationRepository.logIn("","")
    }

    @Test
    fun `if we log out and no exception occurs in the process the response must be true`() {

        val result: Boolean = this.authRule.authenticationRepository.logOut()

        assertThat(result).isTrue()
    }

    @Test
    fun `if we log out and exception occurs in middle of the process the response must be false`() {

        every { authRule.mockAuthentication.signOut() } throws Exception()

        val result: Boolean = this.authRule.authenticationRepository.logOut()

        assertThat(result).isFalse()
    }

    // endregion

    // region sign up

    @Test
    fun `if we mock signUp task as success then response must be true`() = runBlocking {

        authRule.mockkTaskAsSuccess()

        coEvery {
            authRule.mockAuthentication.createUserWithEmailAndPassword("","")
        } returns authRule.successTask

        val result: Boolean = authRule.authenticationRepository.signUp("","")

        assertThat(result).isTrue()
    }

    @Test(expected = FirebaseException::class)
    fun `if we mock signUp task as failure then response must be a firebaseException`(): Unit = runBlocking {

        authRule.mockkTaskAsFailure()

        every {
            authRule.mockAuthentication.createUserWithEmailAndPassword("","")
        } returns authRule.failureTask

        authRule.authenticationRepository.signUp("","")
    }

    // endregion
}