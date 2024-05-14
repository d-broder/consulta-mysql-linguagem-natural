package lmi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.localai.LocalAiChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import util.CMDCommandExecutor;

public class LanguageModelInterface {
    private static List<String> OllamaAvailableModels;
    private List<String> LMStudioAvailableModels;
    private List<String> GUIavailableModels;

    public LanguageModelInterface() {
        List<String> originalOllamaList = CMDCommandExecutor.getOllamaLmsNames();
        List<String> originalLMStudioList = CMDCommandExecutor.getLMStudioLmsNames();
        Pattern pattern = Pattern.compile("([^/]+)\\-[^/]+\\.([^/]+)\\.gguf");

        OllamaAvailableModels = new ArrayList<>(originalOllamaList);
        LMStudioAvailableModels = new ArrayList<>(originalLMStudioList);

        GUIavailableModels = new ArrayList<>();

        for (int i = 0; i < originalOllamaList.size(); i++) {
            String element = originalOllamaList.get(i);
            GUIavailableModels.add("Ollama/" + element);
        }

        for (int i = 0; i < originalLMStudioList.size(); i++) {
            String element = originalLMStudioList.get(i);
            Matcher matcher = pattern.matcher(element);
            if (matcher.find()) {
                String nome = matcher.group(1);
                String tipo = matcher.group(2);
                GUIavailableModels.add("LM Studio/" + nome + "." + tipo);
            }
        }
    }

    // Método para retornar a lista de modelos
    public List<String> getOllamaAvailableModels() {
        return OllamaAvailableModels;
    }

    // Método para retornar a quantidade de modelos
    public int getNumOllamaModels() {
        return OllamaAvailableModels.size();
    }

    // Método para retornar a lista de modelos modificada
    public List<String> getGUIavailableModels() {
        return GUIavailableModels;
    }

    public static String getLMResponse(int lModelIndex, String input) {
        ChatLanguageModel model;
        LanguageModelInterface lmi = new LanguageModelInterface();

        if (lModelIndex >= OllamaAvailableModels.size()) {
            int lmsIndex = lModelIndex - OllamaAvailableModels.size();
            String lmModel = lmi.LMStudioAvailableModels.get(lmsIndex);

            try {
                CMDCommandExecutor.executeCommand("lms unload --all");
                CMDCommandExecutor.executeCommand("lms load " + lmModel + " --gpu max -y");
            } catch (IOException e) {
                // Handle IOException
            } catch (InterruptedException e) {
                // Handle InterruptedException
            }

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
        List<String> availableModelsGUI = lmi.getGUIavailableModels();

        for (String modelo : availableModelsGUI) {
            System.out.println("* " + modelo);
        }

    }
}