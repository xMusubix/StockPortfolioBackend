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
@Table(name = "transaction_stock_table")
public class TransactionStockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    @ColumnDefault("uuid_generate_v4()")
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "market_symbol", referencedColumnName = "market_symbol")
    private WatchlistEntity marketSymbol;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "share", nullable = false, precision = 8)
    private Double share;

    @Column(name = "price", nullable = false, precision = 8)
    private Double price;

    @Column(name = "fee", nullable = false, precision = 2)
    private Double fee;

    @Column(name = "note")
    private String note;
}
