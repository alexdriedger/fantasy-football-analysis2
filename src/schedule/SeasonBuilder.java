package schedule;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

/**
 * Util interface for creating Seasons
 */
public interface SeasonBuilder {

    /**
     * Return a set of {@link SeasonBuilder} such that:
     *
     * Current Weeks + One of the PossibleWeeks
     *
     * For all possible weeks
     *
     * @param possibleWeeks
     *              Weeks to add
     * @return Set of {@link SeasonBuilder}
     */
    Set<SeasonBuilder> addWeeks(Set<Set<ImmutablePair<Integer, Integer>>> possibleWeeks );

    /**
     * Returns a new {@link SeasonBuilder} with a new week added.
     *
     * @param newWeek
     *        week to be added. Must be a valid week (validWeek must return true)
     * @return New SeasonBuilder with a new week added
     */
    SeasonBuilder addWeek(final Set<ImmutablePair<Integer, Integer>> newWeek);

    /**
     * Determines if a week can be added to this without violating
     * the {@link SeasonBuilder}s schedule creation rules
     *
     * @param newWeek
     *          week to check
     * @return true iff week can be added to the SeasonBuilder
     */
    boolean validWeek(final Set<ImmutablePair<Integer, Integer>> newWeek);

    /**
     * Returns the weeks in the {@link SeasonBuilder}
     *
     * @return weeks in the SeasonBuilder
     */
    ConcurrentMap<Integer, Set<ImmutablePair<Integer, Integer>>> getWeeks();

    /**
     * Returns possible matchups which are still allowed to occur for this
     *
     * @return possible matchups
     */
    ConcurrentMap<Integer, Integer> getPossibleMatchups();

    /**
     * Returns a {@link Season} representation of this {@link SeasonBuilder} object.
     *
     * @return {@link Season} representation
     */
    Season toSeason();

    /**
     * Returns the number of weeks in the season
     *
     * @return Weeks in the Season
     */
    int getWeeksInSeason();

    // Change or get rid of this
    Stream<SeasonBuilder> generateWeeks(final Set<Set<ImmutablePair<Integer, Integer>>> possibleWeeks);
}
