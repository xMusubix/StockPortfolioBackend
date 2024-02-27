package own.watcharapon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exchange_rate_table", uniqueConstraints = @UniqueConstraint(columnNames = {"date"}))
public class ExchangeRateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    @ColumnDefault("uuid_generate_v4()")
    private UUID id;

    @Column(name = "last_update", nullable = false)
    private LocalDate lastUpdate;

    @Column(name = "date", nullable = false, unique = true)
    private LocalDate date;

    @Column(name = "exchange_rate", nullable = false)
    private Double exchangeRate;

    public ExchangeRateEntity(LocalDate date, Double exchangeRate) {
        this.date = date;
        this.exchangeRate = exchangeRate;
    }
}
