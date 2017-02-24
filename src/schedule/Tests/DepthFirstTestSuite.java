package schedule.Tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import schedule.Tests.CarryAddTest;
import schedule.Tests.GetMaxRepeatMatchupsTest;
import schedule.Tests.GetPossibleMatchupsTest;
import schedule.Tests.GetPossibleWeeksTest;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        CarryAddTest.class,
        GetMaxRepeatMatchupsTest.class,
        GetPossibleWeeksTest.class,
        GetPossibleMatchupsTest.class
})

/**
 * Test suite for Depth First Season Builder
 */
public class DepthFirstTestSuite {}
