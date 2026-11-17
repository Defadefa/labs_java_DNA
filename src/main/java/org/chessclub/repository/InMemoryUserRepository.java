package org.chessclub.repository;

import org.chessclub.enums.UserRole;
import org.chessclub.model.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements Repository<User, Long> {

    private final Map<Long, User> users = new LinkedHashMap<>();

    @Override
    public void add(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не может быть null");
        }
        if (users.containsKey(user.getId())) {
            throw new IllegalArgumentException("Пользователь с id " + user.getId() + " уже существует");
        }
        users.put(user.getId(), user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean remove(Long id) {
        return users.remove(id) != null;
    }

    @Override
    public int count() {
        return users.size();
    }

    public List<User> findByRole(UserRole role) {
        List<User> result = new ArrayList<>();
        for (User user : users.values()) {
            if (user.getRole() == role) {
                result.add(user);
            }
        }
        return result;
    }

    public List<User> findByNameContains(String part) {
        List<User> result = new ArrayList<>();
        String needle = part.toLowerCase().trim();
        for (User user : users.values()) {
            if (user.getFullName().toLowerCase().contains(needle)) {
                result.add(user);
            }
        }
        return result;
    }
}
