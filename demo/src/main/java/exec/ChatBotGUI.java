package exec;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
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

public class ChatBotGUI extends JFrame {
    private JTextField questionField;
    private JButton submitButton;
    private JTextArea questionArea;

    public ChatBotGUI(String lmodel, String database) {
        setTitle("SQL Query Chatbot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        initComponents(lmodel, database);
    }

    private void initComponents(String lmodel, String database) {
        QueryExecutor sqlQuery = new QueryExecutor(database);

        // Painel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Painel para os campos de entrada
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JLabel questionLabel = new JLabel("Question:");
        questionField = new JTextField(20);
        submitButton = new JButton("Send");

        // Adiciona um ouvinte de ação ao botão
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String question = questionField.getText();
                questionArea.setText("Processing question...");

                // Adiciona um leve atraso antes de chamar a função getAnswer()
                Timer timer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Processa a pergunta e exibe a resposta
                        ChatResponse chatResponse = new ChatResponse(question, database, lmodel);

                        String sql = chatResponse.getLmResponseFromQuestion();
                        System.out.println(sql + "\n");
                        String answer = sqlQuery.executeQuery(sql);
                        questionArea.setText("Question: " + question + "\nAnswer: \n" + answer);
                    }
                });
                timer.setRepeats(false); // Executa apenas uma vez
                timer.start();
            }
        });

        inputPanel.add(questionLabel);
        inputPanel.add(questionField);
        inputPanel.add(submitButton);

        // Área de texto para exibir a pergunta e resposta
        questionArea = new JTextArea();
        questionArea.setEditable(false); // Impede edição da área de texto
        JScrollPane scrollPane = new JScrollPane(questionArea);

        // Adiciona os componentes ao painel principal

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChatBotGUI("ollama/sqlcoder", "teste-api-2").setVisible(true);
            }
        });
    }
}