package org.chessclub.model;

import org.chessclub.enums.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class UserHierarchyTest {

    @Test
    void adminHasAdminRole() {
        User admin = new AdminUser(1L, "Иван Админов", "admin@club.ru");
        Assertions.assertEquals(UserRole.ADMIN, admin.getRole());
    }

    @Test
    void managerHasManagerRoleAndSpecialization() {
        ManagerUser manager = new ManagerUser(2L, "Пётр Тренеров", "coach@club.ru", "Тренер");
        Assertions.assertEquals(UserRole.MANAGER, manager.getRole());
        Assertions.assertEquals("Тренер", manager.getSpecialization());
    }

    @Test
    void memberHasMemberRoleAndActiveMembership() {
        MemberUser member = new MemberUser(3L, "Анна Шахова", "anna@club.ru", 1500, LocalDate.of(2025, 9, 1));
        Assertions.assertEquals(UserRole.MEMBER, member.getRole());
        Assertions.assertEquals(1500, member.getRating());
        Assertions.assertTrue(member.isMembershipActive());
    }

    @Test
    void permissionsDifferByRole() {
        User admin = new AdminUser(1L, "Иван Админов", "admin@club.ru");
        User member = new MemberUser(3L, "Анна Шахова", "anna@club.ru", 1500, LocalDate.now());
        Assertions.assertNotEquals(admin.describePermissions(), member.describePermissions());
    }

    @Test
    void blankNameIsRejected() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new AdminUser(1L, "  ", "admin@club.ru"));
    }

    @Test
    void invalidEmailIsRejected() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new AdminUser(1L, "Иван", "no-at-sign"));
    }

    @Test
    void negativeRatingIsRejected() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new MemberUser(3L, "Анна", "anna@club.ru", -5, LocalDate.now()));
    }

    @Test
    void nameIsTrimmed() {
        User admin = new AdminUser(1L, "  Иван  ", "admin@club.ru");
        Assertions.assertEquals("Иван", admin.getFullName());
    }
}
