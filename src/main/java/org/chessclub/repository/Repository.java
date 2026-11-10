package org.chessclub.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {

    void add(T item);

    Optional<T> findById(ID id);

    List<T> findAll();

    boolean remove(ID id);

    int count();
}
