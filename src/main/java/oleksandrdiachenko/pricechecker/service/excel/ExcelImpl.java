package oleksandrdiachenko.pricechecker.service.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.SheetUtil;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Alexander Diachenko.
 */
@Component
@Scope
public class ExcelImpl implements Excel {

    @Override
    public List<List<String>> read(byte[] bytes) throws IOException {
        List<List<String>> table = new ArrayList<>();
        Workbook workbook = getWorkbook(bytes);
        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            table.addAll(getTableFromSheet(sheet));
        }
        return table;
    }

    private Workbook getWorkbook(byte[] bytes) throws IOException {
        return WorkbookFactory.create(new ByteArrayInputStream(bytes));
    }

    private List<List<String>> getTableFromSheet(Sheet sheet) {
        List<List<String>> table = new ArrayList<>();
        Iterator<Row> rows = sheet.rowIterator();
        while (rows.hasNext()) {
            table.add(getRaw(rows));
        }
        return table;
    }

    private List<String> getRaw(Iterator<Row> rows) {
        List<String> raw = new ArrayList<>();
        Row row = rows.next();
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

    @Override
    public Workbook buildWorkBook(List<List<String>> table) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("New sheet"));
        for (int rawIndex = 0; rawIndex < table.size(); rawIndex++) {
            List<String> raw = table.get(rawIndex);
            Row row = sheet.createRow(rawIndex);
            for (int colIndex = 0; colIndex < raw.size(); colIndex++) {
                Object obj = raw.get(colIndex);
                Cell cell = row.createCell(colIndex);
                cell.setCellValue(String.valueOf(obj));
            }
        }
        autoResizeSheet(sheet);
        return workbook;
    }

    @Override
    public int getColumnCount(Sheet sheet) {
        int columnCount = 0;
        for (int rowNumber = 0; rowNumber <= sheet.getLastRowNum(); rowNumber++) {
            Row row = sheet.getRow(rowNumber);
            short lastCellNum = row.getLastCellNum(); //TODO при пустом файле паадет НПЕ
            columnCount = Math.max(columnCount, lastCellNum);
        }
        return columnCount;
    }

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
