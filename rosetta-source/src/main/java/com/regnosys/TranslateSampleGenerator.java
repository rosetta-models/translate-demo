package com.regnosys;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TranslateSampleGenerator {

    public static final String EXPECTATION_JSON_TEMPLATE = "expectation.json.template";
    public static final String INGESTIONS_JSON_TEMPLATE = "ingestion.json.template";

    public static final String $CATEGORY_NAME$ = "$CATEGORY_NAME$";
    public static final String $TEST_NAME$ = "$TEST_NAME$";
    public static final String $TEST_SAMPLE_NAME$ = "$TEST_SAMPLE_NAME$";
    public static final String $SYNONYM_NAME$ = "$SYNONYM_NAME$";
    public static final String $ROOT_TYPE$ = "$ROOT_TYPE$";


    private final Path base;
    private final String expectationsTemplate;
    private final String ingestionTemplate;

    public TranslateSampleGenerator(Path base) throws IOException {
        this.base = base;
        expectationsTemplate = Files.readString(base.resolve("test-generation").resolve(EXPECTATION_JSON_TEMPLATE));
        ingestionTemplate = Files.readString(base.resolve("test-generation").resolve(INGESTIONS_JSON_TEMPLATE));

    }

    public List<SampleSet> sampleSets(Path testGenerationBase) throws IOException {

        List<SampleSet> sampleSets = new ArrayList<>();

        List<Path> categories = Files.list(testGenerationBase).filter(Files::isDirectory).collect(Collectors.toList());

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
                sampleSets.add(new SampleSet(categoryName, testName, rosettaPath, schemaPath, xmlPath));
            }
        }
        return sampleSets;
    }

    private Path path(String suffix, String msg, Path test) throws IOException {
        Path rosettaPath = Files.list(test)
                .filter(Files::isRegularFile)
                .filter(y -> y.toString().endsWith(suffix))
                .findFirst().orElseThrow(() -> new IllegalStateException(msg + test.toAbsolutePath()));
        return rosettaPath;
    }

    void writeSchema(SampleSet sampleSet) throws IOException {
        Path schemas = Files.createDirectories(base.resolve("schemas").resolve(sampleSet.categoryName)
                .resolve(sampleSet.testName));
        Files.copy(sampleSet.xdsFile,
                schemas.resolve(sampleSet.xdsFile.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
    }

    void writeXMLSamples(SampleSet sampleSet) throws IOException {
        Path testDir = Files.createDirectories(base.resolve("cdm-sample-files").resolve(sampleSet.categoryName)
                .resolve(sampleSet.testName));
        for (Path xmlFile : sampleSet.xmlFiles) {
            Files.copy(xmlFile,
                    testDir.resolve(xmlFile.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
        }

        Path expectationsPath = testDir.resolve("expectations.json");

        String expectations = sampleSet.xmlFiles.stream()
                .map(xmlSample ->
                        expectationsTemplate
                                .replace($CATEGORY_NAME$, sampleSet.categoryName)
                                .replace($TEST_NAME$, sampleSet.testName)
                                .replace($TEST_SAMPLE_NAME$, xmlSample.getFileName().toString())
                )
                .collect(Collectors.joining(",", "[", "]"));

        Files.write(expectationsPath, expectations.getBytes());
    }

    void writeIngestionJson(SampleSet sampleSet) throws IOException {

        Path ingestions = Files.createDirectories(base.resolve("ingestions").resolve(sampleSet.categoryName));
        Path ingestionPath = ingestions
                .resolve(sampleSet.categoryName + sampleSet.testName + "-ingestions.json");

        String ingestionsJson = ingestionTemplate
                .replace($CATEGORY_NAME$, sampleSet.categoryName)
                .replace($TEST_NAME$, sampleSet.testName)
                .replace($SYNONYM_NAME$, sampleSet.getSynonymName())
                .replace($ROOT_TYPE$, "Root");

        Files.write(ingestionPath, ingestionsJson.getBytes());
    }

    void writeRosetta(SampleSet sampleSet) throws IOException {
        Files.copy(sampleSet.rosettaFile,
                base.getParent().resolve("rosetta")
                        .resolve(sampleSet.categoryName + "-" + sampleSet.rosettaFile.getFileName()
                                .toString()), StandardCopyOption.REPLACE_EXISTING);
    }

    void writeUnitTest(SampleSet sampleSet) {

    }

    public static void main(String[] args) throws IOException {

        Path base = Path.of("src/main/resources");
        TranslateSampleGenerator translateSampleGenerator = new TranslateSampleGenerator(base);

        Path testGenerationBase = base.resolve("test-generation/inputs");

        List<SampleSet> sampleSets = translateSampleGenerator.sampleSets(testGenerationBase);
        for (SampleSet sampleSet : sampleSets) {
            translateSampleGenerator.writeSchema(sampleSet);
            translateSampleGenerator.writeIngestionJson(sampleSet);
            translateSampleGenerator.writeRosetta(sampleSet);
            translateSampleGenerator.writeXMLSamples(sampleSet);
            translateSampleGenerator.writeIngestionJson(sampleSet);
        }
    }

    static class SampleSet {
        private final String categoryName;
        private final String testName;
        private final Path rosettaFile;
        private final Path xdsFile;
        private final List<Path> xmlFiles;

        public SampleSet(String categoryName, String testName, Path rosettaFile, Path xdsFile, List<Path> xmlFiles) {
            this.categoryName = categoryName;
            this.testName = testName;
            this.rosettaFile = rosettaFile;
            this.xdsFile = xdsFile;
            this.xmlFiles = xmlFiles;
        }

        public String getSynonymName() {
            return (categoryName + "_" + testName).toUpperCase();
        }
    }
}
