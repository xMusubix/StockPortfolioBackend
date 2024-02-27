package own.watcharapon.payload;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import own.watcharapon.entity.WatchlistEntity;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionStockPayload {
    private String marketSymbol;

    private LocalDate date;

    private String type;

    private Double share;

    private Double price;

    private Double fee;

    private String note;
}
