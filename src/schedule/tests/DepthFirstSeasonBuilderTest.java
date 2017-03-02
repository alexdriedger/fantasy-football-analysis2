package schedule.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import schedule.DepthFirstSeasonBuilder;

import java.util.Arrays;
import java.util.Collection;

/**
 * tests for the DepthFirstSeasonBuilder
 */
@RunWith(Parameterized.class)
public class DepthFirstSeasonBuilderTest {

    private int numTeams;
    private int numWeeks;
    private int expectedResult;

    @Parameterized.Parameters(name = "{0} teams, {1} weeks = {2} possible seasons")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {4, 1, 3},
                {4, 2, 6},
                {4, 3, 6},
                {4, 4, 36},
                {4, 5, 90},
                {4, 6, 90},
                {4, 13, 270270},
                {4, 14, 756756},
                {6, 1, 15},
                {6, 2, 210}
        });
    }

    public DepthFirstSeasonBuilderTest(int numTeams, int numWeeks, int expectedResult) {
        this.numTeams = numTeams;
        this.numWeeks = numWeeks;
        this.expectedResult = expectedResult;
    }

    @Test
    public void testGenerateSeasonsSize() {
        Assert.assertEquals(expectedResult, DepthFirstSeasonBuilder.generateSeasons(numTeams, numWeeks).size());
    }
}
