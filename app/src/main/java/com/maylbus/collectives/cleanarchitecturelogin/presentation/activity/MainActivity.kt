package com.maylbus.collectives.cleanarchitecturelogin.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maylbus.collectives.cleanarchitecturelogin.constants.Navigation.AGE_SCREEN
import com.maylbus.collectives.cleanarchitecturelogin.constants.Navigation.GENRE_SCREEN
import com.maylbus.collectives.cleanarchitecturelogin.constants.Navigation.LOGIN_SCREEN
import com.maylbus.collectives.cleanarchitecturelogin.constants.Navigation.NAME_SCREEN
import com.maylbus.collectives.cleanarchitecturelogin.constants.Navigation.SIGN_UP_SCREEN
import com.maylbus.collectives.cleanarchitecturelogin.constants.Navigation.WELCOME_SCREEN
import com.maylbus.collectives.cleanarchitecturelogin.login.LoginScreen
import com.maylbus.collectives.cleanarchitecturelogin.presentation.viewmodel.LoginViewModel
import com.maylbus.collectives.cleanarchitecturelogin.signup.SignUp
import com.maylbus.collectives.cleanarchitecturelogin.signup.SignUpAgeScreen
import com.maylbus.collectives.cleanarchitecturelogin.signup.SignUpGenreScreen
import com.maylbus.collectives.cleanarchitecturelogin.signup.SignUpNameScreen
import com.maylbus.collectives.cleanarchitecturelogin.presentation.viewmodel.SignUpViewModel
import com.maylbus.collectives.cleanarchitecturelogin.snackbar.SnackBarViewModel
import com.maylbus.collectives.cleanarchitecturelogin.ui.theme.CleanArchitectureLoginTheme
import com.maylbus.collectives.cleanarchitecturelogin.utils.getRandomColor
import com.maylbus.collectives.cleanarchitecturelogin.presentation.screen.WelcomeCompose
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {

    private val snackBarViewModel: SnackBarViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            CleanArchitectureLoginTheme {

                val navController: NavHostController = rememberNavController()
                val snackBarHostState: SnackbarHostState = remember { snackBarViewModel.snackBarHostState }

                CleanArchitectureLogin(
                    navController = navController,
                    snackBarHostState = snackBarHostState,
                    snackBarViewModel = snackBarViewModel,
                    loginViewModel = loginViewModel,
                    signUpViewModel = signUpViewModel
                )
            }
        }
    }
}

@Composable
internal fun CleanArchitectureLogin(
    navController: NavHostController,
    snackBarHostState: SnackbarHostState,
    snackBarViewModel: SnackBarViewModel,
    loginViewModel: LoginViewModel,
    signUpViewModel: SignUpViewModel,
) {

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

        Scaffold(
            scaffoldState = rememberScaffoldState(snackbarHostState = snackBarHostState)
        ) {

            NavHost(
                navController = navController,
                startDestination = LOGIN_SCREEN
            ) {

                composable(LOGIN_SCREEN) {

                    LoginScreen(
                        navController = navController,
                        snackBarViewModel = snackBarViewModel,
                        viewModel = loginViewModel
                    )
                }

                composable(NAME_SCREEN) {

                    val backgroundColor = remember { Color(getRandomColor()) }

                    SignUpNameScreen(
                        navController = navController,
                        backgroundColor = backgroundColor,
                        viewModel = signUpViewModel,
                        snackBarViewModel = snackBarViewModel
                    )

                    signUpViewModel.initializeViewModel()
                }

                composable(GENRE_SCREEN) {

                    val backgroundColor = remember { Color(getRandomColor()) }

                    SignUpGenreScreen(
                        navController = navController,
                        viewModel = signUpViewModel,
                        backgroundColor = backgroundColor
                    )
                }

                composable(AGE_SCREEN) {

                    val backgroundColor = remember { Color(getRandomColor()) }

                    SignUpAgeScreen(
                        navController = navController,
                        viewModel = signUpViewModel,
                        snackBarViewModel = snackBarViewModel,
                        backgroundColor = backgroundColor
                    )
                }

                composable(SIGN_UP_SCREEN) {

                    val backgroundColor = remember { Color(getRandomColor()) }

                    SignUp(
                        navController = navController,
                        viewModel = signUpViewModel,
                        snackBarViewModel = snackBarViewModel,
                        backgroundColor = backgroundColor
                    )
                }

                composable(WELCOME_SCREEN) {

                    val backgroundColor = remember { Color(getRandomColor()) }

                    WelcomeCompose(backgroundColor = backgroundColor)
                }
            }
        }
    }
}