package lmi;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import exec.DatabaseSchemaExtractor;
import util.ReadTextFile;

public class LanguageModelInterface {
    static String prompt = ReadTextFile.readFile();
    static String schema = new DatabaseSchemaExtractor().extractSchema();
    static String lmInput;

    public static String getLMResponse(String question, String lModel) {
        lmInput = getLmInput(question);
        ChatLanguageModel model;

        if (lModel.equals("openai")) {
            model = OpenAiChatModel.withApiKey("demo");
        } else if (lModel.equals("sqlcoder")) {
            model = OllamaChatModel.builder()
                    .baseUrl("http://localhost:11434")
                    .modelName(lModel) // Usando subModelName como modelo dentro do Ollama
                    .build();
        } else {
            throw new IllegalArgumentException("Invalid LM type: " + lModel);
        }

        return model.generate(lmInput);
    }

    public static String getLmInput(String question) {
        return prompt.replace("{question}", question).replace("{schema}", schema);
    }
}
