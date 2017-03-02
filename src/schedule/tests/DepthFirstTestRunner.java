package schedule.tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Test Runner for Depth First Season Builder
 */
public class DepthFirstTestRunner {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(DepthFirstTestSuite.class);

        // How many failures were there
        System.out.println("There were " + result.getFailureCount() + " test failures");

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        if (result.wasSuccessful()) {
            System.out.println("All tests passed");
        } else {
            System.out.println("Warning! All tests did not pass");
        }
    }
}
