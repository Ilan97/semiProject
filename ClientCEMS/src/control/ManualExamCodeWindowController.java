package control;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.ExamFile;
import logic.Message;

/**
 * This is controller class (boundary) for window ManualExamCode in Student.
 * This class handle all events related to this window. This class connect with
 * client.
 *
 * @author Ilan Meikler
 * @author Bat-El Gardin
 * @author Moran Davidov
 * @version May 2021
 */

public class ManualExamCodeWindowController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * The code that is entered.
	 */
	public String code = null;

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private TextField txtCode;
	@FXML
	private Label lblErr;
	@FXML
	private Button btnDown;

	// Instance methods ************************************************

	/**
	 * @return the code from window.
	 */
	private String getCode() {
		return txtCode.getText();
	}

	/**
	 * This is FXML event handler. Handles the action of press on enter key.
	 *
	 * @param event The action event.
	 */
	@FXML
	void inputPass(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER))
			btnDown.fire();
	}

	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 * @return the "real" controller.
	 */
	public Object start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/ManualExamCodeWindow.fxml"));
		Parent root = loader.load();
		ManualExamCodeWindowController cont = loader.getController();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Enter Code");
		primaryStage.setScene(scene);
		primaryStage.showAndWait();
		return cont;
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Download' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void download(ActionEvent event) {
		ExamFile res = null;
		if (getCode().trim().isEmpty())
			lblErr.setText("enter code");
		else {
			// message to server
			Message messageToServer = new Message();
			messageToServer.setMsg(getCode() + " manual");
			messageToServer.setControllerName("ExamController");
			messageToServer.setOperation("downloadManualExam");
			res = (ExamFile) ClientUI.client.handleMessageFromClientUI(messageToServer);
			if (res == null)
				lblErr.setText("invalid code");
			else {
				code = txtCode.getText();
				// choose directory to download the file
				Stage stage = new Stage();
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Resource File");
				String currentUser = System.getProperty("user.home");
				fileChooser.setInitialDirectory(new File(currentUser + "\\downloads"));
				fileChooser.setInitialFileName(res.getFilename());
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("exam file (.txt)", "*.txt"));
				File newFile = fileChooser.showSaveDialog(stage);
				stage.setOnCloseRequest((e) -> {
					stage.close();
				});
				// copy the file to the desktop if no directory was chosen
				if (newFile == null) {
					String LocalfilePath = currentUser + "\\desktop";
					newFile = new File(LocalfilePath + "\\" + res.getFilename());
				}
				try {
					FileOutputStream fis = new FileOutputStream(newFile);
					BufferedOutputStream bis = new BufferedOutputStream(fis);
					bis.write(res.getContent());
					bis.close();
					display("download succeeded!");
					display("file path: " + newFile.getAbsolutePath());
					ManualExamDownloadedWindowController msg = new ManualExamDownloadedWindowController();
					msg.start(new Stage());
				} catch (IOException e) {
					display("fail to download the file");
					// pop up ?
				}
				close(event);
			}
		}
	}

	/**
	 * This method close the current stage.
	 */
	private void close(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

	/**
	 * This method displays a message into the console.
	 *
	 * @param message The string to be displayed.
	 */
	public static void display(String message) {
		System.out.println("> " + message);
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set images
		Image img = new Image(this.getClass().getResource("studentFrame.PNG").toString());
		imgBack.setImage(img);
	}
}
//End of ManualExamCodeWindowController class