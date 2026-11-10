package org.chessclub.model;

import org.chessclub.enums.UserRole;

public abstract class User {

    private final long id;
    private String fullName;
    private String email;
    private final UserRole role;

    protected User(long id, String fullName, String email, UserRole role) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Некорректный email: " + email);
        }
        this.id = id;
        this.fullName = fullName.trim();
        this.email = email.trim();
        this.role = role;
    }

    public abstract String describePermissions();

    public long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым");
        }
        this.fullName = fullName.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Некорректный email: " + email);
        }
        this.email = email.trim();
    }

    public UserRole getRole() {
        return role;
    }

    @Override
    public String toString() {
        return String.format("%s{id=%d, name='%s', email='%s', role=%s}",
                getClass().getSimpleName(), id, fullName, email, role);
    }
}
