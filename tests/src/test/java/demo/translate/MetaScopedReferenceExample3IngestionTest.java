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

public class MetaScopedReferenceExample3IngestionTest extends IngestionTest<demo.translate.meta_scoped_reference.example_3.Root> {

    private static final String SAMPLE_FILES_DIR = "cdm-sample-files/meta-scoped-reference/example-3";
    private static final String INSTANCE_NAME = "target/META_SCOPED_REFERENCE_EXAMPLE_3";

    private static IngestionService ingestionService;

    @BeforeAll
    static void setup() {
        writeActualExpectations = ExpectationUtil.WRITE_EXPECTATIONS;

        ClassLoader cl = MetaScopedReferenceExample3IngestionTest.class.getClassLoader();
        Collection<URL> ingestURLs = List.of(
                Objects.requireNonNull(cl.getResource("ingestions/meta-scoped-reference-example-3-ingestions.json")));
        ModelRuntimeModule runtimeModule = new ModelRuntimeModule();
        initialiseIngestionFactory(INSTANCE_NAME, ingestURLs, runtimeModule, new ArrayList<>(IngestionTestUtil.getPostProcessors(runtimeModule)));
        IngestionFactory factory = IngestionFactory.getInstance(INSTANCE_NAME);
        ingestionService = factory.getService("META_SCOPED_REFERENCE_EXAMPLE_3");
    }

    @Override
    protected Class<demo.translate.meta_scoped_reference.example_3.Root> getClazz() {
        return demo.translate.meta_scoped_reference.example_3.Root.class;
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