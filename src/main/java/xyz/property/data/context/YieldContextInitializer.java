package xyz.property.data.context;

import org.infinispan.protostream.SerializationContextInitializer;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;
import xyz.property.data.model.YieldStats;


@AutoProtoSchemaBuilder(includeClasses = {YieldStats.class}, schemaPackageName = "stats.data.xyz.property")
public interface YieldContextInitializer extends SerializationContextInitializer {
}
