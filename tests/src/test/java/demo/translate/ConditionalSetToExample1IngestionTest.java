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

public class ConditionalSetToExample1IngestionTest extends IngestionTest<demo.translate.conditional_set_to.example_1.Root> {

    private static final String SAMPLE_FILES_DIR = "cdm-sample-files/conditional-set-to/example-1";
    private static final String INSTANCE_NAME = "target/CONDITIONAL_SET_TO_EXAMPLE_1";

    private static IngestionService ingestionService;

    @BeforeAll
    static void setup() {
        writeActualExpectations = ExpectationUtil.WRITE_EXPECTATIONS;

        ClassLoader cl = ConditionalSetToExample1IngestionTest.class.getClassLoader();
        Collection<URL> ingestURLs = List.of(
                Objects.requireNonNull(cl.getResource("ingestions/conditional-set-to-example-1-ingestions.json")));
        ModelRuntimeModule runtimeModule = new ModelRuntimeModule();
        initialiseIngestionFactory(INSTANCE_NAME, ingestURLs, runtimeModule, new ArrayList<>(IngestionTestUtil.getPostProcessors(runtimeModule)));
        IngestionFactory factory = IngestionFactory.getInstance(INSTANCE_NAME);
        ingestionService = factory.getService("CONDITIONAL_SET_TO_EXAMPLE_1");
    }

    @Override
    protected Class<demo.translate.conditional_set_to.example_1.Root> getClazz() {
        return demo.translate.conditional_set_to.example_1.Root.class;
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
