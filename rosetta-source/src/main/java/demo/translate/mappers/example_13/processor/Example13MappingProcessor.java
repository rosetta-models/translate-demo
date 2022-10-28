package demo.translate.mappers.example_13.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.path.RosettaPath;

import  demo.translate.mappers.example_13.metafields.ReferenceWithMetaQuantity;
import demo.translate.mappers.example_6.metafields.FieldWithMetaQuantity;


import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Example13MappingProcessor extends MappingProcessor {

    public Example13MappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    public void map(Path xmlPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {

        setValueAndUpdateMappings(xmlPath.getParent().addElement("b").addElement("c"),
                (xmlValue) -> ((FieldWithMetaQuantity.FieldWithMetaQuantityBuilder) builder).getOrCreateValue().setAmount(new BigDecimal(xmlValue)));
    }

    private Mapping createSuccessMapping(Path xmlPath, RosettaPath modelPath, Reference.ReferenceBuilder reference) {
        return new Mapping(xmlPath, null, PathUtils.toPath(modelPath), reference, null, true, true, false);
    }
}
