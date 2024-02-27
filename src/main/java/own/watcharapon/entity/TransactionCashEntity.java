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
@Table(name = "transaction_cash_table")
public class TransactionCashEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    @ColumnDefault("uuid_generate_v4()")
    private UUID id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "ex_rate", nullable = false, precision = 2)
    private Double exRate;

    @Column(name = "thb", nullable = false, precision = 2)
    private Double thb;

    @Column(name = "usd", nullable = false, precision = 2)
    private Double usd;

    @Column(name = "note")
    private String note;
}
