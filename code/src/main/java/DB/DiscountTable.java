package DB;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DiscountTable {
    @Id
    private int shopID;
    @Id
    private int discountID;
    private boolean AND;
    private boolean OR;
    private boolean XOR;
    private boolean COND;
    private double PRECENTAGE;
    private boolean SIMPLE;
}
