package schedule.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import schedule.ScheduleGenerator;

import java.util.Arrays;
import java.util.Collection;

/**
 * tests for getMaxRepeatMatchups
 */
@RunWith(Parameterized.class)
public class GetMinRepeatMatchupsTest {

    private int numTeams;
    private int numWeeks;
    private int expectedResult;

    @Parameterized.Parameters(name = "{0} teams, {1} weeks = {2} min matchups")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {4, 1, 1},
                {4, 2, 1},
                {4, 3, 1},
                {4, 4, 1}, // Regression test
                {4, 5, 1},
                {4, 6, 2}
//                {2, 1, 1},
//                {2, 3, 3},
//                {4, 14, 5},
//                {4, 2, 1},
//                {4, 3, 1},
//                {4, 15, 5},
//                {4, 16, 6},
//                {10, 10, 2},
//                {10, 9, 1},
//                {10, 11, 2}
        });
    }

    public GetMinRepeatMatchupsTest(int numTeams, int numWeeks, int expectedResult) {
        this.numTeams = numTeams;
        this.numWeeks = numWeeks;
        this.expectedResult = expectedResult;
    }

    @Test
    public void testGetPossibleWeeksSize() {
        Assert.assertEquals(expectedResult, ScheduleGenerator.getMinRepeatMatchups(numTeams, numWeeks));
    }
}
