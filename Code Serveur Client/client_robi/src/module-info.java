module client_robi {
	requires transitive javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	exports application;
	
	opens application to javafx.graphics, javafx.fxml;
}
