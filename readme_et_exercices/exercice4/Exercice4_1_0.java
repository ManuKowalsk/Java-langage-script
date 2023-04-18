package exercice4;

//(space setColor black)  (robi setColor yellow) (space sleep 2000) (space setColor white)  (space sleep 1000) (robi setColor red) (space sleep 1000) (robi translate 100 0) (space sleep 1000) (robi translate 0 50) (space sleep 1000) (robi translate -100 0) (space sleep 1000) (robi translate 0 -40)
	

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import graphicLayer.GRect;
import graphicLayer.GSpace;
import stree.parser.SNode;
import stree.parser.SParser;
import tools.Tools;

public class Exercice4_1_0 {
	public interface Command {
		// le receiver est l'objet qui va executer method
		// method est la s-expression resultat de la compilation
		// d u code source a executer
		// exemple de code source : "(space setColor black)"
		abstract public Reference run(Reference receiver, SNode method);
	}
	
	public class Reference {
		Object receiver;
		Map<String, Command> primitives;
		public Reference(Object receiver) {
			this.receiver = receiver;
			primitives = new HashMap<String, Command>();
		}
		public void run(SNode expr) {
			try {
				(primitives.get(expr.get(1).contents())).run(this, expr);
			}catch(Exception e) {
				System.out.println("Erreur execution S-expression");
			}
		} 
	}
	
	public class Environment {
		HashMap<String, Reference> variables;
		public Environment() {
			variables = new HashMap<String, Reference>();
		}
		public void addReference(String string, Reference spaceRef) {
			variables.put(string, spaceRef);
		}
		public Reference getReferenceByName(String receiverName) {
			return variables.get(receiverName);
		}
	}
	
	public class setColor implements Command{
		public Reference run(Reference ref, SNode method) {
			if(method.get(0).contents().equals("robi")) {
				try {
					((GRect)ref.receiver).setColor(Tools.getColorByName(method.get(2).contents()));
				}catch(Exception e) {
					((GRect)ref.receiver).setColor(Tools.getColorByName(method.get(2).contents()));
				}
			}
			
			if(method.get(0).contents().equals("space")) {
				try {
					((GSpace)ref.receiver).setColor(Tools.getColorByName(method.get(2).contents()));
				}catch(Exception e) {
					((GSpace)ref.receiver).setColor(Tools.getColorByName(method.get(2).contents()));
				}
			}
			
			return null;
		}
	}
	
	public class sleep implements Command {
	    public Reference run(Reference ref, SNode method) {
	        try {
	            int delay = Integer.parseInt(method.get(2).contents());
	            Thread.sleep(delay);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	}

	public class translate implements Command {
	    public Reference run(Reference ref, SNode method) {
	        try {
	            int x = Integer.parseInt(method.get(2).contents());
	            int y = Integer.parseInt(method.get(3).contents());
	            ((GRect)ref.receiver).translate(new Point(x, y));
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	}
	
	// Une seule variable d'instance
	Environment environment = new Environment();

	public Exercice4_1_0() {
		// space et robi sont temporaires ici
		GSpace space = new GSpace("Exercice 4", new Dimension(200, 100));
		GRect robi = new GRect();

		space.addElement(robi);
		space.open();

		Reference spaceRef = new Reference(space);
		Reference robiRef = new Reference(robi);

		// Initialisation des references : on leur ajoute les primitives qu'elles
		// comprenent
		//
		setColor color = new setColor();
		robiRef.primitives.put("setColor", color);
		spaceRef.primitives.put("setColor", color);
		translate trans = new translate();
		robiRef.primitives.put("translate", trans);
		sleep zzz = new sleep();
		spaceRef.primitives.put("sleep", zzz);
		
		
		//
		// Enrigestrement des references dans l'environement par leur nom
		environment.addReference("space", spaceRef);
		environment.addReference("robi", robiRef);

		this.mainLoop();
	}

	private void mainLoop() {
		while (true) {
			// prompt
			System.out.print("> ");
			// lecture d'une serie de s-expressions au clavier (return = fin de la serie)
			String input = Tools.readKeyboard();
			// creation du parser
			SParser<SNode> parser = new SParser<>();
			// compilation
			List<SNode> compiled = null;
			try {
				compiled = parser.parse(input);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// execution des s-expressions compilees
			Iterator<SNode> itor = compiled.iterator();
			while (itor.hasNext()) {
				this.run((SNode) itor.next());
			}
		}
	}

	private void run(SNode expr) {
		// quel est le nom du receiver
		String receiverName = expr.get(0).contents();
		// quel est le receiver
		Reference receiver = environment.getReferenceByName(receiverName);
		// demande au receiver d'executer la s-expression compilee
		receiver.run(expr);
	}

	public static void main(String[] args) {
		new Exercice4_1_0();
	}

}