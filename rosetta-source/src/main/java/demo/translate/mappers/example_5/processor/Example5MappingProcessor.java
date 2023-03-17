package demo.translate.mappers.example_5.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

//import static demo.translate.mappers.example_5.metafields.ReferenceWithMetaQuantity.*;

/**
 * The mapper class name must be in the form "<MapperName>MappingProcessor", and must extend MappingProcessor.
 */
@SuppressWarnings("unused") // Used in generated code
public class Example5MappingProcessor extends MappingProcessor {

    public Example5MappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    /**
     * Override the map method as this mapper is specified on a complex type attribute ResolvablePayoutQuantity->resolvedQuantity
     */
    @Override
    public void map(Path xmlPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        // parameter: xmlPath = a (the path where the "mapper" syntax is specified)
        // parameter: builder = an instance of object ReferenceWithMetaQuantityBuilder.ReferenceWithMetaQuantityBuilderBuilder that can be updated

        // Cast the builder to the correct type, and create a reference
//        ReferenceWithMetaQuantityBuilder quantityBuilder = (ReferenceWithMetaQuantityBuilder) builder;
//        Reference.ReferenceBuilder reference = quantityBuilder.getOrCreateReference();

        // Add a new mapping linking the xml path to this model path, with the relevant reference object so the
        // reference post processor can figure out what should be a reference.
//        getMappings().add(createSuccessMapping(xmlPath, getModelPath(), reference));
    }

    private Mapping createSuccessMapping(Path xmlPath, RosettaPath modelPath, Reference.ReferenceBuilder reference) {
        return new Mapping(xmlPath, null, PathUtils.toPath(modelPath), reference, null, true, true, false);
    }
}
