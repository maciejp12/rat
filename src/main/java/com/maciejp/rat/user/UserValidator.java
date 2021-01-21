package com.maciejp.rat.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {

    private final int minNameLength = 3;
    private final int maxNameLength = 100;

    private final int minPasswordLength = 3;
    private final int maxPasswordLength = 100;

    public boolean validateName(String username) {
        int len = username.length();
        return len >= minNameLength && len <= maxNameLength;
    }

    public boolean validatePassowrd(String password) {
        int len = password.length();
        return len >= minPasswordLength && len <= maxPasswordLength;
    }

    public boolean validateEmail(String email) {
        String emailRegexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}" +
                "\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(emailRegexp);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public boolean validatePhoneNumber(String phoneNumber) {
        String phoneNumberRegexp = "^\\d{9}$";
        Pattern p = Pattern.compile(phoneNumberRegexp);
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    public String getUsernameErrorMessage() {
        return "Username must be between " + minNameLength + " and " + maxNameLength + " characters long";
    }

    public String getPasswordErrorMessage() {
        return "Password must be between " + minPasswordLength + " and " + maxPasswordLength + " characters long";
    }
}
