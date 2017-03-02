package schedule.Tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import schedule.ScheduleGenerator;

import java.util.Arrays;
import java.util.Collection;

/**
 * Tests for getMaxRepeatMatchups
 */
@RunWith(Parameterized.class)
public class GetMaxRepeatMatchupsTest {

    private int numTeams;
    private int numWeeks;
    private int expectedResult;

    @Parameterized.Parameters(name = "{0} teams, {1} weeks = {2} max matchups")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {2, 1, 1},
                {2, 3, 3},
                {4, 14, 5},
                {4, 2, 1},
                {4, 3, 1},
                {4, 15, 5},
                {4, 16, 6},
                {10, 10, 2},
                {10, 9, 1},
                {10, 11, 2}
        });
    }

    public GetMaxRepeatMatchupsTest(int numTeams, int numWeeks, int expectedResult) {
        this.numTeams = numTeams;
        this.numWeeks = numWeeks;
        this.expectedResult = expectedResult;
    }

    @Test
    public void testGetPossibleWeeksSize() {
        Assert.assertEquals(expectedResult, ScheduleGenerator.getMaxRepeatMatchups(numTeams, numWeeks));
    }
}
