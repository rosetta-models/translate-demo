package demo.translate.mappers.example_3.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;

import java.util.List;

/**
 * The mapper class name must be in the form "<MapperName>MappingProcessor", and must extend MappingProcessor.
 */
@SuppressWarnings("unused")
public class Example3MappingProcessor extends MappingProcessor {

    public Example3MappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    /**
     * Override the map method as this mapper is specified on a basic type (with a metadata annotation) attribute Z->str2Field
     */
    @Override
    public void map(Path xmlPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        // parameter: xmlPath = a->b->c->e (the path where the "mapper" syntax is specified)
        // parameter: builder = ana instance of object FieldWithMetaString, where the value has been set to "CHIPS" (the value at the path where the mapper is specified)
        // parameter: parent = an instance of object Z.ZBuilder that can be updated

        Path parentXmlPath = xmlPath.getParent(); // a->b->c
        Path newXmlPath = parentXmlPath.addElement("d"); // a->b->c->d (e.g. value of "FISH")

        // this helper function will look up a xml path, and pass it to the consumer function, then updates the mapping stats
        setValueAndUpdateMappings(newXmlPath,
                // consumer function that takes value found at the xml path
                xmlValueFromNewXmlPath -> {
                    // cast builder object
                    FieldWithMetaString.FieldWithMetaStringBuilder metaStringBuilder =
                            (FieldWithMetaString.FieldWithMetaStringBuilder) builder;
                    // create new value to set
                    String newValue = metaStringBuilder.getValue() + "_" + xmlValueFromNewXmlPath;
                    // set new value on builder object
                    metaStringBuilder.setValue(newValue);
                });
    }
}
