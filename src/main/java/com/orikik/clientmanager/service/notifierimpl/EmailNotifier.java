package com.orikik.clientmanager.service.notifierimpl;

import com.orikik.clientmanager.dto.UserDto;
import com.orikik.clientmanager.service.NotifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service("emailNotifier")
public class EmailNotifier implements NotifierService {
    @Value("${spring.mail.username}")
    private String email;
    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void notifyUser(UserDto user, String header, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(email);
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject(header);
        simpleMailMessage.setText(message);
        emailSender.send(simpleMailMessage);
    }
}
