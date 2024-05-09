package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class CMDCommandExecutor {
    public static void main(String[] args) {
        List<String> OllamaLmsNamesList = getOllamaLmsNames();
        List<String> LMStudioLmsNamesList = getLMStudioLmsNames();

        for (String name : OllamaLmsNamesList) {
            System.out.println(name);
        }

        for (String name : LMStudioLmsNamesList) {
            System.out.println(name);
        }
    }

    public static void executeCommand(String command) throws IOException, InterruptedException {
        @SuppressWarnings("deprecation")
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
    }

    public static List<String> getCommandResult(String command) throws IOException, InterruptedException {
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
            resultList = getCommandResult("ollama list");
        } catch (IOException | InterruptedException e) {
            // Aqui você pode lidar com a exceção localmente
            System.err.println("Ocorreu um erro ao executar o comando: " + e.getMessage());
            resultList = new ArrayList<>(); // Retorna uma lista vazia em caso de erro
        }

        List<String> namesList = new ArrayList<>();
        int lineCount = 0;
        for (String line : resultList) {
            lineCount++;
            String[] parts = line.split("\\s+");
            if (lineCount > 1) {
                String name = parts[0].replace(":latest", ""); // Remove ":latest" from the name
                namesList.add(name);
            }
        }
        return namesList;
    }

    public static List<String> getLMStudioLmsNames() {
        List<String> resultList;
        List<String> paths = new ArrayList<>();

        try {
            resultList = getCommandResult("lms ls --json");
        } catch (IOException | InterruptedException e) {
            // Aqui você pode lidar com a exceção localmente
            System.err.println("Ocorreu um erro ao executar o comando: " + e.getMessage());
            resultList = new ArrayList<>(); // Retorna uma lista vazia em caso de erro
        }

        for (String result : resultList) {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                paths.add(jsonObject.getString("path"));
            }
        }

        return paths;
    }
}
