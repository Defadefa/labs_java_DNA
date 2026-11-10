package org.chessclub.model;

import org.chessclub.enums.GameResult;
import org.chessclub.enums.TournamentStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class TournamentTest {

    private MemberUser anna;
    private MemberUser boris;
    private Tournament tournament;

    @BeforeEach
    void setUp() {
        anna = new MemberUser(1L, "Анна", "anna@club.ru", 1500, LocalDate.now());
        boris = new MemberUser(2L, "Борис", "boris@club.ru", 1400, LocalDate.now());
        tournament = new Tournament(1L, "Весенний кубок", LocalDate.of(2026, 3, 1));
    }

    @Test
    void newTournamentIsPlanned() {
        Assertions.assertEquals(TournamentStatus.PLANNED, tournament.getStatus());
    }

    @Test
    void duplicateParticipantIsNotAdded() {
        Assertions.assertTrue(tournament.addParticipant(anna));
        Assertions.assertFalse(tournament.addParticipant(anna));
        Assertions.assertEquals(1, tournament.getParticipants().size());
    }

    @Test
    void gameWithParticipantsIsAdded() {
        tournament.addParticipant(anna);
        tournament.addParticipant(boris);
        tournament.addGame(new Game(anna, boris, GameResult.WHITE_WIN, LocalDate.now()));
        Assertions.assertEquals(1, tournament.getGames().size());
    }

    @Test
    void gameWithOutsiderIsRejected() {
        tournament.addParticipant(anna);
        Game game = new Game(anna, boris, GameResult.DRAW, LocalDate.now());
        Assertions.assertThrows(IllegalArgumentException.class, () -> tournament.addGame(game));
    }

    @Test
    void playerCannotPlayHimself() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Game(anna, anna, GameResult.DRAW, LocalDate.now()));
    }

    @Test
    void blankTournamentNameIsRejected() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Tournament(2L, " ", LocalDate.now()));
    }
}
