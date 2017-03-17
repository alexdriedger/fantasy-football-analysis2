package structures;

import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Holds the standings for the end of the regular season
 *
 * TODO : MAKE METHODS LESS RELIANT ON THE ORDER OF TEAM RECORDS
 * TODO : GIVE EVERY TEAM AN ID TO USE TO KEEP TRACK OF AS WELL AS A TEAM NAME
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

    public void addWin(int teamID, double pointsScored) {
        teamRecords.get(teamID - 1).incrementWins(pointsScored);
    }

    public void addLoss(int teamID, double pointsScored) {
        teamRecords.get(teamID - 1).incrementLosses(pointsScored);
    }

    public void addTies(int teamID, double pointsScored) {
        teamRecords.get(teamID - 1).incrementTies(pointsScored);
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
     * Returns the standings. Priority is:
     *
     *      1. Wins
     *      2. Points For
     *
     * @return
     *      List in order of place
     */
    public List<String> getStandings() {
        // Create a new list of records and copy over the contents
        List<TeamRecord> records = new ArrayList<>();
        for (TeamRecord tr : teamRecords) {
            records.add(new TeamRecord(tr));
        }

        // Sort the list by wins, then points for if there are ties
        Collections.sort(records, TeamRecord.TeamRecordComparator);
        Collections.reverse(records);

        return records.stream()
                // Map records to Team names
                .map(TeamRecord::getTeamName)
                .collect(Collectors.toList());
    }
}
