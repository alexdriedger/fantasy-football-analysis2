package data;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.text.Style;
import java.awt.*;
import java.awt.Color;
import java.io.FileOutputStream;

import static java.awt.Color.blue;
import static java.awt.Color.red;

/**
 * Utility for writing simulation data to a visually pleasant spreadsheet
 */
public class SpreadSheets {

    public static void main (String[] args) {
        createSeasonOutcomesTemplate("test", 14);
    }

    // Color constants
    private static final byte[] COLOR_RGB_LEAGUE_NAME = new byte[]{(byte) 91, (byte) 155, (byte) 213};
    private static final byte[] COLOR_RGB_LEAGUE_YEAR = new byte[]{(byte) 155, (byte) 194, (byte) 230};
    private static final byte[] COLOR_RGB_WINS_TITLE = new byte[]{(byte) 198, (byte) 224, (byte) 180};
    private static final byte[] COLOR_RGB_CHAMPIONSHIPS_TITLE = new byte[]{(byte) 255, (byte) 192, (byte) 0};
    private static final byte[] COLOR_RGB_BAD_BACKGROUND = new byte[]{(byte) 255, (byte) 199, (byte) 206};
    private static final byte[] COLOR_RGB_BAD_FONT = new byte[]{(byte) 156, (byte) 0, (byte) 6};
    private static final byte[] COLOR_RGB_GOOD_BACKGROUND = new byte[]{(byte) 198, (byte) 239, (byte) 206};
    private static final byte[] COLOR_RGB_GOOD_FONT = new byte[]{(byte) 0, (byte) 97, (byte) 0};
    private static final byte[] COLOR_RGB_NEUTRAL_BACKGROUND = new byte[]{(byte) 255, (byte) 235, (byte) 156};
    private static final byte[] COLOR_RGB_NEUTRAL_FONT = new byte[]{(byte) 156, (byte) 101, (byte) 0};

    // Font constants
    private static final String FONT_DEFAULT_NAME = "Calibri";
    private static final short FONT_LEAGUE_NAME_HEIGHT = 18;
    private static final short FONT_LEAGUE_YEAR_HEIGHT = 11;
    private static final short FONT_DATA_HEIGHT = 11;

    // Column locations
    private static final int COL_BEGINNING_OF_SHEET = 0;

    // Row locations
    private static final int ROW_LEAGUE_NAME = 0;
    private static final int ROW_LEAGUE_YEAR = 1;
    private static final int ROW_RAW_WINS_HEADER = 3;

    // Titles of sections
    private static final String TITLE_WINS = "Wins";

    /**
     * Creates a new {@link Workbook} with one {@link Sheet} created with the season simulation template.
     *
     * @return
     *          Workbook ready for season sim data
     */
    public static Workbook createSeasonOutcomesTemplate(String sheetName, int lengthOfRegularSeason) {
        // TODO : IMPLEMENT THIS
        XSSFWorkbook wb = new XSSFWorkbook();
        CreationHelper creationHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet(sheetName);

        createWinDistributionTemplate(wb, sheet, lengthOfRegularSeason);
        createHeader(wb, sheet, lengthOfRegularSeason);


        try {
            FileOutputStream fileOut = new FileOutputStream("src/data/testing.xlsx");
            wb.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not write output file");
        }

        return wb; // TODO : CHANGE THIS
    }

    /**
     * Creates a custom style. The style will have Arial font and a solid color foreground
     *
     * @param wb
     *          Workbook the style is for. Must be an XSSFWorkbook
     * @param rgb
     *          rgb values of background color
     * @param fontHeight
     *          Height of the font
     * @param bold
     *          True if the font will be bold
     * @return
     *          CellStyle
     */
    private static XSSFCellStyle getCustomStyle(XSSFWorkbook wb, byte[] rgb , short fontHeight, boolean bold) {
        // Create Style
        XSSFCellStyle customStyle = wb.createCellStyle();

        // Fill cell with specified color
        customStyle.setFillForegroundColor(new XSSFColor(rgb));
        customStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Create Font
        Font font = wb.createFont();
        font.setFontHeightInPoints(fontHeight);
        font.setFontName(FONT_DEFAULT_NAME);
        font.setBold(bold);

        // Set font with style
        customStyle.setFont(font);

        customStyle.setAlignment(HorizontalAlignment.CENTER);

        return customStyle;
    }

    /**
     * Creates the league name and year rows for the spreadsheet
     *
     * @param wb
     *          Workbook to be used
     * @param sheet
     *          Sheet the header is going on
     */
    private static void createHeader(XSSFWorkbook wb, Sheet sheet, int lengthOfRegularSeason) {
        // Create row for league name
        createMergeRowWithStyle(sheet,
                getCustomStyle(wb, COLOR_RGB_LEAGUE_NAME, FONT_LEAGUE_NAME_HEIGHT, true),
                ROW_LEAGUE_NAME, COL_BEGINNING_OF_SHEET, (lengthOfRegularSeason + 1));

        // Create row for league year
        createMergeRowWithStyle(sheet,
                getCustomStyle(wb, COLOR_RGB_LEAGUE_YEAR, FONT_LEAGUE_YEAR_HEIGHT, false),
                ROW_LEAGUE_YEAR, COL_BEGINNING_OF_SHEET, (lengthOfRegularSeason + 1));
    }

    private static void createWinDistributionTemplate(XSSFWorkbook wb, Sheet sheet, int maxWins) {
        // Get cell style for the title cell
        XSSFCellStyle titleStyle = getCustomStyle(wb, COLOR_RGB_WINS_TITLE, FONT_DATA_HEIGHT, true);

        // Create title cell
        Row winsRow = sheet.createRow(ROW_RAW_WINS_HEADER);
        Cell winsCell = winsRow.createCell(COL_BEGINNING_OF_SHEET);
        winsCell.setCellStyle(titleStyle);
        winsCell.setCellValue(TITLE_WINS);

        // Create a cell for each number of possible wins
        XSSFCellStyle numberStyle = getCustomStyle(wb, COLOR_RGB_WINS_TITLE, FONT_DATA_HEIGHT, false);
        final int startingColumn = COL_BEGINNING_OF_SHEET + 1;
        for (int wins = 0; wins <= maxWins; wins++) {
            Cell cell = winsRow.createCell(startingColumn + wins);
            cell.setCellValue(wins);
            cell.setCellStyle(numberStyle);
        }
    }

    private static void createMergeRowWithStyle(Sheet sheet, XSSFCellStyle style, int rowNumber, int startCol, int endCol) {
        // Merge cells
        sheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, startCol, endCol));

        // Create row and cell
        Row row = sheet.createRow(rowNumber);
        Cell cell = row.createCell(startCol);
        cell.setCellStyle(style);
    }

    public static void addWinDistribution(Sheet sheet, int[][] winDistribution) {
        // TODO : IMPLEMENT THIS
    }

    public static void addChampionships(int[] champtionships) {
        // TODO : IMPLEMENT THIS
    }
}
