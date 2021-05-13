package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Stack;

import control.GuiController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/** (Singleton) class for navigation between windows */
public class Navigator implements NavigatorInterface {

	private static NavigatorInterface instance = null;
	private static Pane baseNode = null;
	private static String defaultTab = "Motd";
	private Tab current = null;

	private Stack<Tab> history;

	private Navigator() {
		if (baseNode == null)
			throw new RuntimeException("Navigator not initiated, run Navigator.init(Pane baseNode) first");
		history = new Stack<>();
		navigate("logIn");
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
		current = last;
		baseNode.getChildren().clear();
		baseNode.getChildren().add(current.node);
	}

	/** navigates to the default page(empty Page) and clear the history */
	@Override
	public void clearHistory() {
		history = new Stack<>();
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
		current = null;
		navigate(fxml);
	}

	/** helper class for saving windows */
	private class Tab {
		public Node node;
		public GuiController controller;
		public String name;

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

	/** navigation Interruption */
	public static class NavigationInterruption extends RuntimeException {

		private static final long serialVersionUID = 3626458317670172388L;

	}
}
