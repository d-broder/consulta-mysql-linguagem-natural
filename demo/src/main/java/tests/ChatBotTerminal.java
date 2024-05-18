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
    public static void main(String[] args) {
        List<String> questions = Arrays.asList(
                "Qual é a população e o continente do país Estados Unidos?",
                "Qual é a população dos Estados Unidos?",
                "Quantos países têm a população maior que 100 milhões?");

        String database = "world";
        long totalStartTime = System.currentTimeMillis();

        // Verifica se o arquivo Excel já existe
        String filePath = "ChatBotData.xlsx";
        File file = new File(filePath);

        try (Workbook workbook = (file.exists() ? new XSSFWorkbook(new FileInputStream(file)) : new XSSFWorkbook());
                FileOutputStream fileOut = new FileOutputStream(filePath)) {

            // Se o arquivo Excel não tiver uma planilha "ChatBot Data", crie uma
            Sheet sheet = workbook.getSheet("ChatBot Data");
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

            for (int inputFileNumber = 1; inputFileNumber <= 4; inputFileNumber++) {
                for (String question : questions) {
                    double temperature = 0.0;
                    while (temperature < 1) {
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

                        System.out.println("* Input File Number: " + inputFileNumber + ", Question Index: "
                                + questions.indexOf(question)
                                + ", Temperature: "
                                + temperature + ", Time (minutes): " + (totalDuration / 60000) + ", Result: " + result);

                        temperature += 0.05;
                    }
                }
            }
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}