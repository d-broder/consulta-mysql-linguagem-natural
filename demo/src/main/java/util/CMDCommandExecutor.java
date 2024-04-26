package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CMDCommandExecutor {
    public static void main(String[] args) {
        List<String> namesList = getOllamaLmsNames();

        for (String name : namesList) {
            System.out.println(name);
        }
    }

    public static List<String> executeCommand(String command) throws IOException, InterruptedException {
        List<String> resultList = new ArrayList<>();
        @SuppressWarnings("deprecation")
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            resultList.add(line);
        }
        reader.close();

        return resultList;
    }

    public static List<String> getOllamaLmsNames() {
        List<String> resultList;
        try {
            resultList = executeCommand("ollama list");
        } catch (IOException | InterruptedException e) {
            // Aqui você pode lidar com a exceção localmente
            System.err.println("Ocorreu um erro ao executar o comando: " + e.getMessage());
            resultList = new ArrayList<>(); // Retorna uma lista vazia em caso de erro
        }

        List<String> namesList = new ArrayList<>();
        boolean isFirstLine = true;
        for (String line : resultList) {
            if (isFirstLine) {
                isFirstLine = false;
                continue; // Ignora a primeira linha (cabeçalho)
            }
            String[] parts = line.split("\\s+");
            if (parts.length > 0) {
                String name = parts[0].replace(":latest", ""); // Remove ":latest" from the name
                namesList.add(name);
            }
        }
        return namesList;
    }

}
