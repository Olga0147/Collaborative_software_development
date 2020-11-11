package com.nsu.csd.presentation.common;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

public class Validation {

    public static boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email)
                && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordsValid(String pass, String pass_d ) {
        return pass.equals(pass_d)
                && !TextUtils.isEmpty(pass)
                && !TextUtils.isEmpty(pass_d)
                && isPasswordsValid(pass);
    }

    public static boolean isPasswordsValid(String password) {
        return !TextUtils.isEmpty(password)
                && Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", password);
    }

    public static boolean isUserValid(String firstName, String lastName ) {
        return !TextUtils.isEmpty(firstName)
                && !TextUtils.isEmpty(lastName);
    }

    public static boolean isInputValid(String email, String password) {
        return isEmailValid(email)
                && isPasswordsValid(password);
    }

}
