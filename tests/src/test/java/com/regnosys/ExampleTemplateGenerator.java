package com.regnosys;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.regnosys.ExampleGenerator.*;

public class ExampleTemplateGenerator {

    private final String categoryName;
    private final String exampleName;

    private final Path inputExampleBasePath;
    private final Path templatesPath;

    public ExampleTemplateGenerator(Path basePath, String categoryName, String filenameSuffix) {
        this.categoryName = categoryName;
        this.exampleName = String.format("example-%s", filenameSuffix);
        this.inputExampleBasePath = basePath.resolve("inputs").resolve(categoryName).resolve(exampleName);
        this.templatesPath = basePath.resolve("templates");
    }

    public void writeMd() throws IOException {
        Files.createDirectories(inputExampleBasePath);
        Files.copy(templatesPath.resolve("example.md"),
                inputExampleBasePath.resolve(exampleName + ".md"));
    }

    public void writeRosetta() throws IOException {
        Files.createDirectories(inputExampleBasePath);

        String templateRosetta = Files.readString( templatesPath.resolve("example.rosetta.template"));
        String rosetta = templateRosetta
                .replace($NAMESPACE$, getNamespace(categoryName, exampleName))
                .replace($SYNONYM_NAME$, getSynonymName(categoryName, exampleName))
                .replace($ROOT_TYPE$, ROOT_TYPE);

        Path rosettaPath = inputExampleBasePath.resolve(exampleName + ".rosetta");
        Files.write(rosettaPath, rosetta.getBytes());
    }

    public void writeXml() throws IOException {
        Files.createDirectories(inputExampleBasePath);

        String templateXml = Files.readString( templatesPath.resolve("example.xml.template"));
        String xml = templateXml.replace($TEST_NAME$, exampleName);

        Path xmlPath = inputExampleBasePath.resolve(exampleName + "-1.xml");
        Files.write(xmlPath, xml.getBytes());
    }

    public void writeXsd() throws IOException {
        Files.createDirectories(inputExampleBasePath);
        Files.copy(templatesPath.resolve("example.xsd"),
                inputExampleBasePath.resolve(exampleName +  ".xsd"));
    }

    public static void main(String[] args) throws IOException {
        String categoryName = "single-cardinality";
        String filenameSuffix = "5";
        Path basePath = Path.of("src/test/resources/example-generation");

        ExampleTemplateGenerator generator = new ExampleTemplateGenerator(basePath, categoryName, filenameSuffix);
        generator.writeMd();
        generator.writeRosetta();
        generator.writeXml();
        generator.writeXsd();
    }
}
