package com.maylbus.collectives.cleanarchitecturelogin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthException
import com.maylbus.collectives.cleanarchitecturelogin.R
import com.maylbus.collectives.cleanarchitecturelogin.constants.Navigation
import com.maylbus.collectives.cleanarchitecturelogin.di.IODispatcher
import com.maylbus.collectives.cleanarchitecturelogin.uimodel.UiEvent
import com.maylbus.collectives.cleanarchitecturelogin.model.LoginState
import com.maylbus.collectives.cleanarchitecturelogin.repository.auth.AuthenticationProvider
import com.maylbus.collectives.cleanarchitecturelogin.uimodel.UITextModel
import com.maylbus.collectives.cleanarchitecturelogin.utils.isValidEmail
import com.maylbus.collectives.cleanarchitecturelogin.utils.isValidPasswordLength
import com.maylbus.collectives.cleanarchitecturelogin.utils.resId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val authRepository: AuthenticationProvider
) : ViewModel() {

    // region login

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _loginState: MutableStateFlow<LoginState> by lazy {

        MutableStateFlow(LoginState())
    }

    val loginState: StateFlow<LoginState>
        get() = this._loginState

    fun onInputEmailChange(input: String) {

        this._loginState.value = this._loginState.value.copy(email = input)
    }

    fun onInputPasswordChange(input: String) {

        this._loginState.value = this._loginState.value.copy(password = input)
    }

    fun onLoginClick() {

        this.viewModelScope.launch(this.dispatcher) {

            val mail: String = _loginState.value.email
            val password: String = _loginState.value.password

            if (!mail.isValidEmail) {

                displaySnackBar(resId = R.string.form_email_not_valid)
            } else {

                if (!password.isValidPasswordLength) {

                    displaySnackBar(resId = R.string.login_invalid_password)
                } else {

                    _loginState.value = _loginState.value.copy(isValidating = true)
                    try {

                        authRepository.logIn(mail, password)

                        _uiEvent.send(UiEvent.Navigate(route = Navigation.WELCOME_SCREEN))
                    } catch (ex: FirebaseAuthException) {

                        _loginState.value = _loginState.value.copy(isValidating = false)

                        displaySnackBar(resId = ex.resId)
                    }
                }
            }
        }
    }

    // endregion

    // region sign up nav

    fun onSignUpClick() {

        this.viewModelScope.launch(this.dispatcher) {

            _uiEvent.send(UiEvent.Navigate(route = Navigation.NAME_SCREEN))
        }
    }

    // endregion

    // region snackBar

    private fun displaySnackBar(resId: Int? = null, message: String? = null) {

        val snackBarEvent: UiEvent.ShowSnackBar? = when {

            resId != null -> UiEvent.ShowSnackBar(
                message = UITextModel.StringResource(
                    resId = resId
                )
            )

            message != null -> UiEvent.ShowSnackBar(
                message = UITextModel.DynamicString(
                    text = message
                )
            )

            else -> null
        }

        snackBarEvent?.let { event ->

            _uiEvent.trySend(event)
        }
    }

    // endregion
}