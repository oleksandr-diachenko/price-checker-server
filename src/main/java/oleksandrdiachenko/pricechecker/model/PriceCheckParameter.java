package oleksandrdiachenko.pricechecker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriceCheckParameter {

    private int urlColumn;
    private int insertColumn;
}
