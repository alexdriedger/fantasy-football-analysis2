package structures;

import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Holds the standings for the end of the regular season
 */
public class Standings {

    private List<TeamRecord> teamRecords = new ArrayList<>();

    /**
     * Creates a new Standings with the given number of teams in the league. Each team is given
     * an ID from 1 - numTeams inclusive.
     *
     * @param numTeams
     *          Number of teams in the league
     */
    public Standings(int numTeams) {
        // Every team begins with a clean record
        for (int i = 1; i <= numTeams; i++) {
            teamRecords.add(new TeamRecord(Integer.toString(i)));
        }
    }

    public void addWin(int teamID) {
//        MutableTriple<Integer, Integer, Integer> rec = records.get(name);
//        rec.left++;
        teamRecords.get(teamID - 1).incrementWins();
    }

    public void addLoss(int teamID) {
        teamRecords.get(teamID - 1).incrementLosses();
    }

    public void addTies(int teamID) {
        teamRecords.get(teamID - 1).incrementTies();
    }

    public int getWins(int teamID) {
        return teamRecords.get(teamID - 1).getWins();
    }

    public int getLosses(int teamID) {
        return teamRecords.get(teamID - 1).getLosses();
    }

    public int getTies(int teamID) {
        return teamRecords.get(teamID - 1).getTies();
    }

    public int getNumTeams() {
        return teamRecords.size();
    }

    /**
     * Returns the standings, in order of wins.
     * @return
     */
    public List<String> getStandings() {
        return new ArrayList<>();
    }
}
