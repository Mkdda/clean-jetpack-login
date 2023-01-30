package com.maylbus.collectives.cleanarchitecturelogin

import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseAuthException
import com.maylbus.collectives.cleanarchitecturelogin.constants.Navigation
import com.maylbus.collectives.cleanarchitecturelogin.model.LoginState
import com.maylbus.collectives.cleanarchitecturelogin.uimodel.UITextModel
import com.maylbus.collectives.cleanarchitecturelogin.uimodel.UiEvent
import com.maylbus.collectives.cleanarchitecturelogin.utils.KEY_EMAIL_ERROR
import com.maylbus.collectives.cleanarchitecturelogin.utils.isValidEmail
import com.maylbus.collectives.cleanarchitecturelogin.utils.isValidPasswordLength
import com.maylbus.collectives.cleanarchitecturelogin.utils.resId
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.util.UUID

class LoginViewModelTest {

    @get:Rule
    val rule = LoginViewModelTestRule()

    // region login

    @Test
    fun `if we call onInputEmailChange method the value of state must be as expected`() {

        val expectedResult: String = UUID.randomUUID().toString()

        var result: LoginState = this.rule.viewModel.loginState.value

        assertThat(result.email).isEmpty()

        this.rule.viewModel.onInputEmailChange(expectedResult)

        result = this.rule.viewModel.loginState.value

        assertThat(result.email).isEqualTo(expectedResult)
    }

    @Test
    fun `if we call onInputPasswordChange method the value of state must be as expected`() {

        val expectedResult: String = UUID.randomUUID().toString()

        var result: LoginState = this.rule.viewModel.loginState.value

        assertThat(result.password).isEmpty()

        this.rule.viewModel.onInputPasswordChange(expectedResult)

        result = this.rule.viewModel.loginState.value

        assertThat(result.password).isEqualTo(expectedResult)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `if we mock mail as invalid then snack bar event must be as expected`() = runTest {

        val expectedEvent: UiEvent.ShowSnackBar = UiEvent.ShowSnackBar(
            UITextModel.StringResource(R.string.form_email_not_valid)
        )

        val inputMail = "mockedMail@test.com"

        mockkStatic("com.maylbus.collectives.cleanarchitecturelogin.utils.MailUtilsKt")

        every { inputMail.isValidEmail } returns false

        rule.viewModel.onInputEmailChange(inputMail)

        rule.viewModel.onLoginClick()

        val result: UiEvent = rule.viewModel.uiEvent.first()

        assertThat(result).isEqualTo(expectedEvent)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `if we mock mail as valid and password length not then snack bar event must be as expected`() = runTest {

        val expectedEvent: UiEvent.ShowSnackBar = UiEvent.ShowSnackBar(
            UITextModel.StringResource(R.string.login_invalid_password)
        )

        val inputMail = "mockedMail@test.com"
        val inputPassword = "1234"

        mockkStatic("com.maylbus.collectives.cleanarchitecturelogin.utils.MailUtilsKt")

        every { inputMail.isValidEmail } returns true

        rule.viewModel.onInputEmailChange(inputMail)
        rule.viewModel.onInputPasswordChange(inputPassword)

        rule.viewModel.onLoginClick()

        val result: UiEvent = rule.viewModel.uiEvent.first()

        assertThat(result).isEqualTo(expectedEvent)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `if we mock mail and login as valid and correct pass length then state must be as validating and event as expected`() = runTest {

        val expectedEvent = UiEvent.Navigate(Navigation.WELCOME_SCREEN)

        val inputMail = "mockedMail@test.com"
        val inputPassword = "12345678"

        mockkStatic("com.maylbus.collectives.cleanarchitecturelogin.utils.MailUtilsKt")

        every { inputMail.isValidEmail } returns true

        rule.viewModel.onInputEmailChange(inputMail)
        rule.viewModel.onInputPasswordChange(inputPassword)

        rule.fakeAuthenticationRepository.setResponseLogin(true)

        rule.viewModel.onLoginClick()

        val result: UiEvent = rule.viewModel.uiEvent.first()
        val validationResult: Boolean = rule.viewModel.loginState.first().isValidating

        assertThat(result).isEqualTo(expectedEvent)
        assertThat(validationResult).isTrue()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `if we mock an invalid login the validating state must be false an event a snackBar Ex`() = runTest {

        val expectedException = FirebaseAuthException(KEY_EMAIL_ERROR,"error")

        val expectedEvent = UiEvent.ShowSnackBar(
            UITextModel.StringResource(expectedException.resId)
        )

        val inputMail = "mockedMail@test.com"
        val inputPassword = "12345678"

        mockkStatic("com.maylbus.collectives.cleanarchitecturelogin.utils.MailUtilsKt")

        every { inputMail.isValidEmail } returns true

        rule.viewModel.onInputEmailChange(inputMail)
        rule.viewModel.onInputPasswordChange(inputPassword)

        rule.fakeAuthenticationRepository.setShouldReturnErrorLogin(true, expectedException)

        rule.viewModel.onLoginClick()

        val result: UiEvent = rule.viewModel.uiEvent.first()
        val validationResult: Boolean = rule.viewModel.loginState.first().isValidating

        assertThat(result).isEqualTo(expectedEvent)
        assertThat(validationResult).isFalse()
    }

    // endregion

    // region signUp

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `on signUp click navigation test`() = runTest {

        val expectedResult = UiEvent.Navigate(route = Navigation.NAME_SCREEN)

        rule.viewModel.onSignUpClick()

        val result = rule.viewModel.uiEvent.first()

        assertThat(result).isEqualTo(expectedResult)
    }

    // endregion
}