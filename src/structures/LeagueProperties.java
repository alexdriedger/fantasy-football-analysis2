package structures;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Properties of a League
 *
 */
public class LeagueProperties {

    // Different Possible playoff structures
    public static final int PLAYOFF_STRUCTURE_2017 = 1;

    private int lengthOfRegularSeason;
    private int playoffStructure;

    // Years the league was played in
    private Set<Integer> years;

    public LeagueProperties(int lengthOfRegularSeason, int playoffStructure, Set<Integer> years) {
        this.lengthOfRegularSeason = lengthOfRegularSeason;
        this.playoffStructure = playoffStructure;
        this.years = new HashSet<>();
    }

    public int getLengthOfRegularSeason() {
        return lengthOfRegularSeason;
    }

    public int getPlayoffStructure() {
        return playoffStructure;
    }

    /**
     * Get the years of which the league was played.
     *
     * @return
     *          An unmodifiable set of the years that the league was played
     */
    public Set<Integer> getYears() {
        return Collections.unmodifiableSet(years);
    }
}
