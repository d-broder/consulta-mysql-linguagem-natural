package lmi;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import util.ReadTextFile;

public class LMInterface {
    static String modelName = "sqlcoder";
    static String prompt = ReadTextFile.readFile();

    public static String getLMResponse(String question) {
        String lmInput = prompt.replace("{question}", question);

        ChatLanguageModel model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName(modelName)
                .build();

        return model.generate(lmInput);
    }
}
