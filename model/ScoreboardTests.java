package model;

/**
 * ScoreboardTests.java
 *
 * Plain-Java unit tests for model.Scoreboard.
 * No JUnit required — compile and run with a standard JDK.
 *
 * Compile:
 *   javac -d out model/Scoreboard.java ScoreboardTests.java
 * Run:
 *   java -cp out ScoreboardTests
 */
public class ScoreboardTests {

    // ------------------------------------------------------------------ //
    //  Lightweight test harness
    // ------------------------------------------------------------------ //

    private static int passed = 0;
    private static int failed = 0;

    /** Passes when condition is true. */
    private static void assertTrue(String testName, boolean condition) {
        if (condition) {
            System.out.println("PASS: " + testName);
            passed++;
        } else {
            System.out.println("FAIL: " + testName + " — condition was false");
            failed++;
        }
    }

    /** Passes when two ints are equal. */
    private static void assertEquals(String testName, int expected, int actual) {
        if (expected == actual) {
            System.out.println("PASS: " + testName);
            passed++;
        } else {
            System.out.println("FAIL: " + testName
                    + " — expected " + expected + " but got " + actual);
            failed++;
        }
    }

    /** Passes when two Strings are equal (null-safe). */
    private static void assertEquals(String testName, String expected, String actual) {
        if (expected == null ? actual == null : expected.equals(actual)) {
            System.out.println("PASS: " + testName);
            passed++;
        } else {
            System.out.println("FAIL: " + testName
                    + " — expected \"" + expected + "\" but got \"" + actual + "\"");
            failed++;
        }
    }

    /**
     * Passes when the block throws the expected exception type.
     * Fails if no exception is thrown, or if a different type is thrown.
     */
    private static void assertThrows(String testName,
                                     Class<? extends Exception> expected,
                                     Runnable block) {
        try {
            block.run();
            System.out.println("FAIL: " + testName
                    + " — expected " + expected.getSimpleName() + " but no exception was thrown");
            failed++;
        } catch (Exception e) {
            if (expected.isInstance(e)) {
                System.out.println("PASS: " + testName);
                passed++;
            } else {
                System.out.println("FAIL: " + testName
                        + " — expected " + expected.getSimpleName()
                        + " but got " + e.getClass().getSimpleName()
                        + ": " + e.getMessage());
                failed++;
            }
        }
    }

    // ------------------------------------------------------------------ //
    //  Main entry point
    // ------------------------------------------------------------------ //

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  ScoreboardTests — American Football");
        System.out.println("========================================\n");

        testSetTeamNames();
        testAddPointsHappyPath();
        testScoreAccumulation();
        testUndoSingleAction();
        testUndoMultipleActions();
        testUndoRestoresCorrectTeam();
        testClearResetsScores();
        testClearPreservesNames();
        testClearResetsHistory();
        testBlankHomeNameThrows();
        testBlankAwayNameThrows();
        testNullHomeNameThrows();
        testNullAwayNameThrows();
        testUndoOnEmptyHistoryThrows();
        testScoringBeforeNamesThrows();
        testNegativePointsThrows();
        testZeroPointsThrows();
        testLastActionDescriptionAfterHome();
        testLastActionDescriptionAfterUndo();
        testLastActionDescriptionAfterRestart();
        testHasTeamNameFalseByDefault();
        testHasTeamNameTrueAfterSet();
        testHasHistoryFalseByDefault();
        testHasHistoryTrueAfterScore();
        testHasHistoryFalseAfterUndo();

        System.out.println("\n========================================");
        System.out.println("  Results: " + passed + " passed, " + failed + " failed");
        System.out.println("========================================");
    }

    // ------------------------------------------------------------------ //
    //  1. Happy Paths — Team Names
    // ------------------------------------------------------------------ //

    private static void testSetTeamNames() {
        Scoreboard sb = new Scoreboard();
        sb.setTeamNames("Eagles", "Cowboys");
        assertEquals("setTeamNames — home name stored correctly", "Eagles", sb.getHomeName());
        assertEquals("setTeamNames — away name stored correctly", "Cowboys", sb.getAwayName());
    }

    // ------------------------------------------------------------------ //
    //  2. Happy Paths — Adding Points
    // ------------------------------------------------------------------ //

    private static void testAddPointsHappyPath() {
        Scoreboard sb = new Scoreboard();
        sb.setTeamNames("Eagles", "Cowboys");

        sb.addPointsToHome(6);
        assertEquals("addPointsToHome +6 — home score is 6", 6, sb.getHomeScore());
        assertEquals("addPointsToHome +6 — away score unchanged", 0, sb.getAwayScore());

        sb.addPointsToAway(3);
        assertEquals("addPointsToAway +3 — away score is 3", 3, sb.getAwayScore());
        assertEquals("addPointsToAway +3 — home score unchanged", 6, sb.getHomeScore());

        sb.addPointsToHome(2);
        assertEquals("addPointsToHome +2 — home score is 8", 8, sb.getHomeScore());

        sb.addPointsToHome(1);
        assertEquals("addPointsToHome +1 — home score is 9", 9, sb.getHomeScore());
    }

    private static void testScoreAccumulation() {
        Scoreboard sb = new Scoreboard();
        sb.setTeamNames("Bears", "Packers");
        sb.addPointsToHome(6);
        sb.addPointsToHome(1);
        sb.addPointsToHome(3);
        assertEquals("Score accumulation — home score after 6+1+3", 10, sb.getHomeScore());
    }

    // ------------------------------------------------------------------ //
    //  3. Undo Functionality
    // ------------------------------------------------------------------ //

    private static void testUndoSingleAction() {
        Scoreboard sb = new Scoreboard();
        sb.setTeamNames("Raiders", "Chiefs");
        sb.addPointsToHome(6);
        sb.undoLast();
        assertEquals("undoLast — home score reverts to 0", 0, sb.getHomeScore());
    }

    private static void testUndoMultipleActions() {
        Scoreboard sb = new Scoreboard();
        sb.setTeamNames("Dolphins", "Jets");
        sb.addPointsToHome(6);
        sb.addPointsToHome(1);
        sb.addPointsToAway(3);
        // Undo away +3
        sb.undoLast();
        assertEquals("undoLast (multiple) — away score reverts to 0", 0, sb.getAwayScore());
        assertEquals("undoLast (multiple) — home score unaffected", 7, sb.getHomeScore());
        // Undo home +1
        sb.undoLast();
        assertEquals("undoLast (multiple) — home score reverts to 6", 6, sb.getHomeScore());
    }

    private static void testUndoRestoresCorrectTeam() {
        Scoreboard sb = new Scoreboard();
        sb.setTeamNames("Broncos", "Chargers");
        sb.addPointsToAway(6);
        sb.undoLast();
        assertEquals("undoLast — correct team (away) score restored to 0", 0, sb.getAwayScore());
        assertEquals("undoLast — home score still 0", 0, sb.getHomeScore());
    }

    // ------------------------------------------------------------------ //
    //  4. Clear / Restart
    // ------------------------------------------------------------------ //

    private static void testClearResetsScores() {
        Scoreboard sb = new Scoreboard();
        sb.setTeamNames("49ers", "Seahawks");
        sb.addPointsToHome(6);
        sb.addPointsToAway(3);
        sb.restart();
        assertEquals("restart — home score resets to 0", 0, sb.getHomeScore());
        assertEquals("restart — away score resets to 0", 0, sb.getAwayScore());
    }

    private static void testClearPreservesNames() {
        Scoreboard sb = new Scoreboard();
        sb.setTeamNames("Rams", "Cardinals");
        sb.restart();
        assertEquals("restart — home name preserved", "Rams", sb.getHomeName());
        assertEquals("restart — away name preserved", "Cardinals", sb.getAwayName());
    }

    private static void testClearResetsHistory() {
        Scoreboard sb = new Scoreboard();
        sb.setTeamNames("Saints", "Panthers");
        sb.addPointsToHome(6);
        sb.restart();
        // After restart the stack is empty — undoLast should throw
        assertThrows("restart — history is empty after restart",
                IllegalStateException.class, sb::undoLast);
    }

    // ------------------------------------------------------------------ //
    //  5. Error Cases
    // ------------------------------------------------------------------ //

    private static void testBlankHomeNameThrows() {
        Scoreboard sb = new Scoreboard();
        assertThrows("setTeamNames — blank home name throws IllegalArgumentException",
                IllegalArgumentException.class,
                () -> sb.setTeamNames("  ", "Cowboys"));
    }

    private static void testBlankAwayNameThrows() {
        Scoreboard sb = new Scoreboard();
        assertThrows("setTeamNames — blank away name throws IllegalArgumentException",
                IllegalArgumentException.class,
                () -> sb.setTeamNames("Eagles", ""));
    }

    private static void testNullHomeNameThrows() {
        Scoreboard sb = new Scoreboard();
        assertThrows("setTeamNames — null home name throws IllegalArgumentException",
                IllegalArgumentException.class,
                () -> sb.setTeamNames(null, "Cowboys"));
    }

    private static void testNullAwayNameThrows() {
        Scoreboard sb = new Scoreboard();
        assertThrows("setTeamNames — null away name throws IllegalArgumentException",
                IllegalArgumentException.class,
                () -> sb.setTeamNames("Eagles", null));
    }

    private static void testUndoOnEmptyHistoryThrows() {
        Scoreboard sb = new Scoreboard();
        sb.setTeamNames("Giants", "Falcons");
        assertThrows("undoLast — empty history throws IllegalStateException",
                IllegalStateException.class, sb::undoLast);
    }

    private static void testScoringBeforeNamesThrows() {
        Scoreboard sb = new Scoreboard();
        // No names set — should throw IllegalStateException
        assertThrows("addPointsToHome — no names set throws IllegalStateException",
                IllegalStateException.class,
                () -> sb.addPointsToHome(6));
        assertThrows("addPointsToAway — no names set throws IllegalStateException",
                IllegalStateException.class,
                () -> sb.addPointsToAway(6));
    }

    private static void testNegativePointsThrows() {
        Scoreboard sb = new Scoreboard();
        sb.setTeamNames("Vikings", "Lions");
        assertThrows("addPointsToHome — negative points throws IllegalArgumentException",
                IllegalArgumentException.class,
                () -> sb.addPointsToHome(-1));
    }

    private static void testZeroPointsThrows() {
        Scoreboard sb = new Scoreboard();
        sb.setTeamNames("Texans", "Colts");
        assertThrows("addPointsToHome — zero points throws IllegalArgumentException",
                IllegalArgumentException.class,
                () -> sb.addPointsToHome(0));
    }

    // ------------------------------------------------------------------ //
    //  6. Last Action Description
    // ------------------------------------------------------------------ //

    private static void testLastActionDescriptionAfterHome() {
        Scoreboard sb = new Scoreboard();
        sb.setTeamNames("Bills", "Patriots");
        sb.addPointsToHome(6);
        String desc = sb.getLastActionDescription();
        assertTrue("lastActionDescription — contains team name after home score",
                desc != null && desc.contains("Bills"));
    }

    private static void testLastActionDescriptionAfterUndo() {
        Scoreboard sb = new Scoreboard();
        sb.setTeamNames("Steelers", "Ravens");
        sb.addPointsToAway(3);
        sb.undoLast();
        String desc = sb.getLastActionDescription();
        assertTrue("lastActionDescription — contains 'Undo' after undoLast",
                desc != null && desc.toLowerCase().contains("undo"));
    }

    private static void testLastActionDescriptionAfterRestart() {
        Scoreboard sb = new Scoreboard();
        sb.setTeamNames("Bengals", "Browns");
        sb.addPointsToHome(6);
        sb.restart();
        String desc = sb.getLastActionDescription();
        assertTrue("lastActionDescription — non-null and non-empty after restart",
                desc != null && !desc.trim().isEmpty());
    }

    // ------------------------------------------------------------------ //
    //  7. hasTeamName / hasHistory Flags
    // ------------------------------------------------------------------ //

    private static void testHasTeamNameFalseByDefault() {
        Scoreboard sb = new Scoreboard();
        assertTrue("hasTeamName — false before names are set", !sb.hasTeamName());
    }

    private static void testHasTeamNameTrueAfterSet() {
        Scoreboard sb = new Scoreboard();
        sb.setTeamNames("Jaguars", "Titans");
        assertTrue("hasTeamName — true after names are set", sb.hasTeamName());
    }

    private static void testHasHistoryFalseByDefault() {
        Scoreboard sb = new Scoreboard();
        assertTrue("hasHistory — false before any scoring", !sb.hasHistory());
    }

    private static void testHasHistoryTrueAfterScore() {
        Scoreboard sb = new Scoreboard();
        sb.setTeamNames("Washington", "Cowboys");
        sb.addPointsToHome(3);
        assertTrue("hasHistory — true after a score is recorded", sb.hasHistory());
    }

    private static void testHasHistoryFalseAfterUndo() {
        Scoreboard sb = new Scoreboard();
        sb.setTeamNames("Commanders", "Giants");
        sb.addPointsToHome(3);
        sb.undoLast();
        assertTrue("hasHistory — false after last action is undone", !sb.hasHistory());
    }
}