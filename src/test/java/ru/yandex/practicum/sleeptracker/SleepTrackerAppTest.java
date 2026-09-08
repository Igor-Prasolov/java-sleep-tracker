package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SleepTrackerAppTest {


    // AnalyzerCountSession
    @Test
    void testCountSessionShouldReturnCorrectNumberOfSessions() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0), LocalDateTime.of(2025, 10, 2, 8, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0), LocalDateTime.of(2025, 10, 3, 7, 0), Quality.NORMAL)
        );

        AnalyzerCountSession analyzer = new AnalyzerCountSession();
        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals(2, result.getValue());
    }

    @Test
    void testCountSessionShouldReturnZeroForEmptyList() {
        List<SleepingSession> sessions = List.of();

        AnalyzerCountSession analyzer = new AnalyzerCountSession();
        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals(0, result.getValue());
    }


    //  AnalyzerMinSession
    @Test
    void testMinSessionShouldReturnShortestSession() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 7, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 14, 0), LocalDateTime.of(2025, 10, 2, 15, 30), Quality.NORMAL)
        );

        AnalyzerMinSession analyzer = new AnalyzerMinSession();
        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals(90L, result.getValue());
    }

    @Test
    void testMinSessionShouldWorkWithSingleSession() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 8, 0), Quality.GOOD)
        );

        AnalyzerMinSession analyzer = new AnalyzerMinSession();
        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals(540L, result.getValue());
    }


    // AnalyzerMaxSession
    @Test
    void testMaxSessionShouldReturnLongestSession() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 7, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 22, 0), LocalDateTime.of(2025, 10, 3, 9, 0), Quality.NORMAL)
        );

        AnalyzerMaxSession analyzer = new AnalyzerMaxSession();
        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals(660L, result.getValue());
    }

    @Test
    void testMaxSessionShouldWorkWithSingleSession() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 6, 0), Quality.GOOD)
        );

        AnalyzerMaxSession analyzer = new AnalyzerMaxSession();
        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals(420L, result.getValue());
    }


    // AnalyzerAverageSession
    @Test
    void testAverageSessionShouldReturnCorrectAverage() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 8, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0), LocalDateTime.of(2025, 10, 3, 7, 0), Quality.NORMAL)
        );

        AnalyzerAverageSession analyzer = new AnalyzerAverageSession();
        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals(510.0, result.getValue());
    }

    @Test
    void testAverageSessionShouldWorkWithSingleSession() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0), LocalDateTime.of(2025, 10, 2, 7, 30), Quality.GOOD)
        );

        AnalyzerAverageSession analyzer = new AnalyzerAverageSession();
        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals(570.0, result.getValue());
    }


    // AnalyzerBadSession
    @Test
    void testBadSessionShouldCountOnlyBadQualitySessions() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 7, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0), LocalDateTime.of(2025, 10, 3, 7, 0), Quality.BAD),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 23, 0), LocalDateTime.of(2025, 10, 4, 7, 0), Quality.NORMAL),
                new SleepingSession(LocalDateTime.of(2025, 10, 4, 23, 0), LocalDateTime.of(2025, 10, 5, 7, 0), Quality.BAD)
        );

        AnalyzerBadSession analyzer = new AnalyzerBadSession();
        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals(2, result.getValue());
    }

    @Test
    void testBadSessionShouldReturnZeroIfNoBadSessions() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 7, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0), LocalDateTime.of(2025, 10, 3, 7, 0), Quality.NORMAL)
        );

        AnalyzerBadSession analyzer = new AnalyzerBadSession();
        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals(0, result.getValue());
    }


    // AnalyzerSleeplessNight
    @Test
    void testSleeplessNightShouldReturnZeroWhenAllNightsHaveSleep() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 7, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0), LocalDateTime.of(2025, 10, 3, 7, 0), Quality.GOOD)
        );

        AnalyzerSleeplessNight analyzer = new AnalyzerSleeplessNight();
        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals(0L, result.getValue());
    }

    @Test
    void testSleeplessNightShouldDetectOneSleeplessNight() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 7, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 7, 0), LocalDateTime.of(2025, 10, 3, 11, 0), Quality.GOOD)
        );

        AnalyzerSleeplessNight analyzer = new AnalyzerSleeplessNight();
        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals(1L, result.getValue());
    }

    @Test
    void testSleeplessNightShouldHandleSleepThatCrossesMidnight() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 3, 0), Quality.GOOD)
        );

        AnalyzerSleeplessNight analyzer = new AnalyzerSleeplessNight();
        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals(0L, result.getValue());
    }

    @Test
    void testSleeplessNightShouldHandleDaySleepOnly() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 14, 0), LocalDateTime.of(2025, 10, 1, 15, 0), Quality.NORMAL)
        );

        AnalyzerSleeplessNight analyzer = new AnalyzerSleeplessNight();
        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals(1L, result.getValue());
    }


    // AnalyzerChronotype
    @Test
    void testChronotypeShouldReturnOwl() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 30), LocalDateTime.of(2025, 10, 2, 9, 30), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 30), LocalDateTime.of(2025, 10, 3, 9, 30), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 23, 30), LocalDateTime.of(2025, 10, 4, 9, 30), Quality.GOOD)
        );

        AnalyzerChronotype analyzer = new AnalyzerChronotype();
        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals("сова", result.getValue());
    }

    @Test
    void testChronotypeShouldReturnLark() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 21, 30), LocalDateTime.of(2025, 10, 2, 6, 30), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 21, 30), LocalDateTime.of(2025, 10, 3, 6, 30), Quality.GOOD)
        );

        AnalyzerChronotype analyzer = new AnalyzerChronotype();
        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals("жаворонок", result.getValue());
    }

    @Test
    void testChronotypeShouldReturnPigeon() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 8, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0), LocalDateTime.of(2025, 10, 3, 8, 0), Quality.GOOD)
        );

        AnalyzerChronotype analyzer = new AnalyzerChronotype();
        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals("голубь", result.getValue());
    }

    @Test
    void testChronotypeShouldIgnoreDaytimeSleep() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 30), LocalDateTime.of(2025, 10, 2, 9, 30), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 14, 0), LocalDateTime.of(2025, 10, 2, 15, 0), Quality.NORMAL)
        );

        AnalyzerChronotype analyzer = new AnalyzerChronotype();
        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals("сова", result.getValue());
    }

    @Test
    void testChronotypeTieGoesToPigeon() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 30), LocalDateTime.of(2025, 10, 2, 9, 30), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 21, 30), LocalDateTime.of(2025, 10, 3, 6, 30), Quality.GOOD)
        );

        AnalyzerChronotype analyzer = new AnalyzerChronotype();
        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals("голубь", result.getValue());
    }
}