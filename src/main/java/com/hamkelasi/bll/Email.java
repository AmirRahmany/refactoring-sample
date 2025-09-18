package com.hamkelasi.bll;
import javax.mail.*;
import javax.mail.Message;
import javax.mail.internet.*;
import java.util.Properties;

public class Email {
    private String to;
    private String subject;
    private String text;
    private final String adminEmail;

    // Getters and Setters
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public Email() {
        this.adminEmail = EmailSettings.getDefault().getAdminEmail();
    }

    public int send() {
        Validation validation = new Validation();

        if (validation.emailValidator(this.to)) {
            try {
                String fromEmail = EmailSettings.getDefault().getEmailAddress();
                String host = EmailSettings.getDefault().getEmailHost();
                int port = Integer.parseInt(EmailSettings.getDefault().getEmailPort());

                // Set up mail server properties
                Properties props = new Properties();
                props.put("mail.smtp.host", host);
                props.put("mail.smtp.port", String.valueOf(port));

                // Create a mail session
                Session session = Session.getInstance(props, null);
                MimeMessage message = new MimeMessage(session);

                // Set message attributes
                message.setFrom(new InternetAddress(fromEmail, "وب سایت همکلاسی"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.to));
                message.setSubject(this.subject);
                message.setText(this.text); // Plain text, not HTML

                // Send the message
                Transport.send(message);

                return 0; // ایمیل با موفقیت ارسال شد
            } catch (Exception e) {
                return 9; // اشکال در ارسال
            }
        } else {
            return 1; // فرمت ایمیل اشتباه است
        }
    }
}