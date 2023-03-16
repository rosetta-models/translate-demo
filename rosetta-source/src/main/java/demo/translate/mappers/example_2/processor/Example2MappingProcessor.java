package demo.translate.mappers.example_2.processor;

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
public class Example2MappingProcessor extends MappingProcessor {

    public Example2MappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    /**
     * Override the map method as this mapper is specified on a complex type attribute Y->zField
     */
    @Override
    public void map(Path xmlPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        // parameter: xmlPath = a (the path where the "mapper" syntax is specified)
        // parameter: builder = an instance of object Z.ZBuilder that can be updated
        // parameter: parent = an instance of parent object Root.RootBuilder that can be updated

        Path xmlPath1 = xmlPath.addElement("b").addElement("c").addElement("d"); // a->b->c->d (e.g. value of "FISH")

        // this helper function will look up a xml path, and pass it to the consumer function, then updates the mapping stats
        setValueAndUpdateMappings(xmlPath1,
                // consumer function that takes value found at the xml path
                xmlValueFromXmlPath1 -> {
                    // cast builder object
                    Z.ZBuilder zBuilder = (Z.ZBuilder) builder;
                    // set new value on builder object
                    zBuilder.setStr1Field(xmlValueFromXmlPath1 + "X");
                });

        Path xmlPath2 = xmlPath.addElement("b").addElement("c").addElement("e"); // a->b->c->e (e.g. value of "CHIPS")

        // this helper function will look up a xml path, and pass it to the consumer function, then updates the mapping stats
        setValueAndUpdateMappings(xmlPath2,
                // consumer function that takes value found at the xml path
                xmlValueFromXmlPath2 -> {
                    // cast builder object
                    Z.ZBuilder zBuilder = (Z.ZBuilder) builder;
                    // set new value on builder object
                    zBuilder.setStr2Field(xmlValueFromXmlPath2 + "_Y");
                });
    }
}
