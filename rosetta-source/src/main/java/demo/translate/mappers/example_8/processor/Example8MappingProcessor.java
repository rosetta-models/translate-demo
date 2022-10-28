package demo.translate.mappers.example_8.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.path.RosettaPath;
import demo.translate.mappers.example_8.Z;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Example8MappingProcessor extends MappingProcessor {

    public Example8MappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    /**
     * Override the map method as this mapper is specified on a complex type attribute Y->zField to update the items
     */
    @Override
    public void map(Path xmlPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
        Collection<Z.ZBuilder> stringBuilders = (Collection<Z.ZBuilder>) builder;

        stringBuilders.forEach(Z->{
                Z.setStr1Field(Z.getStr1Field() + "_X");
                Z.setStr2Field(Z.getStr2Field() + "_Y");
                Z.setStr3Field(Z.getStr3Field() + "_Z");
                });

    }

    private Mapping createSuccessMapping(Path xmlPath, RosettaPath modelPath, Reference.ReferenceBuilder reference) {
        return new Mapping(xmlPath, null, PathUtils.toPath(modelPath), reference, null, true, true, false);
    }
}
