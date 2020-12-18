package com.su.mailsend.service;

import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Date;
import java.util.Properties;

/**
 * @program: program
 * @Date: 2020/12/16 19:26
 * @Author: Mr.SU
 * @Description:
 */
public class MailStmpSend {

    /**
     * @Description: 网易163邮箱，发送邮件
     * @param: "[to, text, title]"
     * @Return: boolean
     * @Author: supenghui
     * @Date: 2020/12/16 16:54
     */
    private void sendMail(String to, String text, String title) throws Exception {

        String from = "xxx@163.com"; // 发件人邮箱地址
        String user = "xxx@163.com"; // 发件人称号，同邮箱地址
        String password = "VHXCLTZDEOFFWNSA"; // 发件人邮箱客户端授权码

        // 一、创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "smtp.163.com"); // 设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
        props.put("mail.smtp.host", "smtp.163.com");
        props.put("mail.smtp.auth", "true"); // 需要经过授权，也就是用户名和密码的校验，这样才能通过验证（一定要有这一条）

        // 二、根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true); // true 在控制台（console)上看到发送邮件的过程

        // 三、 创建一封复杂邮件（文本+附件）

        try {
            // 3.1. 创建邮件对象
            MimeMessage message = new MimeMessage(session); // 加载发件人地址

            // 3.2. From: 发件人
            message.setFrom(new InternetAddress(from));

            // 3.3. To: 收件人（可以增加多个收件人）
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to)); // 加载收件人地址

            // 3.4. To: 收件人（可以增加多个抄送）
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(to)); // 加载抄件人地址

            // 3.5. Subject: 邮件主题
            message.setSubject(title); // 加载标题

            // 3.6. 邮件内容
            MimeMultipart multipart = new MimeMultipart(); // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            MimeBodyPart contentPart = new MimeBodyPart(); // 设置邮件的文本内容
            contentPart.setContent(text, "text/html;charset=utf-8");
            multipart.addBodyPart(contentPart);

            // 3.7. 邮件附件
            String attPath = "D:\\data\\ftpUpload\\2020\\12\\10\\盖章指令文件.zip";
            MimeBodyPart attachment = new MimeBodyPart();
            DataHandler dh = new DataHandler(new FileDataSource(attPath)); // 读取本地文件
            attachment.setDataHandler(dh); // 将附件数据添加到“节点”
            attachment.setFileName(MimeUtility.encodeText(dh.getName())); // 设置附件的文件名（需要编码）
            multipart.addBodyPart(attachment);

            multipart.setSubType("mixed"); // 混合关系

            // 3.8. 设置整个邮件的关系（将最终的混合“节点”作为邮件的内容添加到邮件对象）
            message.setContent(multipart);

            // 3.9. 设置发件时间
            message.setSentDate(new Date());

            // 3.10. 保存上面的所有设置
            message.saveChanges(); // 保存变化

            // 四、 根据 Session 获取邮件传输对象
            Transport transport = session.getTransport("smtp");

            // 五、 使用 邮箱账号 和 授权码 连接邮件服务器
            transport.connect("smtp.163.com", user, password);

            // 六、 发送邮件,
            transport.sendMessage(message, message.getAllRecipients());

            // 七、 关闭连接
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 网易企业163邮箱发送邮件
     * @param: "[]"
     * @Return: void
     * @Author: supenghui
     * @Date: 2020/12/17 13:55
     */
    public void sendHtmlMail() {
        try {

            String from = "职业年金受托";//发件人昵称展示
//            String[] to = {"xxx@aaa.com.cn", "xxx@aaa.com.cn"};//接收邮箱
            String to = "xxx@163.com";//接收邮箱
//            String[] copy = {"xxx@aaa.com.cn", "xxx@aaa.com.cn"};//抄送邮箱
            String copy = "xxx@163.com";//抄送邮箱
            String subject = "测试邮件";//邮件主题
            String text = "你好，这是一封测试邮件，无需回复。";
            String host = "smtphz.qiye.163.com";//163企业邮箱smtp
            String username = "xxx@su-abc.com.cn";//企业邮箱 @后面是域名
            String password = "123456";//企业邮箱密码

            // 一、创建参数配置, 用于连接邮件服务器的参数配置
            Properties prop = new Properties();
            prop.setProperty("mail.smtp.auth", "true"); // 需要经过授权，也就是用户名和密码的校验，这样才能通过验证（一定要有这一条）
            prop.setProperty("mail.smtp.timeout", "994"); // 加密端口(ssl)  可通过 https://qiye.163.com/help/client-profile.html 进行查询

            MailSSLSocketFactory sf = new MailSSLSocketFactory();// SSL加密
            sf.setTrustAllHosts(true); // 设置信任所有的主机
            prop.put("mail.smtp.ssl.enable", "true");
            prop.put("mail.smtp.ssl.socketFactory", sf);

            //二、创建一封复杂邮件（文本+附件）
            JavaMailSenderImpl javaMailSend = new JavaMailSenderImpl();

            // 3.1. 创建邮件对象
            MimeMessage message = javaMailSend.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");

            // 3.2. From: 发件人
            String nick = MimeUtility.encodeText(from);//设置昵称
            messageHelper.setFrom(new InternetAddress(nick + " <" + username + ">"));// 邮件发送者

            // 3.3. To: 收件人（可以增加多个收件人）
            messageHelper.setTo(to);  //收件人

            // 3.4. To: 收件人（可以增加多个抄送）
            messageHelper.setCc(copy); //抄送人

            // 3.5. Subject: 邮件主题
            messageHelper.setSubject(subject); //邮件标题

            // 3.6. 邮件内容
            messageHelper.setText(text, true); //文本中，如需换行，要用<br>，不能用\n换行

            // 3.7. 邮件附件
            File file = new File("D:\\data\\ftpUpload\\2020\\12\\10\\盖章指令文件.zip");
            messageHelper.addAttachment(MimeUtility.encodeWord(file.getName()), file);
            // 3.8. 设置邮件服务器登录信息
            javaMailSend.setHost(host);
            javaMailSend.setUsername(username);
            javaMailSend.setPassword(password);
            javaMailSend.setJavaMailProperties(prop);

            // 六、 发送邮件,
            javaMailSend.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            MailStmpSend ds = new MailStmpSend();
//          ds.sendMail("xxx@qq.com", "你好，这是一封测试邮件，无需回复。", "测试邮件");
//          ds.sendMail("xxxx@163.com", "你好，这是一封测试邮件，无需回复。", "测试邮件");
            ds.sendHtmlMail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
