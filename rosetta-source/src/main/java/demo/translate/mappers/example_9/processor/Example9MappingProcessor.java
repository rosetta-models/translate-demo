package demo.translate.mappers.example_9.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import demo.translate.mappers.example_9.Z;
import demo.translate.mappers.example_9.Root.RootBuilder;

import java.util.List;
import java.util.Optional;

/**
 * The mapper class name must be in the form "<MapperName>MappingProcessor", and must extend MappingProcessor.
 */
@SuppressWarnings("unused") // Used in generated code
public class Example9MappingProcessor extends MappingProcessor {

    public Example9MappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path xmlPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
        RootBuilder rootBuilder = (RootBuilder) parent;

        // Loop through the xml paths, incrementing the index until no xml elements are found
        int i = 0;
        while (true) {
            // Attempt to build z
            Optional<? extends Z> z = getZBuilder(xmlPath, i++);
            // If z exists, add to the list and try the next index, otherwise break
            if (z.isPresent()) {
                rootBuilder.addZField(z.get());
            } else {
                break;
            }
        }
    }

    private Optional<? extends Z> getZBuilder(Path aPath, int pathIndex) {
        Z.ZBuilder zBuilder = Z.builder();

        // aPath is indexless, get the parent (dataDocument path) and add "a" element with index
        Path dataDocumentPath = aPath.getParent();
        Path cPath = dataDocumentPath.addElement("a", pathIndex).addElement("b").addElement("c");

        setValueAndUpdateMappings(cPath.addElement("d"),
                xmlValue -> zBuilder.setStr1Field(xmlValue + "_X"));

        setValueAndUpdateMappings(cPath.addElement("e"),
                xmlValue -> zBuilder.setStr2Field(xmlValue + "_Y"));

        setValueAndUpdateMappings(cPath.addElement("f"),
                xmlValue -> zBuilder.setStr3Field(xmlValue + "_Z"));

        return zBuilder.hasData() ? Optional.of(zBuilder.build()) : Optional.empty();
    }
}