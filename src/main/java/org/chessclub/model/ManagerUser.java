package org.chessclub.model;

import org.chessclub.enums.UserRole;

public class ManagerUser extends User {

    private final String specialization;

    public ManagerUser(long id, String fullName, String email, String specialization) {
        super(id, fullName, email, UserRole.MANAGER);
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    @Override
    public String describePermissions() {
        return "Организация турниров и работа с участниками (" + specialization + ")";
    }
}
