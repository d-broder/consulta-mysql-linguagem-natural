package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CMDCommandExecutor {
    public static void executeCommand(String command) throws IOException, InterruptedException {
        @SuppressWarnings("deprecation")
        Process process = Runtime.getRuntime().exec(command);

        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((input.readLine()) != null) {
            }
        }

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
            // System.out.println("* " + line);
            resultList.add(line);
        }
        reader.close();

        return resultList;
    }
}
