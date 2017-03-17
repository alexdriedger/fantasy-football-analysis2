package analysis;

import data.XMLParser;
import org.apache.commons.lang3.tuple.ImmutablePair;
import schedule.DepthFirstSeasonBuilder;
import structures.League;
import structures.LeagueProperties;
import structures.Standings;
import structures.Team;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Stats analysis class for the regular season
 */
public class RegularSeason {

    public static void main(String[] args) {
        League league = XMLParser.getLeague("src/data/scores.xml");
        Set<List<Set<ImmutablePair<Integer, Integer>>>> allSeasons = DepthFirstSeasonBuilder.generateSeasons(4, 14);

        System.out.println("Potential seasons created");


        for (int year = 2011; year <= 2016; year++) {

            Set<Standings> results = new HashSet<>();

            for (List<Set<ImmutablePair<Integer, Integer>>> season : allSeasons) {
                results.add(computeSeason(league, season, year));
            }

            System.out.println(year + " : Regular season's simulated");

            Map<String, int[]> playoffResults = computePlayoffs(league, results, year);

            for (String teamName : playoffResults.keySet()) {
                int[] r = playoffResults.get(teamName);

                System.out.println("Results for " + teamName);

                for (int i = 0; i < 4; i++) {
                    System.out.println((i + 1) + " place: " + r[i] + " times");
                }

                System.out.println();
            }
        }

//        int[][] winDistribution = getWinDistribution(results);
//
//        for (int i = 0; i < league.getNumberOfTeams(); i++) {
//            System.out.println(league.getAllTeamsList().get(i).getName() + "'s results:");
//            for (int j = 0; j < 15; j++) {
//                System.out.println("Wins " + j + " occurred " + winDistribution[i][j] + " times");
//            }
//        }

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
                                          final int year) {

        Standings standing = new Standings(league.getNumberOfTeams());

        List<Team> allTeams = league.getAllTeamsList();

        // Iterate through the season
        for (int weekNumberMinusOne = 0; weekNumberMinusOne < league.getLeagueProperties().getLengthOfRegularSeason(); weekNumberMinusOne++) {
            // Get the current week
            Set<ImmutablePair<Integer, Integer>> currentWeek = season.get(weekNumberMinusOne);

            // Iterate throught the matchups in each week
            for (ImmutablePair<Integer, Integer> matchup : currentWeek) {

                // Standing is 1 based. But lists are 0 based!!

                // Get scores for the week
                // Left is team1, right is team2
                double score1 = allTeams.get(matchup.getLeft() - 1).getScore(year, weekNumberMinusOne + 1);
                double score2 = allTeams.get(matchup.getRight() - 1).getScore(year, weekNumberMinusOne + 1);

                if (score1 > score2) {
                    standing.addWin(matchup.getLeft(), score1);
                    standing.addLoss(matchup.getRight(), score2);
                } else if (score2 > score1) {
                    standing.addWin(matchup.getRight(), score2);
                    standing.addLoss(matchup.getLeft(), score1);
                } else {
                    standing.addTies(matchup.getLeft(), score1);
                    standing.addTies(matchup.getRight(), score2);
                }
            }
        }

        return standing;
    }

    /**
     * Determines the number of times each team finished in each place
     *
     * @param league
     *          League to check. Must have game data for the year in question.
     * @param regSeasonStandings
     *          Standings from each simulated regular season
     * @param year
     *          Year to play playoffs from
     * @return
     *          Map with key being the name of the team and the value corresponding to the number of
     *          times that team won the league
     */
    public static Map<String, int[]> computePlayoffs(final League league, Set<Standings> regSeasonStandings,
                                                       final int year) {
        List<Team> allTeams = league.getAllTeamsList();

        // Count the occurrence of each Standings object
        // The purpose is so that you only have to calculate the playoffs once per final standings
        Map<List<String>, Integer> regSeasonFinalStandingsCount = getRegularSeasonStandingsCount(regSeasonStandings);

        System.out.println("Occurrences counted");

        // All names of the teams in the league
        Set<String> allTeamNames = league.getAllTeamsSet().stream()
                .map(Team::getName)
                .collect(Collectors.toSet());

        // Maps team name to times they won the championship for the year
        Map<String, int[]> playoffOutcomes = initializeStringIntMapWithZeros(allTeamNames, league.getNumberOfTeams());

        // Run the numbers using the 2017 playoff structure
        if (league.getLeagueProperties().getPlayoffStructure() == LeagueProperties.PLAYOFF_STRUCTURE_2017) {

            // Run numbers for each unique final regular season standings
            for (List<String> playoffSeeds : regSeasonFinalStandingsCount.keySet()) {
                // Evaluate matchups and update playoffOutcomes - Championship game
                computePlayoffMatchup(allTeams.get(Integer.parseInt(playoffSeeds.get(0)) - 1),
                        allTeams.get(Integer.parseInt(playoffSeeds.get(1)) - 1),
                        playoffOutcomes, regSeasonFinalStandingsCount.get(playoffSeeds), year, 1, 10.0);

                // Third Place game
                computePlayoffMatchup(allTeams.get(Integer.parseInt(playoffSeeds.get(2)) - 1),
                        allTeams.get(Integer.parseInt(playoffSeeds.get(3)) - 1),
                        playoffOutcomes, regSeasonFinalStandingsCount.get(playoffSeeds), year, 3, 5.0);

            }

        }
        return playoffOutcomes;
    }

    private static void computePlayoffMatchup(final Team teamOne, final Team teamTwo, final Map<String, int[]> playoffOutcomes,
                                              final int multiplier, final int year, final int finishPlace, final double advantage) {

        // First place team gets a 10 point advantage
        double team1Score = advantage + teamOne.getScore(year, 15) + teamOne.getScore(year, 16);
        double team2Score = teamTwo.getScore(year, 15) + teamTwo.getScore(year, 16);

        updateFinishPlaces(playoffOutcomes, multiplier, teamOne.getName(), teamTwo.getName(),
                ( team1Score - team2Score), finishPlace);
    }

    /**
     * Update the finish places with the standings for a playoff matchup
     *
     * @param playoffOutcomes
     *          Record to update
     * @param multiplier
     *          Number of times this matchup occurred in playoffs
     * @param teamOne
     *          First team
     * @param teamTwo
     *          Second team
     * @param netScore
     *          Amount of points the first team has compared to the second team. Negative points indicates
     *          that teamTwo won.
     * @param finishPlace
     *          Place teams are playing for
     */
    private static void updateFinishPlaces(final Map<String, int[]> playoffOutcomes, final int multiplier,
                                           final String teamOne, final String teamTwo, final double netScore,
                                           final int finishPlace) {
        int[] teamOneOutcomes = playoffOutcomes.get(teamOne);
        int[] teamTwoOutcomes = playoffOutcomes.get(teamTwo);

        // Higher ranked team won / tie
        if (netScore >= 0) {
            teamOneOutcomes[finishPlace - 1] += multiplier;
            teamTwoOutcomes[finishPlace] += multiplier;
        } else {
            teamTwoOutcomes[finishPlace - 1] += multiplier;
            teamOneOutcomes[finishPlace] += multiplier;
        }
    }

    /**
     * Counts the occurrence of the final regular season results in the set.
     *
     * @param regSeasonStandings
     *          Standings that have information up to the end of the regular season
     * @return
     *          Map of the final regular season standings mapped to how many times it occurs.
     */
    private static Map<List<String>, Integer> getRegularSeasonStandingsCount(Set<Standings> regSeasonStandings) {
        // Count the occurrence of each Standings object
        // The purpose is so that you only have to calculate the playoffs once per final standings
        Map<List<String>, Integer> regSeasonFinalStandingsCount = new HashMap<>();

        // Find out how many of each end of season standings occur
        for (Standings stand : regSeasonStandings) {

            // Standings at the end of the regular season
            List<String> endOfSeasonStandings = stand.getStandings();

            // Current count in the map
            Integer currentCount = regSeasonFinalStandingsCount.putIfAbsent(endOfSeasonStandings, 1);

            // Map contains at least one occurrence of the end of regular season standings
            if (currentCount != null) {
                regSeasonFinalStandingsCount.put(endOfSeasonStandings, currentCount + 1);
            }
        }

        return regSeasonFinalStandingsCount;
    }

    /**
     * Initialize a Map<String, Integer> with each String mapping to an Integer[] with a certain size
     *
     * @param strings
     *          Map keys
     * @param size
     *          Size of Integer[]
     * @return
     *          Map initialized with Integer[]
     */
    private static Map<String, int[]> initializeStringIntMapWithZeros(final Set<String> strings, final int size) {
        Map<String, int[]> map = new HashMap<>();

        // Initialize all teams with 0 wins
        for (String s : strings) {
            map.put(s, new int[size]);
        }

        return map;
    }

}
