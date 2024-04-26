package lmi;

import util.GetDatabaseSchema;
import util.ReadTextFile;

public class ChatResponse {
    static String prompt = ReadTextFile.readFile();

    String question;
    String database;
    int lModelIndex;

    public ChatResponse(String question, String database, int lModelIndex) {
        this.question = question;
        this.database = database;
        this.lModelIndex = lModelIndex;
    }

    public String getLmResponseFromQuestion() {
        String lmInput = getLmInput();
        String lmOutput = LanguageModelInterface.getLMResponse(lModelIndex, lmInput);
        return lmOutput;
    }

    public String getLmInput() {
        String schema = new GetDatabaseSchema().getDatabaseSchema(database);
        String lmInput = prompt.replace("{question}", question).replace("{schema}", schema);
        return lmInput;
    }
}
