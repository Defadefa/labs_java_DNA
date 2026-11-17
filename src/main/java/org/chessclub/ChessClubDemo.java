package org.chessclub;

import org.chessclub.concurrent.ParallelStandingsCalculator;
import org.chessclub.enums.GameResult;
import org.chessclub.enums.UserRole;
import org.chessclub.model.AdminUser;
import org.chessclub.model.Game;
import org.chessclub.model.ManagerUser;
import org.chessclub.model.MemberUser;
import org.chessclub.model.Tournament;
import org.chessclub.model.User;
import org.chessclub.repository.InMemoryUserRepository;
import org.chessclub.service.StandingsService;

import java.time.LocalDate;

public class ChessClubDemo {

    public static void main(String[] args) throws InterruptedException {
        InMemoryUserRepository users = new InMemoryUserRepository();
        users.add(new AdminUser(1L, "Иван Админов", "admin@club.ru"));
        users.add(new ManagerUser(2L, "Пётр Тренеров", "coach@club.ru", "Тренер"));
        MemberUser anna = new MemberUser(3L, "Анна Шахова", "anna@club.ru", 1500, LocalDate.of(2025, 9, 1));
        MemberUser boris = new MemberUser(4L, "Борис Ладьев", "boris@club.ru", 1400, LocalDate.of(2025, 10, 5));
        MemberUser vera = new MemberUser(5L, "Вера Ферзева", "vera@club.ru", 1600, LocalDate.of(2025, 11, 2));
        users.add(anna);
        users.add(boris);
        users.add(vera);

        System.out.println("Все пользователи:");
        for (User user : users.findAll()) {
            System.out.println(user + " -> " + user.describePermissions());
        }
        System.out.println("Участники: " + users.findByRole(UserRole.MEMBER).size());
        System.out.println("Поиск 'шах': " + users.findByNameContains("шах"));

        Tournament tournament = new Tournament(1L, "Осенний кубок", LocalDate.of(2026, 11, 20));
        tournament.addParticipant(anna);
        tournament.addParticipant(boris);
        tournament.addParticipant(vera);
        tournament.addGame(new Game(anna, boris, GameResult.WHITE_WIN, LocalDate.now()));
        tournament.addGame(new Game(boris, vera, GameResult.DRAW, LocalDate.now()));
        tournament.addGame(new Game(vera, anna, GameResult.BLACK_WIN, LocalDate.now()));

        StandingsService standings = new StandingsService();
        System.out.println(standings.formatStandings(tournament));

        new ParallelStandingsCalculator(standings, 3).calculate(tournament);
    }
}
