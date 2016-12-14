package schedule;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.paukov.combinatorics3.Generator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.paukov.combinatorics3.Generator.combination;

/**
 * Util class for creating Seasons. Immutable
 */
public class StreamSeasonBuilder implements SeasonBuilder {

    // Immutability argument:
        // Getter methods return a copy of internal maps
        // ImmutablePair doesn't need to be copied because it is immutable
        // Adding weeks to the SeasonBuilder creates new SeasonBuilder objects
        // without modifying the current one

    // Schedule for the Season
    // RI:
        // 1 <= key <= 16
        // 1 <= ImmutablePair Integers <= MAX_TEAMS_IN_LEAGUE
    // AF:
        // Week in a season maps to the matchups for that week
    private final ConcurrentMap<Integer, Set<ImmutablePair<Integer, Integer>>> weeks;

    // Possible matchups for this season
    // RI:
        // Left maps to hashcode of the matchup
        // Right maps to number of times the matchup can occur again
    // AF:
        // Interpretation of the restraints on the season
        // Current restraint is that there are only a certain amount
        // of times that a team can play another team
        // Example: Team 1 can't play Team 7 10 times in the season
    private final ConcurrentMap<Integer, Integer> possibleMatchups;

    // Number of weeks in the season
    private final int weeksInSeason;

    /**
     * Construct a new empty {@link StreamSeasonBuilder}
     */
    public StreamSeasonBuilder(final int weeksInSeason, final int numTeams) {
        this.weeks = new ConcurrentHashMap<>();
        this.weeksInSeason = weeksInSeason;
        this.possibleMatchups = populatePossibleMatchups(weeksInSeason, numTeams);
    }

    private ConcurrentMap<Integer, Integer> populatePossibleMatchups(final int numWeeks, final int numTeams) {
        ConcurrentMap<Integer, Integer> posMatchups = new ConcurrentHashMap<>();
        Set<ImmutablePair<Integer, Integer>> allMatchups = ScheduleGenerator.getPossibleMatchups(numTeams);

        // maxDuplicateMatchup = (weeksInSeason / number of teams in league) rounded up
        final int maxDuplicateMatchup = (int) Math.ceil( (double) numWeeks / (numTeams - 1.0) );

        for (Iterator<ImmutablePair<Integer, Integer>> it = allMatchups.iterator(); it.hasNext();) {
            ImmutablePair<Integer, Integer> currentMatchup = it.next();
            posMatchups.put(currentMatchup.hashCode(), maxDuplicateMatchup);
        }

        return posMatchups;
    }

    /**
     * Generate all possible schedules for a given number of teams and weeks in the season
     * Duplicate matchups are limited. Teams will every team the same amount of times (+-1)
     *
     * Example: numTeams = 4, numWeeks = 14
     *          756756 possible schedules
     *          Max duplicate matchups = 5
     *
     * @param numTeams
     *          Teams in the league, must be even
     * @param numWeeks
     *          Weeks in the season
     * @return Set of SeasonBuilders with all possible seasons
     *          TODO: Change this to Seasons instead of SeasonBuilders
     */
    public static Set<SeasonBuilder> generateSeasons(final int numTeams, final int numWeeks) {
        // Create synchronized set with one empty SeasonBuilder
        Set<SeasonBuilder> sbSet = Collections.synchronizedSet(new HashSet<>());
        sbSet.add(new StreamSeasonBuilder(numWeeks, numTeams));

        System.out.println("Size of the set is " + sbSet.size());

        // Get possible weeks
        Set<Set<ImmutablePair<Integer, Integer>>> possibleWeeks = ScheduleGenerator.getPossibleWeeks(numTeams);

        // Add all the weeks for the number of weeks in the season
        Stream<SeasonBuilder> sbStream = sbSet.stream();
        for (int i = 1; i <= numWeeks; i++) {
            System.out.println("Working on week " + i);
            sbStream = sbStream.map(sb -> sb.addWeeks(possibleWeeks)).flatMap(Collection::stream);
        }

        // Collect the SeasonBuilders into a Set
        return sbStream.collect(Collectors.toSet());
    }

    /**
     * Create a new SeasonBuilder with a new week. Adjusts the possible matchups left
     * to reflect the new week added
     *
     * @param sb
     *          SeasonBuilder to build off of
     * @param newWeek
     *          week to be added. Must be a valid week (validWeek must return true).
     *          newWeek must be the same size as weeks in sb
     */
    private StreamSeasonBuilder(final SeasonBuilder sb, final Set<ImmutablePair<Integer, Integer>> newWeek) {

        weeksInSeason = sb.getWeeksInSeason();

        // Get previous weeks
        weeks = sb.getWeeks();

        // Check that new week is the same size as other weeks
        final int newWeekSize = newWeek.size();
        weeks.values().forEach(wk -> { assert wk.size() == newWeekSize; });

        // Add the new week
        final int numWeeks = weeks.size();
        // Weeks should not be modified once put in the season
        weeks.put(numWeeks + 1, Collections.unmodifiableSet(newWeek));

        // Get the possible matchups from previous SeasonBuilder
        possibleMatchups = sb.getPossibleMatchups();

        // Populate possible matchups if this is the first week added to the SeasonBuilder
        if (possibleMatchups.size() == 0) {
            Set<ImmutablePair<Integer, Integer>> allMatchups = ScheduleGenerator.getPossibleMatchups(newWeek.size());

            // maxDuplicateMatchup = (weeksInSeason / number of teams in league) rounded up
            final int maxDuplicateMatchup = (int) Math.ceil((double) weeksInSeason / (newWeek.size() * 2.0));

            for (Iterator<ImmutablePair<Integer, Integer>> it = allMatchups.iterator(); it.hasNext();) {
                ImmutablePair<Integer, Integer> currentMatchup = it.next();
                possibleMatchups.put(currentMatchup.hashCode(), maxDuplicateMatchup);
            }
        }

        // Reduce the count on each possible matchup in the week is being added
        for (Iterator<ImmutablePair<Integer, Integer>> it = newWeek.iterator(); it.hasNext();) {
            // Get the matchup in the week
            ImmutablePair<Integer, Integer> currentMatchup = it.next();
            Integer currentHashcode = currentMatchup.hashCode();

            // Reduce its count in possible matchups
            Integer count = possibleMatchups.get(currentHashcode);
            possibleMatchups.put(currentHashcode, count - 1);
        }

    }

    @Override
    public ConcurrentMap<Integer, Set<ImmutablePair<Integer, Integer>>> getWeeks() {
        ConcurrentMap<Integer, Set<ImmutablePair<Integer, Integer>>> weeksCopy = new ConcurrentHashMap<>();

        // Deep copy of weeks. ImmutablePair does not need to be deep copied because
        // it is immutable
        for (Iterator<Integer> it = weeks.keySet().iterator(); it.hasNext(); ) {
            Integer currentWeek = it.next();
            weeksCopy.put(currentWeek, weeks.get(currentWeek));
        }

        return weeksCopy;
    }

    @Override
    public ConcurrentMap<Integer, Integer> getPossibleMatchups() {
        ConcurrentMap<Integer, Integer> matchupsCopy = new ConcurrentHashMap<>();

        // Copy of week so rep is not exposed
        for (Iterator<Integer> it = possibleMatchups.keySet().iterator(); it.hasNext(); ) {
            Integer currentWeek = it.next();
            matchupsCopy.put(currentWeek, possibleMatchups.get(currentWeek));
        }

        return matchupsCopy;
    }

    @Override
    public Set<SeasonBuilder> addWeeks(final Set<Set<ImmutablePair<Integer, Integer>>> possibleWeeks) {
        // Adapted from https://stackoverflow.com/questions/26872827/java-lambda-returning-a-lambda
        return collect(possibleWeeks.stream()
                        .filter(containsMatchup())      // Finds the weeks to add
                        .map(this::addWeek)             // Create new SeasonBuilders with the new week
                , Collectors.toSet());
    }

    // turns the Stream s from receiver to a parameter
    // Adapted from https://stackoverflow.com/questions/26872827/java-lambda-returning-a-lambda
    private static <T, R, A> R collect(Stream<T> s, Collector<? super T, A, R> collector) {
        return s.collect(collector);
    }

    /**
     * Checks if week contains any matchups which are not allowed
     *
     * @return true iff the matchup is week is viable
     */
    private Predicate<Set<ImmutablePair<Integer, Integer>>> containsMatchup() {
        return ptwk -> !ptwk.stream().allMatch(ptmt -> possibleMatchups.get(ptmt.hashCode()).equals(0));
    }

    @Override
    public SeasonBuilder addWeek(final Set<ImmutablePair<Integer, Integer>> newWeek) {
        return new StreamSeasonBuilder(this, newWeek);
    }

    @Override
    public boolean validWeek(final Set<ImmutablePair<Integer, Integer>> newWeek) {
        return newWeek.stream().allMatch(ptmt -> possibleMatchups.get(ptmt.hashCode()).equals(0));
    }

    @Override
    public Season toSeason() {
        // TODO: Impliment this
        return new ConcreateSeason(); // TODO: Change this
    }

    @Override
    public int getWeeksInSeason() {
        return weeksInSeason;
    }
}
