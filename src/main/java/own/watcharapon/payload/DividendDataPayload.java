package own.watcharapon.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DividendDataPayload {
    private Integer year;
    private Integer month;
    private Double totalPrice;
    private Double totalFee;
}
