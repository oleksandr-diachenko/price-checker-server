package oleksandrdiachenko.pricechecker.service;

import oleksandrdiachenko.pricechecker.model.magazine.Magazine;
import oleksandrdiachenko.pricechecker.service.excel.Excel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PriceCheckerServiceTest {

    @InjectMocks
    private PriceCheckService priceCheckService;

    @Mock
    private Excel excel;
    @Mock
    private Magazine magazine;

    @BeforeEach
    void setUp() {
        priceCheckService.setMagazines(Collections.singletonList(magazine));
    }

    @Test
    void shouldCallWriteWithEmptyListWhenBytesIsEmpty() throws Exception {
        byte[] bytes = new byte[0];
        when(excel.read(bytes)).thenReturn(Collections.emptyList());

        priceCheckService.getWorkbook(bytes, 1, 2);

        verify(excel).write(Collections.emptyList());
    }

    @Test
    void shouldCallWriteWithCurrentListWhenUrlColumnHigherThanTableSize() throws Exception {
        byte[] bytes = {1, 2, 3};
        List<List<String>> table = createTable(createList("SBA160002"));
        when(excel.read(bytes)).thenReturn(table);

        priceCheckService.getWorkbook(bytes, 2, 3);

        verify(excel).write(table);
    }

    @Test
    void shouldCallWriteWithCurrentListWhenNotThisWebsite() throws Exception {
        byte[] bytes = {1, 2, 3};
        String url = "www.site.com/product";
        List<List<String>> table = createTable(createList(url));
        when(excel.read(bytes)).thenReturn(table);
        when(magazine.isThisWebsite(url)).thenReturn(false);

        priceCheckService.getWorkbook(bytes, 1, 2);

        verify(excel).write(table);
    }

    private List<String> createList(String... strings) {
        return Arrays.stream(strings).collect(toList());
    }

    @SafeVarargs
    private List<List<String>> createTable(List<String>... lists) {
        return Arrays.stream(lists).collect(toList());
    }
}
