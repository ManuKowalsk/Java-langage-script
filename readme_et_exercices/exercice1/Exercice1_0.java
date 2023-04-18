package exercice1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import tools.Tools;

public class Exercice1_0 {
    GSpace space = new GSpace("Exercice 1", new Dimension(200, 150));
    GRect robi = new GRect();

    
    private Color getRandomColor() {
        Random rand = new Random();
        int r = rand.nextInt(256);
        int g = rand.nextInt(256);
        int b = rand.nextInt(256);
        return new Color(r,g,b);
    }
    
    public Exercice1_0() {
        space.addElement(robi);
        space.open();
        space.setColor(Color.pink);
        robi.setColor(getRandomColor());
        robi.setPosition(new Point(0,0));
        
        while(true) {
	        for(int i = 0;i<space.getWidth()-robi.getWidth();i++) {
	            robi.setPosition(new Point(i,robi.getY()));
	            Tools.sleep(2);
	            robi.setColor(getRandomColor());
	        }
	        
	        for(int i = 0;i<space.getHeight()-robi.getHeight();i++) {
	            robi.setPosition(new Point(robi.getX(),i));
	            Tools.sleep(2);
	            robi.setColor(getRandomColor());
	        }
	        for(int i = robi.getX();i >= 0;i--) {
	            robi.setPosition(new Point(i,robi.getY()));
	            Tools.sleep(2);
	            robi.setColor(getRandomColor());
	        }
	        for(int i = robi.getY();i >= 0;i--) {
	            robi.setPosition(new Point(robi.getX(),i));
	            Tools.sleep(2);
	            robi.setColor(getRandomColor());
	        }
        }
    }

    public static void main(String[] args) {
        new Exercice1_0();
    }
}