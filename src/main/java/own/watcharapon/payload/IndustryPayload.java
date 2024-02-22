package own.watcharapon.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndustryPayload {
    private UUID id;
    private String sector;
    private List<Industry> industryList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Industry {
        private UUID id;
        private String industryName;
    }
}
