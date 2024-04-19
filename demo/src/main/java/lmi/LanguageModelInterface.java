package lmi;

import java.util.ArrayList;
import java.util.List;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.localai.LocalAiChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class LanguageModelInterface {
    // Lista de modelos disponíveis
    private static final List<String> availableModels = new ArrayList<>();

    static {
        availableModels.add("openai");
        availableModels.add("ollama/sqlcoder");
        availableModels.add("ollama/nsql");
        availableModels.add("lmstudio");
    }

    // Método para retornar a lista de modelos
    public List<String> getAvailableModels() {
        return new ArrayList<>(availableModels);
    }

    public static String getLMResponse(String lModel, String input) {
        ChatLanguageModel model;

        // Verifica se o modelo especificado é válido
        if (!availableModels.contains(lModel)) {
            throw new IllegalArgumentException("Invalid LM type: " + lModel);
        }

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
            case "lmstudio":
                model = buildLocalAiChatModel();
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
    private static LocalAiChatModel buildLocalAiChatModel() {
        return LocalAiChatModel.builder()
                .baseUrl("http://localhost:1234/v1/")
                .modelName("anyModelName")
                .temperature(0.6)
                .build();
    }
}
