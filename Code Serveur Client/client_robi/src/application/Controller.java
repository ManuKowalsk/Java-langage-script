package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
//import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
//import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.Socket;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.event.ActionEvent;

public class Controller {

	@FXML
	private TextArea textField;
	@FXML
	private Button buttonExecute;
	@FXML
	private Button buttonEnvoye;
	@FXML
	private RadioButton radioP;
	@FXML
	private RadioButton radioB;
	@FXML
	private ImageView imageField;
	@FXML
	private int mode = 0;// Si 0 alors pas a pas si 1 alors en bloc
	@FXML
	private int index = 0; // initialise l'indice de la première commande
	@FXML
	private MenuItem buttonFichier;
//	@FXML 
//	private Menu buttonFermer;

	FileChooser fileChooser = new FileChooser();
//	private Client client;

	
	
	/**
     * Ouvre un fichier et remplit le champ de texte avec son contenu
     */
	@FXML
	void getText(ActionEvent event) {
		File file = fileChooser.showOpenDialog(new Stage());
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				textField.appendText(scanner.nextLine());
			}
			scanner.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	/**
     * Convertit une chaîne encodée en base64 en une image et l'écrit dans un fichier
     */
	public class Base64Util {

		public static void main(String[] args) {
			String base64String = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAACklEQVR4nGMAAQAABQABDQottAAAAABJRU5ErkJggg==";
			writeBase64ToFileAsImage(base64String, "output.png");
		}

		public static void writeBase64ToFileAsImage(String base64String, String outputPath) {
			try (FileOutputStream stream = new FileOutputStream(new File(outputPath))) {
				byte[] data = Base64.getDecoder().decode(base64String);
				stream.write(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

//	@Override
//	public void initialize(URL arg0, ResourceBundle arg1) {
//		try {
//			client = new Client(new Socket("localhost",8000));
//			System.out.println("Connexion au Serveur réussie.");
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.out.println("Erreur connection serveur");
//		}
//	}

	
	/**
     * Initialize la ou la page d'ouverture de fichier s'ouvre
     */
	public void initialize(URL url, ResourceBundle ressourceBundle) {
		fileChooser.setInitialDirectory(
				new File("C:\\Users\\emanu\\eclipse-workspace\\client_robi\\src\\application\\test.txt"));
	}

	
	/**
     * Passage de String à JSON et d'une classe Commande en string grace à JSON
     */
	public class JsonUtils {
		public static String toJson(Commande commande) {
			StringWriter sw = new StringWriter();

			try {
				JsonGenerator generator = new JsonFactory().createGenerator(sw);
				ObjectMapper mapper = new ObjectMapper();
				generator.setCodec(mapper);
				generator.writeObject(commande);
				generator.close();
			} catch (Exception e) {
				System.out.println("Erreur production JSON " + e.getMessage());
			}

			return sw.toString();
		}

		public static Commande fromJson(String json) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				return mapper.readValue(json, Commande.class);
			} catch (Exception e) {
				System.out.println("Erreur parsing JSON " + e.getMessage());
				return null;
			}
		}
	}

	private TCPClient tcpClient;

	/**
     * Initalize le socket client
     */
	public void initialize() {
		try {
			tcpClient = new TCPClient("localhost", 3000);
			System.out.println("Connexion au serveur réussie.");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erreur de connexion au serveur");
		}
	}

	private class TCPClient {
		public Socket socket;
		// private DataInputStream inputStream;
		// private DataOutputStream outputStream;
		// private ExecutorService executor;
		private PrintStream ps;

		public TCPClient(String host, int port) throws IOException {
			socket = new Socket(host, port);
			// inputStream = new DataInputStream(socket.getInputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// outputStream = new DataOutputStream(socket.getOutputStream());
			ps = new PrintStream(socket.getOutputStream());
			// executor = Executors.newSingleThreadExecutor();

			/**
		     * Thread de recepetion
		     */
			new Thread() {
				public void run() {
					while (true) {
						try {
							String lecture = br.readLine();

							try {
								    // EN COMMANTAIRE CAR
									// fromJson ne marchait pas 
//									Commande c = JsonUtils.fromJson(lecture);
//									String info = c.getCmd();

//									if (c.getCmd().equals("image")) {
//										System.out.println("passe ici lol");
//										String base64 = c.getTxt();
//										Base64Util.writeBase64ToFileAsImage(base64, "src/application/image.jpg");
//										Thread.sleep(5000);
//										Image iv = new Image(getClass().getResource("image.jpg").toExternalForm());
//								        imageField.setImage(iv);
//										
//									}
//									else {
//										System.out.println("Commande inconnue"+ lecture);
//									}

								String base64 = lecture;
								Base64Util.writeBase64ToFileAsImage(base64, "src/application/image.jpg");
								Thread.sleep(4000);
								Image iv = new Image(getClass().getResource("image.jpg").toExternalForm());
								imageField.setImage(iv);
							} catch (Exception e) {
								e.printStackTrace();
								// TODO Auto-generated catch block
								System.out.println("Ligne recue :" + lecture);
							}
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("Erreur lecture socket");
						}

					}

				}
			}.start();
		}
		/**
	     * Envoie la commande passer en parametre en prenant soin de la convertir en json
	     */
		public void sendCommand(Commande commande) throws IOException {

			String json = JsonUtils.toJson(commande);
			System.out.println("Résultat JSON = " + json);

			// outputStream.writeUTF(json);
			ps.println(json);
			ps.flush();
			System.out.println("Envoi de la commande");

		}

		public void close() {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
     * class Commande
     */
	public class Commande {
		String cmd = null;
		String txt = null;

		public Commande() {
		}

		public String getCmd() {
			return cmd;
		}

		public void setCmd(String cmd) {
			this.cmd = cmd;
		}

		public String getTxt() {
			return txt;
		}

		public void setTxt(String txt) {
			this.txt = txt;
		}

	}

	
	/**
     *  Lors du clic sur envoyer, ca envoie le script vers le serveur 
     */
	@FXML
	public void sendTexte(ActionEvent event) {
		String s = textField.getText();
		Commande commande = new Commande();
		commande.setCmd("script");
		commande.setTxt(s);

		try {
			tcpClient.sendCommand(commande);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erreur lors de l'envoi de la commande");
		}
	}
	
	/**
     *  Lors du clic sur executer, ca envoie cmd=exec vers le serveur 
     */
	@FXML
	public void submit(ActionEvent event) {
		Commande commande = new Commande();
		commande.setCmd("exec");
		commande.setTxt(null);

		try {
			tcpClient.sendCommand(commande);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erreur lors de l'envoi de la commande");
		}
	}

	
	/**
     *  Lors du clic sur le bontou pas a  pas , ca envoie cmd=pp vers le serveur 
     */
	@FXML
	public void pas(ActionEvent event) {
		Commande commande = new Commande();
		commande.setCmd("pp");
		commande.setTxt(null);
		System.out.println(commande.cmd);
		try {
			tcpClient.sendCommand(commande);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erreur lors de l'envoi de la commande");
		}

	}

	/**
     *  Lors du clic sur le bontou bloc , ca envoie cmd=bloc vers le serveur 
     */
	@FXML
	public void bloc(ActionEvent event) {
		Commande commande = new Commande();
		commande.setCmd("bloc");
		commande.setTxt(null);
		System.out.println(commande.cmd);
		try {
			tcpClient.sendCommand(commande);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erreur lors de l'envoi de la commande");
		}
	}

	/**
     *  Lors du clic sur quitter , ca ferme la page
     */
	@FXML
	public void quitter(ActionEvent event) {
		System.exit(0);
		tcpClient.close();
	}

}