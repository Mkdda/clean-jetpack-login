package com.maylbus.collectives.cleanarchitecturelogin.signup

import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.maylbus.collectives.cleanarchitecturelogin.R
import com.maylbus.collectives.cleanarchitecturelogin.login.KEY_LINEAR_PROGRESS_INDICATOR
import com.maylbus.collectives.cleanarchitecturelogin.login.LoginScreenForms
import com.maylbus.collectives.cleanarchitecturelogin.login.defaultPadding
import com.maylbus.collectives.cleanarchitecturelogin.model.Genre
import com.maylbus.collectives.cleanarchitecturelogin.model.SignUpState
import com.maylbus.collectives.cleanarchitecturelogin.model.TextState
import com.maylbus.collectives.cleanarchitecturelogin.presentation.viewmodel.SignUpViewModel
import com.maylbus.collectives.cleanarchitecturelogin.snackbar.SnackBarViewModel
import com.maylbus.collectives.cleanarchitecturelogin.uimodel.UITextModel
import com.maylbus.collectives.cleanarchitecturelogin.uimodel.UiEvent
import com.maylbus.collectives.cleanarchitecturelogin.utils.emojiFemaleByAge
import com.maylbus.collectives.cleanarchitecturelogin.utils.emojiMaleByAge
import com.maylbus.collectives.cleanarchitecturelogin.utils.getRandomThinkingEmojis
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun SignUpNameScreen(
    navController: NavController,
    viewModel: SignUpViewModel,
    snackBarViewModel: SnackBarViewModel,
    backgroundColor: Color,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = Color.Magenta,
        unfocusedBorderColor = Color.DarkGray,
        textColor = Color.Black
    )
) {

    val context: Context = LocalContext.current

    val textName: String by viewModel.inputName.collectAsState()

    val textState: TextState by viewModel.textState.collectAsState()

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

    val emojis: String = when (textState) {

        TextState.NO_ONE -> ""

        TextState.TYPING -> getRandomThinkingEmojis()

        TextState.STAND_BY -> viewModel.success
    }

    val onNameChange: (String) -> Unit = { input: String ->

        viewModel.onNameChange(input = input)
    }

    SignUpContainer(backgroundColor = backgroundColor) {

        TextEdit(
            modifier = Modifier.align(Alignment.Center),
            fieldValue = textName,
            onValueChange = onNameChange,
            colors = colors,
            emojis = emojis
        )

        ButtonNext(
            modifier = Modifier.align(Alignment.BottomCenter),
            onClick = viewModel::onNextNameClick
        )
    }
}

@Composable
internal fun SignUpGenreScreen(
    navController: NavController,
    viewModel: SignUpViewModel,
    backgroundColor: Color
) {

    LaunchedEffect(key1 = true) {

        viewModel.uiEvent.collectLatest { event: UiEvent ->

            when (event) {

                is UiEvent.Navigate -> navController.navigate(event.route)

                else -> Unit
            }
        }
    }

    SignUpContainer(backgroundColor = backgroundColor) {

        SignUpGenreScreen(
            modifier = Modifier.align(Alignment.Center),
            onMaleClick = viewModel::onMaleClick,
            onFemaleClick = viewModel::onFemaleClick
        )
    }
}

private val genreDefaultPadding: Dp = 24.dp
private val genreTextEditPadding: Dp = 16.dp

@Composable
internal fun SignUpGenreScreen(
    modifier: Modifier = Modifier,
    onMaleClick: () -> Unit,
    onFemaleClick: () -> Unit,
) {

    Column(modifier = modifier) {

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = genreDefaultPadding, end = genreDefaultPadding),
            onClick = onMaleClick
        ) {

            Text(text = stringResource(id = R.string.male))
        }

        Text(
            modifier = Modifier
                .padding(top = genreTextEditPadding, bottom = genreTextEditPadding)
                .fillMaxWidth(),
            text = stringResource(id = R.string.Or),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = genreDefaultPadding, end = genreDefaultPadding),
            onClick = onFemaleClick
        ) {

            Text(text = stringResource(id = R.string.female))
        }
    }
}

@Composable
internal fun SignUpAgeScreen(
    navController: NavController,
    viewModel: SignUpViewModel,
    snackBarViewModel: SnackBarViewModel,
    backgroundColor: Color
) {

    val context: Context = LocalContext.current

    val textAge: String by viewModel.inputAge.collectAsState()

    val onAgeChange: (String) -> Unit = { input: String ->

        viewModel.onAgeChange(input = input)
    }

    val emojis: String = if (textAge.isNotEmpty()) {

        val age: Int = textAge.toInt()
        val genre: Genre = viewModel.user!!.genre!!

        if (genre.male) {

            age.emojiMaleByAge
        } else {

            age.emojiFemaleByAge
        }
    } else {

        ""
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

    SignUpContainer(backgroundColor = backgroundColor) {

        SignUpAgeScreen(
            modifier = Modifier.align(Alignment.Center),
            textAge = textAge,
            onAgeChange = onAgeChange,
            emojis = emojis
        )

        ButtonNext(
            modifier = Modifier.align(Alignment.BottomCenter),
            onClick = viewModel::onAgeNextClick
        )
    }
}

@Composable
internal fun SignUpAgeScreen(
    modifier: Modifier = Modifier,
    textAge: String,
    onAgeChange: (String) -> Unit,
    emojis: String,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = Color.Magenta,
        unfocusedBorderColor = Color.DarkGray,
        textColor = Color.Black
    )
) {

    Column(modifier = modifier) {

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp),
            value = textAge,
            onValueChange = onAgeChange,
            label = {

                Text(text = stringResource(id = R.string.age))
            },
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = colors
        )

        Text(
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth(),
            text = emojis,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
    }
}

@Composable
internal fun SignUp(
    navController: NavController,
    viewModel: SignUpViewModel,
    snackBarViewModel: SnackBarViewModel,
    backgroundColor: Color
) {

    val context: Context = LocalContext.current

    val signUpState: SignUpState by viewModel.signUpState.collectAsState()

    val onEmailChange: (String) -> Unit = { input: String ->

        viewModel.onInputEmailChange(input = input)
    }

    val onPasswordChange: (String) -> Unit = { input: String ->

        viewModel.onInputPasswordChange(input = input)
    }

    val onPasswordConfirmationChange: (String) -> Unit = { input: String ->

        viewModel.onInputPasswordConfirmationChange(input = input)
    }

    LaunchedEffect(key1 = true) {

        viewModel.uiEvent.collectLatest { event: UiEvent->

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

    SignUpContainer(backgroundColor = backgroundColor) {

        Column(modifier = Modifier.align(Alignment.Center)) {

            SignUpForm(
                emailText = signUpState.email,
                passwordText = signUpState.password,
                passwordConfirmationText = signUpState.passwordConfirmation,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onPasswordConfirmationChange = onPasswordConfirmationChange
            )

            SignUpButton(
                isRegistering = signUpState.isRegistering,
                signUpIsEnabled = signUpState.signUpIsEnabled,
                onSignUpClick = viewModel::onSignUpClick
            )
        }
    }
}

@Composable
internal fun SignUpForm(
    emailText: String,
    passwordText: String,
    passwordConfirmationText: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordConfirmationChange: (String) -> Unit,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = Color.Magenta,
        unfocusedBorderColor = Color.DarkGray,
        textColor = Color.Black
    )
) {

    LoginScreenForms(
        emailText = emailText,
        passwordText = passwordText,
        onEmailChange = onEmailChange,
        onPasswordChange = onPasswordChange,
        colors = colors
    )

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = defaultPadding,
                start = defaultPadding,
                end = defaultPadding
            ),
        value = passwordConfirmationText,
        singleLine = true,
        maxLines = 1,
        label = {

            Text(
                color = Color.Black,
                text = stringResource(id = R.string.password_confirmation)
            )
        },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        onValueChange = onPasswordConfirmationChange,
        colors = colors
    )
}

@Composable
internal fun SignUpButton(
    onSignUpClick:() -> Unit,
    isRegistering: Boolean,
    signUpIsEnabled: Boolean
) {

    val signUpEnabled: Boolean = signUpIsEnabled && !isRegistering

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 24.dp,
                start = 64.dp,
                end = 64.dp
            ),
        enabled = signUpEnabled,
        onClick = onSignUpClick
    ) {

        Text(text = stringResource(id = R.string.sign_up))
    }

    if (isRegistering) {

        LinearProgressIndicator(
            modifier = Modifier
                .testTag(KEY_LINEAR_PROGRESS_INDICATOR)
                .fillMaxWidth()
                .padding(
                    start = 64.dp,
                    end = 64.dp
                )
        )
    }
}

@Composable
internal fun TextEdit(
    modifier: Modifier,
    fieldValue: String,
    onValueChange: (String) -> Unit,
    colors: TextFieldColors,
    emojis: String
) {

    Column(modifier = modifier) {

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp),
            value = fieldValue,
            maxLines = 1,
            singleLine = true,
            label = {

                Text(
                    text = stringResource(id = R.string.name),
                    color = Color.Black
                )
            },
            onValueChange = onValueChange,
            colors = colors
        )

        Text(
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth(),
            text = emojis,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
internal fun ButtonNext(
    modifier: Modifier,
    onClick: () -> Unit
) {

    Button(
        modifier = Modifier
            .padding(bottom = 64.dp)
            .then(modifier),
        onClick = onClick
    ) {

        Text(text = stringResource(id = R.string.next))
    }
}

@Composable
internal fun SignUpContainer(
    backgroundColor: Color,
    content: @Composable() (BoxScope.() -> Unit)
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {

        content()
    }
}