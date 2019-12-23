package oleksandrdiachenko.pricechecker.service;

import org.springframework.stereotype.Service;

@Service
public class MainService {


    public void start(byte[] bytes, int urlColumn, int insertColumn) {
        System.out.println("Bytes: " + bytes.length
                + ", url column: " + urlColumn
                + ", insert column: " + insertColumn);
    }
}
