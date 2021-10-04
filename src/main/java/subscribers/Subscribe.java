package subscribers;

import java.net.*;
import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import java.io.*;
import org.json.*;
import com.cloudant.client.api.*;

@ManagedBean(name = "Subscribe")

public class Subscribe implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Database db = null;
	private String emailAddress = "";
	private String emailAddresses = "";
	private String output = "";

	public void addSubscriberToDB() {

		setOutput("");

		if (getEmailAddress().contains("@")) {

			if (!getEmailAdresses().contains(getEmailAddress())) {

				try {

					setEmailAdresses(getEmailAdresses() + "," + getEmailAddress());

					InputStream result_input_stream = db.find("subscribers");
					JSONObject obj = new JSONObject(convertStreamToString(result_input_stream));

					String _rev = obj.getString("_rev");

					Map<String, Object> h = new HashMap<String, Object>();
					h.put("_id", "subscribers");
					h.put("subscribers", emailAddresses);
					h.put("_rev", _rev);

					db.update(h);

					setEmailAddress("");
					
			        setOutput("Herzlichen Dank für Ihre Anmeldung !");
			        

				} catch (Exception e) {

					System.out.println("Error adding subscriber in Cloudant ! " + emailAddress);
					e.printStackTrace();

				}

			} else {

				
				setEmailAddress("");
				
				setOutput("Sie haben unseren  Newsletter schon abonniert!");

			}

		} else {

			setEmailAddress("");
			
			setOutput("Bitte Ihre e-mail Adresse eintragen.");


		}

	}

	public Subscribe() {

		initDB();
		setEmailAdresses(getSubscribersFromDB());

	}

	public boolean initDB() {

		try {
			CloudantClient client = ClientBuilder
					.url(new URL("https://40f49618-e45a-45d8-9110-1d264e6e2766-bluemix.cloudant.com"))
					.username("40f49618-e45a-45d8-9110-1d264e6e2766-bluemix")
					.password("fc5d08660956c8ada867c0aaf7d237eb3ad76d89d3bd87b34fa110a011080c39").build();

			db = client.database("friseur_blaues_hexagon", false);

			return true;

		} catch (Exception e) {
			System.out.println("Error initalizing Cloudant connection ! ");
			e.printStackTrace();
			return false;
		}

	}

	public String getSubscribersFromDB() {

		try {

			InputStream result_input_stream = db.find("subscribers");
			JSONObject obj = new JSONObject(convertStreamToString(result_input_stream));

			return obj.getString("subscribers").toLowerCase();

		} catch (Exception e) {

			System.out.println("Error getting subscribers from Cloudant ! ");
			return null;

		}

	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress.toLowerCase();
	}

	public String getEmailAdresses() {
		return emailAddresses;
	}

	public void setEmailAdresses(String emailAddresses) {
		this.emailAddresses = emailAddresses;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String convertStreamToString(java.io.InputStream is) {
		@SuppressWarnings("resource")
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

}
