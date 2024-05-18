package lmi;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> model.generate(input));

        try {
            return future.get(120, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            return "[ERRO]";
        } catch (InterruptedException | ExecutionException e) {
            // Log exception (if needed) and return error message
            e.printStackTrace();
            return "[ERRO]";
        }
    }
}