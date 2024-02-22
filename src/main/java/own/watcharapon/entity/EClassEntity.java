package own.watcharapon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "e_class_table")
public class EClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    @ColumnDefault("uuid_generate_v4()")
    private UUID id;

    @Column(name = "fund", nullable = false, length = 10, unique = true)
    private String fund;

    @Column(name = "total_unit", precision = 8)
    private Double totalUnit;

    @Column(name = "cost_value", precision = 4)
    private String costValue;

    @Column(name = "last_update_nav")
    private LocalDateTime lastUpdateNav;

    @Column(name = "last_nav", precision = 4)
    private Double lastNav;

    @Column(name = "target", precision = 2)
    private Double target;

    @Column(name = "note")
    private String note;
}
