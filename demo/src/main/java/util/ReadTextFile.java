package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadTextFile {
    public static String readInputFile(int inputFileNumber) {
        try {
            File filePath = new File("demo/src/main/res/input" + inputFileNumber + ".txt");

            Scanner scanner = new Scanner(filePath);
            StringBuilder content = new StringBuilder();

            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine());
                content.append("\n");
            }
            scanner.close();

            String contentAsString = content.toString();
            return contentAsString;

        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado: " + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(readInputFile(3));
    }
}
