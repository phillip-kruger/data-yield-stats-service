package xyz.property.data.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class OutCodeStats {
    public double avgPrice;
    public int avgPricePsf;
    public double avgRent;
    public double avgYield;
    public double growth1y;
    public double growth3y;
    public double growth5y;
    public String outcode;
    public int salesPerMonth;
    public int turnover;
    public long effectiveDate;
}
