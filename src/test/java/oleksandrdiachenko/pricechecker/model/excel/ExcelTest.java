package oleksandrdiachenko.pricechecker.model.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

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
    void readExcelTest_oneField() throws IOException, InvalidFormatException {
        List<List<Object>> table = excel.read(getFilePath("file/oneField.xlsx"));
        assertEquals("[[SAG060003, AGENT PROVOCATEUR FATALE EDP 50 ml spray, 6, 3760264453741]]", table.toString());
    }

    @Test
    void readExcelTest_oneField_Xls() throws IOException, InvalidFormatException {
        List<List<Object>> table = excel.read(getFilePath("file/oneField.xls"));
        assertEquals("[[SAZ010009, AZZARO CHROME EDT TESTER 100 ml spray]]", table.toString());
    }

    @Test
    void readExcelTest_twoField() throws IOException, InvalidFormatException {
        List<List<Object>> table = excel.read(getFilePath("file/twoField.xlsx"));
        assertEquals(
                "[[SAB070001, ANNA SUI ROMANTICA EDT TESTER 75 ml spray, 10], " +
                        "[SAB070002, ANNA SUI ROMANTICA EDT 30 ml spray, 42]]", table.toString());
    }

    @Test
    void readExcelTest_oneField_withSpace() throws IOException, InvalidFormatException {
        List<List<Object>> table = excel.read(getFilePath("file/oneFieldWithSpace.xlsx"));
        assertEquals(
                "[[SBA030010, , 34]]", table.toString());
    }

    @Test
    void writeExcelTest_oneField() throws IOException, InvalidFormatException {
        List<List<Object>> table = createTable(
                createList("SBA160002", "8411061784273", "ANTONIO BANDERAS KING OF SEDUCTION  MAN EDT 100 ml spray", "100", "EDT", "М", "15,30")
        );
        File tempFile = File.createTempFile("got-", ".xlsx");
        String outputPath = tempFile.getAbsolutePath();

        excel.write(table, outputPath);

        assertEquals("[[SBA160002, 8411061784273, ANTONIO BANDERAS KING OF SEDUCTION  MAN EDT 100 ml spray, 100, EDT, М, 15,30]]", excel.read(outputPath).toString());
    }

    @Test
    void writeExcelTest_twoField() throws IOException, InvalidFormatException {
        List<List<Object>> table = createTable(
                createList("SBA160002", "8411061784273", "ANTONIO BANDERAS KING OF SEDUCTION  MAN EDT 100 ml spray", "100", "EDT", "М", "15,30"),
                createList("SAN020002", "8427395660206", "ANGEL SCHLESSER HOMME EDT 125 ml spray", "125", "EDT", "М", "16,40")
        );
        File tempFile = File.createTempFile("got-", ".xlsx");
        String outputPath = tempFile.getAbsolutePath();

        excel.write(table, outputPath);

        assertEquals(
                "[[SBA160002, 8411061784273, ANTONIO BANDERAS KING OF SEDUCTION  MAN EDT 100 ml spray, 100, EDT, М, 15,30], " +
                        "[SAN020002, 8427395660206, ANGEL SCHLESSER HOMME EDT 125 ml spray, 125, EDT, М, 16,40]]", excel.read(outputPath).toString());
    }

    @Test
    void readExcelTest_twoField_different_size() throws IOException, InvalidFormatException {
        List<List<Object>> table = excel.read(getFilePath("file/twoFieldDifferentSizeFirstShorter.xlsx"));
        assertEquals(
                "[[SOT440001, 3760260453042], " +
                        "[SOT190003, 3760260451994, 50 ml, U]]", table.toString());
    }

    @Test
    void readExcelTest_oneField_withSpaces_beginningAndMiddle() throws IOException, InvalidFormatException {
        List<List<Object>> table = excel.read(getFilePath("file/oneFieldSpacesAtBeginningAndAtMiddle.xlsx"));
        assertEquals(
                "[[, , SOT440001, , , 3760260453042]]", table.toString());
    }

    @Test
    void readExcelTest_twoField_different_size2() throws IOException, InvalidFormatException {
        List<List<Object>> table = excel.read(getFilePath("file/twoFieldDifferentSizeSecondShorter.xlsx"));
        assertEquals(
                "[[SOT190003, 3760260451994, 50 ml, U], " +
                        "[SOT440001, 3760260453042]]", table.toString());
    }

    @Test
    void readExcelTest_threeField_different_size() throws IOException, InvalidFormatException {
        List<List<Object>> table = excel.read(getFilePath("file/threeFieldDifferentSize.xlsx"));
        assertEquals(
                "[[SOT190003, 3760260451994, 50 ml, U], " +
                        "[SOT440001, 3760260453042], " +
                        "[SOT470001, 3760260623042, 100 ml, M, EDP]]", table.toString());
    }

    @Test
    void getColumnCountTest_ThreeFieldDifferentSize() throws IOException {
        String path = getFilePath("file/threeFieldDifferentSize.xlsx");
        int columnCount = excel.getColumnCount(getSheet(path));
        assertEquals(5, columnCount);
    }

    @Test
    void readExcelTest_fiveSheets() throws IOException, InvalidFormatException {
        List<List<Object>> table = excel.read(getFilePath("file/fiveSheets.xlsx"));
        assertEquals(
                "[[SBA030010, , 34], " +
                        "[SOT190003, 3760260451994, 50 ml, U, ], " +
                        "[SOT440001, 3760260453042, , , ], " +
                        "[SOT470001, 3760260623042, 100 ml, M, EDP]]", table.toString());
    }

    private Sheet getSheet(String path) throws IOException {
        Workbook workbook = getWorkbook(path);
        return workbook.getSheetAt(0);
    }

    private String getFilePath(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource != null) {
            return new File(resource.getFile()).getAbsolutePath();
        }
        return "";
    }

    private Workbook getWorkbook(String path) throws IOException {
        try (FileInputStream is = new FileInputStream(new File(path))) {
            return WorkbookFactory.create(is);
        }
    }

    private List<Object> createList(Object... objects) {
        return Arrays.asList(objects);
    }

    @SafeVarargs
    private List<List<Object>> createTable(List<Object>... lists) {
        return Arrays.asList(lists);
    }
}
