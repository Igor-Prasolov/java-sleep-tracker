package ru.yandex.practicum.sleeptracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SleepTrackerApp {
    public static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private static final List<Function<List<SleepingSession>, SleepAnalysisResult>> ANALYZERS = List.of(
            new AnalyzerCountSession(),
            new AnalyzerMinSession(),
            new AnalyzerMaxSession(),
            new AnalyzerAverageSession(),
            new AnalyzerBadSession(),
            new AnalyzerSleeplessNight(),
            new AnalyzerChronotype()
    );

    public static void main(String[] args) {

        SleepTrackerApp app = new SleepTrackerApp();
        try {
            List<String> lines = app.allLinesRead(args[0]);
            List<SleepingSession> sessions = app.parse(lines);

            for (Function<List<SleepingSession>, SleepAnalysisResult> analyzer : ANALYZERS) {
                SleepAnalysisResult result = analyzer.apply(sessions);
                System.out.println(result.getDescription() + ": " + result.getValue());
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }

    private List<String> allLinesRead(String fileName) throws IOException {
        return new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(
                                getClass().getResourceAsStream("/" + fileName)
                        )
                )
        ).lines().toList();
    }

    private List<SleepingSession> parse(List<String> lines) {
        return lines.stream()
                .map(line -> line.split(";"))
                .map(parts -> {
                    LocalDateTime start = LocalDateTime.parse(parts[0].trim(), DATE_TIME_FORMATTER);
                    LocalDateTime end = LocalDateTime.parse(parts[1].trim(), DATE_TIME_FORMATTER);
                    Quality quality = Quality.valueOf(parts[2].trim());
                    return new SleepingSession(start, end, quality);
                })
                .collect(Collectors.toList());
    }
}