package uz.mobiler.examplebot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.mobiler.examplebot.entity.ImageEntity;
import uz.mobiler.examplebot.repository.ImageRepository;
import uz.mobiler.examplebot.service.ImageService;

import java.util.List;

@Component
public class ExampleBot extends TelegramLongPollingBot {
    @Value("${bot.token}")
    private String botToken;
    @Value("${bot.username}")
    private String botUsername;

    @Autowired
    private ImageService imageService;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {
                Message message = update.getMessage();
                if (message.hasText()) {
                    String text = message.getText();
                    if (text.equals("/start")) {
                        execute(imageService.welcome(update));
                    }
                }
            } else if (update.hasCallbackQuery()) {
                String data = update.getCallbackQuery().getData();
                if (data.startsWith("Image#")) {
                    execute(imageService.edit(update));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
