package com.maylbus.collectives.cleanarchitecturelogin.utils

import android.util.Patterns

internal val String.isValidEmail: Boolean
    get() = Patterns.EMAIL_ADDRESS.matcher(this).matches()