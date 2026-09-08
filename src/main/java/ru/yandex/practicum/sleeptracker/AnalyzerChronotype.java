package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnalyzerChronotype implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Хронотип пользователя", "голубь");
        }

        List<SleepingSession> validSession = sessions.stream()
                .filter(session -> session.getStart() != null
                        && session.getEnd() != null)
                .collect(Collectors.toList());
        if (validSession.isEmpty()) {
            return new SleepAnalysisResult("Хронотип пользователя", "голубь");
        }

        LocalDateTime firstStart = validSession.stream()
                .map(SleepingSession::getStart)
                .min(LocalDateTime::compareTo)
                .get();

        LocalDateTime lastEnd = validSession.stream()
                .map(SleepingSession::getEnd)
                .max(LocalDateTime::compareTo)
                .get();

        LocalDate startDate;
        if (firstStart.toLocalTime().isAfter(LocalTime.of(12, 0))) {
            startDate = firstStart.toLocalDate().plusDays(1);
        } else {
            startDate = firstStart.toLocalDate();
        }

        LocalDate endDate;
        if (lastEnd.toLocalTime().isAfter(LocalTime.of(12, 0))) {
            endDate = lastEnd.toLocalDate().plusDays(1);
        } else {
            endDate = lastEnd.toLocalDate();
        }

        long totalDays = Period.between(startDate, endDate).getDays() + 1;

        List<LocalDate> allDays = Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(totalDays)
                .collect(Collectors.toList());

        List<SleepingSession> nightSessions = allDays.stream()
                .map(day -> {
                    LocalDateTime nightStart = day.atTime(0, 0);
                    LocalDateTime nightEnd = day.atTime(6, 0);

                    return validSession.stream()
                            .filter(session ->
                                    session.getStart().isBefore(nightEnd) &&
                                            session.getEnd().isAfter(nightStart)
                            )
                            .findFirst()
                            .orElse(null);
                })
                .filter(session -> session != null)
                .collect(Collectors.toList());

        if (nightSessions.isEmpty()) {
            return new SleepAnalysisResult("Хронотип пользователя", "голубь");
        }

        long owlCount = nightSessions.stream()
                .filter(session -> {
                    LocalTime bedtime = session.getStart().toLocalTime();
                    LocalTime wakeTime = session.getEnd().toLocalTime();
                    return bedtime.isAfter(LocalTime.of(23, 0)) &&
                            wakeTime.isAfter(LocalTime.of(9, 0));
                })
                .count();

        long larkCount = nightSessions.stream()
                .filter(session -> {
                    LocalTime bedtime = session.getStart().toLocalTime();
                    LocalTime wakeTime = session.getEnd().toLocalTime();
                    return bedtime.isBefore(LocalTime.of(22, 0)) &&
                            wakeTime.isBefore(LocalTime.of(7, 0));
                })
                .count();

        long pigeonCount = nightSessions.size() - owlCount - larkCount;

        String chronotype;
        if (owlCount > larkCount && owlCount > pigeonCount) {
            chronotype = "сова";
        } else if (larkCount > owlCount && larkCount > pigeonCount) {
            chronotype = "жаворонок";
        } else {
            chronotype = "голубь";
        }

        String description = String.format("Хронотип пользователя (сов: %d, жаворонков: %d, голубей: %d)",
                owlCount, larkCount, pigeonCount);

        return new SleepAnalysisResult(description, chronotype);
    }
}