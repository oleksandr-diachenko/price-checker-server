package oleksandrdiachenko.pricechecker.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

@Slf4j
public class FileReader {

    public String read(String path) {
        URL resource = getClass().getClassLoader().getResource(path);
        if (ObjectUtils.isEmpty(resource)) {
            log.error("File {} not found", path);
            throw new IllegalArgumentException("File not found");
        }
        File file = new File(resource.getFile());
        try {
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            log.error("Can't read file {}", path);
            throw new IllegalArgumentException("Can't read file");
        }
    }
}
