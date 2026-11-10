package org.chessclub.model;

import org.chessclub.enums.GameResult;

import java.time.LocalDate;

public class Game {

    private final MemberUser white;
    private final MemberUser black;
    private final GameResult result;
    private final LocalDate date;

    public Game(MemberUser white, MemberUser black, GameResult result, LocalDate date) {
        if (white == null || black == null) {
            throw new IllegalArgumentException("Игроки не могут быть null");
        }
        if (white.getId() == black.getId()) {
            throw new IllegalArgumentException("Игрок не может играть сам с собой");
        }
        this.white = white;
        this.black = black;
        this.result = result;
        this.date = date;
    }

    public MemberUser getWhite() {
        return white;
    }

    public MemberUser getBlack() {
        return black;
    }

    public GameResult getResult() {
        return result;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("%s - %s: %s (%s)",
                white.getFullName(), black.getFullName(), result, date);
    }
}
