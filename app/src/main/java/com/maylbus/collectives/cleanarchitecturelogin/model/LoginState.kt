package com.maylbus.collectives.cleanarchitecturelogin.model

internal data class LoginState(
    override val email: String = "",
    override val password: String = "",
    val isValidating: Boolean = false
) : BaseForm(email, password) {

    val loginIsEnabled: Boolean
        get() = this.email.isNotEmpty() && this.password.isNotEmpty()
}