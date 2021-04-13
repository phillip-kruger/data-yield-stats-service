package xyz.property.data.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import xyz.property.data.model.OutCodeStats;
import xyz.property.data.model.YieldStats;

@Mapper
public interface OutcodeStatsMapper {

    OutcodeStatsMapper INSTANCE = Mappers.getMapper(OutcodeStatsMapper.class);

    @Mapping(source = "outcode", target = "postcode")
    YieldStats outcodeStatsToYieldStats(OutCodeStats stats);

    @AfterMapping
    default void fillData(OutCodeStats outCodeStats, @MappingTarget YieldStats yieldStats) {
        yieldStats.postcode_type= "outcode";
        yieldStats.data.long_let.gross_yield = Double.toString(outCodeStats.avgYield);
    }
}
