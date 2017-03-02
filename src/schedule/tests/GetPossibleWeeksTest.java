package schedule.Tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import schedule.ScheduleGenerator;

import java.util.Arrays;
import java.util.Collection;

/**
 * Tests for getPossibleSeasons
 */
@RunWith(Parameterized.class)
public class GetPossibleWeeksTest {

    private int numTeams;
    private int expectedSize;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {2, 1},
                {4, 3},
                {6, 15},
                {8, 105},
                {10, 945},
                //{12, 10395}
        });
    }

    public GetPossibleWeeksTest(int numTeams, int expectedSize) {
        this.numTeams = numTeams;
        this.expectedSize = expectedSize;
    }

    @Test
    public void testGetPossibleWeeksSize() {
        Assert.assertEquals(expectedSize, ScheduleGenerator.getPossibleWeeks(numTeams).size());
    }
}
