package serveur_Robi;


import java.awt.AWTException;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;



import javax.imageio.ImageIO;



public class Main {
	
	Environment environment = new Environment();
	
	public static void main(String[] args) throws Exception {
		System.out.println("Le Serveur_Robi");

		ServerSocket serveurROBI = new ServerSocket(3000);
		Socket socket = serveurROBI.accept();
		System.out.println("Client connected");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintStream ps = new PrintStream(socket.getOutputStream());
		//DataInputStream inputStream = new DataInputStream(socket.getInputStream());

		//ps.println(" Bienvenue ! ");
		//ps.println(" Serveur_Robi.");

		
		int mode = 0; // 0 pour pp , 1 pour bloc
		Data data = new Data(null, null);
		String commande = "";
		List<String> snodes = new ArrayList<>();
		int x = 0;

		// Attente de reception de commandes et leur execution
		while (!(commande = br.readLine()).equals("{\"cmd\":\"script\",\"txt\":\"bye\"}")) {
			System.out.println("Received: " + commande);
			
			// Conversion du texte JSON en objet Java
			try {
				data = Data.fromJson(commande);
				System.out.println("refait data");
			} catch (Exception e) {
				System.out.println("Erreur lecture JSON " + e.getMessage());
			}
						
			if(data.getCmd().equals("script") && (data.getTxt().length()!=0)) {
				System.out.println("passeici1");
				snodes.clear(); 
				// Séparation des commandes et ajout dans la liste snodes
				String[] listeCmd = data.getTxt().split("(?<=\\))\\s*(?=\\()");
				for(int i=0; i<listeCmd.length; i++) {
					snodes.add(listeCmd[i]);
					System.out.println(listeCmd[i]);
					System.out.println("snodes"+ snodes.get(i));
				}
			}
			
			Data dataretour = new Data(null, null);
			// il va falloir modifier pour utiliser l'interpréteur dans executeSnode
			// normalement peu importe le mode de la commande pp ou bloc on doit recevoir toutes les snodes d'un coup
			// il va falloir les splits correctement
			// pour pp il faut executer le 1 de la liste pour le supprimer pour passer au suivant à la fin la liste est vide
			// pour bloc on vide la liste une fois tout fait après le while
			// si possible il faudrait tout dispatcher le code en différentes classes.
			// a voir si on utilise le exec ou si on test si y a pp et txt vide pareil pour bloc pour l'execution
				
			
			if (data.getCmd().equals("pp")) { 
				// Exécuter la snode en cours
				mode = 0;
			}
			
			
			if(data.getCmd().equals("exec") && (snodes.size()!=0) && mode ==0) {
				String snode = "";
				String trans = "";
				for(int i=0;i <= x;i++) {
					trans = snodes.get(i);
					snode += trans;
				}
				System.out.println(snode);
				if(x+1<snodes.size()) {
					x++;
				}
				
                BufferedImage image = executeSnode(snode,"Fenetre d'execution");
                String encodedImage = encodeImage(image);
                dataretour.setCmd("image");
                dataretour.setTxt(encodedImage);
                System.out.println(encodedImage);
                
                // Envoyer l'image en base64
                //String json = Data.toJson(dataretour);
                //ps.println(json);
                ps.println(encodedImage);
                System.out.println("ENVOYE");
				
				// Effacer la liste des snodes
			
			}
                
			
			
			// il va falloir modifier pour utiliser l'interpréteur dans executeSnode	
			if (data.getCmd().equals("bloc")) {
				mode = 1;
			}
			
			if(data.getCmd().equals("exec") && (snodes.size()!=0) && mode ==1) {
				x=0;
				String encodedImage = null;
				BufferedImage image = null;
				String snode = "";
			    for (String node : snodes) {
			        snode += node;
			    }
				System.out.println(snode);
				image = executeSnode(snode, "Fenetre d'execution");
				encodedImage = encodeImage(image);
				// on recup la derniere image
				dataretour.setCmd("image");
	            dataretour.setTxt(encodedImage);
	
	            System.out.println(encodedImage);
	            // Envoyer l'image en base64
//	            String json = Data.toJson(dataretour);
//	            ps.println(json);
	            ps.println(encodedImage);
                System.out.println("ENVOYE");
	            System.out.println("ENVOYE");
				
				//snodes.add(data.getTxt());
				System.out.println("Ajout dans la liste des Snodes pour bloc ");
				
				// Effacer la liste des snodes
	            //snodes.clear();  
			}							
	              
	            
	
		}

		System.out.println("Liste des Snode : " + snodes);
		ps.println("Serveur ferme !");
		serveurROBI.close();
		socket.close();
	}

	
	//Exécute un snode et génère l'image correspondante.
	private static BufferedImage executeSnode(String snode, String windowTitle) throws InterruptedException {
	    BufferedImage screenshot = null;
	    @SuppressWarnings("unused")
		Executeur execSnode = new Executeur(snode);
	    
	    try {
	        // Créer une instance de la classe Robot pour contrôler la souris et le clavier
	        Robot robot = new Robot();
	        
	        // Attendre une seconde pour laisser le temps à la page de charger
	        Thread.sleep(1000);
	        
	        // Obtenir toutes les fenêtres actives
	        Window[] windows = Window.getWindows();
	        
	        // Parcourir toutes les fenêtres pour trouver la fenêtre avec le titre spécifié
	        for (Window window : windows) {
	            if (window instanceof Frame && ((Frame) window).getTitle().equals("Fenetre d'execution")) {
	            	
	                // Prendre une capture d'écran de la zone de la fenêtre avec le titre spécifié
	                Rectangle bounds = window.getBounds();
	                screenshot = robot.createScreenCapture(bounds);
	                
	                ((Frame) window).dispose();
	                
	            }
	        }

	    } catch (AWTException ex) {
	        System.out.println("Erreur lors de la capture d'écran : " + ex.getMessage());
	    }

	    return screenshot;
	}


	//Encode l image en base64
	private static String encodeImage(BufferedImage image) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
		byte[] imageBytes = baos.toByteArray();
		return Base64.getEncoder().encodeToString(imageBytes);
	}
}