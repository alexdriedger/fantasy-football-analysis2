package structures.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import structures.League;
import structures.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Tests for League
 */
@RunWith(Parameterized.class)
public class LeagueGetAllTeamsListTest {

    private List<String> actual;
    private List<String> expected;


    @Parameterized.Parameters(name = "{index}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {   new ArrayList<>(Arrays.asList("Alex", "Bilal", "Fred", "Ryan")),
                    new ArrayList<>(Arrays.asList("Alex", "Bilal", "Fred", "Ryan"))},
                {   new ArrayList<>(Arrays.asList("Bilal", "Alex", "Ryan", "Fred")),
                    new ArrayList<>(Arrays.asList("Alex", "Bilal", "Fred", "Ryan"))},
                {   new ArrayList<>(Arrays.asList("Two", "One")),
                    new ArrayList<>(Arrays.asList("One", "Two"))},
                {   new ArrayList<>(Arrays.asList("Bilal", "Alex", "Ryan", "Fred", "Blah", "Apple")),
                    new ArrayList<>(Arrays.asList("Alex", "Apple", "Bilal", "Blah", "Fred", "Ryan"))},
                {   new ArrayList<>(),
                    new ArrayList<>()}

        });
    }

    public LeagueGetAllTeamsListTest(List<String> teamNames, List<String> expected) {
        this.expected = expected;
        this.actual = new ArrayList<>();

        // Add teams to league
        League league = new League();
        for (String name : teamNames) {
            league.addTeam(new Team(name));
        }

        // Add each team name to the actual output
        List<Team> teams = league.getAllTeamsList();
        for (int i = 0; i < teamNames.size(); i++) {
            this.actual.add(teams.get(i).getName());
        }
    }

    @Test
    public void testCarryAdd() {
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }
}
