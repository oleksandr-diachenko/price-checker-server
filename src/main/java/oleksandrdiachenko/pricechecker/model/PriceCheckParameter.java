package oleksandrdiachenko.pricechecker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriceCheckParameter {

    private String name;
    private int urlColumn;
    private int insertColumn;
    byte[] bytes;
}
