package tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dao.QueryExecutor;
import lmi.ChatResponse;

public class ChatBotTerminal {
    public static void main(String[] args) {
        String question = "Quantos países existem no banco de dados?";
        String database = "world";
        double temperature = 0.0;
        long totalStartTime = System.currentTimeMillis();

        // Verifica se o arquivo Excel já existe
        String filePath = "ChatBotData.xlsx";
        File file = new File(filePath);
        Workbook workbook;
        if (file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                workbook = new XSSFWorkbook(fileInputStream);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        } else {
            workbook = new XSSFWorkbook();
        }

        // Se o arquivo Excel não tiver uma planilha "ChatBot Data", crie uma
        Sheet sheet = workbook.getSheet("ChatBot Data");
        if (sheet == null) {
            sheet = workbook.createSheet("ChatBot Data");
            // Criação da linha de cabeçalho se o arquivo Excel for recém-criado
            Row headerRow = sheet.createRow(0);
            String[] columns = { "Input", "Database", "Temperature", "Time (seconds)", "SQL Command", "Result" };
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }
        }

        try {
            FileOutputStream fileOut = new FileOutputStream(filePath);

            while (temperature < 1) {
                long startTime = System.currentTimeMillis();

                ChatResponse chatResponse = new ChatResponse(question, database, temperature);
                String lminput = chatResponse.getLmInput();

                QueryExecutor sqlQuery = new QueryExecutor(database);

                String lmResponse = chatResponse.getLmResponseFromQuestion();
                String result = sqlQuery.executeQuery(lmResponse);

                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;

                // Escrever os dados no arquivo Excel
                Row row = sheet.createRow(sheet.getLastRowNum() + 1);
                row.createCell(0).setCellValue(lminput);
                row.createCell(1).setCellValue(database);
                row.createCell(2).setCellValue(temperature);
                row.createCell(3).setCellValue(duration / 1000.0);
                row.createCell(4).setCellValue(lmResponse);
                row.createCell(5).setCellValue(result);

                System.out.println("\n* Temperature: " + temperature);
                System.out.println("* Time (seconds): " + (duration / 1000) + "\n* SQL command: \"" + lmResponse
                        + "\"\n* Result: " + result);

                temperature += 0.01;
            }

            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

            long totalEndTime = System.currentTimeMillis();
            long totalDuration = totalEndTime - totalStartTime;
            System.out.println("\n** Total time (minutes): " + (totalDuration / 60000));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}