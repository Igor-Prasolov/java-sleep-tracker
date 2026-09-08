package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class AnalyzerMaxSession implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long max = Math.toIntExact(sessions.stream()
                .filter(session -> session.getStart() != null
                        && session.getEnd() != null)
                .mapToLong(session
                        -> Duration.between(session.getStart(), session.getEnd()).toMinutes())
                .max()
                .orElse(0));
        return new SleepAnalysisResult("Максимальная продолжительность сна", max);
    }
}
