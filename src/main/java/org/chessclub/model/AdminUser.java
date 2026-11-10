package org.chessclub.model;

import org.chessclub.enums.UserRole;

public class AdminUser extends User {

    public AdminUser(long id, String fullName, String email) {
        super(id, fullName, email, UserRole.ADMIN);
    }

    @Override
    public String describePermissions() {
        return "Полный доступ: управление пользователями, турнирами и настройками клуба";
    }
}
