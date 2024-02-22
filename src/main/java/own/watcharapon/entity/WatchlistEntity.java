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
@Table(name = "watchlist_table")
public class WatchlistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    @ColumnDefault("uuid_generate_v4()")
    private UUID id;

    //[market]#[symbol]
    @Column(name = "market_symbol", nullable = false, length = 20, unique = true)
    private String marketSymbol;

    @Column(name = "market", nullable = false, length = 10)
    private String market;

    @Column(name = "symbol", nullable = false, length = 10)
    private String symbol;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "industry", referencedColumnName = "industry_name")
    private IndustryEntity industry;

    @Column(name = "last_update_jitta")
    private LocalDateTime lastUpdateJitta;

    @Column(name = "score", precision = 2)
    private Double score;

    @Column(name = "state", length = 20)
    private String state;

    @Column(name = "line", precision = 2)
    private Double line;

    @Column(name = "note")
    private String note;

    public WatchlistEntity(String marketSymbol, String market, String symbol, IndustryEntity industry) {
        this.marketSymbol = marketSymbol;
        this.market = market;
        this.symbol = symbol;
        this.industry = industry;
    }

    public WatchlistEntity(UUID id, String market, String symbol) {
        this.id = id;
        this.market = market;
        this.symbol = symbol;
    }

    public WatchlistEntity(UUID id, String marketSymbol) {
        this.id = id;
        this.marketSymbol = marketSymbol;
    }
}
