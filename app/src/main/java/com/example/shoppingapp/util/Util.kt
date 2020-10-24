package com.example.shoppingapp.util

import java.util.regex.Matcher
import java.util.regex.Pattern

abstract class Util{
    companion object{
        fun isEmailValid(email: String?): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

    }
}
