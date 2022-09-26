package com.automation.irrigationsystem.common;

import com.automation.irrigationsystem.dataclass.ScheduleFailEmailModel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Session;
import javax.mail.Transport;

@Slf4j
@NoArgsConstructor
public class SendEmailUtility {

    public void triggerEmail(ScheduleFailEmailModel model) {

        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();

        // Setting up mail server
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // creating session object to get properties
        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(model.getEmailFrom(), model.getEmailPdw());
            }
        });

        try {
            // MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From Field: adding senders email to from field.
            message.setFrom(new InternetAddress(model.getEmailFrom()));

            // Set To Field: adding recipient's email to from field.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(model.getEmailTo()));

            // Set Subject: subject of the email
            message.setSubject(model.getEmailSubject());

            // set body of the email.
            message.setText(model.getEmailBody());

            // Send email.
            Transport.send(message);
            log.info("Mail successfully sent");
        }
        catch (MessagingException mex) {
            log.debug("MessagingException in sending the email error message is: {}", mex.getMessage(), mex);
        }catch (Exception e) {
            log.debug("Error in sending the email, error message is: {}", e.getMessage(), e);
        }
    }
}
