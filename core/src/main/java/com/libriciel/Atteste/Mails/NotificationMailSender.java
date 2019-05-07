package com.libriciel.Atteste.Mails;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;
import com.libriciel.Atteste.BDD.mails.Mail;

@Component
public class NotificationMailSender {
	
	public void send(Notification n, Mail m) {
        Properties properties = new Properties();

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
