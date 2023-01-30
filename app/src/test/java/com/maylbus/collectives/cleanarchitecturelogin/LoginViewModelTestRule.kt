package com.maylbus.collectives.cleanarchitecturelogin

import com.maylbus.collectives.cleanarchitecturelogin.presentation.viewmodel.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTestRule: TestWatcher() {

    private val dispatcher: TestDispatcher by lazy {

        StandardTestDispatcher()
    }

    val fakeAuthenticationRepository = FakeAuthenticationRepository()

    internal val viewModel = LoginViewModel(
        dispatcher = dispatcher,
        authRepository = fakeAuthenticationRepository
    )

    override fun starting(description: Description) {

        super.starting(description)

        Dispatchers.setMain(this.dispatcher)
    }

    override fun finished(description: Description) {

        super.finished(description)

        Dispatchers.resetMain()

        this.dispatcher.cancel()
    }
}