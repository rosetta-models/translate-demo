package demo.translate.mappers.example_12.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import demo.translate.mappers.example_12.A;
import demo.translate.mappers.example_12.Root.RootBuilder;
import demo.translate.mappers.example_12.Z;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Example12MappingProcessor extends MappingProcessor {

    public Example12MappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    /**
     * Override the mapBasic method as this mapper is specified on a basic type list attribute Z->str2Field
     */

    @Override
    public void map(Path xmlPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
        Z.ZBuilder zBuilder = (Z.ZBuilder) parent;
        zBuilder.setAField(new ArrayList<>());

        int i = 0;
        while(true){
            Optional<? extends A> aField = getaBuilder(xmlPath, i++);
            if (aField.isPresent()){
                zBuilder.addAField(aField.get());
            } else {
                break;
            }
        }
    }

    private Optional<? extends A> getaBuilder(Path xmlPath, Integer i) {

        A.ABuilder a = A.builder();
        /*
        //Alternative code
        setValueAndUpdateMappings(xmlPath.getParent().addElement(xmlPath.getLastElement().getPathName(), i).addElement("c"),
                xmlValue -> {
                    a.setStr1Field(xmlValue + "_X");
                });
        setValueAndUpdateMappings(xmlPath.getParent().addElement(xmlPath.getLastElement().getPathName(), i).addElement("d"),
                xmlValue -> {
                    a.setStr2Field(xmlValue + "_X");
                });
        */

        setValueAndUpdateMappings(xmlPath.getParent().addElement( "b", i).addElement("c"),
                xmlValue -> {
                    a.setStr1Field(xmlValue + "_X");
                });
        setValueAndUpdateMappings(xmlPath.getParent().addElement("b", i).addElement("d"),
                xmlValue -> {
                    a.setStr2Field(xmlValue + "_X");
                });
        setValueAndUpdateMappings(xmlPath.getParent().addElement( "e", i).addElement("f"),
                xmlValue -> {
                    a.setStr3Field(xmlValue + "_Y");
                });
        setValueAndUpdateMappings(xmlPath.getParent().addElement("e", i).addElement("g"),
                xmlValue -> {
                    a.setStr4Field(xmlValue + "_Y");
                });
        return a.hasData() ? Optional.of(a.build()) : Optional.empty();
    }
}