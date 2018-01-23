package callforproject;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import fr.aeris.rest.services.callforproject.CallForProject;
import fr.sedoo.commons.mail.Email;
import fr.sedoo.commons.mail.EmailSenderConfig;
import fr.sedoo.commons.mail.EmailUtils;

public class CallForProjectTest {

	@Test
	public void generateJsonFromObject() {
		CallForProject callForProject = new CallForProject();
		
		callForProject.setApplicantName("marina");
		callForProject.setApplicantLaboratory("cnrs");
		callForProject.setApplicantEmail("marina@obs");
		callForProject.setComments("commentaires");
		callForProject.setComputingResources("je ne sais pas");
		callForProject.setContactsAsString("contact chaine");
		callForProject.setContext("contexte du projet");
		callForProject.setDescription("descr du projet");
		callForProject.setDirectorEmail("director@obs");
		callForProject.setDirectorName("nom directeur");
		Date today = new Date();
		today.setHours(0); 
		today.setMinutes(0);
		today.setSeconds(0);
		callForProject.setDueDate(today.toLocaleString());
		callForProject.setDueDateJustification("parce que");
		callForProject.setHumanResources("moi");
		callForProject.setId(Long.parseLong("123456789"));
		
		ObjectMapper mapper = new ObjectMapper();
		

		//Object to JSON in file
		try {
			mapper.writeValue(new File("/home/mrouille/Documents/servicesRestAeris/file.json"), callForProject);
			//Object to JSON in String
			String jsonInString = mapper.writeValueAsString(callForProject);
			System.out.println(jsonInString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Test
	public void generateObjectFromJson() {
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = "{\"id\":123456789,\"name\":null,\"context\":\"contexte du projet\",\"comments\":\"commentaires\",\"status\":null,\"description\":\"descr du projet\",\"support\":null,\"use\":null,\"submissionDate\":null,\"dueDate\":\"01-01-2019\",\"dueDateJustification\":\"parce que\",\"contactsAsString\":\"contact chaine\",\"humanResources\":\"moi\",\"computingResources\":\"je ne sais pas\",\"directorEmail\":\"director@obs\",\"directorName\":\"nom directeur\",\"applicantEmail\":\"marina@obs\",\"applicantName\":null,\"applicantLaboratory\":\"cnrs\",\"responsibles\":null}";

		try {
			//JSON from file to Object
			CallForProject callForProjectFromFile = mapper.readValue(new File("/home/mrouille/Documents/servicesRestAeris/file.json"), CallForProject.class);
			System.out.println("name: " + callForProjectFromFile.getApplicantName());

			//JSON from String to Object
			CallForProject callForProjectFromString = mapper.readValue(jsonInString, CallForProject.class);
			System.out.println("name: " + callForProjectFromString.getApplicantName());
			
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	public void envoyerMail() {
		Email email = new Email() {
			
			@Override
			public String getSubject() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<String> getRecipients() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getContent() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<String> getCc() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<String> getBcc() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		String emailServer = null;
		String emailLogin = null;
		String emailPassword = null;
		String emailProtocol = null;
		String sslEnabled = null;
		String emailPort = null;
		String emailReplyTo = null;
		EmailSenderConfig config = new EmailSenderConfig(emailServer, emailLogin, emailPassword, emailProtocol, sslEnabled, emailPort, emailReplyTo);
		try {
			EmailUtils.sendEmail(email, config);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
