package exercice4;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import graphicLayer.GBounded;
import graphicLayer.GContainer;
import graphicLayer.GElement;
import graphicLayer.GImage;
import graphicLayer.GOval;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import graphicLayer.GString;
import stree.parser.SNode;
import stree.parser.SParser;
import tools.Tools;


public class Exercice4_2_0 {
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
		public Reference run(SNode expr) {
			try {
				return (primitives.get(expr.get(1).contents())).run(this, expr);
			}catch(Exception e) {
				return null;
			}
		}
		public void addCommand(String string, Command command) {
			primitives.put(string, command);
		}
		public Object getReceiver() {
			return receiver;
		}
		public void setReceiver(Object receiver) {
			this.receiver = receiver;
		}
		public Map<String, Command> getPrimitives() {
			return primitives;
		}
		public void setPrimitives(Map<String, Command> primitives) {
			this.primitives = primitives;
		}	
	}
	
/******************************* setColor, translate, sleep,  *******************************************************/
	public class setColor implements Command{
		public Reference run(Reference ref, SNode method) {
			if(ref.getReceiver().getClass() == GRect.class) {
				try {
					((GRect)ref.receiver).setColor(Tools.getColorByName(method.get(2).contents()));
				}catch(Exception e) {
					((GRect)ref.receiver).setColor(Tools.getColorByName(method.get(2).contents()));
				}
			}
			
			if(ref.getReceiver().getClass() == GOval.class) {
				try {
					((GOval)ref.receiver).setColor(Tools.getColorByName(method.get(2).contents()));
				}catch(Exception e) {
					((GOval)ref.receiver).setColor(Tools.getColorByName(method.get(2).contents()));
				}
			}
			
			if(ref.getReceiver().getClass() == GSpace.class) {
				try {
					((GSpace)ref.receiver).setColor(Tools.getColorByName(method.get(2).contents()));
				}catch(Exception e) {
					((GSpace)ref.receiver).setColor(Tools.getColorByName(method.get(2).contents()));
				}
			}
			
			if(ref.getReceiver().getClass() == GString.class) {
				try {
					((GString)ref.receiver).setColor(Tools.getColorByName(method.get(2).contents()));
				}catch(Exception e) {
					((GString)ref.receiver).setColor(Tools.getColorByName(method.get(2).contents()));
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

	class translate implements Command {
	    public Reference run(Reference ref, SNode method) {
	    	try {
	        	if(ref.getReceiver().getClass()==GRect.class) {
		            int x = Integer.parseInt(method.get(2).contents());
		            int y = Integer.parseInt(method.get(3).contents());
		            ((GRect)ref.receiver).translate(new Point(x, y));
	        	}
	        	
	        	if(ref.getReceiver().getClass()==GOval.class) {
		            int x = Integer.parseInt(method.get(2).contents());
		            int y = Integer.parseInt(method.get(3).contents());
		            ((GOval)ref.receiver).translate(new Point(x, y));
	        	}
	        	
	        	if(ref.getReceiver().getClass()==GImage.class) {
		            int x = Integer.parseInt(method.get(2).contents());
		            int y = Integer.parseInt(method.get(3).contents());
		            ((GImage)ref.receiver).setPosition(new Point(x,y));
	        	}
	        	
	        	if(ref.getReceiver().getClass()==GString.class) {
		            int x = Integer.parseInt(method.get(2).contents());
		            int y = Integer.parseInt(method.get(3).contents());
		            ((GString)ref.receiver).translate(new Point(x, y));
	        	}
	        	
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	}
	
/******************************* NewElement, NewImage, NewString ********************************************/
	class NewElement implements Command {
		public Reference run(Reference reference, SNode method) {
		try {
			@SuppressWarnings("unchecked")
			GElement e = ((Class<GElement>)reference.getReceiver()).getDeclaredConstructor().newInstance();
			
			Reference ref = new Reference(e);
			ref.addCommand("setColor", new setColor());
			ref.addCommand("translate", new translate());
			ref.addCommand("setDim", new setDim());
			return ref;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		}
	}
	
	class NewImage implements Command {
		public Reference run(Reference reference, SNode method) {
			File nomFic = new File(method.get(2).contents());
			BufferedImage rawImage = null;
			try {
				rawImage = ImageIO.read(nomFic);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			GImage im = new GImage(rawImage);
			Reference r = new Reference(im);
			
			r.addCommand("translate", new translate());
			return r;
		}
	}
	
	class NewString implements Command {
		public Reference run(Reference reference, SNode method) {
			GString str = new GString(method.get(2).contents());
            Reference ref = new Reference(str);
            ref.addCommand("setColor", new setColor());
            ref.addCommand("translate", new translate());
            ref.addCommand("setDim", new setDim());
            return ref;
		}
	}
	
/******************************* AddElement, DelElement, SetDim *******************************************************/
	class AddElement implements Command{
		@Override
		public Reference run(Reference ref, SNode method) { 
			Reference arg = new Reference(method.get(0));
			
			Reference r = new Interpreter().compute(environment, method.get(3));
			
			environment.addReference(method.get(2).contents(), r);
			arg.addCommand("new", new NewElement());
			
			GContainer container  = (GContainer) ref.getReceiver();
			container.addElement((GElement)r.getReceiver());
			container.repaint();
			
			return null;
		}
		
	}
	
	class DelElement implements Command{
		@Override
		public Reference run(Reference ref, SNode method) {
			String element = method.get(2).contents();
			Reference r = environment.getReferenceByName(element);
			
			if(r!=null) {
				GContainer container  = (GContainer) ref.getReceiver();
	            GElement elementToRemove = (GElement) r.getReceiver();
	            
	            container.removeElement(elementToRemove);
	            environment.delReference(element, r);
	            container.repaint();
	            
				System.out.println("composant supprimé");
	            return null;
			} else {
				System.out.println("ce composant n'existe pas");
				return null;
			}
			
		}
	}
	
	class setDim implements Command{

		@Override
		public Reference run(Reference ref, SNode method) {
			if(ref.getReceiver().getClass()==GRect.class) {
				int x = Integer.parseInt(method.get(2).contents());
	            int y = Integer.parseInt(method.get(3).contents());
	            ((GRect)ref.receiver).setDimension(new Dimension(x,y));
        	}
			if(ref.getReceiver().getClass()==GOval.class) {
	            int x = Integer.parseInt(method.get(2).contents());
	            int y = Integer.parseInt(method.get(3).contents());
	            ((GOval)ref.receiver).setDimension(new Dimension(x,y));
        	}
        	if(ref.getReceiver().getClass()==GString.class) {
	            int x = Integer.parseInt(method.get(2).contents());
	            int y = Integer.parseInt(method.get(3).contents());
	            ((GString)ref.receiver).setDimension(new Dimension(x,y));
        	}
			return null;
		}
		
	}
/******************************* Environment *******************************************************/
	class Environment {
		HashMap<String, Reference> variables;
		public Environment() {
			variables = new HashMap<String, Reference>();
		}
		public void addReference(String string, Reference spaceRef) {
			variables.put(string, spaceRef);
		}
		public void delReference(String string, Reference spaceRef) {
			variables.remove(string, spaceRef);
		}
		public Reference getReferenceByName(String receiverName) {
			return variables.get(receiverName);
		}
	}
	
/******************************* Interpreter *******************************************************/
	class Interpreter {
		public Reference compute(Environment environment, SNode next) {
			// quel est le nom du receiver
			String receiverName = next.get(0).contents();
			// quel est le receiver
			Reference receiver = environment.getReferenceByName(receiverName);
			// demande au receiver d'executer la s-expression compileeS
			return receiver.run(next);
			
		}
		
	}
	
	// Une seule variable d'instance
	Environment environment = new Environment();

	public Exercice4_2_0() {
		GSpace space = new GSpace("Exercice 4", new Dimension(200, 100));
		space.open();

		Reference spaceRef = new Reference(space);
		Reference rectClassRef = new Reference(GRect.class);
		Reference ovalClassRef = new Reference(GOval.class);
		Reference imageClassRef = new Reference(GImage.class);
		Reference stringClassRef = new Reference(GString.class);

		spaceRef.addCommand("setColor", new setColor());
		spaceRef.addCommand("sleep", new sleep());

		spaceRef.addCommand("add", new AddElement());
		spaceRef.addCommand("del", new DelElement());
		
		rectClassRef.addCommand("new", new NewElement());
		ovalClassRef.addCommand("new", new NewElement());
		imageClassRef.addCommand("new", new NewImage());
		stringClassRef.addCommand("new", new NewString());

		environment.addReference("space", spaceRef);
		environment.addReference("rect.class", rectClassRef);
		environment.addReference("oval.class", ovalClassRef);
		environment.addReference("image.class", imageClassRef);
		environment.addReference("label.class", stringClassRef);
		
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
				new Interpreter().compute(environment, itor.next());
			}
		}
	}

	public static void main(String[] args) {
		new Exercice4_2_0();
	}

}