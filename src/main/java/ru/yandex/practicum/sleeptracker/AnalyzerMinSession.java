package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class AnalyzerMinSession implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long min = Math.toIntExact(sessions.stream()
                .mapToLong(session
                        -> Duration.between(session.getStart(), session.getEnd()).toMinutes())
                .min()
                .getAsLong());
        return new SleepAnalysisResult("Минимальная продолжительность сна", min);
    }
}
