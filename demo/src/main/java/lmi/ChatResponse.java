package lmi;

import util.GetDatabaseSchema;
import util.ReadTextFile;

public class ChatResponse {
    static String prompt = ReadTextFile.readFile();

    private String question;
    private String database;
    private double temperature;

    public ChatResponse(String question, String database, double temperature) {
        this.question = question;
        this.database = database;
        this.temperature = temperature;
    }

    public String getLmResponseFromQuestion() {
        String lmInput = getLmInput();
        String lmOutput = LanguageModelInterface.getLMResponse(lmInput, temperature);
        return lmOutput;
    }

    public String getLmInput() {
        String schema = new GetDatabaseSchema().getDatabaseSchema(database);
        String lmInput = prompt.replace("{question}", question).replace("{schema}", schema);
        return lmInput;
    }
}
