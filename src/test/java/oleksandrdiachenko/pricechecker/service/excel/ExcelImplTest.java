package oleksandrdiachenko.pricechecker.service.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Alexander Diachenko.
 */
@ExtendWith(MockitoExtension.class)
class ExcelImplTest {

    @InjectMocks
    private ExcelImpl excel;

    @Mock
    private Sheet sheet;

    @Test
    void shouldContainsOneRowWhenExcelXlsxContainsOneRow() throws IOException, InvalidFormatException {
        List<List<String>> table = excel.read(getBytes("file/xls/oneField.xlsx"));

        assertThat(table.toString()).isEqualTo("[[SAG060003, AGENT PROVOCATEUR FATALE EDP 50 ml spray, 6, 3760264453741]]");
    }

    @Test
    void shouldContainsOneRowWhenExcelXlsContainsOneRow() throws IOException, InvalidFormatException {
        List<List<String>> table = excel.read(getBytes("file/xls/oneField.xls"));

        assertThat(table.toString()).isEqualTo("[[SAZ010009, AZZARO CHROME EDT TESTER 100 ml spray]]");
    }

    @Test
    void shouldContainsTwoRowsWhenExcelContainsTwoRows() throws IOException, InvalidFormatException {
        List<List<String>> table = excel.read(getBytes("file/xls/twoField.xlsx"));


        assertThat(table.toString()).isEqualTo("[[SAB070001, ANNA SUI ROMANTICA EDT TESTER 75 ml spray, 10], " +
                "[SAB070002, ANNA SUI ROMANTICA EDT 30 ml spray, 42]]");
    }

    @Test
    void shouldContainsOneRowWithSpacesWhenExcelContainsOneRowWithSpace() throws IOException, InvalidFormatException {
        List<List<String>> table = excel.read(getBytes("file/xls/oneFieldWithSpace.xlsx"));

        assertThat(table.toString()).isEqualTo("[[SBA030010, , 34]]");
    }

    @Test
    void shouldContainsTwoRowsWhenExcelContainsTwoRowsFirstShorter() throws Exception {
        List<List<String>> table = excel.read(getBytes("file/xls/twoFieldDifferentSizeFirstShorter.xlsx"));

        assertThat(table.toString()).isEqualTo("[[SOT440001, 3760260453042], [SOT190003, 3760260451994, 50 ml, U]]");
    }

    @Test
    void shouldContainsTwoRowWhenExcelContainsTwoRowSecondShorter() throws Exception {
        List<List<String>> table = excel.read(getBytes("file/xls/twoFieldDifferentSizeSecondShorter.xlsx"));

        assertThat(table.toString()).isEqualTo("[[SOT190003, 3760260451994, 50 ml, U], [SOT440001, 3760260453042]]");
    }

    @Test
    void shouldContainsOneRowWithSpacesWhenExcelContainsOneRowWithSpaces() throws Exception {
        List<List<String>> table = excel.read(getBytes("file/xls/oneFieldSpacesAtBeginningAndAtMiddle.xlsx"));

        assertThat(table.toString()).isEqualTo("[[, , SOT440001, , , 3760260453042]]");
    }

    @Test
    void shouldContainsThreeRowsDifferentSizeWhenExcelContainsThreeRowsDifferentSize() throws Exception {
        List<List<String>> table = excel.read(getBytes("file/xls/threeFieldDifferentSize.xlsx"));

        assertThat(table.toString()).isEqualTo("[[SOT190003, 3760260451994, 50 ml, U], [SOT440001, 3760260453042], " +
                "[SOT470001, 3760260623042, 100 ml, M, EDP]]");
    }

    @Test
    void shouldContainsFiveRowsWhenExcelContainsFiveSheetsWithFiveRows() throws Exception {
        List<List<String>> table = excel.read(getBytes("file/xls/fiveSheets.xlsx"));

        assertThat(table.toString()).isEqualTo("[[SBA030010, , 34], [SOT190003, 3760260451994, 50 ml, U, ], " +
                "[SOT440001, 3760260453042, , , ], [SOT470001, 3760260623042, 100 ml, M, EDP]]");
    }

    @Test
    void shouldCreateWorkbookWithOneRowWhenTableContainsOneRow() throws IOException {
        List<List<String>> table = createTable(createList("SBA160002", "8411061784273",
                "ANTONIO BANDERAS KING OF SEDUCTION  MAN EDT 100 ml spray", "100", "EDT", "М", "15,30"));

        Workbook workbook = excel.buildWorkBook(table);

        assertThat(getCellStringValue(workbook, 0, 0)).isEqualTo("SBA160002");
        assertThat(getCellStringValue(workbook, 0, 3)).isEqualTo("100");
        assertThat(getCellStringValue(workbook, 0, 6)).isEqualTo("15,30");
    }

    @Test
    void shouldCreateWorkbookWithTwoRowWhenTableContainsTwoRowSameSize() throws Exception {
        List<List<String>> table = createTable(createList("SBA160002", "8411061784273",
                "ANTONIO BANDERAS KING OF SEDUCTION  MAN EDT 100 ml spray", "100", "EDT", "М", "15,30"),
                createList("SAN020002", "8427395660206",
                        "ANGEL SCHLESSER HOMME EDT 125 ml spray", "125", "EDT", "М", "16,40"));

        Workbook workbook = excel.buildWorkBook(table);

        assertThat(getCellStringValue(workbook, 0, 0)).isEqualTo("SBA160002");
        assertThat(getCellStringValue(workbook, 0, 6)).isEqualTo("15,30");
        assertThat(getCellStringValue(workbook, 1, 0)).isEqualTo("SAN020002");
        assertThat(getCellStringValue(workbook, 1, 3)).isEqualTo("125");
        assertThat(getCellStringValue(workbook, 1, 6)).isEqualTo("16,40");
    }

    @Test
    void shouldCreateWorkbookWithTwoRowWithHiddenColumnWhenTableContainsTwoRowWithBlankColumn() throws Exception {
        List<List<String>> table = createTable(createList("SBA160002", "8411061784273",
                "", "100", "EDT", "М", "15,30"),
                createList("SAN020002", "8427395660206", "", "125", "EDT", "М", "16,40"));

        Workbook workbook = excel.buildWorkBook(table);

        assertThat(workbook.getSheet("New sheet").getColumnWidth(2)).isEqualTo(265);
    }

    @Test
    void shouldReturnZeroWhenColumnCountCalledWithEmptySheet() {
        when(sheet.getLastRowNum()).thenReturn(0);

        int columnCount = excel.getColumnCount(sheet);

        assertThat(columnCount).isEqualTo(0);
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
