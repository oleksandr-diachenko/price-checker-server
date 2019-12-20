package oleksandrdiachenko.pricechecker.service.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Alexander Diachenko.
 */
class ExcelTest {

    private Excel excel;

    @BeforeEach
    void setUp() {
        excel = new ExcelImpl();
    }

    @Test
    void shouldContainsOneRowWhenExcelXlsxContainsOneRow() throws IOException, InvalidFormatException {
        List<List<String>> table = excel.read(getBytes("file/oneField.xlsx"));

        assertEquals("[[SAG060003, AGENT PROVOCATEUR FATALE EDP 50 ml spray, 6, 3760264453741]]",
                table.toString());
    }

    @Test
    void shouldContainsOneRowWhenExcelXlsContainsOneRow() throws IOException, InvalidFormatException {
        List<List<String>> table = excel.read(getBytes("file/oneField.xls"));

        assertEquals("[[SAZ010009, AZZARO CHROME EDT TESTER 100 ml spray]]", table.toString());
    }

    @Test
    void shouldContainsTwoRowsWhenExcelContainsTwoRows() throws IOException, InvalidFormatException {
        List<List<String>> table = excel.read(getBytes("file/twoField.xlsx"));

        assertEquals("[[SAB070001, ANNA SUI ROMANTICA EDT TESTER 75 ml spray, 10], " +
                "[SAB070002, ANNA SUI ROMANTICA EDT 30 ml spray, 42]]", table.toString());
    }

    @Test
    void shouldContainsOneRowWithSpacesWhenExcelContainsOneRowWithSpace() throws IOException, InvalidFormatException {
        List<List<String>> table = excel.read(getBytes("file/oneFieldWithSpace.xlsx"));

        assertEquals("[[SBA030010, , 34]]", table.toString());
    }

    @Test
    void shouldContainsTwoRowsWhenExcelContainsTwoRowsFirstShorter() throws Exception {
        List<List<String>> table = excel.read(getBytes("file/twoFieldDifferentSizeFirstShorter.xlsx"));

        assertEquals("[[SOT440001, 3760260453042], [SOT190003, 3760260451994, 50 ml, U]]", table.toString());
    }

    @Test
    void shouldContainsTwoRowWhenExcelContainsTwoRowSecondShorter() throws Exception {
        List<List<String>> table = excel.read(getBytes("file/twoFieldDifferentSizeSecondShorter.xlsx"));

        assertEquals("[[SOT190003, 3760260451994, 50 ml, U], [SOT440001, 3760260453042]]", table.toString());
    }

    @Test
    void shouldContainsOneRowWithSpacesWhenExcelContainsOneRowWithSpaces() throws Exception {
        List<List<String>> table = excel.read(getBytes("file/oneFieldSpacesAtBeginningAndAtMiddle.xlsx"));

        assertEquals("[[, , SOT440001, , , 3760260453042]]", table.toString());
    }

    @Test
    void shouldContainsThreeRowsDifferentSizeWhenExcelContainsThreeRowsDifferentSize() throws Exception {
        List<List<String>> table = excel.read(getBytes("file/threeFieldDifferentSize.xlsx"));

        assertEquals("[[SOT190003, 3760260451994, 50 ml, U], [SOT440001, 3760260453042], " +
                        "[SOT470001, 3760260623042, 100 ml, M, EDP]]", table.toString());
    }

    @Test
    void shouldContainsFiveRowsWhenExcelContainsFiveSheetsWithFiveRows() throws Exception {
        List<List<String>> table = excel.read(getBytes("file/fiveSheets.xlsx"));

        assertEquals("[[SBA030010, , 34], [SOT190003, 3760260451994, 50 ml, U, ], " +
                        "[SOT440001, 3760260453042, , , ], [SOT470001, 3760260623042, 100 ml, M, EDP]]",
                table.toString());
    }

    @Test
    void shouldCreateWorkbookWithOneRowWhenTableContainsOneRow() throws IOException {
        List<List<String>> table = createTable(createList("SBA160002", "8411061784273",
                        "ANTONIO BANDERAS KING OF SEDUCTION  MAN EDT 100 ml spray", "100", "EDT", "М", "15,30"));

        Workbook workbook = excel.write(table);

        assertEquals("SBA160002", getCellStringValue(workbook, 0, 0));
        assertEquals("100", getCellStringValue(workbook, 0, 3));
        assertEquals("15,30", getCellStringValue(workbook, 0, 6));
    }

    @Test
    void shouldCreateWorkbookWithTwoRowWhenTableContainsTwoRowSameSize() throws Exception {
        List<List<String>> table = createTable(createList("SBA160002", "8411061784273",
                        "ANTONIO BANDERAS KING OF SEDUCTION  MAN EDT 100 ml spray", "100", "EDT", "М", "15,30"),
                createList("SAN020002", "8427395660206",
                        "ANGEL SCHLESSER HOMME EDT 125 ml spray", "125", "EDT", "М", "16,40"));

        Workbook workbook = excel.write(table);

        assertEquals("SBA160002", getCellStringValue(workbook, 0, 0));
        assertEquals("15,30", getCellStringValue(workbook, 0, 6));
        assertEquals("SAN020002", getCellStringValue(workbook, 1, 0));
        assertEquals("125", getCellStringValue(workbook, 1, 3));
        assertEquals("16,40", getCellStringValue(workbook, 1, 6));
    }

    @Test
    void shouldCreateWorkbookWithTwoRowWithHiddenColumnWhenTableContainsTwoRowWithBlankColumn() throws Exception {
        List<List<String>> table = createTable(createList("SBA160002", "8411061784273",
                "", "100", "EDT", "М", "15,30"),
                createList("SAN020002", "8427395660206", "", "125", "EDT", "М", "16,40"));

        Workbook workbook = excel.write(table);

        assertEquals(256, workbook.getSheet("New sheet").getColumnWidth(2));
    }

    private byte[] getBytes(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        try {
            return Objects.requireNonNull(resource).openStream().readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read file", e);
        }
    }

    private List<String> createList(String... strings) {
        return Arrays.asList(strings);
    }

    @SafeVarargs
    private List<List<String>> createTable(List<String>... lists) {
        return Arrays.asList(lists);
    }

    private String getCellStringValue(Workbook workbook, int rowNumber, int cellNumber) {
        return workbook.getSheet("New sheet").getRow(rowNumber).getCell(cellNumber).getStringCellValue();
    }
}
