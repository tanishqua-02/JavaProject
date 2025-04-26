import javax.swing.*;
import java.io.*;
import java.nio.file.*;

public class UploadMaterials extends JFrame {

    public UploadMaterials() {
        setTitle("Upload Materials");
        setSize(400, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JButton uploadButton = new JButton("Choose and Upload File");
        uploadButton.setBounds(100, 40, 200, 40);
        add(uploadButton);

        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                try {
                    // Create the upload directory if it does not exist
                    File destinationDir = new File("C:/UploadedMaterials/");
                    if (!destinationDir.exists()) {
                        destinationDir.mkdirs();
                    }

                    File destination = new File(destinationDir, selectedFile.getName());

                    // Copy the selected file to the upload directory
                    Files.copy(selectedFile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    JOptionPane.showMessageDialog(this,
                            "File uploaded successfully to:\n" + destination.getPath(),
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                            "File upload failed:\n" + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // For testing this class independently
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UploadMaterials().setVisible(true));
    }
}
