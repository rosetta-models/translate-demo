package demo.translate.mappers.example_11.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;
import demo.translate.mappers.example_11.Z;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Example11MappingProcessor extends MappingProcessor {

    public Example11MappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    /**
     * Override the mapBasic method as this mapper is specified on a basic type list attribute Z->str2Field
     */
    @Override
    public void map(Path xmlPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
            // parameter: xmlPath = a (the path where the "mapper" syntax is specified)
            // parameter: builder = an instance of object Z.ZBuilder that can be updated
            // parameter: parent = an instance of parent object Root.RootBuilder that can be updated
            // create new value to set
            setValueAndUpdateMappings(xmlPath,
                xmlValueFromPath -> {
                    FieldWithMetaString.FieldWithMetaStringBuilder metaStringBuilder =
                            (FieldWithMetaString.FieldWithMetaStringBuilder) builder;
                    String newValue = metaStringBuilder.getValue() + "_" + xmlValueFromPath;
                    // set new value on builder object
                    metaStringBuilder
                            .setValue(newValue)
                            .setMeta(MetaFields.builder()
                                    .setScheme("foo-scheme-set-by-mapper-multi-cardinality"));
                });

            Path xmlPath1 = xmlPath.addElement("b").addElement("c").addElement("d"); // a->b->c->d (e.g. value of "FISH")

            // this helper function will look up a xml path, and pass it to the consumer function, then updates the mapping stats
            setValueAndUpdateMappings(xmlPath1,
                    // consumer function that takes value found at the xml path
                    xmlValueFromXmlPath1 -> {
                        // cast builder object
                        Z.ZBuilder zBuilder = (Z.ZBuilder) builder;
                        // set new value on builder object
                        zBuilder.setStr1Field(xmlValueFromXmlPath1 + "_X");
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

        Path xmlPath3 = xmlPath.addElement("b").addElement("c").addElement("f"); // a->b->c->e (e.g. value of "CHIPS")
        setValueAndUpdateMappings(xmlPath3,
                // consumer function that takes value found at the xml path
                xmlValueFromXmlPath3 -> {
                    // cast builder object
                    Z.ZBuilder zBuilder = (Z.ZBuilder) builder;
                    // set new value on builder object
                    zBuilder.setStr3Field(xmlValueFromXmlPath3 + "_Z");

                });
    }
}