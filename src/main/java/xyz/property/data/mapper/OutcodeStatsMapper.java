package xyz.property.data.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import xyz.property.data.model.YieldStats;
import xyz.property.data.model.OutCodeStats;

@Mapper
public interface OutcodeStatsMapper {

    OutcodeStatsMapper INSTANCE = Mappers.getMapper(OutcodeStatsMapper.class);

    @Mapping(source = "outcode", target = "postcode")
    @Mapping(source="salesPerMonth", target= "average_sales_per_month")
    @Mapping(source="turnover", target = "turnover_per_month")
    YieldStats outcodeStatsToYieldStats(OutCodeStats stats);

    @AfterMapping
    default void fillData(OutCodeStats outCodeStats, @MappingTarget YieldStats yieldStats) {
        yieldStats.postcode_type= "outcode";
    }
}
