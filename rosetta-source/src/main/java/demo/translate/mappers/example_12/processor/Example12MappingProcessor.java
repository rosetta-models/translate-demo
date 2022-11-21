package demo.translate.mappers.example_12.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import demo.translate.mappers.example_12.Y;
import demo.translate.mappers.example_12.Z;

import java.util.List;
import java.util.Optional;

/**
 * The mapper class name must be in the form "<MapperName>MappingProcessor", and must extend MappingProcessor.
 */
@SuppressWarnings("unused")
public class Example12MappingProcessor extends MappingProcessor {

    public Example12MappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    /**
     * Override the map (list) method as this mapper is specified on a list type list attribute Root->zField
     */
    @Override
    public void map(Path xmlPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
        // parameter: xmlPath = a.b (the path where the "mapper" syntax is specified)
        // parameter: builder = an empty list of Z.ZBuilder (as nothing is mapped)
        // parameter: parent = an instance of Y.YBuilder

        Y.YBuilder zBuilder = (Y.YBuilder) parent;

        // Loop through the xml paths, incrementing the index until no xml elements are found
        int i = 0;
        while (true) {
            // Get parent path, e.g. from "a.b" to "a"
            Path aPath = xmlPath.getParent();
            Optional<? extends Z> z = getZBuilder(i++, aPath);
            // If z exists, add to the list and try the next index, otherwise break
            if (z.isPresent()) {
                zBuilder.addZField(z.get());
            } else {
                break;
            }
        }
    }

    private Optional<? extends Z> getZBuilder(Integer i, Path aPath) {
        Z.ZBuilder zBuilder = Z.builder();

        // set from path a.b(i).c
        setValueAndUpdateMappings(aPath.addElement("b", i).addElement("c"),
                xmlValue -> zBuilder.setStr1Field(xmlValue + "_X"));

        // set from path a.b(i).d
        setValueAndUpdateMappings(aPath.addElement("b", i).addElement("d"),
                xmlValue -> zBuilder.setStr2Field(xmlValue + "_X"));

        // set from path a.e(i).f
        setValueAndUpdateMappings(aPath.addElement("e", i).addElement("f"),
                xmlValue -> zBuilder.setStr3Field(xmlValue + "_Y"));

        // set from path a.e(i).g
        setValueAndUpdateMappings(aPath.addElement("e", i).addElement("g"),
                xmlValue -> zBuilder.setStr4Field(xmlValue + "_Y"));

        return zBuilder.hasData() ? Optional.of(zBuilder.build()) : Optional.empty();
    }
}