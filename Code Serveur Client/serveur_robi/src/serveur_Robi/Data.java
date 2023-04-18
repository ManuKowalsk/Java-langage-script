package serveur_Robi;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;



// classe pour l'envoi de commandes sur le socket

	public class Data {

	String cmd = null;
	String txt = null;
	
	public Data() {
		
	}
	
	public Data(String cmd, String json) {
		this.cmd = cmd;
		this.txt = json;
	}

	public static String toJson(Data d) {
		
		
		
		try {
			StringWriter res = new StringWriter();
			JsonGenerator generator = new JsonFactory().createGenerator(res);
			ObjectMapper mapper = new ObjectMapper();
			generator.setCodec(mapper);
			generator.writeObject(d);
			generator.close();
			return res.toString();
		}
		catch (Exception e) {
			
		}
		
		return null;

	}
	
	public static Data fromJson(String json) throws IOException {
	    ObjectMapper objectMapper = new ObjectMapper();
	    Data data = objectMapper.readValue(json, Data.class);
	    return data;
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
