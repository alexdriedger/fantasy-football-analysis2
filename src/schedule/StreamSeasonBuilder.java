package schedule;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Util class for creating Seasons. Methods use of {@link java.util.stream.Stream}
 */
public class StreamSeasonBuilder implements SeasonBuilder {

    // Schedule for the Season
    // RI:
        // 1 <= key <= 16
        // 1 <= ImmutablePair Integers <= MAX_TEAMS_IN_LEAGUE
    // AF:
        // Week in a season maps to the matchups for that week
    private ConcurrentMap<Integer, Set<ImmutablePair<Integer, Integer>>> weeks;
    // TODO: Final?

    // Possible matchups for this season
    // RI:
        // Left maps to hashcode of the matchup
        // Right maps to number of times the matchup can occur again
    // AF:
        // Interpretation of the restraints on the season
        // Current restraint is that there are only a certain amount
        // of times that a team can play another team
        // Example: Team 1 can't play Team 7 10 times in the season
    private ConcurrentMap<Integer, Integer> possibleMatchups;
    // TODO: Final?

    /**
     * Construct a new empty {@link StreamSeasonBuilder}
     */
    public StreamSeasonBuilder() {
        this.weeks = new ConcurrentHashMap<>();
        this.possibleMatchups = new ConcurrentHashMap<>();
    }

    /**
     * Create a new SeasonBuilder with a new week
     *
     * @param sb
     *          SeasonBuilder to build off of
     * @param newWeek
     *          week to be added. Must be a valid week (validWeek must return true)
     */
    private StreamSeasonBuilder(final SeasonBuilder sb, final Set<ImmutablePair<Integer, Integer>> newWeek) {
        // TODO: Impliment this

    }

    public ConcurrentMap<Integer, Set<ImmutablePair<Integer, Integer>>> getWeeks() {
        return new ConcurrentHashMap<>();
    }

    @Override
    public Set<SeasonBuilder> addWeeks(final Set<Set<ImmutablePair<Integer, Integer>>> possibleWeeks) {
        // TODO: Impliment this
        return new HashSet<>(Arrays.asList(new StreamSeasonBuilder())); // TODO: Change this
    }

    @Override
    public SeasonBuilder addWeek(final Set<ImmutablePair<Integer, Integer>> newWeek) {
        // TODO: Impliment this
        return new StreamSeasonBuilder(this, newWeek); // TODO: Change this
    }

    @Override
    public boolean validWeek(final Set<ImmutablePair<Integer, Integer>> newWeek) {
        // TODO: Impliment this
        return Boolean.TRUE; // TODO: Change this
    }

    @Override
    public Season toSeason() {
        // TODO: Impliment this
        return new ConcreateSeason(); // TODO: Change this
    }
}
