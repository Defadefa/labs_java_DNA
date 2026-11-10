package org.chessclub.service;

import org.chessclub.model.Game;
import org.chessclub.model.MemberUser;
import org.chessclub.model.Tournament;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StandingsService {

    public double calculatePoints(Tournament tournament, MemberUser player) {
        double points = 0.0;
        for (Game game : tournament.getGames()) {
            boolean isWhite = game.getWhite().getId() == player.getId();
            boolean isBlack = game.getBlack().getId() == player.getId();
            if (!isWhite && !isBlack) {
                continue;
            }
            switch (game.getResult()) {
                case WHITE_WIN:
                    if (isWhite) {
                        points += 1.0;
                    }
                    break;
                case BLACK_WIN:
                    if (isBlack) {
                        points += 1.0;
                    }
                    break;
                case DRAW:
                    points += 0.5;
                    break;
                default:
                    break;
            }
        }
        return points;
    }

    public Map<MemberUser, Double> buildStandings(Tournament tournament) {
        List<MemberUser> players = new ArrayList<>(tournament.getParticipants());
        Map<MemberUser, Double> points = new LinkedHashMap<>();
        for (MemberUser player : players) {
            points.put(player, calculatePoints(tournament, player));
        }
        players.sort((a, b) -> {
            int byPoints = Double.compare(points.get(b), points.get(a));
            if (byPoints != 0) {
                return byPoints;
            }
            return Integer.compare(b.getRating(), a.getRating());
        });
        Map<MemberUser, Double> sorted = new LinkedHashMap<>();
        for (MemberUser player : players) {
            sorted.put(player, points.get(player));
        }
        return sorted;
    }

    public String formatStandings(Tournament tournament) {
        StringBuilder sb = new StringBuilder();
        sb.append("Турнир: ").append(tournament.getName()).append("\n");
        int place = 1;
        for (Map.Entry<MemberUser, Double> entry : buildStandings(tournament).entrySet()) {
            sb.append(String.format("%d. %s - %.1f%n",
                    place++, entry.getKey().getFullName(), entry.getValue()));
        }
        return sb.toString();
    }
}
