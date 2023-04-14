package com.example.planner.models;

import android.provider.BaseColumns;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private long id;
    private String email;
    private String username;
    private String password;

    public static final class UserEntry implements BaseColumns{
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static boolean validatePassword(String password){
        Pattern pattern = Pattern.compile("\\A(?=.*[A-Z])(?=.*\\d)[a-zA-Z0-9]{5,}\\z");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
