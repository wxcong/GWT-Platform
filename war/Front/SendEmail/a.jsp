<%@ page contentType="text/html; charset=gb2312" language="java" errorPage="" %>
<%@ page import="java.sql.*,java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.*,javax.mail.*"%>
<%@ page import="javax.mail.internet.*"%>
<%
String qm ="123456apple";
String tu = "gmail.com";
String tto= "containermailtest@gmail.com";
String ttitle="abc";
String tcontent="ABC";
Properties props=new Properties();
props.put("mail.smtp.host","smtp."+tu);
props.put("mail.smtp.auth","true");
Session s=Session.getInstance(props);
s.setDebug(true);
 
MimeMessage message=new MimeMessage(s);

InternetAddress from=new InternetAddress("jeffcontainertest@"+tu);
message.setFrom(from);
InternetAddress to=new InternetAddress(tto);
message.setRecipient(Message.RecipientType.TO,to);
message.setSubject(ttitle);
message.setSentDate(new Date());
 
 
BodyPart mdp=new MimeBodyPart();
mdp.setContent(tcontent,"text/html;charset=gb2312");
Multipart mm=new MimeMultipart();
mm.addBodyPart(mdp);
message.setContent(mm);
 
message.saveChanges();
Transport transport=s.getTransport("smtp");
transport.connect("smtp."+tu,"jeffcontainertest",qm);
transport.sendMessage(message,message.getAllRecipients());
transport.close();
%>