package tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dao.QueryExecutor;
import lmi.ChatResponse;
import util.CMDCommandExecutor;

public class ChatBotTerminal {
    private static Workbook workbook;
    private static FileOutputStream fileOut;
    private static Sheet sheet;
    private static long totalStartTime;

    public static void main(String[] args) {
        List<String> questions = Arrays.asList(
                "Qual é a cidade com a maior população",
                "Qual é o país com a maior população",
                "Quantos países tem a população maior ou igual a população do Brasil?",
                "Quantos países existem na Europa?",
                "Which city has the largest population",
                "Which country has the largest population",
                "How many countries have a population greater than or equal to the population of Brazil?",
                "How many countries are there in Europe?");

        String database = "world";
        int numberInputFiles = 3;

        // Definição das condições iniciais
        int startInputFileNumber = 2;
        int startQuestionNumber = 7;
        double startTemperature = 0.80;

        totalStartTime = System.currentTimeMillis();

        // Verifica se o arquivo Excel já existe
        String filePath = "ChatBotData.xlsx";
        File file = new File(filePath);

        try {
            workbook = (file.exists() ? new XSSFWorkbook(new FileInputStream(file)) : new XSSFWorkbook());
            fileOut = new FileOutputStream(filePath);

            // Se o arquivo Excel não tiver uma planilha "ChatBot Data", crie uma
            sheet = workbook.getSheet("ChatBot Data");
            if (sheet == null) {
                sheet = workbook.createSheet("ChatBot Data");
                // Criação da linha de cabeçalho se o arquivo Excel for recém-criado
                Row headerRow = sheet.createRow(0);
                String[] columns = { "Input", "Input File Number", "Question", "Temperature", "Time (seconds)",
                        "SQL Command", "Result" };
                for (int i = 0; i < columns.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columns[i]);
                }
            }

            // Registra o shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    if (workbook != null && fileOut != null) {
                        workbook.write(fileOut);
                        fileOut.close();
                        System.out.println("Workbook saved successfully.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));

            // Carregar o modelo de linguagem
            try {
                CMDCommandExecutor.executeCommand("lms unload --all");
                CMDCommandExecutor.executeCommand(
                        "lms load MaziyarPanahi/sqlcoder-7b-Mistral-7B-Instruct-v0.2-slerp-GGUF --gpu max -y");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            boolean startConditionMet = false;

            for (int inputFileNumber = 1; inputFileNumber <= numberInputFiles; inputFileNumber++) {
                for (int questionNumber = 1; questionNumber <= questions.size(); questionNumber++) {
                    String question = questions.get(questionNumber - 1); // Ajusta para índice zero

                    double initialTemperature = 0.0;
                    if (inputFileNumber == startInputFileNumber && questionNumber == startQuestionNumber) {
                        initialTemperature = startTemperature; // Define a temperatura inicial a partir da condição
                                                               // inicial
                        startConditionMet = true;
                    } else if (!startConditionMet) {
                        continue; // Pula as iterações até encontrar a condição inicial
                    }

                    for (double temperature = initialTemperature; temperature < 1; temperature += 0.05) {
                        long startTime = System.currentTimeMillis();

                        ChatResponse chatResponse = new ChatResponse(question, database, temperature, inputFileNumber);
                        String lminput = chatResponse.getLmInput();

                        QueryExecutor sqlQuery = new QueryExecutor(database);

                        String lmResponse = chatResponse.getLmResponseFromQuestion();
                        String result = sqlQuery.executeQuery(lmResponse);

                        long endTime = System.currentTimeMillis();
                        long duration = endTime - startTime;

                        long totalEndTime = System.currentTimeMillis();
                        long totalDuration = totalEndTime - totalStartTime;

                        // Escrever os dados no arquivo Excel
                        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
                        row.createCell(0).setCellValue(lminput);
                        row.createCell(1).setCellValue(inputFileNumber);
                        row.createCell(2).setCellValue(question);
                        row.createCell(3).setCellValue(temperature);
                        row.createCell(4).setCellValue(duration / 1000.0);
                        row.createCell(5).setCellValue(lmResponse);
                        row.createCell(6).setCellValue(result);

                        System.out.println("* Input File: " + inputFileNumber + "/" + numberInputFiles
                                + ", Question Number: " + questionNumber + "/" + questions.size()
                                + ", Temperature: " + String.format("%.2f", temperature)
                                + ", Time (minutes): " + (totalDuration / 60000)
                                + ", Result: " + result);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
