package org.chessclub.concurrent;

import org.chessclub.enums.GameResult;
import org.chessclub.model.Game;
import org.chessclub.model.MemberUser;
import org.chessclub.model.Tournament;
import org.chessclub.service.StandingsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

class ParallelStandingsCalculatorTest {

    @Test
    void parallelResultMatchesSequential() throws InterruptedException {
        StandingsService service = new StandingsService();
        Tournament tournament = new Tournament(1L, "Кубок", LocalDate.now());
        MemberUser[] players = new MemberUser[6];
        for (int i = 0; i < players.length; i++) {
            players[i] = new MemberUser(i + 1L, "Игрок" + (i + 1),
                    "p" + i + "@club.ru", 1000 + i * 50, LocalDate.now());
            tournament.addParticipant(players[i]);
        }
        for (int i = 0; i < players.length - 1; i++) {
            tournament.addGame(new Game(players[i], players[i + 1],
                    GameResult.WHITE_WIN, LocalDate.now()));
        }

        ParallelStandingsCalculator calculator = new ParallelStandingsCalculator(service, 3);
        Map<Long, Double> parallel = calculator.calculate(tournament);

        Assertions.assertEquals(players.length, parallel.size());
        for (MemberUser player : players) {
            Assertions.assertEquals(service.calculatePoints(tournament, player),
                    parallel.get(player.getId()));
        }
    }

    @Test
    void threadFactoryGivesNames() {
        NamedThreadFactory factory = new NamedThreadFactory("test");
        Assertions.assertEquals("test-1", factory.newThread(() -> { }).getName());
        Assertions.assertEquals("test-2", factory.newThread(() -> { }).getName());
    }
}
