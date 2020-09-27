package com.tiny.services;

import com.tiny.entities.User;
import com.tiny.entities.VerificationToken;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class EmailService {

    private JavaMailSenderImpl mailSender;
    private TemplateEngine templateEngine;
    private VerificationTokenService verificationTokenService;

    public EmailService(
            JavaMailSenderImpl mailSender,
            TemplateEngine templateEngine,
            VerificationTokenService verificationTokenService
    ) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.verificationTokenService = verificationTokenService;
    }

    public void sendVerificationEmail(User user) throws MessagingException {
        VerificationToken verificationToken = verificationTokenService.findByUser(user);
        if( verificationToken != null ){
            String token = verificationToken.getToken();

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name()
            );

            Context context = new Context();
            context.setVariable("username", user.getUsername());
            context.setVariable("link", "http://localhost:8081/account/verify?token="+token);
            String html = templateEngine.process("verification", context);

            ClassPathResource image = new ClassPathResource("static/cloud.png");
            helper.addInline("cloud", image);
            helper.setTo(user.getEmail());
            helper.setSubject("Email address verification");
            helper.setText(html, true);

            mailSender.send(mimeMessage);
        }
    }

}
