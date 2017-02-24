package structures;

import java.util.*;

/**
 * A Fantasy Football Team
 */
public class Team {

    // Integer represents the year the fantasy season begun in. i.e. the
    // year the first game took place from the given season.
    //
    // Score for each week of the season is held in an list of doubles.
    // Score from the week 'i' is held in index 'i-1'. Eg. Week 1 score is located
    // at index 0, week 2 score is at index 1..... week 14 score is at index 13
    private Map<Integer, List<Double>> seasons;

    // Team name
    private String name;

    // Create a team with no seasons
    public Team(String teamName) {
        this.name = teamName;
        this.seasons = new HashMap<>();
    }

    public String getName() { return name; }

    /**
     * Adds a season for the team. If a season for the year already exists,
     * the previous season will be overwritten
     *
     * @param year
     *          year the season took place
     * @param scores
     *          List of scores from the season. Week 1 is at index 0, week 2
     *          is at index 1.... week n is at index n - 1.
     */
    public void addSeason(int year, List<Double> scores) {
        // Copy list to prevent outside modification
        List<Double> newScores = new ArrayList<>(scores);

        // Add season
        seasons.put(year, newScores);
    }

    /**
     * Get the score for a week. Throws an exception if the year and/or week do not exist
     * for this team
     *
     * @param year
     *          year the season took place
     * @param week
     *          week of interest
     * @return
     *          score
     */
    public double getScore(int year, int week) {
        return seasons.get(year).get(week - 1);
    }

    /**
     * Get the scores for a season
     *
     * @param year
     *          year to get scores of
     * @return
     *          scores
     */
    public List<Double> getSeasonScores(int year) {
        // Copy list to protect rep
        return new ArrayList<>(seasons.get(year));
    }

    /**
     * Returns all scores from all seasons the team has played
     *
     * @return
     *          scores
     */
    public List<Double> getAllScores() {
        List<Double> scores = new ArrayList<>();

        for (List<Double> season : seasons.values()) {
            // Copy values to protect rep
            scores.addAll(new ArrayList<>(season));
        }
        return scores;
    }

    /**
     * Returns all points for in a season
     *
     * @param year
     *          year of interest
     * @return
     *          points for
     */
    public double getPointsFor(int year) {
        double pointsFor = 0;

        // Sum scores from all weeks
        for (double week : seasons.get(year)) {
            pointsFor += week;
        }

        return pointsFor;
    }
}
