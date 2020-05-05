package oleksandrdiachenko.pricechecker.service;

import oleksandrdiachenko.pricechecker.model.magazine.Magazine;
import oleksandrdiachenko.pricechecker.service.excel.Excel;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PriceCheckerServiceTest {

    private static final String URL = "http://www.site.com/product";
    private static final byte[] BYTES = {1, 2, 3};
    private static final String PRICE = "100";

    @InjectMocks
    private PriceCheckService priceCheckService;

    @Spy
    private HashSet<Magazine> magazines;
    @Mock
    private Excel excel;
    @Mock
    private Magazine magazine;
    @Mock
    private Document document;

    @BeforeEach
    void setUp() {
        magazines.add(magazine);
    }

    @Test
    void shouldCallWriteWithEmptyListWhenBytesIsEmpty() throws Exception {
        when(excel.read(new byte[0])).thenReturn(Collections.emptyList());

        priceCheckService.getWorkbook(new byte[0], 0, 1);

        verify(excel).buildWorkBook(Collections.emptyList());
    }

    @Test
    void shouldCallWriteWithCurrentListWhenUrlColumnHigherThanTableSize() throws Exception {
        List<List<String>> table = createTable(createList("SBA160002"));
        when(excel.read(BYTES)).thenReturn(table);

        priceCheckService.getWorkbook(BYTES, 1, 2);

        verify(excel).buildWorkBook(table);
    }

    @Test
    void shouldCallWriteWithCurrentListWhenNotThisWebsite() throws Exception {
        List<List<String>> table = createTable(createList(URL));
        when(excel.read(BYTES)).thenReturn(table);
        when(magazine.isThisWebsite(URL)).thenReturn(false);

        priceCheckService.getWorkbook(BYTES, 0, 1);

        verify(excel).buildWorkBook(table);
    }

    @Test
    void shouldCallWriteWithInsertedListWhenListContainsOneField() throws Exception {
        List<List<String>> table = createTable(createList(URL));
        when(excel.read(BYTES)).thenReturn(table);
        when(magazine.isThisWebsite(URL)).thenReturn(true);
        when(magazine.getDocument(URL)).thenReturn(document);
        when(magazine.getPrice(document)).thenReturn(PRICE);

        priceCheckService.getWorkbook(BYTES, 0, 1);

        List<List<String>> priceTable = createTable(createList(URL, PRICE));
        verify(excel).buildWorkBook(priceTable);
    }

    @Test
    void shouldCallWriteWithInsertedListWhenInsertingInBusyColumn() throws Exception {
        List<List<String>> table = createTable(createList(URL, "data"));
        when(excel.read(BYTES)).thenReturn(table);
        when(magazine.isThisWebsite(URL)).thenReturn(true);
        when(magazine.getDocument(URL)).thenReturn(document);
        when(magazine.getPrice(document)).thenReturn(PRICE);

        priceCheckService.getWorkbook(BYTES, 0, 1);

        List<List<String>> priceTable = createTable(createList(URL, PRICE, "data"));
        verify(excel).buildWorkBook(priceTable);
    }

    @Test
    void shouldCallWriteWithInsertedListWhenInsertingInNonExistingColumn() throws Exception {
        List<List<String>> table = createTable(createList(URL));
        when(excel.read(BYTES)).thenReturn(table);
        when(magazine.isThisWebsite(URL)).thenReturn(true);
        when(magazine.getDocument(URL)).thenReturn(document);
        when(magazine.getPrice(document)).thenReturn(PRICE);

        priceCheckService.getWorkbook(BYTES, 0, 2);

        List<List<String>> priceTable = createTable(createList(URL, EMPTY, PRICE));
        verify(excel).buildWorkBook(priceTable);
    }

    @Test
    void shouldCallWriteWithCurrentListWhenUrlColumnMinus() throws Exception {
        List<List<String>> table = createTable(createList(URL));
        when(excel.read(BYTES)).thenReturn(table);

        priceCheckService.getWorkbook(BYTES, -1, 1);

        verify(excel).buildWorkBook(table);
    }

    @Test
    void shouldCallWriteWithCurrentListWhenInsertedColumnMinus() throws Exception {
        List<List<String>> table = createTable(createList(URL));
        when(excel.read(BYTES)).thenReturn(table);

        priceCheckService.getWorkbook(BYTES, 0, -1);

        verify(excel).buildWorkBook(table);
    }

    private List<String> createList(String... strings) {
        return Arrays.stream(strings).collect(toList());
    }

    @SafeVarargs
    private List<List<String>> createTable(List<String>... lists) {
        return Arrays.stream(lists).collect(toList());
    }
}
