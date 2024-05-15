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
    private static List<String> LMStudioAvailableModels;
    private static List<String> GUIavailableModels;

    static {
        initializeLists();
    }

    private static void initializeLists() {
        List<String> originalOllamaList = CMDCommandExecutor.getOllamaLmsNames();
        List<String> originalLMStudioList = CMDCommandExecutor.getLMStudioLmsNames();
        Pattern pattern = Pattern.compile("([^/]+)\\-[^/]+\\.([^/]+)\\.gguf");

        OllamaAvailableModels = new ArrayList<>(originalOllamaList);
        LMStudioAvailableModels = new ArrayList<>(originalLMStudioList);
        GUIavailableModels = new ArrayList<>();

        for (String element : originalOllamaList) {
            GUIavailableModels.add("Ollama/" + element);
        }

        for (String element : originalLMStudioList) {
            Matcher matcher = pattern.matcher(element);
            if (matcher.find()) {
                String nome = matcher.group(1);
                String tipo = matcher.group(2);
                GUIavailableModels.add("LM Studio/" + nome + "." + tipo);
            }
        }
    }

    public static List<String> getOllamaAvailableModels() {
        return OllamaAvailableModels;
    }

    public static int getNumOllamaModels() {
        return OllamaAvailableModels.size();
    }

    public static List<String> getGUIavailableModels() {
        return GUIavailableModels;
    }

    public static String getLMResponse(int lModelIndex, String input) {
        ChatLanguageModel model;

        if (lModelIndex >= OllamaAvailableModels.size()) {
            int lmsIndex = lModelIndex - OllamaAvailableModels.size();
            String lmModel = LMStudioAvailableModels.get(lmsIndex);

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
                    .temperature(0.1)
                    .build();
        } else {
            model = OllamaChatModel.builder()
                    .baseUrl("http://localhost:11434")
                    .modelName(OllamaAvailableModels.get(lModelIndex))
                    .build();
        }
        return model.generate(input);
    }

    public static void main(String[] args) {
        for (String modelo : GUIavailableModels) {
            System.out.println("* " + modelo);
        }
    }
}