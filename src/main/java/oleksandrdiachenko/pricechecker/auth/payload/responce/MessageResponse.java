package oleksandrdiachenko.pricechecker.auth.payload.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MessageResponse {

    private final String message;
}
