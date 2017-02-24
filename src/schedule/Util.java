package schedule;

/**
 * Utility class for various schedule generation methods
 */
public class Util {

    // TODO : CHECKING FOR WHEN CARRY ADD RETURNED FALSE IN CALLING FUNCTION. I CHANGED
    // TODO : THE OVERFLOW, WHICH MAY HAVE CAUSED THE OTHER THINGS TO SCREW UP

    /**
     * The array acts as a number with a base of maxVal. carryAdd then increments
     * this array. Returns false if it overflows.
     *
     * All elements with a higher index than the one given are reset to zero.
     * The element located at the index. If any value is above maxVal, it may or may
     * not be changed. Array cannot contain negative numbers. Overflows to all zeros.
     *
     * Example:
     *      carryAdd( [1, 3, 5, 4], 3, 5) ===> [1, 3, 5, 5] && true
     *      carryAdd( [1, 3, 5, 4], 2, 5) ===> [1, 4, 0, 0] && true
     *      carryAdd( [1, 3, 5, 4], 1, 5) ===> [1, 4, 0, 0] && true
     *      carryAdd( [2, 3, 3, 3], 3, 3) ===> [3, 0, 0, 0] && true
     *      carryAdd( [3, 3, 3, 3], 3, 3) ===> [0, 0, 0, 0] && false
     *
     * @param a
     *          array to mutate and increment
     * @param index
     *          index to increment at.
     * @param maxVal
     *          must be > 0
     * @return true if method was successful. False if overflow occurs
     */
    public static boolean carryAdd(final int[] a, int index, final int maxVal) {
        // Check preconditions
        // TODO : COMMENT THIS MORE
        // TODO : MAKE INDEX FINAL
        assert index >= 0;
        assert index < a.length;
        assert maxVal > 0;

        if (a.length == 0) {
            return false;
        }

        // Reset all elements after index to 0
        for (int i = index + 1; i < a.length; i++) {
            a[i] = 0;
        }

        a[index]++;
        while (index >=0) {
            if (a[index] > maxVal) {
                if (index == 0) {
                    a[index] = 0;
                    return false;
                }
                a[index] = 0;
                a[index -1]++;
            }
            else {
                return true;
            }
            index--;
        }

        // Code should never reach here
        System.out.println("Carry Add reached code it should not have");
        throw new RuntimeException("Carry Add reached code it should not have");
    }
}
