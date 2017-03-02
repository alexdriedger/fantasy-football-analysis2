package schedule.tests;

import org.junit.Assert;
import org.junit.Test;
import schedule.SeasonBuilder;
import schedule.StreamSeasonBuilder;

import java.util.Set;

/**
 * tests for {@link StreamSeasonBuilder}
 */
public class StreamSeasonBuilderTest {

    @Test
    public void fourTeamGenerateSeasonsSizeTest() {
        long startTime = System.currentTimeMillis();
        Set<SeasonBuilder> actual = StreamSeasonBuilder.generateSeasons(4, 14);
        Assert.assertEquals(756756, actual.size());

        System.out.println("Total time was " + (System.currentTimeMillis() - startTime) + " milliseconds");
    }

    @Test
    public void sixTeamGenerateSeasonsTest() {
        long startTime = System.currentTimeMillis();
        // Out of memory error after 18 minutes
        Set<SeasonBuilder> actual = StreamSeasonBuilder.generateSeasons(6, 5);
        System.out.println("Size of set is " + actual.size());

        System.out.println("Total time was " + (System.currentTimeMillis() - startTime) + " milliseconds");
    }
}
