package exercice2;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.awt.Point;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import stree.parser.SNode;
import stree.parser.SParser;
import tools.Tools;

public class Exercice2_1_0 {
	GSpace space = new GSpace("Exercice 2_1", new Dimension(200, 100));
	GRect robi = new GRect();
	String script = "(space color white) (robi color red) (robi translate 10 0) (space sleep 100) (robi translate 0 10) (space sleep 100) (robi translate -10 0) (space sleep 100) (robi translate 0 -10)";

	public Exercice2_1_0() {
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
		String espace = expr.get(0).contents();
		String cmd = expr.get(1).contents();
		String param1 = expr.get(2).contents();
		
		
		if(espace.equals("space")) {
			if(cmd.equals("sleep")){
				Tools.sleep(Integer.valueOf(param1));
			}
			if(cmd.equals("color"))
			space.setColor(Tools.getColorByName(param1));
		}
		
		
		if(espace.equals("robi")) {
			if(cmd.equals("translate")){
				 String param2 = expr.get(3).contents();
				 robi.setPosition(new Point(robi.getX() + Integer.valueOf(param1),robi.getY() + Integer.valueOf(param2)));
			}
			if(cmd.equals("color"))
			robi.setColor(Tools.getColorByName(param1));
		}	
	}

	public static void main(String[] args) {
		new Exercice2_1_0();
	}

}