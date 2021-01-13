package uz.mobiler.examplebot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.mobiler.examplebot.entity.ImageEntity;
import uz.mobiler.examplebot.repository.ImageRepository;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    ImageRepository imageRepository;

    @Override
    public SendPhoto welcome(Update update) {

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(update.getMessage().getChatId().toString());
        sendPhoto.setCaption("Bla bla");

        List<ImageEntity> imageEntityList = imageRepository.findAll();
        try {
            InputStream is = new URL(imageEntityList.get(0).getImg_url()).openStream();
            InputFile inputFile = new InputFile();
            inputFile.setMedia(is, "example.jpg");
            sendPhoto.setPhoto(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inList = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        inList.add(inlineKeyboardButtons);

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Next");
        inlineKeyboardButton2.setText("Prev");
        inlineKeyboardButton1.setCallbackData("Image#" + 2);
        inlineKeyboardButton2.setCallbackData("Image#" + 0);
        inlineKeyboardButtons.add(inlineKeyboardButton1);
        inlineKeyboardButtons.add(inlineKeyboardButton2);
        inlineKeyboardMarkup.setKeyboard(inList);
        sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
        return sendPhoto;
    }

    @Override
    public EditMessageMedia edit(Update update) {
        EditMessageMedia editMessageMedia = new EditMessageMedia();
        editMessageMedia.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
        editMessageMedia.setInlineMessageId(update.getCallbackQuery().getInlineMessageId());
        editMessageMedia.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        InputMediaPhoto inputMediaPhoto = new InputMediaPhoto();
        inputMediaPhoto.setCaption("Bla bla");
        String data = update.getCallbackQuery().getData();
        int i = data.indexOf("#");

        String index = data.substring(i + 1);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inList = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        inList.add(inlineKeyboardButtons);
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Next");
        inlineKeyboardButton2.setText("Prev");
        inlineKeyboardButton1.setCallbackData("Image#" + (Integer.parseInt(index) + 1));
        inlineKeyboardButton2.setCallbackData("Image#" + (Integer.parseInt(index) - 1));
        inlineKeyboardButtons.add(inlineKeyboardButton1);
        inlineKeyboardButtons.add(inlineKeyboardButton2);
        inlineKeyboardMarkup.setKeyboard(inList);
        editMessageMedia.setReplyMarkup(inlineKeyboardMarkup);
        Optional<ImageEntity> optionalImageEntity = imageRepository.findById(Integer.parseInt(index));
        if (optionalImageEntity.isPresent()) {
            ImageEntity imageEntity = optionalImageEntity.get();
            try {
                InputStream is = new URL(imageEntity.getImg_url()).openStream();
                inputMediaPhoto.setMedia(is, "example.jpg");
                editMessageMedia.setMedia(inputMediaPhoto);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return editMessageMedia;
    }
}
