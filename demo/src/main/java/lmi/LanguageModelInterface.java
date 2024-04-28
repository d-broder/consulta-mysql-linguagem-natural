package lmi;

import java.util.ArrayList;
import java.util.List;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.localai.LocalAiChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import util.CMDCommandExecutor;

public class LanguageModelInterface {
    private List<String> OllamaAvailableModels;
    private List<String> GUIavailableModels;
    private static int numModels;

    public LanguageModelInterface() {
        List<String> originalList = CMDCommandExecutor.getOllamaLmsNames();

        OllamaAvailableModels = new ArrayList<>(originalList);
        GUIavailableModels = new ArrayList<>();

        for (int i = 0; i < originalList.size(); i++) {
            String element = originalList.get(i);
            GUIavailableModels.add("Ollama/" + element);
        }
        GUIavailableModels.add("LM Studio");

        numModels = GUIavailableModels.size();
    }

    // Método para retornar a lista de modelos
    public List<String> getOllamaAvailableModels() {
        return OllamaAvailableModels;
    }

    // Método para retornar a lista de modelos modificada
    public List<String> getGUIavailableModels() {
        return GUIavailableModels;
    }

    public static String getLMResponse(int lModelIndex, String input) {
        ChatLanguageModel model;
        LanguageModelInterface lmi = new LanguageModelInterface();

        if (lModelIndex == numModels - 1) {
            model = LocalAiChatModel.builder()
                    .baseUrl("http://localhost:1234/v1/")
                    .modelName("anyModelName")
                    .temperature(0.6)
                    .build();
        } else {
            model = OllamaChatModel.builder()
                    .baseUrl("http://localhost:11434")
                    .modelName(lmi.getOllamaAvailableModels().get(lModelIndex))
                    .build();
        }

        return model.generate(input);
    }

    public static void main(String[] args) {
        LanguageModelInterface lmi = new LanguageModelInterface();
        List<String> availableModels = lmi.getOllamaAvailableModels();
        List<String> availableModelsGUI = lmi.getGUIavailableModels();
        System.out.println(availableModels);
        System.out.println(availableModelsGUI);
    }
}