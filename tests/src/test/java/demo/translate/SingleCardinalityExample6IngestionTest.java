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

//@org.junit.jupiter.api.Disabled
public class SingleCardinalityExample6IngestionTest extends IngestionTest<demo.translate.single_cardinality.example_6.Root> {

    private static final String SAMPLE_FILES_DIR = "cdm-sample-files/single-cardinality/example-6";
    private static final String INSTANCE_NAME = "target/SINGLE_CARDINALITY_EXAMPLE_6";

    private static IngestionService ingestionService;

    @BeforeAll
    static void setup() {
        writeActualExpectations = ExpectationUtil.WRITE_EXPECTATIONS;

        ClassLoader cl = SingleCardinalityExample6IngestionTest.class.getClassLoader();
        Collection<URL> ingestURLs = List.of(
                Objects.requireNonNull(cl.getResource("ingestions/single-cardinality-example-6-ingestions.json")));
        ModelRuntimeModule runtimeModule = new ModelRuntimeModule();
        initialiseIngestionFactory(INSTANCE_NAME, ingestURLs, runtimeModule, new ArrayList<>(IngestionTestUtil.getPostProcessors(runtimeModule)));
        IngestionFactory factory = IngestionFactory.getInstance(INSTANCE_NAME);
        ingestionService = factory.getService("SINGLE_CARDINALITY_EXAMPLE_6");
    }

    @Override
    protected Class<demo.translate.single_cardinality.example_6.Root> getClazz() {
        return demo.translate.single_cardinality.example_6.Root.class;
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
