package oleksandrdiachenko.pricechecker.util;

import java.io.IOException;

/**
 * @author : Oleksandr Diachenko
 * @since : 7/3/2020
 **/
public interface Reader {

    String read(String path) throws IOException;
}
