package own.watcharapon.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavingSummaryPayload {
    private String application;
    private Double amount;
}
