package own.watcharapon.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummaryAssetsData {
    private LocalDateTime updateTime = LocalDateTime.now();
    private Long assetsCount;
    private Double target;
    private Double minScore;
    private Double maxScore;

    public SummaryAssetsData(Long assetsCount, Double target, Double minScore, Double maxScore) {
        this.assetsCount = assetsCount;
        this.target = target;
        this.minScore = minScore;
        this.maxScore = maxScore;
    }
}
