package org.chessclub.service;

import org.chessclub.enums.GameResult;
import org.chessclub.model.Game;
import org.chessclub.model.MemberUser;
import org.chessclub.model.Tournament;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class StandingsServiceTest {

    private StandingsService service;
    private MemberUser anna;
    private MemberUser boris;
    private Tournament tournament;

    @BeforeEach
    void setUp() {
        service = new StandingsService();
        anna = new MemberUser(1L, "Анна", "anna@club.ru", 1500, LocalDate.now());
        boris = new MemberUser(2L, "Борис", "boris@club.ru", 1400, LocalDate.now());
        tournament = new Tournament(1L, "Кубок", LocalDate.now());
        tournament.addParticipant(anna);
        tournament.addParticipant(boris);
    }

    @Test
    void winnerGetsOnePoint() {
        tournament.addGame(new Game(anna, boris, GameResult.WHITE_WIN, LocalDate.now()));
        Assertions.assertEquals(1.0, service.calculatePoints(tournament, anna));
        Assertions.assertEquals(0.0, service.calculatePoints(tournament, boris));
    }

    @Test
    void blackWinGivesPointToBlack() {
        tournament.addGame(new Game(anna, boris, GameResult.BLACK_WIN, LocalDate.now()));
        Assertions.assertEquals(1.0, service.calculatePoints(tournament, boris));
    }

    @Test
    void drawGivesHalfPointToBoth() {
        tournament.addGame(new Game(anna, boris, GameResult.DRAW, LocalDate.now()));
        Assertions.assertEquals(0.5, service.calculatePoints(tournament, anna));
        Assertions.assertEquals(0.5, service.calculatePoints(tournament, boris));
    }

    @Test
    void standingsSortedByPoints() {
        tournament.addGame(new Game(anna, boris, GameResult.BLACK_WIN, LocalDate.now()));
        List<MemberUser> order = new ArrayList<>(service.buildStandings(tournament).keySet());
        Assertions.assertEquals(boris, order.get(0));
        Assertions.assertEquals(anna, order.get(1));
    }

    @Test
    void tieBrokenByRating() {
        tournament.addGame(new Game(anna, boris, GameResult.DRAW, LocalDate.now()));
        List<MemberUser> order = new ArrayList<>(service.buildStandings(tournament).keySet());
        Assertions.assertEquals(anna, order.get(0));
    }

    @Test
    void formatContainsNamesAndPoints() {
        tournament.addGame(new Game(anna, boris, GameResult.WHITE_WIN, LocalDate.now()));
        String text = service.formatStandings(tournament);
        Assertions.assertTrue(text.contains("Анна"));
        Assertions.assertTrue(text.contains("Кубок"));
    }
}
