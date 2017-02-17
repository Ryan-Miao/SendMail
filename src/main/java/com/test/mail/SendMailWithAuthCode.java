package com.test.mail;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ryan on 2017/2/17/017.
 */
public class SendMailWithAuthCode {

    public static final String username = "miaorf1";
    public static  String auth_code = "shouquan";
    public static final String from_address = username+"@163.com";
    public static final String to_address = "1060388212@qq.com";
    public static final String SMTP_HOST = "smtp.163.com";

    public static void main(String[] args) throws Exception {
        if (args.length>0){
            auth_code = args[0];
            System.out.println("使用授权码为："+auth_code);
        }
        //config authorization
        Session session = getSession();
        //compose message
        Message message = getMessage(session);
        //send
        Transport.send(message);
        System.out.println("邮件发送成功，发送者："+from_address+", 接受者："+to_address);
    }

    /**
     * compose message, include from_address, to_address, attachment
     * @param session
     * @return message
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    private static Message getMessage(Session session) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        // from address
        Address from = new InternetAddress(from_address);
        message.setFrom(from);
        // check to_address
        List<String> toAddressList = Arrays.asList(to_address);
        String toAddress = checkAddressAndComposeToAddress(toAddressList);

        if (!toAddress.isEmpty()) {
            // to address
            Address[] to = InternetAddress.parse(toAddress);
            message.setRecipients(Message.RecipientType.TO, to);
            // subject
             message.setSubject("java邮件测试");
            // content container
            MimeMultipart mimeMultiPart = new MimeMultipart();
            // content body
            BodyPart bodyPart = composeContent();
            mimeMultiPart.addBodyPart(bodyPart);

            // add attachment
//            String resource = SendMailWithAuthCode.class.getClassLoader().getResource("test.txt").getPath();
//            System.out.println("文件path:"+resource);
//            List<String> fileAddressList = Arrays.asList(resource);
//            BodyPart attachment = composeAttachment(fileAddressList);
//            mimeMultiPart.addBodyPart(attachment);

            message.setContent(mimeMultiPart);
            message.setSentDate(new Date());
            // save
            message.saveChanges();

            return message;
        }
        return message;
    }

    /**
     * compose content
     * @return BodyPart
     * @throws MessagingException
     */
    private static BodyPart composeContent() throws MessagingException {
        BodyPart bodyPart = new MimeBodyPart();
        // compose the real content to send
        Properties props=System.getProperties(); //系统属性
        String str = "<p>第一次发邮件，成功！</p>  <p>系统用户:%s , 用户目录: %s , 当前工作目录： %s</p>";
        String htmlText = String.format(
                str, props.getProperty("user.name"),
                props.getProperty("user.home"),
                props.getProperty("user.dir"));
        bodyPart.setContent(htmlText, "text/html;charset=utf-8");
        return bodyPart;
    }

    /**
     * compose attachment
     * @param fileAddressList  The file path list
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    private static BodyPart composeAttachment(List<String> fileAddressList)
            throws MessagingException, UnsupportedEncodingException {
        BodyPart attchPart = null;
        if (fileAddressList != null) {
            for (int i = 0; i < fileAddressList.size(); i++) {
                if (!fileAddressList.get(i).isEmpty()) {
                    attchPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(fileAddressList.get(i));
                    attchPart.setDataHandler(new DataHandler(source));
                    attchPart.setFileName(MimeUtility.encodeText(source.getName()));
                }
            }
        }

        return attchPart;
    }

    private static String checkAddressAndComposeToAddress(List<String> toAddressList) {
        StringBuffer buffer = new StringBuffer();
        if (!toAddressList.isEmpty()) {
            String regEx = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern p = Pattern.compile(regEx);
            for (int i = 0; i < toAddressList.size(); i++) {
                Matcher match = p.matcher(toAddressList.get(i));
                if (match.matches()) {
                    buffer.append(toAddressList.get(i));
                    if (i < toAddressList.size() - 1) {
                        buffer.append(",");
                    }
                }
            }
        }
        return buffer.toString();
    }

    private static Session getSession() throws GeneralSecurityException {
        Properties pro = new Properties();
        pro.put("mail.smtp.host", SMTP_HOST);
        pro.put("mail.smtp.auth", "true");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        //just ignore CA
        sf.setTrustAllHosts(true);
        pro.put("mail.smtp.ssl.enable", "true");
        pro.put("mail.smtp.ssl.socketFactory", sf);
        //username is the username of 163, @163 should exclude
        //password is the authorization code, you can get it from 163's setting
        MailAuthenticator authenticator = new MailAuthenticator(username, auth_code);
        return Session.getInstance(pro, authenticator);
    }

}
