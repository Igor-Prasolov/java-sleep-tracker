package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public class AnalyzerBadSession implements Function<List<SleepingSession>, SleepAnalysisResult> {



    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        int badCount = Math.toIntExact(
                sessions.stream()
                        .filter(session -> session.getQuality() == Quality.BAD)
                        .count()
        );
        return new SleepAnalysisResult("Количество плохих сессий", badCount);
    }


}
