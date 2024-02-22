package own.watcharapon.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndustryPlainPayload {
    private UUID sectorId;
    private String sector;
    private UUID industryId;
    private String industry;
}
