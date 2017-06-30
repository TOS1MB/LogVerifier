package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private File file;
    private BufferedReader reader;


    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField secretKeyTextField;
    @FXML
    private Text fileText;
    @FXML
    private Button verifyButton, selectFileButton;

    public void selectButtonAction() {

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Log files (*.log)", "*.log");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File selectedFile = fileChooser.showOpenDialog(null);
        this.file = selectedFile;

        if (selectedFile != null) {
            fileText.setText("Chosen File: " + "'" + selectedFile.getName() + "'");

        } else {
            fileText.setText("No file selected...");
        }

    }

    public void verifyButtonAction() {

        if (fileText.getText().equals("No file selected...") && secretKeyTextField.getText().trim().equals("")) {
            AlertBox.display("log verifier v1.0", "Please choose file and enter secret key");
            return;
        }

        if (secretKeyTextField.getText().trim().equals("")) {
            AlertBox.display("log verifier v1.0", "Please enter secret key");
            return;
        }

        if (fileText.getText().equals("No file selected...")) {
            AlertBox.display("log verifier v1.0", "Please choose file");
            return;
        } else {

            boolean valid = verifyLog(secretKeyTextField.getText());

            if (valid) {
                AlertBox.display("log verifier v1.0", "Log file: " + "'" + file.getName() + "'" + " is valid");
            } else AlertBox.display("log verifier v1.0", "Log file: " + "'" + file.getName() + "'" + " is corrupt");
        }
    }


    public boolean verifyLog(String secret) {
        try {
            String charset = "UTF-8";
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
            String line;
            int lineCounter = 0;

            while ((line = reader.readLine()) != null) {

                lineCounter++;

                //noinspection Since15
                if (line.isEmpty()) { // checking if line is empty
                    return false;
                }

                String HMACtoVerifiy = line.substring(line.lastIndexOf("=") + 1);// extracting
                // hmac
                // from
                // line
                //System.out.println(HMACtoVerifiy);
                String[] parts = line.split("="); // separating line into values

                try {//if HMAC is not in the right place or there isn't even one

                    String HMACfromLine = parts[4]; // extracting hmac-value to
                    // delete it from line for
                    // computing the hmac of the
                    // original log
                    String logWithoutHMAC = line.replace(HMACfromLine, "");
                    //System.out.println(logWithoutHMAC);
                    String actualHMAC = Crypt.generateHmac(secret, logWithoutHMAC, lineCounter);
                    //System.out.println(actualHMAC);

                    if (HMACtoVerifiy.equals(actualHMAC)) {
                        continue;
                    } else
                        return false;
                } catch (Exception e) {
                    return false;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void initialize(URL location, ResourceBundle resources) {
//        verifyButton.setStyle("-fx-background-color: lightcoral");
//        selectFileButton.setStyle("-fx-background-color: lightcoral");
//        anchorPane.setStyle("-fx-background-color: lightslategray");

    }
}
