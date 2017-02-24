package schedule.Tests;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import schedule.ScheduleGenerator;

import java.util.Set;

/**
 * Test for ScheduleGenerator
 */
public class GetPossibleMatchupsTest {

    private Set<ImmutablePair<Integer, Integer>> actual;

    @Test
    public void fourTeamGetPossibleMatchupsTest() {
        actual = ScheduleGenerator.getPossibleMatchups(4);

        // Check all expected matchups exist
        Assert.assertTrue(actual.contains(new ImmutablePair<>(1, 2)) || actual.contains(new ImmutablePair<>(2, 1)));
        Assert.assertTrue(actual.contains(new ImmutablePair<>(1, 3)) || actual.contains(new ImmutablePair<>(3, 1)));
        Assert.assertTrue(actual.contains(new ImmutablePair<>(1, 4)) || actual.contains(new ImmutablePair<>(4, 1)));
        Assert.assertTrue(actual.contains(new ImmutablePair<>(2, 3)) || actual.contains(new ImmutablePair<>(3, 2)));
        Assert.assertTrue(actual.contains(new ImmutablePair<>(2, 4)) || actual.contains(new ImmutablePair<>(4, 2)));
        Assert.assertTrue(actual.contains(new ImmutablePair<>(3, 4)) || actual.contains(new ImmutablePair<>(4, 3)));

        // Check there are no extra matchups
        Assert.assertEquals(6, actual.size());
    }

    @Test
    public void twoTeamGetPossibleMatchupsTest() {
        actual = ScheduleGenerator.getPossibleMatchups(2);

        // Check all expected matchups exist
        Assert.assertTrue(actual.contains(new ImmutablePair<>(1, 2)) || actual.contains(new ImmutablePair<>(2, 1)));

        // Check there are no extra matchups
        Assert.assertEquals(1, actual.size());
    }

    @Test
    public void sixTeamGetPossibleMatchupsTest() {
        actual = ScheduleGenerator.getPossibleMatchups(6);

        Assert.assertEquals(15, actual.size());
    }

    @Test
    public void tenTeamGetPossibleMatchupsTest() {
        actual = ScheduleGenerator.getPossibleMatchups(10);

        Assert.assertEquals(45, actual.size());
    }
}
