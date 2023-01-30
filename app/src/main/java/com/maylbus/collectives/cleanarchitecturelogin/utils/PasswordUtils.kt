package com.maylbus.collectives.cleanarchitecturelogin.utils

private const val MIN_PASSWORD_LENGTH = 8

internal val String.isValidPasswordLength: Boolean
    get() = this.length >= MIN_PASSWORD_LENGTH

const val REGEX_NUMBERS = ".*[0-9].*"

internal fun passwordsMatch(password: String, confirmPassword: String): Boolean = password == confirmPassword

internal val String.atLeastOneNumber: Boolean
    get() = this.matches(Regex(REGEX_NUMBERS))