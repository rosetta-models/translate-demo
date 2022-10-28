package demo.translate.mappers.example_9.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;
import demo.translate.mappers.example_9.Z;
import demo.translate.mappers.example_9.Root.RootBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Example9MappingProcessor extends MappingProcessor {

    public Example9MappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    /**
     * Override the mapBasic method as this mapper is specified on a basic type list attribute Z->str2Field
     */
    @Override
    public void map(Path xmlPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
        RootBuilder rootBuilder = (RootBuilder) parent;
        rootBuilder.setZField(new ArrayList<>());

        int i = 0;
        while(true){
            Optional<? extends Z> zField = getzBuilder(xmlPath, i++);
            if (zField.isPresent()){
                rootBuilder.addZField(zField.get());
            } else {
                break;
            }
        }
    }

    private Optional<? extends Z> getzBuilder(Path xmlPath, Integer i) {
        Z.ZBuilder z = Z.builder();
        setValueAndUpdateMappings(xmlPath.getParent().addElement("a", i).addElement("b").addElement("c").addElement("d"),
                xmlValue -> {
                    z.setStr1Field(xmlValue + "_X");
                });
        setValueAndUpdateMappings(xmlPath.getParent().addElement("a", i).addElement("b").addElement("c").addElement("e"),
                xmlValue -> {
                    z.setStr2Field(xmlValue + "_Y");
                });
        setValueAndUpdateMappings(xmlPath.getParent().addElement("a", i).addElement("b").addElement("c").addElement("f"),
                xmlValue -> {
                    z.setStr3Field(xmlValue + "_Z");
                });
        return z.hasData() ? Optional.of(z.build()) : Optional.empty();
    }
}