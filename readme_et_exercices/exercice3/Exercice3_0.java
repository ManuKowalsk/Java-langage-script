package exercice3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import graphicLayer.GRect;
import graphicLayer.GSpace;
import stree.parser.SNode;
import stree.parser.SParser;
import tools.Tools;

public class Exercice3_0 {
	GSpace space = new GSpace("Exercice 3", new Dimension(200, 100));
	GRect robi = new GRect();
	String script = "" +
	"   (space setColor black) " +
	"   (robi setColor yellow)" +
	"   (space sleep 1000)" +
	"   (space setColor white)\n" + 
	"   (space sleep 1000)" +
	"	(robi setColor red) \n" + 
	"   (space sleep 1000)" +
	"	(robi translate 100 0)\n" + 
	"	(space sleep 1000)\n" + 
	"	(robi translate 0 50)\n" + 
	"	(space sleep 1000)\n" + 
	"	(robi translate -100 0)\n" + 
	"	(space sleep 1000)\n" + 
	"	(robi translate 0 -40)";

	public Exercice3_0() {
		space.addElement(robi);
		space.open();
		this.runScript();
	}
	private void runScript() {
		SParser<SNode> parser = new SParser<>();
		List<SNode> rootNodes = null;
		try {
			rootNodes = parser.parse(script);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Iterator<SNode> itor = rootNodes.iterator();
		while (itor.hasNext()) {
			this.run(itor.next());
		}
	}

	private void run(SNode expr) {
		Command cmd = getCommandFromExpr(expr);
		if (cmd == null)
			throw new Error("unable to get command for: " + expr);
		cmd.run();
	}
	
	Command getCommandFromExpr(SNode expr) {
		if(expr.get(0).contents().equals("space")) {
			if(expr.get(1).contents().equals("setColor")) {
				return new SpaceChangeColor(Tools.getColorByName(expr.get(2).contents()));
			}
			
			else if(expr.get(1).contents().equals("sleep")) {
				try {
				    int sleepTime = Integer.parseInt(expr.get(2).contents());
				    if (sleepTime < 0) {
				        throw new IllegalArgumentException("sleep doit être supérieur à zéro");
				    }
				    return new SpaceSleep(Integer.valueOf(expr.get(2).contents()));
				} catch (NumberFormatException e) {
				    throw new IllegalArgumentException("sleep doit être un entier positif", e);
				}
			}
			else 
				throw new Error("Commande Invalide pour space " + expr);
		}
		
		else if(expr.get(0).contents().equals("robi")) {
			if(expr.get(1).contents().equals("setColor")) {
				return new RobiChangeColor(Tools.getColorByName(expr.get(2).contents()));
			}
			else if(expr.get(1).contents().equals("translate")) {
				String param1 = expr.get(2).contents();
			    String param2 = expr.get(3).contents();
			    try {
			        int x = Integer.parseInt(param1);
			        int y = Integer.parseInt(param2);
			        return new RobiTranslate(x, y);
			    } catch (NumberFormatException e) {
			        throw new IllegalArgumentException("Les paramètres pour la fonction translate doivent etre des entiers: " + param1 + ", " + param2);
			    }
			}
			else 
				throw new Error("Commande Invalide pour robi " + expr);
		}
		else {
			throw new Error("Commande incorrect");
		}
	}

	public static void main(String[] args) {
		new Exercice3_0();
	}

	public interface Command {
		abstract public void run();
	}

	public class SpaceChangeColor implements Command {
		Color newColor;

		public SpaceChangeColor(Color newColor) {
			this.newColor = newColor;
		}

		@Override
		public void run() {
			space.setColor(newColor);
		}

	}
	
	public class RobiChangeColor implements Command {
		Color newColor;

		public RobiChangeColor(Color newColor) {
			this.newColor = newColor;
		}

		@Override
		public void run() {
			robi.setColor(newColor);
		}

	}
	
	public class RobiTranslate implements Command {
		Integer x;
		Integer y;

		public RobiTranslate(Integer x,Integer y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public void run() {
			robi.setPosition(new Point(robi.getX()+x,robi.getY()+y));
		}

	}
	
	 public class SpaceSleep implements Command {
         int Sleep;
         
         public SpaceSleep(int Sleep) {
             this.Sleep = Sleep;
         }

         @Override
         public void run() {
             Tools.sleep(Integer.valueOf(Sleep));
         }
	
	 }
}