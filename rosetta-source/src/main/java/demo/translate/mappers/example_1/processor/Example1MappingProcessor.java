package demo.translate.mappers.example_1.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import demo.translate.mappers.example_1.Z;

import java.util.List;

/**
 * The mapper class name must be in the form "<MapperName>MappingProcessor", and must extend MappingProcessor.
 */
@SuppressWarnings("unused")
public class Example1MappingProcessor extends MappingProcessor {

    public Example1MappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    /**
     * Override the mapBasic method as this mapper is specified on a basic type attribute Z->str2Field
     */
    @Override
    public <T> void mapBasic(Path xmlPath, T xmlValueFromXmlPath, RosettaModelObjectBuilder parent) {
        // parameter: xmlPath = a->b->c->e (the path where the "mapper" syntax is specified)
        // parameter: xmlValueFromXmlPath = "CHIPS" (the value at the path where the mapper is specified)
        // parameter: parent = an instance of object Z.ZBuilder that can be updated

        Path parentXmlPath = xmlPath.getParent(); // a->b->c
        Path newXmlPath = parentXmlPath.addElement("d"); // a->b->c->d (e.g. value of "FISH")

        // this helper function will look up a xml path, and pass it to the consumer function, then updates the mapping stats
        setValueAndUpdateMappings(newXmlPath,
                // consumer function that takes value found at the xml path
                xmlValueFromNewXmlPath -> {
                    // cast builder object
                    Z.ZBuilder zBuilder = (Z.ZBuilder) parent;
                    // create new value to set
                    String newValue = xmlValueFromXmlPath + "_" + xmlValueFromNewXmlPath;
                    // set new value on builder object
                    zBuilder.setStr2Field(newValue);
                });
    }
}