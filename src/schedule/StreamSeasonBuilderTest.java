package schedule;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

/**
 * Tests for {@link StreamSeasonBuilder}
 */
public class StreamSeasonBuilderTest {

    @Test
    public void fourTeamGenerateSeasonsSizeTest() {
        Set<SeasonBuilder> actual = StreamSeasonBuilder.generateSeasons(4, 14);
        Assert.assertEquals(756756, actual.size());
    }
}
