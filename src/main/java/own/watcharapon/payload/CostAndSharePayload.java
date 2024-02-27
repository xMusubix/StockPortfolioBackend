package own.watcharapon.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CostAndSharePayload {
    private String marketSymbol;
    private Double costValue;
    private Double totalShare;
}
