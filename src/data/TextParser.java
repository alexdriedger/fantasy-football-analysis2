package data;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.List;

import static data.xmlParser.getScoresFromXML;

/**
 * All of the hackey methods of getting data into and out of an xml file
 */
public class textParser {

    public static void main(String[] args) {
        scoresToSpreadSheet("src/data/scores.xml");
    }

    public static void scoresToSpreadSheet(String filePath) {
        Workbook wb = new HSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("Every Score");
        Row alex = sheet.createRow(0);
        alex.createCell(0).setCellValue(createHelper.createRichTextString("Alex"));

        Row ryan = sheet.createRow(1);
        ryan.createCell(0).setCellValue(createHelper.createRichTextString("Ryan"));

        Row bilal = sheet.createRow(2);
        bilal.createCell(0).setCellValue(createHelper.createRichTextString("Bilal"));

        Row fred = sheet.createRow(3);
        fred.createCell(0).setCellValue(createHelper.createRichTextString("Fred"));

        try {
            // Prepare to parse xml
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File file = new File(filePath);
            Document doc = builder.parse(file);
            XPath xpath = XPathFactory.newInstance().newXPath();

            // Get all the seasons
            NodeList seasons = (NodeList) xpath.compile("/league/season").evaluate(doc, XPathConstants.NODESET);

            // Get seasons
            for (int i = 0; i < seasons.getLength(); i++) {
                Element season = (Element) seasons.item(i);
                System.out.println("Current season is " + season.getAttributes().getNamedItem("year").getNodeValue());

                // Get teams
                NodeList teams = season.getElementsByTagName("team");
                for (int j = 0; j < teams.getLength(); j++) {
                    Element team = (Element) teams.item(j);
                    String teamName = team.getAttributes().getNamedItem("name").getNodeValue();
                    System.out.println("Current team is " + teamName);

                    Row row;

                    switch (teamName) {
                        case "Alex": row = alex; break;
                        case "Ryan": row = ryan; break;
                        case "Bilal": row = bilal; break;
                        case "Fred": row = fred; break;
                        default: throw new RuntimeException("team name was not expcted");
                    }

                    // Print scores
                    List<Double> scores = getScoresFromXML(team.getElementsByTagName("score"));
                    for (int s = 0; s < scores.size(); s++) {
                        int lastCellNum = row.getLastCellNum();
                        row.createCell(lastCellNum).setCellValue(scores.get(s));
                    }
                }
            }

            FileOutputStream fileOut = new FileOutputStream("All Scores.xls");
            wb.write(fileOut);
            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to parse xml");
        }
    }

    /**
     * Takes an array of scores an outputs xml representing a team with those scores
     *
     * @param scores
     *          array of scores
     * @param name
     *          name of team
     */
    public static void arrayToXML (double[] scores, String name) {
        System.out.println("<team name=\"" + name + "\">");
        for (int week = 1; week <= 17; week++) {
            System.out.print("    ");
            System.out.println("<score week=\"" + week + "\">" + scores[week-1] + "</score>");
        }
        System.out.println("    <!-- Get weeks 15 & 16 -->");
        System.out.println("</team>");
    }

    /**
     * Used to get the scores from a copied and pasted history webpage into an xml file
     *
     * @param filePath
     *          text file
     */
    public static void textToXML (String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            double[] scores = new double[17];
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                if (line.contains("-")) {
                    String[] s = line.split("-");
                    scores[count] = Double.parseDouble(s[0]);
                    count++;
                }
            }
            arrayToXML(scores, "Ryan");
        } catch (Exception e) {
            System.out.println("Failed to read text to XML");
        }
    }

}
