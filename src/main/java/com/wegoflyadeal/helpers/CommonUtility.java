package com.wegoflyadeal.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class CommonUtility {
	
	public static void sendMailTesting(String from, String to, String subject, String text)
	{
		Session session = null;
		try {
			Properties props = System.getProperties();
			try {
				props.load(new FileInputStream(new File("src/main/resources/email.properties")));
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}

			session = Session.getDefaultInstance(props, 
					new javax.mail.Authenticator(){
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(
							"automation@rehlat.com", "HyD$0202");
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			message.setSubject(subject);
			message.setText(text);
			Transport.send(message);
			System.out.println("Sent mail successfully");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
	
	public static void sendMail(String from, String to, String subject, String text)
	{
		Session session = null;
		try {
			Properties props = System.getProperties();
			try {
				props.load(new FileInputStream(new File("src/main/resources/email.properties")));
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}

			session = Session.getDefaultInstance(props, 
					new javax.mail.Authenticator(){
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(
							"automation@rehlat.com", "HyD$0202");
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("veerendra.parvataneni@rehlat.com"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("rajendra.purohit@rehlat.com"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("qainterns@rehlat.com"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("pricing@rehlat.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("madduru.chaitanya@rehlat.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("vijayasai.ganta@rehlat.com"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("anoop.gundavarapu@rehlat.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("shaibaz.syed@rehlat.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("battu.gopichandu@rehlat.com"));
			message.setSubject(subject);
			message.setText(text);
			Transport.send(message);
			System.out.println("Sent mail successfully");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	
	public static void sendMailHtml(String from, String to, String subject, String text)
	{
		Session session = null;
		try {
			Properties props = System.getProperties();
			try {
				props.load(new FileInputStream(new File("src/main/resources/email.properties")));
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}

			session = Session.getDefaultInstance(props, 
					new javax.mail.Authenticator(){
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(
							"automation@rehlat.com", "HyD$0202");
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		//	message.addRecipient(Message.RecipientType.TO, new InternetAddress("veerendra.parvataneni@rehlat.com"));
		//	message.addRecipient(Message.RecipientType.TO, new InternetAddress("rajendra.purohit@rehlat.com"));
		//	message.addRecipient(Message.RecipientType.TO, new InternetAddress("anil.gaddipati@rehlat.com"));
		//	message.addRecipient(Message.RecipientType.TO, new InternetAddress("qainterns@rehlat.com"));
			message.setSubject(subject);
			//message.setText(text);
			 message.setContent(text,"text/html" );  
			Transport.send(message);
			System.out.println("Sent mail successfully");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	
	public static void sendMailWithAtachments(String from, String to, String subject, String text)
	{
		Session session = null;
		try {
			Properties props = System.getProperties();
			try {
				props.load(new FileInputStream(new File("src/main/resources/email.properties")));
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}

			session = Session.getDefaultInstance(props, 
					new javax.mail.Authenticator(){
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(
							"automation@rehlat.com", "HyD$0202");
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		//	message.addRecipient(Message.RecipientType.TO, new InternetAddress("veerendra.parvataneni@rehlat.com"));
			message.setSubject(subject);
			message.setText(text);

			 BodyPart messageBodyPart = new MimeBodyPart();
	         messageBodyPart.setText("This is message sent by Gopi");
			Multipart multipart = new MimeMultipart();
	         multipart.addBodyPart(messageBodyPart);
	         messageBodyPart = new MimeBodyPart();
	         String filename = "src/main/resources/email.properties";
	         DataSource source = new FileDataSource(filename);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(filename);
	         multipart.addBodyPart(messageBodyPart);
	         message.setContent(multipart);
			
			Transport.send(message);
			System.out.println("Sent mail successfully");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}


}
