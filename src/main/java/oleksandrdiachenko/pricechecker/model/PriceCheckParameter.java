package oleksandrdiachenko.pricechecker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
public class PriceCheckParameter {

    private String name;
    private int urlColumn;
    private int insertColumn;
    @ToString.Exclude
    byte[] bytes;
}
