package com.tuan.assignment2.util;

import org.springframework.stereotype.Component;

@Component
public class EmailUtils {

    public boolean isValidEmail(String email) {
        String regex = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9._%+-]+@gmail\\.com$";
        return email.matches(regex);
    }

}
