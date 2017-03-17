package structures;

import org.apache.commons.lang3.tuple.MutableTriple;

import java.util.Comparator;
import java.util.Map;

/**
 * Keeps track of wins, losses and ties for a team
 */
public class TeamRecord {

    private String teamName;
    private int wins;
    private int losses;
    private int ties;
    private double pointsFor;

    public TeamRecord(String name) {
        this.teamName = name;
        this.wins = 0;
        this.losses = 0;
        this.ties = 0;
    }

    /**
     * Creates a deep copy of a TeamRecord
     *
     * @param old
     *          TeamRecord to copy
     */
    public TeamRecord(TeamRecord old) {
        this.teamName = old.getTeamName();
        this.wins = old.getWins();
        this.losses = old.getLosses();
        this.ties = old.getTies();
        this.pointsFor = old.getPointsFor();
    }

    public static final Comparator<TeamRecord> TeamRecordComparator = new Comparator<TeamRecord>() {
        @Override
        public int compare(TeamRecord o1, TeamRecord o2) {
            if (o1.getWins() > o2.getWins()) { return 1; }
            if (o1.getWins() == o2.getWins()) {
                if (o1.getPointsFor() > o2.getPointsFor()) { return 1; }
                if (o1.getPointsFor() == o2.getPointsFor()) { return 0; }
                if (o1.getPointsFor() < o2.getPointsFor()) {return -1; }
            }
            if (o1.getWins() < o2.getWins()) { return -1; }

            // Code should not reach this point
            System.out.println("Standings reached code it should not have");
            return -10;
        }
    };

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

    public double getPointsFor() {
        return pointsFor;
    }

    public void incrementWins(double pointsFor) {
        wins++;
        this.pointsFor += pointsFor;
    }

    public void incrementLosses(double pointsFor) {
        losses++;
        this.pointsFor += pointsFor;
    }

    public void incrementTies(double pointsFor) {
        ties++;
        this.pointsFor += pointsFor;
    }
}
