package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnalyzerSleeplessNight implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult("Бессонные ночи", 0);
        }

        List<SleepingSession> validSession = sessions.stream()
                .filter(session -> session.getStart() != null
                        && session.getEnd() != null)
                .collect(Collectors.toList());
        if (validSession.isEmpty()) {
            return new SleepAnalysisResult("Бессонные ночи", 0);
        }

        LocalDateTime start = validSession.stream()
                .map(SleepingSession::getStart)
                .min(LocalDateTime::compareTo)
                .get();

        LocalDateTime end = validSession.stream()
                .map(SleepingSession::getEnd)
                .max(LocalDateTime::compareTo)
                .get();

        LocalDate startDate;
        if (start.toLocalTime().isAfter(LocalTime.of(12, 0))) {
            startDate = start.toLocalDate().plusDays(1);
        } else {
            startDate = start.toLocalDate();
        }

        LocalDate endDate;
        if (end.toLocalTime().isAfter(LocalTime.of(12, 0))) {
            endDate = end.toLocalDate().plusDays(1);
        } else {
            endDate = end.toLocalDate();
        }

        long totalDays = Period.between(startDate, endDate).getDays() + 1;

        List<LocalDate> allDays = Stream.iterate(startDate, localDate -> localDate.plusDays(1))
                .limit(totalDays)
                .collect(Collectors.toList());

        long nightWithSleep = allDays.stream()
                .filter(day -> {
                    LocalDateTime nightStart = day.atTime(0, 0);
                    LocalDateTime nightEnd = day.atTime(6, 0);
                    return validSession.stream()
                            .anyMatch(session -> session.getStart().isBefore(nightEnd)
                                    && session.getEnd().isAfter(nightStart)
                            );
                })
                .count();

        long sleeplessNights = totalDays - nightWithSleep;
        return new SleepAnalysisResult("Бессонные ночи", sleeplessNights);
    }
}