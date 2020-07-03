package oleksandrdiachenko.pricechecker.util;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author : Oleksandr Diachenko
 * @since : 7/3/2020
 **/
@Component
public class FileReader implements Reader {

    @Override
    public String read(String path) throws IOException {
        return IOUtils.toString(getClass().getResourceAsStream(path), UTF_8);
    }
}
