package org.chessclub.concurrent;

import org.chessclub.model.MemberUser;
import org.chessclub.model.Tournament;
import org.chessclub.service.StandingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ParallelStandingsCalculator {

    private static final Logger log = LoggerFactory.getLogger(ParallelStandingsCalculator.class);

    private final StandingsService standingsService;
    private final int threadCount;

    public ParallelStandingsCalculator(StandingsService standingsService, int threadCount) {
        this.standingsService = standingsService;
        this.threadCount = threadCount;
    }

    public Map<Long, Double> calculate(Tournament tournament) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(
                threadCount, new NamedThreadFactory("standings-worker"));
        Map<Long, Double> result = new ConcurrentHashMap<>();
        List<Future<?>> futures = new ArrayList<>();

        for (MemberUser player : tournament.getParticipants()) {
            Callable<Void> task = () -> {
                double points = standingsService.calculatePoints(tournament, player);
                result.put(player.getId(), points);
                log.info("Поток {} обработал игрока {}: {} очк.",
                        Thread.currentThread().getName(), player.getFullName(), points);
                return null;
            };
            futures.add(executor.submit(task));
        }

        executor.shutdown();
        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
            executor.shutdownNow();
        }
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (java.util.concurrent.ExecutionException e) {
                throw new IllegalStateException("Ошибка в рабочем потоке", e.getCause());
            }
        }
        return result;
    }
}
