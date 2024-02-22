package own.watcharapon.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SymbolDataPayload {
    private String symbol;
    private String sector;
    private String industry;
    private String market;

    public SymbolDataPayload(String sector, String industry, String market) {
        this.sector = sector;
        this.industry = industry;
        this.market = market;
    }
}
