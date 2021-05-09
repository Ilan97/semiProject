package control;

import java.io.IOException;

import client.ClientUI;
import javafx.stage.Stage;

/**
 * <pre>
 * interface for GUI to save them in the navigator,
 * the init() function is called on load in the navigator
 * </pre>
 */
public interface GuiController {

	/** method to run after loading */
	public default void init() {
	}
}
