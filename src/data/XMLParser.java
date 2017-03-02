package data;

import org.w3c.dom.*;
import structures.League;
import structures.Team;

import javax.xml.parsers.*;
import javax.xml.xpath.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses scores.xml into useful information
 */
public class XMLParser {

    public static List<Double> getScoresFromXML (NodeList weeks) {
        // List will be the same length as the NodeList that is passed in
        List<Double> scores = new ArrayList<>(weeks.getLength());

        for (int i = 0; i < weeks.getLength(); i++) {
            Element week = (Element) weeks.item(i);
            // Get the score of the week
            scores.add(Double.parseDouble(week.getTextContent()));
//            System.out.println("Week " + (i+1) + " had score " + week.getTextContent());
        }
        return scores;
    }

    /**
     * Returns a league with teams populated with their scores from the file
     *
     * @param filePath
     *          xml life with scores
     * @return
     *          league
     */
    public static League getLeague(String filePath) {
        try {
            // Prepare to parse xml
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File file = new File(filePath);
            Document doc = builder.parse(file);
            XPath xpath = XPathFactory.newInstance().newXPath();

            // Get all the seasons
            NodeList seasons = (NodeList) xpath.compile("/league/season").evaluate(doc, XPathConstants.NODESET);

            // Create empty League
            League league = new League();

            // Iterate through seasons
            for (int i = 0; i < seasons.getLength(); i++) {
                Element season = (Element) seasons.item(i);
                int currentYear = Integer.parseInt(season.getAttributes().getNamedItem("year").getNodeValue());
                System.out.println("Current season is " + currentYear);

                // Get teams for each season
                NodeList teams = season.getElementsByTagName("team");
                for (int j = 0; j < teams.getLength(); j++) {
                    Element team = (Element) teams.item(j);
                    String currentTeamName = team.getAttributes().getNamedItem("name").getNodeValue();
                    System.out.println("Current team is " + currentTeamName);

                    // Put team in league if it doesn't exist yet
                    if (! league.containsTeam(currentTeamName)) {
                        league.addTeam(new Team(currentTeamName));
                    }

                    // Get the scores for the current team
                    List<Double> scores = getScoresFromXML(team.getElementsByTagName("score"));

                    // Add scores to the correct team
                    Team currentTeam = league.getTeam(currentTeamName);
                    currentTeam.addSeason(currentYear, scores);
                }
            }

            return league;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse xml into a league object");
        }
    }
}
