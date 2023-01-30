package com.maylbus.collectives.cleanarchitecturelogin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthException
import com.maylbus.collectives.cleanarchitecturelogin.R
import com.maylbus.collectives.cleanarchitecturelogin.constants.Navigation
import com.maylbus.collectives.cleanarchitecturelogin.di.IODispatcher
import com.maylbus.collectives.cleanarchitecturelogin.model.Genre
import com.maylbus.collectives.cleanarchitecturelogin.model.User
import com.maylbus.collectives.cleanarchitecturelogin.repository.auth.AuthenticationProvider
import com.maylbus.collectives.cleanarchitecturelogin.repository.firestore.FirestoreProvider
import com.maylbus.collectives.cleanarchitecturelogin.model.SignUpState
import com.maylbus.collectives.cleanarchitecturelogin.model.TextState
import com.maylbus.collectives.cleanarchitecturelogin.uimodel.UITextModel
import com.maylbus.collectives.cleanarchitecturelogin.uimodel.UiEvent
import com.maylbus.collectives.cleanarchitecturelogin.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SignUpViewModel @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val authRepository: AuthenticationProvider,
    private val firesStoreRepository: FirestoreProvider
) : ViewModel() {

    var user: User? = null

    companion object {

        private const val MIN_NAME_CHARACTERS = 3
        private const val TIMEOUT = 200L
    }

    fun initializeViewModel() {

        this.viewModelScope.launch(this.dispatcher) {

            onNameListener()
        }
    }

    // region event ui event

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    // endregion

    // region text state

    private val _textState: MutableStateFlow<TextState> by lazy {

        MutableStateFlow(TextState.NO_ONE)
    }

    val textState: StateFlow<TextState>
        get() = this._textState

    private fun setTextState(value: String) {

        if (value.isEmpty()) {

            _textState.value = TextState.NO_ONE
        } else {

            _textState.value = TextState.STAND_BY
        }
    }

    // endregion

    // region name

    private val _inputName: MutableStateFlow<String> by lazy {

        MutableStateFlow("")
    }

    val inputName: StateFlow<String>
        get() = this._inputName

    var success: String = "\uD83C\uDF89"
        private set

    fun onNameChange(input: String) {

        this.viewModelScope.launch(this.dispatcher) {

            _inputName.value = input

            _textState.value = TextState.TYPING
        }
    }

    // endregion

    // region listeners

    @OptIn(FlowPreview::class)
    private suspend fun onNameListener() {

        this._inputName.debounce(TIMEOUT).collectLatest { value: String ->

            setTextState(value = value)
        }

        this._inputAge.debounce(TIMEOUT).collectLatest { value: String ->

            setTextState(value = value)
        }
    }

    // endregion

    // region next

    fun onNextNameClick() {

        this.viewModelScope.launch(this.dispatcher) {

            when {

                inputName.value.isEmpty() -> displaySnackBar(resId = R.string.name_empty)

                inputName.value.length < MIN_NAME_CHARACTERS -> displaySnackBar(resId = R.string.incorrect_name)

                else -> {

                    user = User(name = inputName.value)

                    _uiEvent.send(UiEvent.Navigate(route = Navigation.GENRE_SCREEN))
                }
            }
        }
    }

    fun onAgeNextClick() {

        this.viewModelScope.launch(this.dispatcher) {

            when {

                inputAge.value.isEmpty() -> displaySnackBar(resId = R.string.age_empty)

                else -> {

                    user = user!!.copy(age = inputAge.value.toInt())

                    _uiEvent.send(UiEvent.Navigate(route = Navigation.SIGN_UP_SCREEN))
                }
            }
        }
    }

    fun onMaleClick() {

        this.viewModelScope.launch(this.dispatcher) {

            user = user!!.copy(genre = Genre(male = true))

            _uiEvent.send(UiEvent.Navigate(route = Navigation.AGE_SCREEN))
        }
    }

    fun onFemaleClick() {

        this.viewModelScope.launch(this.dispatcher) {

            user = user!!.copy(genre = Genre(female = true))

            _uiEvent.send(UiEvent.Navigate(route = Navigation.AGE_SCREEN))
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

    // region age

    private val _inputAge: MutableStateFlow<String> by lazy {

        MutableStateFlow("")
    }

    val inputAge: StateFlow<String>
        get() = this._inputAge

    fun onAgeChange(input: String) {

        try {

            if (input.isNotEmpty()) {

                input.toInt()

                _inputAge.value = input
            } else {

                _inputAge.value = input
            }
        } catch (ex: Exception) {

            displaySnackBar(resId = R.string.error_age)
        }
    }

    // endregion

    // region sign up

    private val _signUpState: MutableStateFlow<SignUpState> by lazy {

        MutableStateFlow(SignUpState())
    }

    val signUpState: StateFlow<SignUpState>
        get() = this._signUpState

    fun onInputEmailChange(input: String) {

        this._signUpState.value = this._signUpState.value.copy(email = input)
    }

    fun onInputPasswordChange(input: String) {

        this._signUpState.value = this._signUpState.value.copy(password = input)
    }

    fun onInputPasswordConfirmationChange(input: String) {

        this._signUpState.value = this._signUpState.value.copy(passwordConfirmation = input)
    }

    fun onSignUpClick() {

        this.viewModelScope.launch {

            val mail: String = _signUpState.value.email
            val password: String = _signUpState.value.password
            val passwordConfirmation: String = _signUpState.value.passwordConfirmation

            when {

                !mail.isValidEmail -> displaySnackBar(resId = R.string.form_email_not_valid)

                !password.isValidPasswordLength -> displaySnackBar(resId = R.string.form_password_too_short)

                !password.atLeastOneNumber -> displaySnackBar(resId = R.string.signup_password_not_have_numbers)

                !passwordsMatch(password,passwordConfirmation) -> displaySnackBar(resId = R.string.signup_password_no_match)

                else -> {

                    _signUpState.value = _signUpState.value.copy(isRegistering = true)

                    try {

                        authRepository.signUp(mail,password)

                        firesStoreRepository.insertUser(user!!)

                        displaySnackBar(resId = R.string.sign_up_success)

                        authRepository.logIn(mail,password)

                        _uiEvent.send(UiEvent.Navigate(route = Navigation.WELCOME_SCREEN))
                    } catch (ex: FirebaseAuthException) {

                        _signUpState.value = _signUpState.value.copy(isRegistering = false)

                        displaySnackBar(resId = ex.resId)
                    }
                }
            }
        }
    }

    // endregion
}