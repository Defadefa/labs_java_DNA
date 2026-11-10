package org.chessclub.repository;

import org.chessclub.enums.UserRole;
import org.chessclub.model.AdminUser;
import org.chessclub.model.MemberUser;
import org.chessclub.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

class UserRepositoryTest {

    private InMemoryUserRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
        repository.add(new AdminUser(1L, "Иван Админов", "admin@club.ru"));
        repository.add(new MemberUser(2L, "Анна Шахова", "anna@club.ru", 1500, LocalDate.now()));
        repository.add(new MemberUser(3L, "Борис Ладьев", "boris@club.ru", 1400, LocalDate.now()));
    }

    @Test
    void addIncreasesCount() {
        Assertions.assertEquals(3, repository.count());
    }

    @Test
    void duplicateIdIsRejected() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> repository.add(new AdminUser(1L, "Другой", "other@club.ru")));
    }

    @Test
    void findByIdReturnsUser() {
        Assertions.assertTrue(repository.findById(2L).isPresent());
        Assertions.assertEquals("Анна Шахова", repository.findById(2L).get().getFullName());
    }

    @Test
    void findByIdReturnsEmptyWhenMissing() {
        Assertions.assertTrue(repository.findById(99L).isEmpty());
    }

    @Test
    void removeDeletesUser() {
        Assertions.assertTrue(repository.remove(1L));
        Assertions.assertFalse(repository.remove(1L));
        Assertions.assertEquals(2, repository.count());
    }

    @Test
    void findByRoleFiltersUsers() {
        List<User> members = repository.findByRole(UserRole.MEMBER);
        Assertions.assertEquals(2, members.size());
    }

    @Test
    void findByNameIsCaseInsensitive() {
        List<User> found = repository.findByNameContains("ШАХ");
        Assertions.assertEquals(1, found.size());
        Assertions.assertEquals(2L, found.get(0).getId());
    }
}
