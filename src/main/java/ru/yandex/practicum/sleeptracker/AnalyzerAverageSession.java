package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class AnalyzerAverageSession implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        double average = sessions.stream()
                .filter(session -> session.getStart() != null
                        && session.getEnd() != null)
                .mapToLong(session
                        -> Duration.between(session.getStart(), session.getEnd()).toMinutes())
                .average()
                .orElse(0);
        return new SleepAnalysisResult("Средняя продолжительность сессии", average);
    }
}
