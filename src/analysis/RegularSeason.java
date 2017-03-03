package analysis;

import data.XMLParser;
import org.apache.commons.lang3.tuple.ImmutablePair;
import schedule.DepthFirstSeasonBuilder;
import structures.League;
import structures.Standings;
import structures.Team;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Stats analysis class for the regular season
 */
public class RegularSeason {

    // TODO : REDO EVERYTHING

    public static void main(String[] args) {
        League league = XMLParser.getLeague("src/data/scores.xml");
        Set<List<Set<ImmutablePair<Integer, Integer>>>> allSeasons = DepthFirstSeasonBuilder.generateSeasons(4, 14);

        Set<Standings> results = new HashSet<>();

        for (List<Set<ImmutablePair<Integer, Integer>>> season : allSeasons) {
            results.add(computeSeason(league, season, 2015, 14));
        }

        int[][] winDistribution = getWinDistribution(results);

        for (int i = 0; i < league.getNumberOfTeams(); i++) {
            System.out.println(league.getAllTeamsList().get(i).getName() + "'s results:");
            for (int j = 0; j < 15; j++) {
                System.out.println("Wins " + j + " occurred " + winDistribution[i][j] + " times");
            }
        }

    }

    public static int[][] getWinDistribution(Set<Standings> standings) {

        System.out.println("There were " + standings.size() + " seasons processed and added to the standings set");

        int[][] winDistribution = new int[4][15];

        for (Standings s : standings) {
            for (int i = 1; i <= s.getNumTeams(); i++) {
                winDistribution[i - 1][s.getWins(i)]++;
            }
        }

        return winDistribution;
    }

    public static Standings computeSeason(final League league, final List<Set<ImmutablePair<Integer, Integer>>> season,
                                          final int year, final int lengthOfRegularSeason) {

        Standings standing = new Standings(league.getNumberOfTeams());

        List<Team> allTeams = league.getAllTeamsList();

        // Iterate through the season
        for (int weekNumberMinusOne = 0; weekNumberMinusOne < lengthOfRegularSeason; weekNumberMinusOne++) {
            // Get the current week
            Set<ImmutablePair<Integer, Integer>> currentWeek = season.get(weekNumberMinusOne);

            // Iterate throught the matchups in each week
            for (ImmutablePair<Integer, Integer> matchup : currentWeek) {

                // Standing is 1 based. But lists are 0 based!!

                // Get scores for the week
                double score1 = allTeams.get(matchup.getLeft() - 1).getScore(year, weekNumberMinusOne + 1);
                double score2 = allTeams.get(matchup.getRight() - 1).getScore(year, weekNumberMinusOne + 1);

                if (score1 > score2) {
                    standing.addWin(matchup.getLeft());
                    standing.addLoss(matchup.getRight());
                } else if (score2 > score1) {
                    standing.addWin(matchup.getRight());
                    standing.addLoss(matchup.getLeft());
                } else {
                    standing.addTies(matchup.getLeft());
                    standing.addTies(matchup.getRight());
                }
            }
        }

//        for (int weekNum = 1; weekNum <= lengthOfRegularSeason; weekNum++) {
//            Set<ImmutablePair<Integer, Integer>> currentWeek = season.get(weekNum);
//            for (ImmutablePair<Integer, Integer> matchup : currentWeek) {
//                double score1 = allTeams.get(matchup.getLeft() - 1).getScore(year, weekNum);
//                double score2 = allTeams.get(matchup.getRight() - 1).getScore(year, weekNum);
//
//                if (score1 > score2) {
//                    standing.addWin(matchup.getLeft() - 1);
//                    standing.addLoss(matchup.getRight() - 1);
//                } else if (score2 > score1) {
//                    standing.addWin(matchup.getRight() - 1);
//                    standing.addLoss(matchup.getLeft() - 1);
//                } else {
//                    standing.addTies(matchup.getLeft() - 1);
//                    standing.addTies(matchup.getRight() - 1);
//                }
//            }
//        }

        return standing;
    }

}
