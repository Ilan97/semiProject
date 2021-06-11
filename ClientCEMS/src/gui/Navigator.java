package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Stack;

import control.GuiController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/** (Singleton) class for navigation between windows */
public class Navigator implements NavigatorInterface {

	private static NavigatorInterface instance = null;
	private static Pane baseNode = null;
	private static String defaultTab = "LoginForm";
	private Tab current = null;

	private Stack<Tab> history;
	private Stack<Tab> next;

	private Navigator() {
		if (baseNode == null)
			throw new RuntimeException("Navigator not initiated, run Navigator.init(Pane baseNode) first");
		history = new Stack<>();
		next = new Stack<>();
		navigate("LoginForm");
	}

	public static void setNavigator(NavigatorInterface nav) {
		instance = nav;
	}

	/**
	 * <pre>
	 * (Singleton) get an instance of navigator
	 * Navigator.init() need to be called before the call to this function
	 * </pre>
	 * 
	 * @exception RuntimeException if Navigator.init() not called once before the
	 *                             call to this function
	 * @return Navigator - the instance of this class
	 */
	public static NavigatorInterface instance() {
		if (instance == null)
			instance = new Navigator();
		return instance;
	}

	/**
	 * initialize the Navigator to change the content of the given Pane
	 * 
	 * @param baseNode the node that the navigator need to change
	 */
	public static void init(Pane baseNode) {
		Navigator.baseNode = baseNode;
	}

	/**
	 * navigate to the given file(window) and push the current view to the history
	 */
	@Override
	public GuiController navigate(String destenation) {
		String fxmlName = null;
		if (destenation == null) {
			baseNode.getChildren().clear();
			return null;
		}
		if (destenation.endsWith(".fxml"))
			fxmlName = destenation;
		else
			fxmlName = destenation + ".fxml";

		// push the current tab to the history
		if (current != null)
			history.push(current);
		URL screen = getClass().getResource(fxmlName);
		if (screen == null)
			return navigate(null);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(screen);
		try {
			current = new Tab();
			current.node = loader.load();
			current.controller = loader.getController();
			current.controller.init();
			current.name = destenation;
			next = new Stack<>();
			baseNode.getChildren().clear();
			baseNode.getChildren().add(current.node);
			baseNode.getScene().getWindow().sizeToScene();
			return current.controller;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NavigationInterruption e) {
			back();
		}
		return null;
	}

	/** navigates to the last page all the data from current page will be deleted */
	@Override
	public void back() {
		if (history.isEmpty())
			return;
		Tab last = history.pop();
		next.push(current);
		current = last;
		baseNode.getChildren().clear();
		baseNode.getChildren().add(current.node);
	}

	/** navigates to the last page all the data from current page will be deleted */
	@Override
	public void next() {
		if (next.isEmpty())
			return;
		Tab last = next.pop();
		history.push(current);
		current = last;
		baseNode.getChildren().clear();
		baseNode.getChildren().add(current.node);
	}

	/** navigates to the default page(empty Page) and clear the history */
	@Override
	public void clearHistory() {
		history = new Stack<>();
		next = new Stack<>();
		current = null;
		navigate(defaultTab);
	}

	/**
	 * navigates to the given page and clear the history
	 * 
	 * @param fxml the page to navigate
	 */
	@Override
	public void clearHistory(String fxml) {
		history = new Stack<>();
		next = new Stack<>();
		current = null;
		navigate(fxml);
	}

	/** helper class for saving windows */
	private class Tab {
		public Node node;
		public GuiController controller;
		@SuppressWarnings("unused")
		public String name;

		@SuppressWarnings("unused")
		public Tab(Node body, GuiController controller, String name) {
			super();
			this.node = body;
			this.controller = controller;
			this.name = name;
		}

		public Tab() {
			super();
		}
	}

	/**
	 * This method create pop up alert for warning the user. click OK will navigate
	 * to "formName" form
	 *
	 * @param formName The name of the file.
	 */
	public void alertPopUp(String formName) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Alert");
		alert.setHeaderText("Leaving this page will cause the data to be lost\r\n");
		alert.setContentText("Are you ok with this?");
		Image image = new Image(GuiController.class.getResource("error.png").toString());
		Label label = new Label();
		label.setPrefSize(image.getWidth(), image.getWidth());
		label.setGraphic(new ImageView(image));
		alert.setGraphic(label);
		alert.getDialogPane().getStylesheets().add(this.getClass().getResource("style.css").toString());
		ButtonBar bb = (ButtonBar) alert.getDialogPane().lookup(".button-bar");
		bb.setButtonOrder("C_L+Ok_R");
		bb.getButtons().forEach(b -> {
			Button bu = (Button) b;
			if (bu.getText().equals("Cancel")) {
				bu.getStyleClass().add("Cancel");
			}
		});
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) { // user chose OK
			clearHistory(formName);
		} else {
		} // user chose CANCEL or closed the dialog
	}

	/** navigation Interruption */
	public static class NavigationInterruption extends RuntimeException {

		private static final long serialVersionUID = 3626458317670172388L;

	}
}
