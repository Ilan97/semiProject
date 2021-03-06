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
 * @author Ohad Shamir
 * @version May 2021
 */

public class ManualExamCodeWindowController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * The code that is entered.
	 */
	public String code;
	/**
	 * Check if user choose directory to save the file.
	 */
	public boolean chooseDir;

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
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 * @throws IOException if an I/O error occurs when opening.
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
	 * @return the code from window.
	 */
	private String getCode() {
		return txtCode.getText();
	}

	/**
	 * This is FXML event handler. Handles the action of press on enter key.
	 *
	 * @param event The action event - user pressed on 'Enter' key.
	 */
	@FXML
	void inputPass(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER))
			btnDown.fire();
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Download' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void download(ActionEvent event) {
		// message to server
		Message messageToServer;
		ExamFile res = null;
		Object object = null;
		if (getCode().trim().isEmpty())
			lblErr.setText("enter code");
		else {
			// message to server
			messageToServer = new Message();
			messageToServer.setMsg(getCode() + " manual " + LoginController.user.getUsername());
			messageToServer.setControllerName("ExamController");
			messageToServer.setOperation("downloadManualExam");
			object = ClientUI.client.handleMessageFromClientUI(messageToServer);
			if (object == null)
				lblErr.setText("invalid code");
			else if (object instanceof ExamFile) {
				res = (ExamFile) object;
				code = txtCode.getText();
				// choose directory to download the file
				Stage stage = new Stage();
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Resource File");
				String currentUser = System.getProperty("user.home");
				fileChooser.setInitialDirectory(new File(currentUser + "\\downloads"));
				fileChooser.setInitialFileName(res.getFilename());
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("exam file (.docx)", "*.docx"));
				File newFile = fileChooser.showSaveDialog(stage);
				stage.setOnCloseRequest((e) -> {
					stage.close();
				});
				if (newFile != null) {
					chooseDir = true;
					try {
						FileOutputStream fis = new FileOutputStream(newFile);
						BufferedOutputStream bis = new BufferedOutputStream(fis);
						bis.write(res.getContent());
						bis.close();
						UsefulMethods.instance().display("download succeeded!");
						UsefulMethods.instance().display("file path: " + newFile.getAbsolutePath());
						ManualExamDownloadedWindowController msg = new ManualExamDownloadedWindowController();
						msg.start(new Stage());
					} catch (IOException e) {
						UsefulMethods.instance().display("fail to download the file");
						UsefulMethods.instance().printException(e);
					}
				}
				UsefulMethods.instance().close(event);
			} else {
				switch ((String) object) {

				case "too late to get into the exam":
					lblErr.setText("too late..");
					break;

				case "student is already did the exam":
					lblErr.setText("already done");
					break;

				}
			}
		}
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		chooseDir = false;
		code = null;
		// set images
		Image img = new Image(this.getClass().getResource("studentFrame.PNG").toString());
		imgBack.setImage(img);
	}
}
//End of ManualExamCodeWindowController class