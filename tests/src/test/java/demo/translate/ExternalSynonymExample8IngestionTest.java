package demo.translate;

import com.regnosys.granite.ingestor.ExpectationUtil;
import com.regnosys.granite.ingestor.IngestionTest;
import com.regnosys.granite.ingestor.IngestionTestUtil;
import com.regnosys.granite.ingestor.service.IngestionFactory;
import com.regnosys.granite.ingestor.service.IngestionService;
import com.regnosys.model.ModelRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.provider.Arguments;

import java.net.URL;
import java.util.*;
import java.util.stream.Stream;

@org.junit.jupiter.api.Disabled
public class ExternalSynonymExample8IngestionTest extends IngestionTest<demo.translate.external_synonym.example_8.Root> {

    private static final String SAMPLE_FILES_DIR = "cdm-sample-files/external-synonym/example-8";
    private static final String INSTANCE_NAME = "target/EXTERNAL_SYNONYM_EXAMPLE_8";

    private static IngestionService ingestionService;

    @BeforeAll
    static void setup() {
        writeActualExpectations = ExpectationUtil.WRITE_EXPECTATIONS;

        ClassLoader cl = ExternalSynonymExample8IngestionTest.class.getClassLoader();
        Collection<URL> ingestURLs = List.of(
                Objects.requireNonNull(cl.getResource("ingestions/external-synonym-example-8-ingestions.json")));
        ModelRuntimeModule runtimeModule = new ModelRuntimeModule();
        initialiseIngestionFactory(INSTANCE_NAME, ingestURLs, runtimeModule, new ArrayList<>(IngestionTestUtil.getPostProcessors(runtimeModule)));
        IngestionFactory factory = IngestionFactory.getInstance(INSTANCE_NAME);
        ingestionService = factory.getService("EXTERNAL_SYNONYM_EXAMPLE_8");
    }

    @Override
    protected Class<demo.translate.external_synonym.example_8.Root> getClazz() {
        return demo.translate.external_synonym.example_8.Root.class;
    }

    @Override
    protected IngestionService ingestionService() {
        return ingestionService;
    }

    @SuppressWarnings("unused")//used by the junit parameterized test
    private static Stream<Arguments> fpMLFiles() {
        return readExpectationsFromPath(SAMPLE_FILES_DIR);
    }

}
