package org.chessclub.model;

import org.chessclub.enums.TournamentStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Tournament {

    private final long id;
    private final String name;
    private final LocalDate date;
    private TournamentStatus status;
    private final Set<MemberUser> participants = new LinkedHashSet<>();
    private final List<Game> games = new ArrayList<>();

    public Tournament(long id, String name, LocalDate date) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Название турнира не может быть пустым");
        }
        this.id = id;
        this.name = name.trim();
        this.date = date;
        this.status = TournamentStatus.PLANNED;
    }

    public boolean addParticipant(MemberUser member) {
        return participants.add(member);
    }

    public void addGame(Game game) {
        if (!participants.contains(game.getWhite()) || !participants.contains(game.getBlack())) {
            throw new IllegalArgumentException("Оба игрока должны быть участниками турнира");
        }
        games.add(game);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public TournamentStatus getStatus() {
        return status;
    }

    public void setStatus(TournamentStatus status) {
        this.status = status;
    }

    public Set<MemberUser> getParticipants() {
        return participants;
    }

    public List<Game> getGames() {
        return games;
    }
}
