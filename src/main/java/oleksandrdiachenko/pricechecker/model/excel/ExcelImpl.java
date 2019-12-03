package oleksandrdiachenko.pricechecker.model.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.SheetUtil;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Alexander Diachenko.
 */
public class ExcelImpl implements Excel {

    /**
     * Return list representation of excel file.
     *
     * @param path Path to excel file.
     * @return List representation of excel file.
     * @throws IOException            Throws IOException if file read failed.
     */
    @Override
    public List<List<Object>> read(String path) throws IOException {
        List<List<Object>> table = new ArrayList<>();
        Workbook workbook = getWorkbook(path);
        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            table.addAll(getTableFromSheet(sheet));
        }
        return table;
    }

    private Workbook getWorkbook(String path) throws IOException {
        try (FileInputStream is = new FileInputStream(new File(path))) {
            return WorkbookFactory.create(is);
        }
    }

    private List<List<Object>> getTableFromSheet(Sheet sheet) {
        List<List<Object>> table = new ArrayList<>();
        Iterator rows = sheet.rowIterator();
        while (rows.hasNext()) {
            table.add(getRaw(rows));
        }
        return table;
    }

    private List<Object> getRaw(Iterator rows) {
        List<Object> raw = new ArrayList<>();
        Row row = (Row) rows.next();
        int index = 0;
        short lastCellNum = row.getLastCellNum();
        while (index < lastCellNum) {
            raw.add(getCell(row, index++));
        }
        return raw;
    }

    private String getCell(Row row, int index) {
        Cell cell = row.getCell(index, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        return getFormattedCell(cell);
    }

    private String getFormattedCell(Cell cell) {
        DataFormatter df = new DataFormatter();
        String result = df.formatCellValue(cell);
        return !result.equals("#NULL!") ? result : "";
    }

    /**
     * Write List<List<>> to checker.excel file.
     *
     * @param table Data in List<List<>>.
     * @param path  Path to new excel file.
     * @throws IOException Throws IOException if file write failed.
     */
    @Override
    public void write(List<List<Object>> table, String path) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("New sheet"));
        for (int rawIndex = 0; rawIndex < table.size(); rawIndex++) {
            List<Object> raw = table.get(rawIndex);
            Row row = sheet.createRow(rawIndex);
            for (int colIndex = 0; colIndex < raw.size(); colIndex++) {
                Object obj = raw.get(colIndex);
                Cell cell = row.createCell(colIndex);
                cell.setCellValue(String.valueOf(obj));
            }
        }
        autoResizeSheet(sheet);
        write(workbook, path);
    }

    private void write(Workbook workbook, String path) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(new File(path))) {
            workbook.write(outputStream);
        }
    }

    /**
     * Return sheet column count.
     *
     * @param sheet sheet of excel file.
     * @return int column count.
     */
    @Override
    public int getColumnCount(Sheet sheet) {
        int columnCount = 0;
        for (int rowNumber = 0; rowNumber <= sheet.getLastRowNum(); rowNumber++) {
            Row row = sheet.getRow(rowNumber);
            short lastCellNum = row.getLastCellNum();
            columnCount = Math.max(columnCount, lastCellNum);
        }
        return columnCount;
    }

    /**
     * Auto resize excel table. If column is empty - hide it
     *
     * @param sheet of table
     */
    @Override
    public void autoResizeSheet(Sheet sheet) {
        for (int columnIndex = 0; columnIndex < getColumnCount(sheet); columnIndex++) {
            if (isEmpty(sheet, columnIndex)) {
                hideColumn(sheet, columnIndex);
            }
            sheet.autoSizeColumn(columnIndex);
        }
    }

    private boolean isEmpty(Sheet sheet, int index) {
        double columnWidth = SheetUtil.getColumnWidth(sheet, index, true);
        return columnWidth < 2;
    }

    private void hideColumn(Sheet sheet, int index) {
        sheet.setColumnHidden(index, true);
    }
}
