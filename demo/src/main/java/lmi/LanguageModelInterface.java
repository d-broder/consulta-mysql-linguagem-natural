package lmi;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class LanguageModelInterface {
    static String lModel = "sqlcoder";

    public static String getLModel() {
        return lModel;
    }

    public static String getLMResponse(String input) {
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

        return model.generate(input);
    }
}
