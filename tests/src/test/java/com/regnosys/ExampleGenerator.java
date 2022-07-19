package com.regnosys;

import com.google.common.base.CaseFormat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExampleGenerator {

    public static final String EXPECTATION_JSON_TEMPLATE = "expectation.json.template";
    public static final String INGESTIONS_JSON_TEMPLATE = "ingestion.json.template";
    public static final String UNIT_TEST_JAVA_TEMPLATE = "ingestion-test.java.template";

    public static final String $UNIT_TEST_NAME$ = "$UNIT_TEST_NAME$";
    public static final String $NAMESPACE$ = "$NAMESPACE$";
    public static final String $CATEGORY_NAME$ = "$CATEGORY_NAME$";
    public static final String $TEST_NAME$ = "$TEST_NAME$";
    public static final String $TEST_SAMPLE_NAME$ = "$TEST_SAMPLE_NAME$";
    public static final String $SYNONYM_NAME$ = "$SYNONYM_NAME$";
    public static final String $ROOT_TYPE$ = "$ROOT_TYPE$";

    private final Path mainResourcesPath;
    private final Path testJavaPath;

    private final String expectationsTemplate;
    private final String ingestionTemplate;
    private final String unitTestTemplate;

    public ExampleGenerator(Path mainPath, Path testPath) throws IOException {
        this.mainResourcesPath = mainPath.resolve("resources");
        this.testJavaPath = testPath.resolve("java");

        Path testResourcesPath = testPath.resolve("resources");
        expectationsTemplate = Files.readString(testResourcesPath.resolve("example-generation").resolve(EXPECTATION_JSON_TEMPLATE));
        ingestionTemplate = Files.readString(testResourcesPath.resolve("example-generation").resolve(INGESTIONS_JSON_TEMPLATE));
        unitTestTemplate = Files.readString(testResourcesPath.resolve("example-generation").resolve(UNIT_TEST_JAVA_TEMPLATE));
    }

    public List<ExampleSet> inputExampleSets(Path exampleGenerationInputsPath) throws IOException {
        List<ExampleSet> exampleSets = new ArrayList<>();

        List<Path> categories = Files.list(exampleGenerationInputsPath)
                .filter(Files::isDirectory)
                .collect(Collectors.toList());

        for (Path category : categories) {
            String categoryName = category.getFileName().toString();
            List<Path> tests = Files.list(category).filter(Files::isDirectory).collect(Collectors.toList());
            for (Path test : tests) {
                String testName = test.getFileName().toString();
                Path rosettaPath = path(".rosetta", "Only one rosetta file can be in ", test);
                Path schemaPath = path(".xsd", "Only one xsd file can be in ", test);
                List<Path> xmlPath = Files.list(test).filter(Files::isRegularFile)
                        .filter(x -> x.toString().endsWith(".xml"))
                        .collect(Collectors.toList());
                exampleSets.add(new ExampleSet(categoryName, testName, rosettaPath, schemaPath, xmlPath));
            }
        }
        return exampleSets;
    }

    private Path path(String suffix, String msg, Path test) throws IOException {
        return Files.list(test)
                .filter(Files::isRegularFile)
                .filter(y -> y.toString().endsWith(suffix))
                .findFirst().orElseThrow(() -> new IllegalStateException(msg + test.toAbsolutePath()));
    }

    void writeSchema(ExampleSet exampleSet) throws IOException {
        Path schemas = Files.createDirectories(mainResourcesPath.resolve("schemas").resolve(exampleSet.categoryName));
        Files.copy(exampleSet.xsdFile,
                schemas.resolve(exampleSet.xsdFile.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
    }

    void writeXmlSamples(ExampleSet exampleSet) throws IOException {
        Path exampleXmlPath = Files.createDirectories(mainResourcesPath
                .resolve("cdm-sample-files")
                .resolve(exampleSet.categoryName)
                .resolve(exampleSet.testName));

        for (Path xmlFile : exampleSet.xmlFiles) {
            Files.copy(xmlFile,
                    exampleXmlPath.resolve(xmlFile.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
        }

        Path expectationsPath = exampleXmlPath.resolve("expectations.json");

        String expectations = exampleSet.xmlFiles.stream()
                .map(xmlSample ->
                        expectationsTemplate
                                .replace($CATEGORY_NAME$, exampleSet.categoryName)
                                .replace($TEST_NAME$, exampleSet.testName)
                                .replace($TEST_SAMPLE_NAME$, xmlSample.getFileName().toString())
                )
                .collect(Collectors.joining(",\n", "[", "]"));

        Files.write(expectationsPath, expectations.getBytes());
    }

    void writeIngestionJson(ExampleSet exampleSet) throws IOException {
        Path ingestions = Files.createDirectories(mainResourcesPath.resolve("ingestions"));
        Path ingestionPath = ingestions
                .resolve(exampleSet.categoryName + "-" + exampleSet.testName + "-ingestions.json");

        String ingestionsJson = ingestionTemplate
                .replace($NAMESPACE$, exampleSet.getNamespace())
                .replace($CATEGORY_NAME$, exampleSet.categoryName)
                .replace($TEST_NAME$, exampleSet.testName)
                .replace($SYNONYM_NAME$, exampleSet.getSynonymName())
                .replace($ROOT_TYPE$, "Root");

        Files.write(ingestionPath, ingestionsJson.getBytes());
    }

    void writeRosetta(ExampleSet exampleSet) throws IOException {
        Files.copy(exampleSet.rosettaFile,
                mainResourcesPath.getParent().resolve("rosetta")
                        .resolve(exampleSet.categoryName + "-" + exampleSet.rosettaFile.getFileName()
                                .toString()), StandardCopyOption.REPLACE_EXISTING);
    }

    void writeUnitTest(ExampleSet exampleSet) throws IOException {
        Path unitTestBasePath = Files.createDirectories(testJavaPath.resolve("demo").resolve("translate"));
        Path unitTestPath = unitTestBasePath.resolve(exampleSet.getUnitTestName() + ".java");

        String unitTestJava = unitTestTemplate
                .replace($UNIT_TEST_NAME$, exampleSet.getUnitTestName())
                .replace($NAMESPACE$, exampleSet.getNamespace())
                .replace($CATEGORY_NAME$, exampleSet.categoryName)
                .replace($TEST_NAME$, exampleSet.testName)
                .replace($SYNONYM_NAME$, exampleSet.getSynonymName())
                .replace($ROOT_TYPE$, "Root");

        Files.write(unitTestPath, unitTestJava.getBytes());
    }

    public static void main(String[] args) throws IOException {
        Path mainPath = Path.of("../rosetta-source/src/main");
        Path testPath = Path.of("src/test");

        ExampleGenerator exampleGenerator = new ExampleGenerator(mainPath, testPath);

        Path exampleGenerationInputsPath = testPath
                .resolve("resources")
                .resolve("example-generation/inputs");

        List<ExampleSet> inputSets = exampleGenerator.inputExampleSets(exampleGenerationInputsPath);
        for (ExampleSet inputSet : inputSets) {
            exampleGenerator.writeSchema(inputSet);
            exampleGenerator.writeIngestionJson(inputSet);
            exampleGenerator.writeRosetta(inputSet);
            exampleGenerator.writeXmlSamples(inputSet);
            exampleGenerator.writeUnitTest(inputSet);
        }
    }

    static class ExampleSet {
        private final String categoryName;
        private final String testName;
        private final Path rosettaFile;
        private final Path xsdFile;
        private final List<Path> xmlFiles;

        public ExampleSet(String categoryName, String testName, Path rosettaFile, Path xsdFile, List<Path> xmlFiles) {
            this.categoryName = categoryName;
            this.testName = testName;
            this.rosettaFile = rosettaFile;
            this.xsdFile = xsdFile;
            this.xmlFiles = xmlFiles;
        }

        public String getSynonymName() {
            return (categoryName + "_" + testName).toUpperCase().replace("-", "_");
        }

        public String getNamespace() {
            return ("demo.translate." + categoryName + "." + testName).toLowerCase().replace("-", "_");
        }

        public String getUnitTestName() {
            return (CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, categoryName)
                    + CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, testName)
                    + "IngestionTest");
        }
    }
}
