package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import dao.QueryExecutor;
import lmi.ChatResponse;
import lmi.LanguageModelInterface;
import util.GetDatabaseSchema;
import util.GetDatabasesNames;

public class ChatBotGUI extends JFrame {
    private JComboBox<String> lmDropdown;
    private JComboBox<String> dbDropdown;
    private JTextField questionField;
    private JButton submitButton;
    private JTextArea questionArea;
    private Timer timer;
    private String fullText = "";
    private int processingCounter = 0;

    public ChatBotGUI() {
        setTitle("Database Selection & Chatbot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); // Centralize window on screen

        initComponents();

        // Inicializa o temporizador para alternar entre os diferentes "Processing"
        timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProcessingText();
            }
        });
    }

    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Dropdown panel
        JPanel dropdownPanel = new JPanel();
        dropdownPanel.setLayout(new FlowLayout());

        JLabel lmLabel = new JLabel("Language model:");
        lmDropdown = new JComboBox<>();
        LanguageModelInterface lmi = new LanguageModelInterface();
        List<String> lmOptions = lmi.getGUIavailableModels();
        for (String option : lmOptions) {
            lmDropdown.addItem(option);
        }

        JLabel dbLabel = new JLabel("Database:");
        dbDropdown = new JComboBox<>();
        GetDatabasesNames dbNamesGetter = new GetDatabasesNames();
        List<String> dbOptions = dbNamesGetter.getDatabasesNames();
        for (String dbName : dbOptions) {
            dbDropdown.addItem(dbName);
        }

        JButton showSchemaButton = new JButton("Show Schema");
        showSchemaButton.addActionListener(e -> {
            GetDatabaseSchema extractor = new GetDatabaseSchema();
            String schema = extractor.getDatabaseSchema((String) dbDropdown.getSelectedItem());

            ShemaWindow resultWindow = new ShemaWindow(schema);
            resultWindow.setVisible(true);
        });

        dropdownPanel.add(lmLabel);
        dropdownPanel.add(lmDropdown);
        dropdownPanel.add(dbLabel);
        dropdownPanel.add(dbDropdown);
        dropdownPanel.add(showSchemaButton);

        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JLabel questionLabel = new JLabel("Question:");
        questionField = new JTextField(20);
        submitButton = new JButton("Send");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Adiciona um KeyListener para quando a tecla Enter é pressionada
        questionField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        inputPanel.add(questionLabel);
        inputPanel.add(questionField);
        inputPanel.add(submitButton);

        // Text area
        questionArea = new JTextArea();
        questionArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(questionArea);

        // Add components to main panel
        mainPanel.add(dropdownPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void sendMessage() {
        String question = questionField.getText();
        fullText += "YOU:\n" + question + "\n\n";

        // Atualize a área de texto imediatamente para exibir a entrada do usuário
        questionField.setText("");
        questionArea.setText(fullText);

        // Ative o temporizador apenas quando uma pergunta é feita
        timer.start();

        // Agora processe a resposta do chatbot em segundo plano
        new Thread(new Runnable() {
            @Override
            public void run() {
                int selectedLmIndex = lmDropdown.getSelectedIndex();
                String selectedDbOption = (String) dbDropdown.getSelectedItem();

                ChatResponse chatResponse = new ChatResponse(question, selectedDbOption, selectedLmIndex);
                String sql = chatResponse.getLmResponseFromQuestion();

                // System.out.println("Query: " + sql);

                QueryExecutor sqlQuery = new QueryExecutor(selectedDbOption);
                String answer = sqlQuery.executeQuery(sql);

                // Substitua "Processing..." pela resposta do chatbot
                fullText += "CHATBOT:\n" + answer + "\n\n";

                // Pare o temporizador após a resposta do chatbot ser recebida
                timer.stop();

                // Atualize a área de texto novamente
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        questionArea.setText(fullText);
                    }
                });
            }
        }).start();
    }

    // Método para atualizar o texto de "Processing" com as diferentes pontuações
    private void updateProcessingText() {
        String[] processingTexts = { "Processing", "Processing.", "Processing..", "Processing..." };
        questionArea.setText(fullText + "CHATBOT:\n" + processingTexts[processingCounter]);
        processingCounter = (processingCounter + 1) % processingTexts.length;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChatBotGUI().setVisible(true);
            }
        });
    }
}
