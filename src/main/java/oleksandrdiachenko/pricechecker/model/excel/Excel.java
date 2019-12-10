package oleksandrdiachenko.pricechecker.model.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Alexander Diachenko.
 */
public interface Excel {
    /**
     * Return list representation of excel file.
     *
     * @param bytes Byte array of the excel file.
     * @return List representation of excel file.
     * @throws IOException            Throws IOException if file read failed.
     * @throws InvalidFormatException Throws InvalidFormatException if it is not excel file(.xls or .xlsx).
     */
    List<List<String>> read(final byte[] bytes) throws IOException, InvalidFormatException;

    /**
     * Write List<List<>> to excel file.
     *
     * @param table Data in List<List<>>.
     * @throws IOException Throws IOException if file write failed.
     */
    byte[] getNewTable(final List<List<String>> table) throws IOException;

    /**
     * Return sheet column count.
     *
     * @param sheet sheet of excel file.
     * @return column count.
     */
    int getColumnCount(final Sheet sheet);

    /**
     * Auto resize excel table. If column is empty - hide it
     *
     * @param sheet       of table
     */
    void autoResizeSheet(final Sheet sheet);
}
