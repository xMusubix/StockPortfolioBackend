package own.watcharapon.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SumTargetBySector {
    private String sectorName;
    private Double totalTarget;
    private Double totalActual;
}
