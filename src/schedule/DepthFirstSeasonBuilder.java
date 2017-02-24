package schedule;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;

import static schedule.ScheduleGenerator.getMaxRepeatMatchups;

/**
 * Creates one permutation at a time to create all permutations
 * // TODO : DOESN'T WORK FOR MANY COMBINATIONS THAT ARE NOT 4 TEAMS 14 WEEKS
 * // TODO : DOESN'T FILTER OUT WEEKS IS 5 5 3 REPEAT MATCHUPS
 */
public class DepthFirstSeasonBuilder {

    // Thoughts for things to change
    // Create possible seasons -> queue -> validate season -> queue -> write to file / database

    private static final int MIN_NUM_TEAMS = 4;
    private static final int MIN_NUM_WEEKS = 1;

    private static int count = 0;

    /**
     * Generate all possible schedules for a season
     *
     * // TODO : WRITE TO A FILE / DATABASE / JSON
     *
     * @param numTeams
     *          Number of teams in the league. Must be even and >= 4
     * @param numWeeks
     *          Number of weeks in the season. Must be >= 1.
     */
    public static Set<List<Set<ImmutablePair<Integer, Integer>>>> generateSeasons(int numTeams, int numWeeks) {
        // Check preconditions
        assert numTeams >= MIN_NUM_TEAMS;
        assert numWeeks >= MIN_NUM_WEEKS;

        // All potential weeks, as a list
        final List<Set<ImmutablePair<Integer, Integer>>> allPotentialWeeks = new ArrayList<>(ScheduleGenerator.getPossibleWeeks(numTeams));

        final int maxNumRepeatMatchups = ScheduleGenerator.getMaxRepeatMatchups(numTeams, numWeeks);
        final int minNumRepeatMatchups = ScheduleGenerator.getMinRepeatMatchups(numTeams, numWeeks);
        final int numPotentialWeeks = allPotentialWeeks.size();

        // Keeps track of the current attempted matchup to validate
        final int[] currentWeekToValidate = new int[numWeeks];

        // Current week to test
        List<Set<ImmutablePair<Integer, Integer>>> currentWeek;

        // TODO : CHANGE THIS. THIS IS SO I DON'T HAVE TO DEAL WITH WRITING TO A FILE RIGHT NOW
        final Set<List<Set<ImmutablePair<Integer, Integer>>>> finalSeasons = new HashSet<>();

        // TODO : CREATE CONSUMERS TO VALIDATE CREATED WEEKS

        while (currentWeekToValidate[0] < numPotentialWeeks) {

            currentWeek = new ArrayList<>(numWeeks);

            // Get the weeks for the current season
            for (int i = 0; i < numWeeks; i++) {
                currentWeek.add(i, allPotentialWeeks.get(currentWeekToValidate[i]));
            }

            // TODO: Make ASyncronous - Add to queue
            // Maybe add to a queue instead to later be added if necessary
            DepthFirstSeasonBuilder.addIfValid(currentWeek, finalSeasons, maxNumRepeatMatchups, minNumRepeatMatchups);

            // Increment Weeks returning false means that there was overflow and all possible weeks have been
            // iterated through
            if (! DepthFirstSeasonBuilder.incrementWeeks(currentWeekToValidate, numPotentialWeeks,
                                                        maxNumRepeatMatchups, minNumRepeatMatchups) ) {
                return finalSeasons;
            }
        }
        return finalSeasons;
    }

    /**
     *
     * // Returns false when all weeks are finished
     * // Threshold = number of repeat weeks ?????
     * // numPotentialWeeks = number of different weeks ?????????
     *
     *
     * @param weeks
     * @param numPotentialWeeks
     *
     * @param maxNumRepeatMatchups
     *          Number of repeat matchups allowed
     * @return
     */
    private static boolean incrementWeeks(int[] weeks, int numPotentialWeeks,
                                          int maxNumRepeatMatchups, int minNumRepeatMatchups) {
        // Increment weeks at final node
        // Returns false if it is not possible to continue
        if (! Util.carryAdd(weeks, weeks.length - 1, numPotentialWeeks - 1) ) {
            return false;
        }

        // Check and adjust duplicates until week is valid
        // TODO : MAKE CARRY ADD MORE EFFICIENT AND ADJUST AT INDEX OF WHERE DUPLICATE OCCURED
        // TODO : UPDATE TO INCLUDE LOWER THRESHOLD
        while (hasWrongNumOfDuplicates(weeks, maxNumRepeatMatchups, minNumRepeatMatchups)) {
            // Return false if all possible weeks have been reached
            if (! Util.carryAdd(weeks, weeks.length - 1, numPotentialWeeks - 1)) {
                return false;
            }
        };

        return true;
    }

    /**
     * Returns true if a week is repeated more than the upper threshold number of times
     * or lower than the lower threshold number of times
     *
     * // TODO : UPDATE SPEC TO MAKE IT MORE DESCRIPTIVE
     * // TODO : RETURN INDEX OF WHERE DUPLICATE OCCURED
     * // TODO : MAKE MORE GENERAL TO TAKE IN A LIST OF OBJECTS
     *
     * @param weeks
     *          Array to check for duplicates
     * @param upperThreshold
     *          Max repeats allowed in the array
     * @return
     *          True if the there is a week repeated more than the upper threshold or less than lower threshold
     */
    private static boolean hasWrongNumOfDuplicates(int[] weeks, int upperThreshold, int lowerThreshold) {
        // Check if there are too many duplicate weeks
        Map<Integer, Integer> counts = new HashMap<>();

        for (int i = 0; i < weeks.length; i++) {

            // Current value will be null if this is the first time the value is put in the map
            Integer currentValue = counts.putIfAbsent(weeks[i], 1);

            if (currentValue != null) {
                // Too many duplicates
                if (currentValue >= upperThreshold) {
                    return true;
                }
                counts.put(weeks[i], currentValue + 1);
            }
        }

        return counts.values().stream().anyMatch(c -> c < lowerThreshold);
    }

    // False if not possible to continue. Index should be in range of the array

    // Checks if week is valid and if it is, it writes it asynchornisly to the db
    private static boolean addIfValid(List<Set<ImmutablePair<Integer, Integer>>> week,
                                      // TODO : CHANGE THIS
                                      Set<List<Set<ImmutablePair<Integer, Integer>>>> finalSet,
                                      int maxNumRepeatMatchups, int minNumRepeatMatchups) {
        if (! DepthFirstSeasonBuilder.isValid(week, maxNumRepeatMatchups, minNumRepeatMatchups)) {
            return false;
        }

        count++;

        if (count % 10000 == 0) {
            System.out.println(count);
        }

        finalSet.add(week);

        return true;
    }

    // threshold is the number of same matchups allowed
    // TODO : WRITE SPEC
    private static boolean isValid(List<Set<ImmutablePair<Integer, Integer>>> weeks,
                                   int maxNumRepeatMatchups, int minNumRepeatMatchups) {

        // Check if there are too many repeat weeks (amount a week occurs >= threshold)
        Map<Integer, Integer> counts = new HashMap<>();

        // TODO : THIS SHOULDN'T BE NECESSARY. CHECK TO MAKE SURE THE RETURN CODE NEVER EXECUTES
        for (Set<ImmutablePair<Integer, Integer>> wk : weeks) {
            if (! updateCounts(wk.hashCode(), counts, maxNumRepeatMatchups)) {
                // This code should only execute on the first round through
                return false;
            }
        }

        if ( counts.values().stream().anyMatch(c -> c < minNumRepeatMatchups) ) {
            return false;
        }

        // Should go in it's own method
        final int numTeams = weeks.get(0).size() * 2;
        int numPossibleWeeks = numTeams - 1;
        for (int i = numTeams - 3; i > 1; i -= 2) {
            numPossibleWeeks *= i;
        }

        // Returns false if all weeks are the same (when less weeks than possible weeks
        if ( (weeks.size() <= numPossibleWeeks && weeks.stream().distinct().count() < weeks.size() )) {
            return false;
        }

        if (weeks.size() > numPossibleWeeks && weeks.stream().distinct().count() < numPossibleWeeks) {
            return false;
        }

        // Check if there are enough repeat matchups (a matchup occurs >= threshold)
        counts = new HashMap<>();

        for (Set<ImmutablePair<Integer, Integer>> wk : weeks) {
            for (ImmutablePair<Integer, Integer> matchup : wk) {
                if (! updateCounts(matchup.toString().hashCode(), counts, maxNumRepeatMatchups)) {
                    return false;
                }
            }
        }

        return true;
    }

    // Mutator!
    // TODO : WRITE SPEC
    private static boolean updateCounts(int key, Map<Integer, Integer> counts, int threshold) {
        Integer currentValue = counts.putIfAbsent(key, 1);

        if (currentValue != null) {
            if (currentValue > threshold) {
                return false;
            }
            counts.put(key, currentValue + 1);
        }

        return true;
    }

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        Set<List<Set<ImmutablePair<Integer, Integer>>>> finalSeasons = generateSeasons(4, 14);

        System.out.println("It took " + (System.currentTimeMillis() - startTime) + " milliseconds!");

        System.out.println("There were " + count + " potential seasons");
        System.out.println("There were " + finalSeasons.size() + " seasons in the big set");
    }
    private void printCount(Map<Integer, Integer> map, List<Integer> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println("number: " + i + ", count: " + map.get(i));
            while(i != (list.size() - 1) && list.get(i).equals(list.get(i + 1))) {
                i++;
            }
        }
    }
}
