package structures;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A Fantasy Football League
 */
public class League {

    // Key is <team>.getName()
    private Map<String, Team> league;

    // Standard deviation in the league
    // Must call getRefreshedStdDev to get an actual value
    private double stdDev = Double.MIN_VALUE;

    /**
     * Creates an empty league
     */
    public League () {
        this.league = new HashMap<>();
    }

    /**
     * Add a {@link Team} to the league. If a team with the same name already exists,
     * it is replaced with the new {@link Team}
     *
     * @param team
     *          Team to add
     */
    public void addTeam(Team team) {
        league.put(team.getName(), team);
    }

    /**
     * Returns true if the league contains the specified teams
     *
     * @param name
     *          Name of team
     * @return
     *          True iff the league contains the team
     */
    public boolean containsTeam(String name) {
        return league.get(name) != null;
    }

    /**
     * Returns the {@link Team} with the specified name. Returns null if team does
     * not exist in the league
     *
     * @param name
     *          Name of team. Same as returned by the Team.getName()
     * @return
     *          Team
     */
    public Team getTeam(String name) {
        return league.get(name);
    }

    /**
     * Returns an ordering mapped to team names. This ordering is based on Team names
     *
     * @return
     *          map of teamID to teamName
     */
    public Map<Integer, String> getTeamsMapping() {
        // TODO : IMPLEMETNT THIS
        return null;
    }

    /**
     * Returns all {@link Team}s associated with the {@link League}
     * @return
     *          All Teams in the League
     */
    public Set<Team> getAllTeamsSet() {
        return new HashSet<>(league.values());
    }

    /**
     * Returns Teams in alphabetical order by team name
     *
     * @return
     *          List of teams in alphabetical order
     */
    public List<Team> getAllTeamsList() {
        List<Team> allTeams = new ArrayList<>();
        allTeams.addAll(league.values());

        Collections.sort(allTeams, new Comparator<Team>() {
            @Override
            public int compare(Team o1, Team o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return allTeams;
    }

    public int getNumberOfTeams() {
        return league.size();
    }

    /**
     * Returns standard deviation of all scores in the league. If the standard deviation has already been
     * calculated, it will return the old value. To get an updated value, call getRefreshedStdDev()
     *
     * @return
     *          Standard deviation across all scores in the league
     */
    public double getStdDev() {
        if (stdDev == Double.MIN_VALUE) {
            calculateStdDev();
        }
        return stdDev;
    }

    /**
     * Calculates and returns the standard deviation across all scores in the league
     *
     * @return
     *          Standard deviation of scores in the league
     */
    public double getRefreshedStdDev() {
        calculateStdDev();
        return stdDev;
    }

    private void calculateStdDev() {
        SummaryStatistics stats = new SummaryStatistics();

        Set<Team> allTeams = getAllTeamsSet();
        allTeams.stream()
                .map(Team::getAllScores)
                .flatMap(List::stream)
                .forEach(stats::addValue);

        stdDev = stats.getStandardDeviation();
    }
}
