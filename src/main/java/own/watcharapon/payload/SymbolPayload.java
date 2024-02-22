package own.watcharapon.payload;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SymbolPayload {
    private UUID id;
    private String market;
    private String symbol;
}