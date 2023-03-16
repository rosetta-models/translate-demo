package demo.translate.mappers.example_8.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import demo.translate.mappers.example_12.Z;

import java.util.List;

/**
 * The mapper class name must be in the form "<MapperName>MappingProcessor", and must extend MappingProcessor.
 */
@SuppressWarnings("unused") // Used in generated code
public class Example8MappingProcessor extends MappingProcessor {

    public Example8MappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    /**
     * Override the map method as this mapper is specified on a list attribute Y->zField
     */
    @Override
    public void map(Path xmlPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
        List<Z.ZBuilder> zBuilders = (List<Z.ZBuilder>) builder;

        zBuilders.forEach(zBuilder -> {
            zBuilder.setStr1Field(zBuilder.getStr1Field() + "_X");
            zBuilder.setStr2Field(zBuilder.getStr2Field() + "_Y");
            zBuilder.setStr3Field(zBuilder.getStr3Field() + "_Z");
        });
    }
}
