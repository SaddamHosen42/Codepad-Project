import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;

public class CodepadController {
    @FXML
    private TextArea textArea;

    @FXML
    private MenuItem undo, redo, cut, copy, paste;

    private File currentFile = null;

    // Initialize Key Event Listener
    @FXML
    public void initialize() {
        textArea.setOnKeyPressed(event -> {
            if (new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN).match(event)) {
                saveFileAs();
                event.consume();
            }
        });
    }

    // Creates a new empty file
    @FXML
    private void newFile() {
        textArea.clear();
        currentFile = null;
    }

    // Opens an existing text file
    @FXML
    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");

        // set file type whice you want to open
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*")

        );

        File file = fileChooser.showOpenDialog(textArea.getScene().getWindow());
        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                textArea.setText(content.toString());
                currentFile = file;
            } catch (IOException e) {
                showAlert("Error", "Failed to open file.");
            }
        }
    }

    // Saves the current file
    @FXML
    private void saveFile() {
        if (currentFile != null) {
            writeFile(currentFile);
        } else {
            saveFileAs();
        }
    }

    // Saves the file with a new name
    @FXML
    private void saveFileAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        // Allow saving in multiple formats
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("Java Files (*.java)", "*.java"),
                new FileChooser.ExtensionFilter("C Files (*.c)", "*.c"),
                new FileChooser.ExtensionFilter("C++ Files (*.cpp)", "*.cpp"),
                new FileChooser.ExtensionFilter("Python Files (*.py)", "*.py"),
                new FileChooser.ExtensionFilter("HTML Files (*.html)", "*.html"),
                new FileChooser.ExtensionFilter("CSS Files (*.css)", "*.css"),
                new FileChooser.ExtensionFilter("JavaScript Files (*.js)", "*.js"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file = fileChooser.showSaveDialog(textArea.getScene().getWindow());
        if (file != null) {
            writeFile(file);
            currentFile = file;
        }
    }

    // Helper method to write file
    private void writeFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(textArea.getText());
        } catch (IOException e) {
            showAlert("Error", "Failed to save file.");
        }
    }

    // Closes the application with confirmation
    @FXML
    private void exitApplication() {
        if (!textArea.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit");
            alert.setHeaderText("You have unsaved changes.");
            alert.setContentText("Do you really want to exit without saving?");

            alert.showAndWait().ifPresent(response -> {
                if (response.getText().equals("OK")) {
                    Stage stage = (Stage) textArea.getScene().getWindow();
                    stage.close();
                }
            });
        } else {
            Stage stage = (Stage) textArea.getScene().getWindow();
            stage.close();
        }
    }

    // Undo and Redo functionality
    @FXML
    private void undoAction() {
        textArea.undo();
    }

    @FXML
    private void redoAction() {
        textArea.redo();
    }

    // Cut, Copy, Paste actions
    @FXML
    private void cutText() {
        textArea.cut();
    }

    @FXML
    private void copyText() {
        textArea.copy();
    }

    @FXML
    private void pasteText() {
        textArea.paste();
    }

    // Show alert for errors
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
