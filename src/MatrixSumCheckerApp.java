
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MatrixSumCheckerApp extends JFrame {
    private JTextField inputField;
    private JButton loadButton;
    private JTable matrixTable;
    private JLabel resultLabel;

    public MatrixSumCheckerApp() {
        setTitle("Matrix Sum Checker");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Input and Load Button
        JPanel inputPanel = new JPanel();
        inputField = new JTextField(20);
        loadButton = new JButton("Load Matrix");
        loadButton.addActionListener(new LoadMatrixListener());
        inputPanel.add(new JLabel("Enter file path:"));
        inputPanel.add(inputField);
        inputPanel.add(loadButton);

        // Matrix Table
        matrixTable = new JTable(20, 20);
        JScrollPane tableScroll = new JScrollPane(matrixTable);

        // Result Label
        resultLabel = new JLabel("Result: ");

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(tableScroll, BorderLayout.CENTER);
        panel.add(resultLabel, BorderLayout.SOUTH);
        
        add(panel);
    }

    private class LoadMatrixListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String filePath = inputField.getText();
            try {
                int[][] matrix = loadMatrixFromFile(filePath);
                displayMatrix(matrix);
                checkMatrixSums(matrix);
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(MatrixSumCheckerApp.this, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(MatrixSumCheckerApp.this, "Error reading file!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (InvalidDataFormatException ex) {
                JOptionPane.showMessageDialog(MatrixSumCheckerApp.this, ex.getMessage(), "Data Error", JOptionPane.ERROR_MESSAGE);
            } catch (LargeNumberException ex) {
                JOptionPane.showMessageDialog(MatrixSumCheckerApp.this, ex.getMessage(), "Custom Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private int[][] loadMatrixFromFile(String filePath) throws IOException, InvalidDataFormatException, LargeNumberException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<int[]> rows = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] elements = line.trim().split("\\s+");
                if (elements.length > 20) throw new InvalidDataFormatException("Matrix row exceeds size limit.");

                int[] row = new int[elements.length];
                for (int i = 0; i < elements.length; i++) {
                    try {
                        row[i] = Integer.parseInt(elements[i]);
                        // Custom Exception: Number is too large
                        if (Math.abs(row[i]) > 1000) throw new LargeNumberException("Number too large: " + row[i]);
                    } catch (NumberFormatException ex) {
                        throw new InvalidDataFormatException("Invalid number format: " + elements[i]);
                    }
                }
                rows.add(row);
            }
            return rows.toArray(new int[0][0]);
        }
    }

    private void displayMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrixTable.setValueAt(matrix[i][j], i, j);
            }
        }
    }

    private void checkMatrixSums(int[][] matrix) {
        int n = matrix.length;
        int[] rowSums = new int[n];
        int[] colSums = new int[n];

        // Calculate row and column sums
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rowSums[i] += matrix[i][j];
                colSums[j] += matrix[i][j];
            }
        }

        // Check for matching sums
        boolean found = false;
        StringBuilder result = new StringBuilder("<html>Matching sums found in rows and columns:<br>");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (rowSums[i] == colSums[j]) {
                    found = true;
                    result.append("Row ").append(i + 1).append(" and Column ").append(j + 1).append(" have the same sum.<br>");
                }
            }
        }
        if (!found) {
            result.append("No matching row and column sums found.");
        }
        result.append("</html>");
        resultLabel.setText(result.toString());
    }

    // Custom Exception for invalid data format
    private static class InvalidDataFormatException extends Exception {
        public InvalidDataFormatException(String message) {
            super(message);
        }
    }

    // Custom Exception for large numbers (inherits ArithmeticException)
    private static class LargeNumberException extends ArithmeticException {
        public LargeNumberException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MatrixSumCheckerApp app = new MatrixSumCheckerApp();
            app.setVisible(true);
        });
    }
}