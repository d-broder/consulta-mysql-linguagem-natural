package lmi;

import util.DatabaseSchemaExtractor;
import util.ReadTextFile;

public class ChatResponse {
    static String database = "teste-api-2";
    static String prompt = ReadTextFile.readFile();
    static String schema = new DatabaseSchemaExtractor().extractSchema(database);
    static String lmInput;
    static String lmOutput;

    public static String getLmResponseFromQuestion(String question) {
        lmInput = getLmInput(question);
        lmOutput = LanguageModelInterface.getLMResponse(lmInput);
        return lmOutput;
    }

    public static String getLmInput(String question) {
        lmInput = prompt.replace("{question}", question).replace("{schema}", schema);
        return lmInput;
    }
}