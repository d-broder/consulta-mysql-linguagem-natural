package lmi;

import util.GetDatabaseSchema;
import util.ReadTextFile;

public class ChatResponse {
    static String prompt = ReadTextFile.readFile();

    String question;
    String database;
    String lModel;

    public ChatResponse(String question, String database, String lModel) {
        this.question = question;
        this.database = database;
        this.lModel = lModel;
    }

    public String getLmResponseFromQuestion() {
        String lmInput = getLmInput();
        String lmOutput = LanguageModelInterface.getLMResponse(lModel, lmInput);
        return lmOutput;
    }

    public String getLmInput() {
        String schema = new GetDatabaseSchema().getDatabaseSchema(database);
        String lmInput = prompt.replace("{question}", question).replace("{schema}", schema);
        return lmInput;
    }
}
