package kz.kaznu.smartbot.utils;

import kz.kaznu.smartbot.models.entities.Item;
import kz.kaznu.smartbot.models.entities.Order;
import kz.kaznu.smartbot.models.entities.Photo;
import kz.kaznu.smartbot.models.enums.CallbackData;
import kz.kaznu.smartbot.models.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Keyboards {

    private final Properties properties;

    public ReplyKeyboard getNButtons(List<String> texts) {
        ReplyKeyboardMarkup replyKeyboardMarkup = createReplyKeyboard();
        List<KeyboardRow> listRows = new ArrayList<>();
        for (String text : texts) {
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add(new KeyboardButton(text));
            listRows.add(keyboardRow);
        }
        replyKeyboardMarkup.setKeyboard(listRows);
        return replyKeyboardMarkup;
    }


    public ReplyKeyboard getNButtons(String... texts) {
        return getNButtons(Arrays.stream(texts).collect(Collectors.toList()));
    }


    public ReplyKeyboard getMenuButtons(boolean isAuthorized) {
        ReplyKeyboardMarkup replyKeyboardMarkup = createReplyKeyboard();

        List<KeyboardRow> listRows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(isAuthorized ? new KeyboardButton(properties.getProperty("bot.buttons.logout")) :
                new KeyboardButton(properties.getProperty("bot.buttons.login")));
        listRows.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(properties.getProperty("bot.buttons.items")));
        row2.add(new KeyboardButton(properties.getProperty("bot.buttons.myOrders")));
        listRows.add(row2);

        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton(properties.getProperty("bot.buttons.favorites")));
        row3.add(new KeyboardButton(properties.getProperty("bot.buttons.cart")));
        listRows.add(row3);

        KeyboardRow row4 = new KeyboardRow();
        row4.add(new KeyboardButton(properties.getProperty("bot.buttons.contacts")));
        listRows.add(row4);

        replyKeyboardMarkup.setKeyboard(listRows);
        return replyKeyboardMarkup;
    }


    public ReplyKeyboard getCourierMenuButtons(boolean isAuthorized) {
        ReplyKeyboardMarkup replyKeyboardMarkup = createReplyKeyboard();

        List<KeyboardRow> listRows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(isAuthorized ? new KeyboardButton(properties.getProperty("bot.buttons.logout")) :
                new KeyboardButton(properties.getProperty("bot.buttons.login")));
        listRows.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(properties.getProperty("bot.buttons.courierOrders")));
        listRows.add(row2);

        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton(properties.getProperty("bot.buttons.courierDeliveredOrders")));
        listRows.add(row3);

        replyKeyboardMarkup.setKeyboard(listRows);
        return replyKeyboardMarkup;
    }

    @SneakyThrows
    public ReplyKeyboard getItemInfoButton(Item item, CallbackData favorite, CallbackData cart) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        if (item.getPhotos().size() > 1) {
            InlineKeyboardButton button = new InlineKeyboardButton("Келесі сурет  ⏭");
            button.setCallbackData(CallbackData.PHOTO.name() + ":" + item.getId() + ":1");
            row1.add(button);
            rows.add(row1);
        }

        if (favorite.equals(CallbackData.ADD_FAVORITE)) {
            InlineKeyboardButton add = new InlineKeyboardButton(properties.getProperty("bot.inline.buttons.addFavorite"));
            add.setCallbackData(CallbackData.ADD_FAVORITE.name() + ":" + item.getId().toString());
            rows.add(List.of(add));
        }
        else {
            InlineKeyboardButton delete = new InlineKeyboardButton(properties.getProperty("bot.inline.buttons.deleteFavorite"));
            delete.setCallbackData(CallbackData.DELETE_FAVORITE.name() + ":" + item.getId().toString());
            rows.add(List.of(delete));
        }

        if (cart.equals(CallbackData.ADD_CART)) {
            InlineKeyboardButton addCart = new InlineKeyboardButton(properties.getProperty("bot.inline.buttons.addCart"));
            addCart.setCallbackData(CallbackData.ADD_CART.name() + ":" + item.getId().toString());
            rows.add(List.of(addCart));
        }
        else {
            InlineKeyboardButton deleteCart = new InlineKeyboardButton(properties.getProperty("bot.inline.buttons.deleteCart"));
            deleteCart.setCallbackData(CallbackData.DELETE_CART.name() + ":" + item.getId().toString());
            rows.add(List.of(deleteCart));
        }

        InlineKeyboardButton more = new InlineKeyboardButton(properties.getProperty("bot.inline.buttons.moreInfo"));
        more.setCallbackData(CallbackData.MORE_INFO.name() + ":" + item.getId().toString());
        rows.add(List.of(more));

        keyboard.setKeyboard(rows);
        return keyboard;
    }


    public InlineKeyboardMarkup editInlineButtonPagination(InlineKeyboardMarkup replyMarkup, Item item, Integer photoId) {
        List<Photo> photos = item.getPhotos();
        Integer left = null;
        Integer right = null;

        if (photoId == 0) {
            right = photos.size() > 1 ? 1 : null;
        }
        else if (photoId == photos.size() - 1) {
            left = photoId - 1;
        }
        else {
            left = photoId - 1;
            right = photoId + 1;
        }

        List<InlineKeyboardButton> newRow = replyMarkup.getKeyboard().get(0);
        newRow.removeAll(newRow);
        if (left != null) {
            InlineKeyboardButton button = new InlineKeyboardButton("⏮  Алдыңғы сурет");
            button.setCallbackData(CallbackData.PHOTO.name() + ":" + item.getId() + ":" + left);
            newRow.add(button);
        }
        if (right != null) {
            InlineKeyboardButton button = new InlineKeyboardButton("Келесі сурет  ⏭");
            button.setCallbackData(CallbackData.PHOTO.name() + ":" + item.getId() + ":" + right);
            newRow.add(button);
        }
        return replyMarkup;
    }


    public ReplyKeyboard getRemoveKeyboard() {
        return ReplyKeyboardRemove.builder()
                .selective(true)
                .removeKeyboard(true)
                .build();
    }

    private ReplyKeyboardMarkup createReplyKeyboard() {
        return ReplyKeyboardMarkup.builder()
                .selective(true)
                .resizeKeyboard(true)
                .build();
    }

    public InlineKeyboardMarkup editMoreOrShortInfoButton(InlineKeyboardMarkup replyMarkup, Item item, boolean isMore) {
        List<List<InlineKeyboardButton>> keyboard = replyMarkup.getKeyboard();
        int buttonIndex = keyboard.size() - 1;
        List<InlineKeyboardButton> updatedRow = keyboard.get(buttonIndex);
        updatedRow.removeAll(updatedRow);

        if (isMore) {
            InlineKeyboardButton more = new InlineKeyboardButton(properties.getProperty("bot.inline.buttons.shortInfo"));
            more.setCallbackData(CallbackData.SHOR_INFO.name() + ":" + item.getId().toString());
            updatedRow.add(more);
        }
        else {
            InlineKeyboardButton more = new InlineKeyboardButton(properties.getProperty("bot.inline.buttons.moreInfo"));
            more.setCallbackData(CallbackData.MORE_INFO.name() + ":" + item.getId().toString());
            updatedRow.add(more);
        }
        return replyMarkup;
    }

    public ReplyKeyboard getContactButtons() {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        InlineKeyboardButton instagram = new InlineKeyboardButton();
        instagram.setText("INSTAGRAM");
        instagram.setUrl("https://instagram.com/mekhrat02?igshid=ZDdkNTZiNTM=");
        rows.add(Collections.singletonList(instagram));

        InlineKeyboardButton telegram = new InlineKeyboardButton();
        telegram.setText("TELEGRAM");
        telegram.setUrl("https://t.me/Mekhrat_Ashirbekov");
        rows.add(Collections.singletonList(telegram));

        InlineKeyboardButton whatsapp = new InlineKeyboardButton();
        whatsapp.setText("WHATSAPP");
        whatsapp.setUrl("https://wa.me/77059191561");
        rows.add(Collections.singletonList(whatsapp));

        keyboard.setKeyboard(rows);
        return keyboard;
    }

    public ReplyKeyboard getOrderButtons(Order order) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        order.getItems().forEach(item -> {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(item.getFullName());
            button.setCallbackData(CallbackData.GET_ITEM.name() + ":" + item.getId());
            rows.add(Collections.singletonList(button));
        });

        if (order.getStatus().equals(OrderStatus.IN_PROCESSING)) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(properties.getProperty("bot.inline.buttons.cancelOrder"));
            button.setCallbackData(CallbackData.CANCEL_ORDER.name() + ":" + order.getId());
            rows.add(Collections.singletonList(button));
        }
        markup.setKeyboard(rows);
        return markup;
    }

    public ReplyKeyboard getOrderInfoButton(Order order) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        InlineKeyboardButton delivery = new InlineKeyboardButton();
        delivery.setText("Тапсырысты беру");
        delivery.setCallbackData(CallbackData.DELIVERED.name() + ":" + order.getId());

        keyboard.setKeyboard(Collections.singletonList(Collections.singletonList(delivery)));
        return keyboard;
    }
}
