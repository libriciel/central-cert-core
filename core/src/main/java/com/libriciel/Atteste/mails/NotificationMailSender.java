package com.libriciel.Atteste.mails;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;
import com.libriciel.Atteste.BDD.mails.Mail;
import com.libriciel.Atteste.BDD.notifs.Notification;

@Service
public class NotificationMailSender {

	public void send(Mail m, Notification n) {
        Properties properties = new Properties();
        InputStream input = null;
		
		try {
			input = new FileInputStream("src/main/resources/application.properties");
			properties.load(input);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	    Session session = Session.getDefaultInstance(properties);

	    try {
	       MimeMessage message = new MimeMessage(session);

	       message.setFrom(new InternetAddress("atteste@libriciel.coop"));

	       message.addRecipient(Message.RecipientType.TO, new InternetAddress(m.getAdresse()));

	       message.setSubject(n.getObjet());

	       message.setText(n.getMessage());

	       Transport.send(message);
	       System.out.println("Sent message successfully....");
	    } catch (MessagingException e) {
	       e.printStackTrace();
	    }
	}

}
