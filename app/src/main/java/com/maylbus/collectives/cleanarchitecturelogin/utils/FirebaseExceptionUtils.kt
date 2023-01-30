package com.maylbus.collectives.cleanarchitecturelogin.utils

import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.internal.api.FirebaseNoSignedInUserException
import com.maylbus.collectives.cleanarchitecturelogin.R

internal const val KEY_EMAIL_ERROR = "ERROR_USER_NOT_FOUND"
internal const val KEY_PASSWORD_ERROR = "ERROR_WRONG_PASSWORD"
internal const val KEY_MAIL_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE"

internal val FirebaseAuthException.resId: Int
    get() {

        return when (this.errorCode) {

            KEY_EMAIL_ERROR -> R.string.login_email_not_found

            KEY_PASSWORD_ERROR -> R.string.login_invalid_password

            KEY_MAIL_IN_USE -> R.string.signup_email_already_used

            else -> R.string.unknown_error
        }
    }