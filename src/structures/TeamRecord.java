package structures;

import org.apache.commons.lang3.tuple.MutableTriple;

import java.util.Map;

/**
 * Keeps track of wins, losses and ties for a team
 */
public class TeamRecord {

    private String teamName;
    private int wins;
    private int losses;
    private int ties;

    public TeamRecord(String name) {
        this.teamName = name;
        this.wins = 0;
        this.losses = 0;
        this.ties = 0;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getTies() {
        return ties;
    }

    public void incrementWins() {
        wins++;
    }

    public void incrementLosses() {
        losses++;
    }

    public void incrementTies() {
        ties++;
    }
}
