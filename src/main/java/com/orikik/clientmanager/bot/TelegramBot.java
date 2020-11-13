package com.orikik.clientmanager.bot;

import com.orikik.clientmanager.entity.UserEntity;
import com.orikik.clientmanager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Transactional
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private static final Logger LOG = LoggerFactory.getLogger(TelegramBot.class);
    @Autowired
    UserRepository userRepository;

    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if (messageText.equals("/start")) {
                sendMessage("Привет! Введите username для регистрации", chatId);
                return;
            }
            Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(messageText);
            if (optionalUserEntity.isPresent()) {
                UserEntity userEntity = optionalUserEntity.get();
                userEntity.setTelegramId(chatId);
                userRepository.save(userEntity);
                sendMessage("Поздравляю! Вы зарегестрированы!", chatId);
            } else {
                sendMessage("Пользователя с таким username не существует, попробуйте ввести снова", chatId);
            }
        }
    }


    public void sendMessage(String messageText, long chatId) {
        SendMessage message = SendMessage
                .builder()
                .chatId(Long.toString(chatId))
                .text(messageText)
                .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            LOG.error("client with chatId ={} have a problem", chatId, e);
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
