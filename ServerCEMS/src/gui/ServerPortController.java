package gui;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.Client;
import logic.ClientStatus;
import ocsf.server.ConnectionToClient;
import server.ServerController;

/**
 * This is controller class (boundary) for windows ServerPort and
 * ClientConnectedToServer. This class handle all events related to these
 * windows. This class connect with server.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @author Ohad Shamir
 * @version May 2021
 */

public class ServerPortController {

	// Instance variables **********************************************

	/**
	 * static instance for ServerController instance. Will be create only once for
	 * each run.
	 */
	public static ServerController server;
	/**
	 * FXML variables.
	 */
	@FXML
	private TextField txtPort;
	@FXML
	private Label lblErrorPort;
	@FXML
	private TableView<Client> table;
	@FXML
	private TableColumn<Client, String> columnIP;
	@FXML
	private TableColumn<Client, String> columnHost;
	@FXML
	private TableColumn<Client, ClientStatus> columnStatus;

	/**
	 * The server's first window and this window's first method. load and show this
	 * window.
	 *
	 * @param primaryStage The stage for window's scene.
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ServerPort.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Server Port");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * @return the port number user puts in
	 */
	private String getport() {
		return txtPort.getText();
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'connect' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	public void connectActionButton(ActionEvent event) throws Exception {
		String port = getport();
		FXMLLoader loader = new FXMLLoader();
		// check if user put port number
		if (port.trim().isEmpty())
			lblErrorPort.setText("you must enter port number");
		// trim isn't empty
		else {
			// initialize the instance
			server = new ServerController(Integer.parseInt(port));
			// hiding the current window
			((Node) event.getSource()).getScene().getWindow().hide();
			//try to listening for connections
			server.runServer();
			if (!server.connectionSuccessfull)
				lblErrorPort.setText("connection faild");
			// connection succeeded
			else {
				// load the next window
				Stage primaryStage = new Stage();
				Pane root = loader.load(getClass().getResource("/gui/ClientConnectedToServer.fxml").openStream());
				// show the loaded scene
				Scene scene = new Scene(root);
				primaryStage.setTitle("Server Connections");
				primaryStage.setScene(scene);
				// close the program
				primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				    @Override
				    public void handle(WindowEvent e) {
				     Platform.exit();
				     System.exit(0);
				    }
				  });
				primaryStage.show();
			}
		}
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'refresh' button.
	 *
	 * @param event The action event.
	 * @throws IOException 
	 */
	@SuppressWarnings({ "static-access", "resource" })
	@FXML
	public void refreshActionButton(ActionEvent event) throws IOException {
		// this list save the details of all connected clients
		ArrayList<Client> allClients = new ArrayList<>();
		String clientAddress; // the whole client's data
		String[] ca; // tmp for string clientAddress for split method
		String ip; // client's ip address
		String hostName; // client's host name
		
		// there is at least 1 client that connected to server
		if (server.getNumberOfClients() != 0) {
			Thread[] clientConnected = new Thread[server.getNumberOfClients()];
			clientConnected = server.getClientConnections();
			for (Thread client : clientConnected) {
				// get client's ip address and host's name
				clientAddress = (((ConnectionToClient) client).getInetAddress().getLocalHost()).toString();
				ca = clientAddress.split("/");
				hostName = ca[0];
				ip = ca[1] + " : " + (new Socket(((ConnectionToClient) client).getInetAddress(), server.getPort())).getLocalPort();
				// add the details to the table
				allClients.add(new Client(ip, hostName, ClientStatus.CONNECTED));
			}
		}
		setTable(allClients);
	}

	/**
	 * This method set all clients details in the table, in the match columns.
	 *
	 * @param clients All clients connected to server.
	 */
	public void setTable(ArrayList<Client> clients) {
		ObservableList<Client> tableContent = FXCollections.observableArrayList(clients);
		columnIP.setCellValueFactory(new PropertyValueFactory<Client, String>("ip"));
		columnHost.setCellValueFactory(new PropertyValueFactory<Client, String>("hostName"));
		columnStatus.setCellValueFactory(new PropertyValueFactory<Client, ClientStatus>("status"));
		table.setItems(tableContent);
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'exit' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	public void getExitButton(ActionEvent event) {
		display("exit server");
		System.exit(0);
	}

	/**
	 * This method displays a message into the console.
	 *
	 * @param message The string to be displayed.
	 */
	public static void display(String message) {
		System.out.println("> " + message);
	}
}
//End of ServerPortController class
