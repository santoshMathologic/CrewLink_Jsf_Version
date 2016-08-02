package com.crew.util;
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



@ViewScoped
@ManagedBean(name="sendjavamail",eager=true)
public class SendJavaMail implements Serializable {
	private static final long serialVersionUID = 1L;
	public void sendTemplateEmail(String passkey,String userAccname,String firstname,String customermailId)throws AddressException, MessagingException {

        Properties props = new Properties();  
         //props.put("mail.smtp.host", "smtp.gmail.com"); 
       //  props.put("mail.smtp.host", "mail.netberry.in");
          
         props.put("mail.smtp.host", "smtp.netberry.in");
        
        props.put("mail.smtp.auth", "true");  
        props.put("mail.debug", "true");  
        props.put("mail.smtp.port",587);  
        props.put("mail.smtp.socketFactory.port",110);  
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.transport.protocol", "smtp");
        Session mailSession = null;

        mailSession = Session.getInstance(props,  
                new javax.mail.Authenticator() {  
            protected PasswordAuthentication getPasswordAuthentication() {  
                return new PasswordAuthentication("demo@netberry.in", "Demo@123");
            	//return new PasswordAuthentication("santosh.citech@gmail.com", "1cd09mca13");
            }  
        });  


        try {

            Transport transport = mailSession.getTransport();
            MimeMessage message = new MimeMessage(mailSession);
            message.setSubject("Mathologic Technology private Limited: Open an Account Confirmation Mail");
            message.setFrom(new InternetAddress("demo@netberry.in"));
            //String []to = new String[]{"santosh.citech@gmail.com","santosh_citech@live.in"};
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(customermailId));
            //String body = "Dear Customer,This is  Your mail Confirmation Code please Enter the same in Confirmation code on Your SignUp ";
            
            //StringBuffer strbuffer = new StringBuffer(body);
            //strbuffer.append(uniqueId);
            
            message.setSentDate(new Date());
            //message.setContent(body,"text/html");
            
            String messages = "<table  border=1 width=800 height=50><tr><td><i>Greetings from Mathologic Technologies Pvt Limited !</i></tr></td><br>";
            messages += "<b>Dear "+firstname+",</b><br><br>";
            messages += "<b>Please Login by clicking the link below,using the username and password provided,to activate your account.</b><br>";
            messages+="Your user name is :<h5 style='color:blue'>"+userAccname+"</h5><br>Your password is : <h5 style='color:blue'> "+passkey+"</h5><br>http://localhost:8080/CrewLink/activate.xhtml?userguid=4dbb9759-2c20-4cca-8168-9765fe1ce683</br>  <br/><br/>Warm Regards, <br/>Team Mathologic</table>";
            message.setContent(messages,"text/html" ); 
            transport.connect();
            transport.sendMessage(message,message.getRecipients(Message.RecipientType.TO));
            transport.close();
        } catch (Exception exception) {

        }
    }
	
	
	

}
