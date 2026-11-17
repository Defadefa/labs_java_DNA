package org.chessclub.repository;

import org.chessclub.enums.TournamentStatus;
import org.chessclub.model.Tournament;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryTournamentRepository implements Repository<Tournament, Long> {

    private final Map<Long, Tournament> tournaments = new LinkedHashMap<>();

    @Override
    public void add(Tournament tournament) {
        if (tournament == null) {
            throw new IllegalArgumentException("Турнир не может быть null");
        }
        if (tournaments.containsKey(tournament.getId())) {
            throw new IllegalArgumentException("Турнир с id " + tournament.getId() + " уже существует");
        }
        tournaments.put(tournament.getId(), tournament);
    }

    @Override
    public Optional<Tournament> findById(Long id) {
        return Optional.ofNullable(tournaments.get(id));
    }

    @Override
    public List<Tournament> findAll() {
        return new ArrayList<>(tournaments.values());
    }

    @Override
    public boolean remove(Long id) {
        return tournaments.remove(id) != null;
    }

    @Override
    public int count() {
        return tournaments.size();
    }

    public List<Tournament> findByStatus(TournamentStatus status) {
        List<Tournament> result = new ArrayList<>();
        for (Tournament tournament : tournaments.values()) {
            if (tournament.getStatus() == status) {
                result.add(tournament);
            }
        }
        return result;
    }
}
