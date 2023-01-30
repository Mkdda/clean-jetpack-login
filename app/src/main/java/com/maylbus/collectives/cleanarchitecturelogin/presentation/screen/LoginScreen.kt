package com.maylbus.collectives.cleanarchitecturelogin.login

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.maylbus.collectives.cleanarchitecturelogin.R
import com.maylbus.collectives.cleanarchitecturelogin.model.LoginState
import com.maylbus.collectives.cleanarchitecturelogin.presentation.viewmodel.LoginViewModel
import com.maylbus.collectives.cleanarchitecturelogin.snackbar.SnackBarViewModel
import com.maylbus.collectives.cleanarchitecturelogin.uimodel.UITextModel
import com.maylbus.collectives.cleanarchitecturelogin.uimodel.UiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun LoginScreen(
    viewModel: LoginViewModel,
    snackBarViewModel: SnackBarViewModel,
    navController: NavController,
    context: Context = LocalContext.current,
) {

    val loginState: LoginState by viewModel.loginState.collectAsState()

    val onEmailChange: (String) -> Unit = {

        viewModel.onInputEmailChange(it)
    }

    val onPasswordChange: (String) -> Unit = {

        viewModel.onInputPasswordChange(it)
    }

    LaunchedEffect(key1 = true) {

        viewModel.uiEvent.collectLatest { event: UiEvent ->

            when (event) {

                is UiEvent.Navigate -> navController.navigate(event.route)

                is UiEvent.ShowSnackBar -> {

                    val messageUIText = event.message as UITextModel.StringResource
                    val message = context.getString(messageUIText.resId)

                    snackBarViewModel.onSnackBarShow(textContent = message)
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LoginScreenForms(
            emailText = loginState.email,
            passwordText = loginState.password,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange
        )

        LoginScreenButtons(
            onLoginClick = viewModel::onLoginClick,
            onSignUpClick = viewModel::onSignUpClick,
            isLoading = loginState.isValidating,
            loginIsEnabled = loginState.loginIsEnabled
        )
    }
}

internal val defaultTopPadding: Dp = 16.dp
internal val defaultPadding: Dp = 20.dp

internal const val KEY_EMAIL_TEXT_FIELD = "email_text_field"
internal const val KEY_PASSWORD_TEXT_FIELD = "password_tex_field"

@Composable
internal fun LoginScreenForms(
    emailText: String,
    passwordText: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    colors: TextFieldColors? = null
) {

    val textFieldColors: TextFieldColors = colors ?: TextFieldDefaults.outlinedTextFieldColors()
    val texColor: Color = if (colors!=null) {

        Color.Black
    } else {

        Color.White
    }

    OutlinedTextField(
        modifier = Modifier
            .testTag(KEY_EMAIL_TEXT_FIELD)
            .fillMaxWidth()
            .padding(
                start = defaultPadding,
                end = defaultPadding
            ),
        value = emailText,
        singleLine = true,
        label = {

            Text(
                color = texColor,
                text = stringResource(id = R.string.mail)
            )
        },
        onValueChange = onEmailChange,
        colors = textFieldColors
    )

    OutlinedTextField(
        modifier = Modifier
            .testTag(KEY_PASSWORD_TEXT_FIELD)
            .fillMaxWidth()
            .padding(
                top = defaultTopPadding,
                start = defaultPadding,
                end = defaultPadding
            ),
        value = passwordText,
        singleLine = true,
        label = {

            Text(
                color = texColor,
                text = stringResource(id = R.string.password)
            )
        },
        onValueChange = onPasswordChange,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = textFieldColors
    )
}

private val topPaddingFromPassword: Dp = 24.dp
private val loginPadding: Dp = 64.dp

internal const val KEY_LOGIN_BUTTON = "login_button"
internal const val KEY_LOGIN_TEXT = "login_text"

internal const val KEY_LINEAR_PROGRESS_INDICATOR = "linear_progress"

internal const val KEY_TEXT_LABEL = "text_label"

internal const val KEY_SIGN_UP_BUTTON = "sign_up_button"
internal const val KEY_SIGN_UP_TEXT = "sign_up_text"


@Composable
internal fun LoginScreenButtons(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    isLoading: Boolean,
    loginIsEnabled: Boolean = false
) {

    val loginEnabled: Boolean = loginIsEnabled && !isLoading
    val signUpEnabled: Boolean = !isLoading

    Button(
        modifier = Modifier
            .testTag(KEY_LOGIN_BUTTON)
            .fillMaxWidth()
            .padding(
                top = topPaddingFromPassword,
                start = loginPadding,
                end = loginPadding
            ),
        onClick = onLoginClick,
        enabled = loginEnabled
    ) {

        Text(
            modifier = Modifier.testTag(KEY_LOGIN_TEXT),
            text = stringResource(id = R.string.log_in)
        )
    }

    if (isLoading) {

        LinearProgressIndicator(
            modifier = Modifier
                .testTag(KEY_LINEAR_PROGRESS_INDICATOR)
                .fillMaxWidth()
                .padding(
                    start = loginPadding,
                    end = loginPadding
                )
        )
    }

    Text(
        modifier = Modifier
            .testTag(KEY_TEXT_LABEL)
            .padding(
                top = topPaddingFromPassword,
                start = loginPadding,
                end = loginPadding
            ),
        text = stringResource(id = R.string.Or)
    )

    Button(
        modifier = Modifier
            .testTag(KEY_SIGN_UP_BUTTON)
            .fillMaxWidth()
            .padding(
                top = topPaddingFromPassword,
                start = loginPadding,
                end = loginPadding
            ),
        onClick = onSignUpClick,
        enabled = signUpEnabled
    ) {

        Text(
            modifier = Modifier.testTag(KEY_SIGN_UP_TEXT),
            text = stringResource(id = R.string.sign_up)
        )
    }
}