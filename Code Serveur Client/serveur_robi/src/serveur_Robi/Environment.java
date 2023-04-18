package serveur_Robi;

import java.util.HashMap;

import serveur_Robi.Executeur.Reference;

public class Environment {
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
