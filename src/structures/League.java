package structures;

import java.util.*;

/**
 * A Fantasy Football League
 */
public class League {

    private Map<String, Team> league;

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
     * Returns all {@link Team}s associated with the {@link League}
     * @return
     *          All Teams in the League
     */
    public Set<Team> getAllTeams() {
        return new HashSet<>(league.values());
    }

    public int getNumberOfTeams() {
        return league.size();
    }
}
