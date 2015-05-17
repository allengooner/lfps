package com.logicmonitor.util;

/**
 * Created by allen.gl on 2015/5/16.
 */
public abstract class Emailer {

//    public static void sendEmail(String from, String to, String subject, String text) {
//        // Get system properties
//        Properties properties = System.getProperties();
//
//        // Setup mail server
//        properties.setProperty("mail.smtp.host", "localhost");
//
//        // Get the default Session object.
//        Session session = Session.getDefaultInstance(properties);
//
//        // html email
//        try{
//            // Create a default MimeMessage object.
//            MimeMessage message = new MimeMessage(session);
//
//            // Set From: header field of the header.
//            message.setFrom(new InternetAddress(from));
//
//            // Set To: header field of the header.
//            message.addRecipient(Message.RecipientType.TO,
//                    new InternetAddress(to));
//
//            // Set Subject: header field
//            message.setSubject("This is the Subject Line!");
//
//            // Send the actual HTML message, as big as you like
//            message.setContent("<h1>This is actual message</h1>",
//                    "text/html" );
//
//            // Create the message part
//            BodyPart messageBodyPart = new MimeBodyPart();
//
//            // Fill the message
//            messageBodyPart.setText("This is message body");
//
//            // Create a multipar message
//            Multipart multipart = new MimeMultipart();
//
//            // Set text message part
//            multipart.addBodyPart(messageBodyPart);
//
//            // Part two is attachment
//            messageBodyPart = new MimeBodyPart();
//            String filename = "file.txt";
//            DataSource source = new FileDataSource(filename);
//            messageBodyPart.setDataHandler(new DataHandler(source));
//            messageBodyPart.setFileName(filename);
//            multipart.addBodyPart(messageBodyPart);
//
//            // Send the complete message parts
//            message.setContent(multipart );
//
//            // Send message
//            Transport.send(message);
//            System.out.println("Sent message successfully....");
//        }catch (MessagingException mex) {
//            mex.printStackTrace();
//        }
//    }
}
