package own.watcharapon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "assets_table")
public class AssetsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    @ColumnDefault("uuid_generate_v4()")
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "market_symbol", referencedColumnName = "market_symbol", unique = true)
    private WatchlistEntity marketSymbol;

    @Column(name = "total_share", precision = 8)
    private Double totalShare;

    @Column(name = "cost_value", precision = 8)
    private Double costValue;

    @Column(name = "holding_value", precision = 8)
    private Double holdingValue;

    @Column(name = "target", precision = 2)
    private Double target;

    @Column(name = "last_update_history_price")
    private LocalDateTime lastUpdateHistoryPrice;

    @Column(name = "ytd_price", precision = 2)
    private Double ytdPrice;

    @Column(name = "wk_price_1", precision = 2)
    private Double wkPrice1;

    @Column(name = "mo_price_1", precision = 2)
    private Double moPrice1;

    @Column(name = "mo_price_3", precision = 2)
    private Double moPrice3;

    @Column(name = "mo_price_6", precision = 2)
    private Double moPrice6;

    @Column(name = "year_price_1", precision = 2)
    private Double yearPrice1;

    @Column(name = "year_high", precision = 2)
    private Double yearHigh;

    @Column(name = "year_low", precision = 2)
    private Double yearLow;

    @Column(name = "percent_year", precision = 2)
    private Double percentYear;

    @Column(name = "last_update_last_price")
    private LocalDateTime lastUpdateLastPrice;

    @Column(name = "last_price", precision = 2)
    private Double lastPrice;

    @Column(name = "last_price_change", precision = 2)
    private Double lastPriceChange;

    @Column(name = "last_price_change_percentage", precision = 2)
    private Double lastPriceChangePercentage;

    @Column(name = "last_update_dividend")
    private LocalDateTime lastUpdateDividend;

    @Column(name = "ex_date")
    private LocalDate exDate;

    @Column(name = "dividend_yield", precision = 2)
    private Double dividendYield;

    @Column(name = "dividend_amount", precision = 2)
    private Double dividendAmount;

    @Column(name = "payout_ratio", precision = 2)
    private Double payoutRatio;

    @Column(name = "note")
    private String note;
}
