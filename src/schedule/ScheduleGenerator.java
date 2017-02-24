package schedule;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.paukov.combinatorics3.Generator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Generates schedules for an n-team Fantasy Football League
 */
public class ScheduleGenerator {

    /**
     * Create all possible weeks for a given number of teams
     *
     * Example: numTeams = 4
     *      [(0, 1), (2, 3)]
     *      [(0, 2), (1, 3)]
     *      [(0, 3), (1, 2)]
     *
     * @param numTeams
     *              Number of teams. Must be even and >= 2.
     * @return Unmodifiable Set of Set of ImmutablePairs. Order within ImmutablePairs is arbitrary
     */
    public static Set<Set<ImmutablePair<Integer, Integer>>> getPossibleWeeks(final int numTeams) {
        // Immutability argument:
            // getPossibleMatchups returns an Immutable Set
            // Returned Set is also wrapped in an Unmodifiable Set

        // Check precondition
        assert numTeams % 2 == 0;
        assert numTeams >= 2;

        // Get all possible matchups for the given number of teams
        final Set<ImmutablePair<Integer, Integer>> possibleMatchups = getPossibleMatchups(numTeams);

        // Matchups per week
        final int numMatchups = numTeams / 2;

        // Create all possible weeks
        return Collections.unmodifiableSet(
                Generator.combination(possibleMatchups)
                    .simple(numMatchups)                // Matchups per week (# of teams / 2)
                    .stream()
                    .map(ConcurrentSkipListSet::new)    // Generator creates a list, map it to a set
                    .filter(p -> p.stream()             // Remove weeks where the same player plays
                            .filter(distinctByTwoKeys(      // multiple times
                                    ImmutablePair::getLeft,
                                    ImmutablePair::getRight))
                            .count() == numMatchups )
                    .collect(Collectors.toSet())
        );

        // Size of set returned is (numTeams - 1)!!
        // Example: numTeams = 6, |set| = 5*3*1 = 15
    }

    /**
     * Creates all possible head to head matchups for a given number of teams
     *
     * Example: numTeams = 4
     *      [0, 1], [0, 2], [0, 3]
     *      [1, 2], [1, 3], [2, 3]
     *
     * @param numTeams
     *              Number of teams. Must be even and >= 2.
     * @return Unmodifiable Set of ImmutablePairs. Right and left element of a Pair are in arbitrary order
     */
    public static Set<ImmutablePair<Integer, Integer>> getPossibleMatchups(final int numTeams) {
        // Immutability argument
            // ImmutablePairs are immutable
            // Set of Immutable Pairs is wrapped in an unmodifiable Set wrapper

        // Check preconditions
        assert numTeams % 2 == 0;
        assert numTeams >= 2;

        List<Integer> anonPlayers = new ArrayList<>();
        for (int i = 1; i <= numTeams; i++) {       // Number of teams in the league
            anonPlayers.add(i);
        }

        // Create all possible combination of matchups of the two teams
        return Collections.unmodifiableSet(
                Generator.combination(anonPlayers)
                    .simple(2)      // Teams per matchup (always 2)
                    .stream()
                    .map(matchup -> new ImmutablePair<>(matchup.get(0), matchup.get(1)))
                    .collect(Collectors.toSet())
        );

        // Size of set returned is 1 + 2 + ... + (numTeams - 1)
        // Example: numTeams = 6, |set| = 1 + 2 + 3 + 4 + 5 = 15
    }

    /**
     * Predicate for uniqueness by both functions.
     * Adapted from http://howtodoinjava.com/java-8/java-stream-distinct-examples/
     * @param keyExtractor1
     *              First function
     * @param keyExtractor2
     *              Second function
     * @return Predicate
     */
    private static <T> Predicate<T> distinctByTwoKeys(Function<? super T, Object> keyExtractor1,
                                                     Function<? super T, Object> keyExtractor2) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor1.apply(t), Boolean.TRUE) == null
                && map.putIfAbsent(keyExtractor2.apply(t), Boolean.TRUE) == null;
    }

    /**
     * Returns the maximum allowed number of repeat matchups in a season.
     * This is calculated as
     *
     *               (   Number of Weeks     )
     *    Rounded Up (  -------------------  )
     *               (  Number of Teams - 1  )
     *
     * @param numTeams
     *          Number of teams in the league. Must be even and >= 2
     * @param numWeeks
     *          Number of weeks in the season. Must be >= 1
     * @return
     *          Maximum allowed repeat matchups
     */
    public static int getMaxRepeatMatchups(final int numTeams, final int numWeeks) {
        // Check preconditions
        assert numTeams % 2 == 0;
        assert numTeams >= 2;
        assert numWeeks >= 1;

        return (int) Math.ceil( (double) numWeeks / (double) ( numTeams - 1) );
    }

    /**
     * Returns the maximum allowed number of repeat matchups in a season.
     * This is calculated as
     *
     *    Min Repeat Matchups = Max Repeat Matchups - 1
     *
     *    Unless ( numWeeks % Max Repeat Matchups ) == 0. Then Min Repeat Matchups = Max Repeat Matchups
     *
     * @param numTeams
     *          Number of teams in the league. Must be even and >= 2
     * @param numWeeks
     *          Number of weeks in the season. Must be >= 1
     * @return
     *          Minimum allowed repeat matchups
     */
    public static int getMinRepeatMatchups(final int numTeams, final int numWeeks) {
        int maxRepeatMatchups = getMaxRepeatMatchups(numTeams, numWeeks);

        // Must be the same number of repeat matchups for everyone
        if (numWeeks % (numTeams - 1) == 0 || maxRepeatMatchups == 1) {
            return maxRepeatMatchups;
        }

        return maxRepeatMatchups -1;
    }

}
