package lmi;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.localai.LocalAiChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class LanguageModelInterface {
    static String lModel = "lmstudio/nsql";

    public static String getLModel() {
        return lModel;
    }

    public static String getLMResponse(String input) {
        ChatLanguageModel model;

        switch (lModel) {
            case "openai":
                model = OpenAiChatModel.withApiKey("demo");
                break;
            case "ollama/sqlcoder":
                model = buildOllamaChatModel("sqlcoder");
                break;
            case "ollama/nsql":
                model = buildOllamaChatModel("nsql");
                break;
            case "lmstudio/sqlcoder":
                model = buildLocalAiChatModel("TheBloke/sqlcoder2-GGUF/sqlcoder2.Q2_K.gguf");
                break;
            case "lmstudio/nsql":
                model = buildLocalAiChatModel("motherduckdb/DuckDB-NSQL-7B-v0.1-GGUF/DuckDB-NSQL-7B-v0.1-q8_0.gguf");
                break;
            default:
                throw new IllegalArgumentException("Invalid LM type: " + lModel);
        }

        return model.generate(input);
    }

    // Ollama
    private static OllamaChatModel buildOllamaChatModel(String modelName) {
        return OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName(modelName)
                .build();
    }

    // LMStudio
    private static LocalAiChatModel buildLocalAiChatModel(String modelName) {
        return LocalAiChatModel.builder()
                .baseUrl("http://localhost:1234/v1/")
                .modelName("a")
                .temperature(0.6)
                .build();
    }
}
