package demo.translate.mappers.example_6.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.path.RosettaPath;

import java.math.BigDecimal;
import java.util.List;

import static demo.translate.mappers.example_6.metafields.FieldWithMetaQuantity.FieldWithMetaQuantityBuilder;

/**
 * The mapper class name must be in the form "<MapperName>MappingProcessor", and must extend MappingProcessor.
 */
@SuppressWarnings("unused")
public class Example6MappingProcessor extends MappingProcessor {

    public Example6MappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    /**
     * Override the map method as this mapper is specified on a complex type attribute PriceQuantity->quantity
     */
    @Override
    public void map(Path xmlPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        // parameter: xmlPath = a (the path where the "mapper" syntax is specified)
        // parameter: builder = an instance of object FieldWithMetaQuantityBuilder.FieldWithMetaQuantityBuilderBuilder that can be updated

        // Set the quantity from the path (and update mappings), and the location/address reference should just work
        setValueAndUpdateMappings(xmlPath.addElement("c"),
                (xmlValue) -> ((FieldWithMetaQuantityBuilder) builder).getOrCreateValue().setAmount(new BigDecimal(xmlValue)));
    }

    private Mapping createSuccessMapping(Path xmlPath, RosettaPath modelPath, Reference.ReferenceBuilder reference) {
        return new Mapping(xmlPath, null, PathUtils.toPath(modelPath), reference, null, true, true, false);
    }
}