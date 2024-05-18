package lmi;

import util.GetDatabaseSchema;
import util.ReadTextFile;

public class ChatResponse {
    private String question;
    private String database;
    private double temperature;
    private int inputFileNumber;

    public ChatResponse(String question, String database, double temperature, int inputFileNumber) {
        this.question = question;
        this.database = database;
        this.temperature = temperature;
        this.inputFileNumber = inputFileNumber;
    }

    public String getLmResponseFromQuestion() {
        String lmInput = getLmInput();
        String lmOutput = LanguageModelInterface.getLMResponse(lmInput, temperature);
        return lmOutput;
    }

    public String getLmInput() {
        String prompt = ReadTextFile.readInputFile(inputFileNumber);
        String schema = new GetDatabaseSchema().getDatabaseSchema(database);
        String lmInput = prompt.replace("{question}", question).replace("{schema}", schema);
        return lmInput;
    }
}
