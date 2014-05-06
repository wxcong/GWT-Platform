package org.server.servlet;
 
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
 
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
@SuppressWarnings("serial")
public class SendEmailServlet extends HttpServlet {
 
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        InputStream is = req.getInputStream();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int r = 0;
        while(r >= 0) {
          r = is.read(buf);
          if(r >= 0) {
            os.write(buf, 0, r);
          }         
        }
        String s = new String(os.toByteArray(), "UTF-8");
        //String decoded = URLDecoder.decode(s, "UTF-8");

        //resp.setContentType("text/plain");
        //resp.getWriter().println(decoded);
        //resp.getWriter().println(s);
        
        String[] params = s.split("\"");
    
        String subject = params[3];
        String content = params[7];
        String email = params[11];
        String name = params[15];
 
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        boolean error = false;
 
        String msgBody = name + "\n" + content;
        
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("wxcnyu2014@gmail.com", "andy"));
            msg.addRecipient(Message.RecipientType.TO, 
            		new InternetAddress(email, "Dear Sir or Madam"));
            msg.setSubject(subject);
            msg.setText(msgBody);
            Transport.send(msg);
        } catch (Exception e) {
            resp.setContentType("text/plain");
            resp.getWriter().println("Something went wrong. Please try again.");
            resp.getWriter().println(subject + "&" + content + "&" + email + "&" + name);
            error = true;
        }

        
        if(!error) {
          resp.setContentType("text/plain");
          resp.getWriter().println(
                "Thank you for your feedback. An Email has been send out.");
        }
 
    }
}