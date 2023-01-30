package com.maylbus.collectives.cleanarchitecturelogin.model

internal data class SignUpState(
    override val email: String = "",
    override val password: String = "",
    val passwordConfirmation: String = "",
    val isRegistering: Boolean = false
): BaseForm(email, password) {

    val signUpIsEnabled: Boolean
        get() = this.email.isNotEmpty() && this.password.isNotEmpty() && this.passwordConfirmation.isNotEmpty()
}