package gui;

import control.GuiController;

public interface NavigatorInterface {

	/**
	 * navigate to the given file(window) and push the current view to the history
	 */
	GuiController navigate(String destenation);

	/** navigates to the last page all the data from current page will be deleted */
	void back();

	/** navigates to the default page(empty Page) and clear the history */
	void clearHistory();

	/**
	 * navigates to the given page and clear the history
	 * 
	 * @param fxml the page to navigate
	 */
	void clearHistory(String fxml);

	void alertPopUp(String string);

	/** navigates to the last page all the data from current page will be deleted */
	void next();

}