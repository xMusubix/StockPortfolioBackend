package own.watcharapon.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "industry_table")
public class IndustryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    @ColumnDefault("uuid_generate_v4()")
    private UUID id;

    @Column(name = "industry_name ", nullable = false, unique = true, length = 50)
    private String industryName;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_name", referencedColumnName = "sector_name")
    private SectorEntity sectorName;

    public IndustryEntity(String industryName, SectorEntity sectorName) {
        this.industryName = industryName;
        this.sectorName = sectorName;
    }

    public IndustryEntity(UUID id, String industryName) {
        this.id = id;
        this.industryName = industryName;
    }
}
