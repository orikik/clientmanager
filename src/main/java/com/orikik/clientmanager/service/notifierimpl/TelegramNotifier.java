package com.orikik.clientmanager.service.notifierimpl;

import com.orikik.clientmanager.bot.TelegramBot;
import com.orikik.clientmanager.dto.UserDto;
import com.orikik.clientmanager.service.NotifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("telegramNotifier")
public class TelegramNotifier implements NotifierService {
    @Autowired
    private TelegramBot telegramBot;

    @Override
    public void notifyUser(UserDto user, String header, String message) {
        telegramBot.sendMessage(message, user.getTelegramId());
    }
}
