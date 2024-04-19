package exec;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import lmi.LanguageModelInterface;
import util.GetDatabaseNames;

public class DatabaseSelectionGUI extends JFrame {
    private JComboBox<String> lmDropdown;
    private JComboBox<String> dbDropdown;
    private JButton nextButton;

    public DatabaseSelectionGUI() {
        setTitle("Database Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 150);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        initComponents();
    }

    private void initComponents() {
        // Painel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Painel para os dropdowns
        JPanel dropdownPanel = new JPanel();
        dropdownPanel.setLayout(new FlowLayout());

        JLabel lmLabel = new JLabel("Language model:");
        lmDropdown = new JComboBox<>();
        LanguageModelInterface lmi = new LanguageModelInterface();
        List<String> lmOptions = lmi.getAvailableModels();
        for (String option : lmOptions) {
            lmDropdown.addItem(option);
        }

        JLabel dbLabel = new JLabel("Database:");
        dbDropdown = new JComboBox<>();
        GetDatabaseNames dbNamesGetter = new GetDatabaseNames();
        List<String> dbOptions = dbNamesGetter.getAllDatabaseNames();
        for (String dbName : dbOptions) {
            dbDropdown.addItem(dbName);
        }

        nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedLmOption = (String) lmDropdown.getSelectedItem();
                String selectedDbOption = (String) dbDropdown.getSelectedItem();
                ChatBotGUI chatBotGUI = new ChatBotGUI(selectedLmOption, selectedDbOption);
                chatBotGUI.setVisible(true);
                dispose(); // Fecha esta janela após avançar
            }
        });

        dropdownPanel.add(lmLabel);
        dropdownPanel.add(lmDropdown);
        dropdownPanel.add(dbLabel);
        dropdownPanel.add(dbDropdown);

        // Adiciona os componentes ao painel principal
        mainPanel.add(dropdownPanel, BorderLayout.CENTER);
        mainPanel.add(nextButton, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DatabaseSelectionGUI().setVisible(true);
            }
        });
    }
}
