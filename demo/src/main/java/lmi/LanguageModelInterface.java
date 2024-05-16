package lmi;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.localai.LocalAiChatModel;

public class LanguageModelInterface {
    public static String getLMResponse(String input, double temperature) {
        ChatLanguageModel model;

        model = LocalAiChatModel.builder()
                .baseUrl("http://localhost:1234/v1/")
                .modelName("anyModelName")
                .temperature(temperature)
                .build();
        return model.generate(input);
    }
}