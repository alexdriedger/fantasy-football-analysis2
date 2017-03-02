package schedule.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import schedule.Util;

import java.util.Arrays;
import java.util.Collection;

/**
 * tests for carryAdd
 */
@RunWith(Parameterized.class)
public class CarryAddTest {

    private int[] input;
    private int[] expectedArray;
    private boolean expectedBoolean;
    private int index;
    private int maxVal;

    // TODO : CHANGE NAME CONVENTION
    @Parameterized.Parameters(name = "{index}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // 0 - Standard test case
                {   new int[]{1, 3, 5, 4},
                    new int[]{1, 3, 5, 5}, true, 3, 5},

                // 1 - Carry add not at the last element
                {   new int[]{1, 3, 5, 4},
                    new int[]{1, 4, 0, 0}, true, 2, 5},

                // 2 - Carry add on first element
                {   new int[]{1, 3, 5, 4},
                    new int[]{2, 0, 0, 0}, true, 0, 7},

                // 3 - Reset in the middle of array
                {   new int[]{1, 3, 5, 4},
                    new int[]{1, 4, 0, 0}, true, 1, 5},

                // 4 - Carry from the last element to the first element
                {   new int[]{2, 3, 3},
                    new int[]{3, 0, 0}, true, 2, 3},

                // 5 - Array is at limit. Larger num than maxVal
                {   new int[]{4, 4, 17, 4, 4},
                    new int[]{0, 0, 0,  0, 0}, false, 3, 4},

                // 6 - First increment
                {   new int[]{0, 0, 0, 0, 0},
                    new int[]{0, 0, 0, 0, 1}, true, 4, 18},

                // 7 - Empty Array
                {   new int[]{},
                    new int[]{}, false, 0, 11},

                // 8 - Single element array no overflow
                {   new int[]{4},
                    new int[]{5}, true, 0, 5},

                // 9 - Single element array with overflow
                {   new int[]{17},
                    new int[]{0}, false, 0, 17},

                // 10- Binary standard
                {   new int[]{1, 0, 0, 1, 1, 0},
                    new int[]{1, 0, 0, 1, 1, 1}, true, 5, 1},

                // 11 - Binary carry add
                {   new int[]{1, 0, 0, 1, 1, 1},
                    new int[]{1, 0, 1, 0, 0, 0}, true, 5, 1},

                // 12 - Binary carry add not at last element
                {   new int[]{1, 0, 0, 1, 1, 0},
                    new int[]{1, 0, 1, 0, 0, 0}, true, 4, 1},

                // 13 - Binary overflow
                {   new int[]{1, 1, 1, 1, 1, 1},
                    new int[]{0, 0, 0, 0, 0, 0}, false, 3, 1},

                // 14 - Non-one overflow
                {   new int[]{3, 3, 3, 3},
                    new int[]{0, 0, 0, 0}, false, 3, 3},

                // 15 - 14 week season
                {   new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
                    new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, false, 13, 3}

        });
    }

    public CarryAddTest(int[] input, int[] expectedArray, boolean expectedBoolean,
                        int index, int maxVal) {
        this.input = input;
        this.expectedArray = expectedArray;
        this.expectedBoolean = expectedBoolean;
        this.index = index;
        this.maxVal = maxVal;
    }

    @Test
    public void testCarryAdd() {
        // carryAdd mutates array
        boolean actualBoolean = Util.carryAdd(input, index, maxVal);

        Assert.assertArrayEquals(expectedArray, input);
        Assert.assertEquals(expectedBoolean, actualBoolean);
    }
}
