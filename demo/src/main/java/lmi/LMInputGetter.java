package lmi;

import exec.DatabaseSchemaExtractor;
import util.ReadTextFile;

public class LMInputGetter {
    private static String prompt = ReadTextFile.readFile();
    private static String schema = new DatabaseSchemaExtractor().extractSchema();

    public static String getLmInput(String question) {
        return prompt.replace("{question}", question).replace("{schema}", schema);
    }
}