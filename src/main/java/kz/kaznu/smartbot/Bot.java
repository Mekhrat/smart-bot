package kz.kaznu.smartbot;

import kz.kaznu.smartbot.models.dto.ConsumerInfo;
import kz.kaznu.smartbot.models.dto.ItemParamsDto;
import kz.kaznu.smartbot.models.entities.*;
import kz.kaznu.smartbot.models.enums.*;
import kz.kaznu.smartbot.models.enums.Role;
import kz.kaznu.smartbot.services.*;
import kz.kaznu.smartbot.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class Bot extends TelegramLongPollingBot implements MessageHandler {
    private final TUserService userService;
    private final Properties properties;
    private final Keyboards keyboards;
    private final MailSender mailSender;
    private final TelegramSessionService sessionService;
    private final FavoriteService favoriteService;
    private final ItemService itemService;
    private final ParamsService paramsService;
    private final CartService cartService;
    private final OrderService orderService;

    @Value("${PHOTO_URL}")
    private String PHOTO_URL;

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("REQUEST: {}", update);
        if (update.hasMessage()) {
            handleMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
    }


    @Override
    public void handleMessage(Message message) {
        TelegramUser user = userService.getOrCreateUser(message);
        if (message.hasText()) {
            if (user.getRole().equals(Role.USER)) {
                handleMessageText(user, message.getText());
            } else if (user.getRole().equals(Role.COURIER)) {
                handleCourierMessageText(user, message.getText());
            }
        }
        userService.save(user);
    }

    @Override
    public void handleMessageText(TelegramUser user, String message) {
        /**
         * HANDLE TEXT
         */
        if (message.equals(properties.getProperty("bot.commands.start"))) {
            user.setStatus(Status.EMPTY);
            sendPhoto(user.getChatId(),
                    properties.getProperty("bot.messages.welcome"),
                    null,
                    "Smart-Logo-1998.png");
            sendMenuMessage(user);
        } else if (message.equals(properties.getProperty("bot.buttons.backToMenu"))) {
            user.setStatus(Status.EMPTY);
            sendMenuMessage(user);
        } else if (message.equals(properties.getProperty("bot.buttons.login"))) {
            user.setStatus(Status.SEND_EMAIL);
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.messages.sendEmail"),
                    keyboards.getNButtons(properties.getProperty("bot.buttons.backToMenu")));
        } else if (message.equals(properties.getProperty("bot.buttons.logout"))) {
            user.setStatus(Status.EMPTY);
            sessionService.logout(user);
            sendMenuMessage(user);
        } else if (message.equals(properties.getProperty("bot.buttons.items"))) {
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.selectItemType"),
                    keyboards.getNButtons(properties.getProperty("bot.buttons.search"), properties.getProperty("bot.buttons.smartphones"), properties.getProperty("bot.buttons.headphones"), properties.getProperty("bot.buttons.smartwhaches"), properties.getProperty("bot.buttons.backToMenu")));
        } else if (message.equals(properties.getProperty("bot.buttons.myOrders"))) {
            sendListMyOrdersByUser(user);
        } else if (message.equals(properties.getProperty("bot.buttons.favorites"))) {
            sendFavoriteItemsByUser(user);
        } else if (message.equals(properties.getProperty("bot.buttons.cart"))) {
            sendItemsInCartByUser(user);
        } else if (message.equals(properties.getProperty("bot.buttons.contacts"))) {
            sendPhoto(user.getChatId(),
                    properties.getProperty("bot.message.contacts"),
                    keyboards.getContactButtons(),
                    "Smart-Logo-1998.png");
        } else if (message.equals(properties.getProperty("bot.buttons.search"))) {
            user.setStatus(Status.SEARCH);
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.search"),
                    keyboards.getNButtons(properties.getProperty("bot.buttons.backToMenu")));
        } else if (message.equals(properties.getProperty("bot.buttons.smartphones"))) {
            showItemsByItemType(user, ItemType.SMARTPHONE);
        } else if (message.equals(properties.getProperty("bot.buttons.headphones"))) {
            showItemsByItemType(user, ItemType.HEADPHONE);
        } else if (message.equals(properties.getProperty("bot.buttons.smartwhaches"))) {
            showItemsByItemType(user, ItemType.SMARTWATCH);
        } else if (message.equals(properties.getProperty("bot.buttons.removeCart"))) {
            deleteAllItemsInCart(user);
        } else if (message.equals(properties.getProperty("bot.buttons.createNewOrder"))) {
            checkSessionAndSendFullNameMessage(user);
        } else if (message.equals(properties.getProperty("bot.buttons.next"))) {
            createNewOrder(user, "");
        } else if (message.equals(properties.getProperty("bot.buttons.pay"))) {
            sendPaymentPage(user);
        }

        /**
         * HANDLE STATUS
         */
        else if (user.getStatus().equals(Status.SEND_EMAIL)) {
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.messages.checkEmail"),
                    null);
            checkEmailAndSendActivateCode(user, message);
        } else if (user.getStatus().equals(Status.SEND_ACTIVATE_CODE)) {
            checkActivateCodeAndUpdateSession(user, message);
        } else if (user.getStatus().equals(Status.SEND_FULL_NAME)) {
            checkSessionAndSendPhoneMessage(user, message);
        } else if (user.getStatus().equals(Status.SEND_PHONE)) {
            checkSessionAndSendAddressMessage(user, message);
        } else if (user.getStatus().equals(Status.SEND_ADDRESS)) {
            checkSessionAndSendIndexAMessage(user, message);
        } else if (user.getStatus().equals(Status.SEND_INDEX)) {
            createNewOrder(user, message);
        } else if (user.getStatus().equals(Status.SEARCH)) {
            sendSearchItemsByTest(user, message);
        }
    }


    @Override
    public void handleCourierMessageText(TelegramUser user, String message) {
        if (message.equals(properties.getProperty("bot.commands.start"))) {
            user.setStatus(Status.EMPTY);
            sendCourierMenuMessage(user);
        }
        else if (message.equals(properties.getProperty("bot.buttons.logout"))) {
            user.setStatus(Status.EMPTY);
            sessionService.logout(user);
            sendMenuMessage(user);
        }
        else if (message.equals(properties.getProperty("bot.buttons.courierOrders"))) {
            sendCourierOrders(user);
        }
        else if (message.equals(properties.getProperty("bot.buttons.courierDeliveredOrders"))) {
            sendCourierDeliveredOrders(user);
        }
        else if (user.getStatus().equals(Status.CONFIRM_ORDER)) {
            checkOrderCode(user, message);
        }
    }


    @Override
    public void handleCallbackQuery(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        Message message = callbackQuery.getMessage();
        TelegramUser user = userService.getOrCreateUser(message);

        if (callbackData.contains(CallbackData.PHOTO.name())) {
            sendEditPhotoPaginationMessage(message, callbackData);
        } else if (callbackData.contains(CallbackData.MORE_INFO.name())) {
            sendParamsForItem(message, callbackData, true);
        } else if (callbackData.contains(CallbackData.SHOR_INFO.name())) {
            sendParamsForItem(message, callbackData, false);
        } else if (callbackData.contains(CallbackData.DELETE_FAVORITE.name())) {
            deleteFavoriteItemsByUser(user, message, callbackData);
        } else if (callbackData.contains(CallbackData.ADD_CART.name())) {
            addItemToCart(user, callbackData);
        } else if (callbackData.contains(CallbackData.DELETE_CART.name())) {
            deleteItemInCart(user, message, callbackData);
        } else if (callbackData.contains(CallbackData.ADD_FAVORITE.name())) {
            addItemToFavorite(user, callbackData);
        } else if (callbackData.contains(CallbackData.CANCEL_ORDER.name())) {
            cancelOrderByUser(user, message, callbackData);
        } else if (callbackData.contains(CallbackData.GET_ITEM.name())) {
            Long itemId = Long.parseLong(callbackData.split(":")[1]);
            sendItemInfo(user, itemId);
        }
        /**
         * COURIER
         */
        else if (callbackData.contains(CallbackData.DELIVERED.name())) {
            Long orderId = Long.parseLong(callbackData.split(":")[1]);
            sendConfirmMessage(user, orderId, message);
        }
        userService.save(user);
    }

    private void sendMenuMessage(TelegramUser user) {
        boolean isAuthorized = sessionService.checkSessionByUserEmail(user);
        sendMessage(user.getChatId(),
                properties.getProperty("bot.messages.menuMessage"),
                keyboards.getMenuButtons(isAuthorized));
    }


    private void sendCourierMenuMessage(TelegramUser user) {
        boolean isAuthorized = sessionService.checkSessionByUserEmail(user);
        sendMessage(user.getChatId(),
                properties.getProperty("bot.messages.menuMessage"),
                keyboards.getCourierMenuButtons(isAuthorized));
    }


    private void checkEmailAndSendActivateCode(TelegramUser user, String email) {
        if (BotUtils.validateEmail(email)) {
            String code = LoginUtils.generateActivateCodeForEmail(email);
            boolean isSent = mailSender.send("Активация коды",
                    "Сәлеметсіз бе! </br>SMART интернет дүкеніне қош келдіңіз. Сіздің тіркелу кодыңыз: " + code,
                    email);
            if (isSent) {
                user.setStatus(Status.SEND_ACTIVATE_CODE);
                user.setEmail(email);
                sendMessage(user.getChatId(),
                        properties.getProperty("bot.messages.activateCodeSentSuccessfully"),
                        null);
            }
        } else {
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.messages.emailError"),
                    null);
        }
    }


    private void checkActivateCodeAndUpdateSession(TelegramUser user, String code) {
        if (LoginUtils.checkActivateCode(user.getEmail(), code)) {
            sessionService.login(user);
            user.setStatus(Status.EMPTY);

            sendMessage(user.getChatId(),
                    properties.getProperty("bot.messages.authSuccess"),
                    null);

            if (user.getRole().equals(Role.USER)) {
                sendMenuMessage(user);
            }
            else {
                sendCourierMenuMessage(user);
            }
        } else {
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.messages.authError"),
                    keyboards.getNButtons(properties.getProperty("bot.buttons.backToMenu")));
        }
    }


    private void sendItemInfo(TelegramUser user, Long itemId) {
        Optional<Item> opItem = itemService.getItemById(itemId);
        opItem.ifPresent(item -> {
            List<Photo> photos = item.getPhotos();
            List<ItemParamsDto> mainParams = paramsService.getMainParamsByItem(item);
            String itemInfo = ItemUtils.createMainItemParamsInfo(item, mainParams);
            sendPhoto(user.getChatId(),
                    itemInfo,
                    keyboards.getItemInfoButton(item, CallbackData.ADD_FAVORITE, CallbackData.ADD_CART),
                    PHOTO_URL + photos.get(0).getPhotoName() + ".jpg");
        });
    }


    private void showItemsByItemType(TelegramUser user, ItemType itemType) {
        List<Item> items = itemService.getTopItemsByType(itemType, 0, 100).stream().collect(Collectors.toList());
        if (items.size() > 0) {
            items.forEach(item -> {
                sendItemInfo(user, item.getId());
            });
        } else {
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.itemNotFound"),
                    null);
        }
    }


    private void sendSearchItemsByTest(TelegramUser user, String message) {
        List<Item> items = itemService.search(message, 0, 100).stream().collect(Collectors.toList());
        if (items.size() > 0) {
            items.forEach(item -> {
                sendItemInfo(user, item.getId());
            });
        } else {
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.itemNotFound"),
                    null);
        }
    }


    private void sendFavoriteItemsByUser(TelegramUser user) {
        if (sessionService.checkSessionByUserEmail(user)) {
            List<Item> favorites = favoriteService.getFavoriteItemsByUser(user.getEmail());
            if (favorites.size() > 0) {
                for (Item item : favorites) {
                    List<Photo> photos = item.getPhotos();
                    List<ItemParamsDto> mainParams = paramsService.getMainParamsByItem(item);
                    String itemInfo = ItemUtils.createMainItemParamsInfo(item, mainParams);
                    sendPhoto(user.getChatId(),
                            itemInfo,
                            keyboards.getItemInfoButton(item, CallbackData.DELETE_FAVORITE, CallbackData.ADD_CART),
                            PHOTO_URL + photos.get(0).getPhotoName() + ".jpg");
                }
            } else {
                sendMessage(user.getChatId(),
                        properties.getProperty("bot.message.favoritesNotFound"),
                        null);
            }
        } else {
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.youNeedToLogin"),
                    keyboards.getMenuButtons(false));
        }
    }


    private void addItemToFavorite(TelegramUser user, String callbackData) {
        Long itemId = Long.parseLong(callbackData.split(":")[1]);
        Optional<Item> item = itemService.getItemById(itemId);
        if (item.isPresent()) {
            boolean created = favoriteService.createNewFavorite(user, item.get());
            if (created) {
                sendMessage(user.getChatId(),
                        properties.getProperty("bot.message.favoriteAddedSuccess"),
                        null);
            }
        }
    }


    private void deleteFavoriteItemsByUser(TelegramUser user, Message message, String callbackData) {
        Long itemId = Long.parseLong(callbackData.split(":")[1]);
        Optional<Item> item = itemService.getItemById(itemId);
        if (item.isPresent()) {
            Optional<Favorite> favoriteItem = favoriteService.getFavoriteItemsByUserAndItem(user.getEmail(), item.get());
            if (favoriteItem.isPresent()) {
                favoriteService.delete(favoriteItem.get());
                deleteMessage(message.getChatId().toString(), message.getMessageId());
            }
        }
    }


    private void sendEditPhotoPaginationMessage(Message message, String callbackData) {
        String[] values = callbackData.split(":");
        Long itemId = Long.parseLong(values[1]);
        int photoId = Integer.parseInt(values[2]);
        Optional<Item> item = itemService.getItemById(itemId);
        if (item.isPresent()) {
            List<Photo> photos = item.get().getPhotos();

            editMessageMedia(message.getChatId().toString(),
                    message.getMessageId(),
                    message.getCaption(),
                    keyboards.editInlineButtonPagination(message.getReplyMarkup(), item.get(), photoId),
                    PHOTO_URL + photos.get(photoId).getPhotoName() + ".jpg");
        }
    }


    private void sendParamsForItem(Message message, String callbackData, boolean isMore) {
        Long itemId = Long.parseLong(callbackData.split(":")[1]);
        Optional<Item> itemById = itemService.getItemById(itemId);

        if (itemById.isPresent()) {
            Item item = itemById.get();
            String itemInfo;
            if (isMore) {
                Map<String, List<ItemParamsDto>> params = paramsService.getAllSortedItemParams(item);
                itemInfo = ItemUtils.createInfoTextWithAllParams(item, params);
            } else {
                List<ItemParamsDto> mainParams = paramsService.getMainParamsByItem(item);
                itemInfo = ItemUtils.createMainItemParamsInfo(item, mainParams);
            }

            if (itemInfo.length() > 1020)
                itemInfo = itemInfo.substring(0, 1020);

            editMessageCaption(message.getChatId().toString(),
                    message.getMessageId(),
                    itemInfo,
                    keyboards.editMoreOrShortInfoButton(message.getReplyMarkup(), item, isMore));
        }
    }


    private void addItemToCart(TelegramUser user, String callbackData) {
        Long itemId = Long.parseLong(callbackData.split(":")[1]);
        boolean added = cartService.addItemToCardBySession(user, itemId);
        if (added) {
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.cartAddedSuccess"),
                    null);
        }
    }


    private void sendItemsInCartByUser(TelegramUser user) {
        List<Item> cartItems = cartService.getItemsInCartByEmail(user.getEmail());
        if (cartItems.size() > 0) {
            for (Item item : cartItems) {
                List<Photo> photos = item.getPhotos();
                List<ItemParamsDto> mainParams = paramsService.getMainParamsByItem(item);
                String itemInfo = ItemUtils.createMainItemParamsInfo(item, mainParams);
                sendPhoto(user.getChatId(),
                        itemInfo,
                        keyboards.getItemInfoButton(item, CallbackData.ADD_FAVORITE, CallbackData.DELETE_CART),
                        PHOTO_URL + photos.get(0).getPhotoName() + ".jpg");
            }

            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.emptyText"),
                    keyboards.getNButtons(properties.getProperty("bot.buttons.createNewOrder"),
                            properties.getProperty("bot.buttons.removeCart"),
                            properties.getProperty("bot.buttons.backToMenu")));
        } else {
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.cartItemsIsEmpty"),
                    null);
        }
    }


    private void deleteItemInCart(TelegramUser user, Message message, String callbackData) {
        Long itemId = Long.parseLong(callbackData.split(":")[1]);
        boolean deleted = cartService.deleteByUserAndItem(user, itemId);
        if (deleted) {
            deleteMessage(message.getChatId().toString(), message.getMessageId());
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.cartDeletedSuccess"),
                    null);
        }
    }


    private void deleteAllItemsInCart(TelegramUser user) {
        boolean deleted = cartService.deleteAllItemsByUser(user);
        if (deleted) {
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.deleteCartsSuccess"),
                    null);
            sendMenuMessage(user);
        }
    }


    private void checkSessionAndSendFullNameMessage(TelegramUser user) {
        if (sessionService.checkSessionByUserEmail(user)) {
            user.setStatus(Status.SEND_FULL_NAME);
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.sendFullName"),
                    keyboards.getNButtons(properties.getProperty("bot.buttons.backToMenu")));
        } else {
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.youNeedToLogin"),
                    keyboards.getMenuButtons(false));
        }
    }


    private void checkSessionAndSendPhoneMessage(TelegramUser user, String message) {
        if (sessionService.checkSessionByUserEmail(user)) {
            OrderUtils.addName(user, message);
            user.setStatus(Status.SEND_PHONE);
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.sendPhone"),
                    null);
        } else {
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.youNeedToLogin"),
                    keyboards.getMenuButtons(false));
        }
    }


    private void checkSessionAndSendAddressMessage(TelegramUser user, String message) {
        if (sessionService.checkSessionByUserEmail(user)) {
            if (BotUtils.validatePhone(message)) {
                OrderUtils.addPhone(user, message);
                user.setStatus(Status.SEND_ADDRESS);
                sendMessage(user.getChatId(),
                        properties.getProperty("bot.message.sendAddress"),
                        null);
            } else {
                sendMessage(user.getChatId(),
                        properties.getProperty("bot.message.sendAgainPhone"),
                        null);
            }
        } else {
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.youNeedToLogin"),
                    keyboards.getMenuButtons(false));
        }
    }


    private void checkSessionAndSendIndexAMessage(TelegramUser user, String message) {
        if (sessionService.checkSessionByUserEmail(user)) {
            OrderUtils.addAddress(user, message);
            user.setStatus(Status.SEND_INDEX);
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.sendIndex"),
                    keyboards.getNButtons(properties.getProperty("bot.buttons.next"), properties.getProperty("bot.buttons.pay") ,properties.getProperty("bot.buttons.backToMenu")));
        } else {
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.youNeedToLogin"),
                    keyboards.getMenuButtons(false));
        }
    }


    private void sendListMyOrdersByUser(TelegramUser user) {
        if (sessionService.checkSessionByUserEmail(user)) {
            List<Order> orders = orderService.getOrdersByUserEmail(user.getEmail());
            if (orders.size() > 0) {
                orders.forEach(order -> {
                    String messageText = ItemUtils.createOrdersText(order);
                    sendMessage(user.getChatId(),
                            messageText,
                            keyboards.getOrderButtons(order));
                });
            } else {
                sendMessage(user.getChatId(),
                        properties.getProperty("bot.message.orderNotFound"),
                        null);
            }
        }
        else {
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.youNeedToLogin"),
                    keyboards.getMenuButtons(false));
        }
    }



    private void createNewOrder(TelegramUser user, String index) {
        if (BotUtils.validateIndex(index)) {
            ConsumerInfo consumerInfo = OrderUtils.getOrderInfo(user.getEmail());
            if (consumerInfo != null) {
                consumerInfo.setIndex(index);
                List<Item> cartItems = cartService.getItemsInCartByEmail(user.getEmail());

                if (cartItems.size() > 0) {
                    orderService.createNewOrder(user, consumerInfo, cartItems);
                    cartService.deleteAllItemsByUser(user);
                    user.setStatus(Status.EMPTY);

                    sendMessage(user.getChatId(),
                            properties.getProperty("bot.messages.orderCreatedSuccess"),
                            keyboards.getNButtons(properties.getProperty("bot.buttons.backToMenu")));
                }
            }
        } else {
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.indexError"),
                    null);
        }
    }


    private void cancelOrderByUser(TelegramUser user, Message message, String callbackData) {
        Long orderId = Long.parseLong(callbackData.split(":")[1]);
        orderService.cancelOrderById(orderId);
        deleteMessage(message.getChatId().toString(), message.getMessageId());
        sendMessage(user.getChatId(),
                properties.getProperty("bot.message.cancelOrderSuccess"),
                null);
    }


    private void sendCourierOrders(TelegramUser user) {
        List<Order> orders = orderService.getAllCourierOrders(user.getEmail());
        if (!orders.isEmpty()) {
            orders.forEach(order -> {
                String text = String.format(properties.getProperty("bot.message.orderText"),
                        order.getConsumerName(),
                        order.getConsumerPhone(),
                        order.getConsumerEmail(),
                        order.getDeliveryAddress(),
                        order.getConsumerIndex(),
                        order.getTotal(),
                        order.getItems().stream().map(Item::getFullName).collect(Collectors.joining(",\n")));

                sendMessage(user.getChatId(),
                        text,
                        keyboards.getOrderInfoButton(order));
            });
        }
        else {
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.ordersNotFound"),
                    null);
        }
    }


    private void sendCourierDeliveredOrders(TelegramUser user) {
        List<Order> orders = orderService.getAllDeliveredCourierOrders(user.getEmail());
        if (!orders.isEmpty()) {
            orders.forEach(order -> {
                String text = String.format(properties.getProperty("bot.message.deliveredOrderText"),
                        order.getConsumerName(),
                        order.getConsumerPhone(),
                        order.getConsumerEmail(),
                        order.getDeliveryAddress(),
                        order.getConsumerIndex(),
                        order.getTotal(),
                        order.getItems().stream().map(Item::getFullName).collect(Collectors.joining(",\n")),
                        order.getOrderDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));

                sendMessage(user.getChatId(),
                        text,
                        null);
            });
        }
        else {
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.ordersNotFound"),
                    null);
        }
    }


    private void sendConfirmMessage(TelegramUser user, Long orderId, Message message) {
        Optional<Order> opOrder = orderService.getById(orderId);
        opOrder.ifPresent(order -> {
            String code = DeliveryUtils.generateCode(orderId);

            deleteMessage(user.getChatId(), message.getMessageId());
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.emailSending"),
                    null);

            mailSender.send("Smart shop",
                    "Сәлеметсіз бе! Тапсырысты растау коды: " + code,
                    order.getConsumerEmail());

            user.setStatus(Status.CONFIRM_ORDER);
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.confirmOrder"),
                    null);
        });
    }


    private void checkOrderCode(TelegramUser user, String code) {
        Long orderId = DeliveryUtils.get(code);
        if (orderId != null) {
            Optional<Order> opOrder = orderService.getById(orderId);
            opOrder.ifPresent(order -> {
                order.setStatus(OrderStatus.DELIVERED);
                order.setOrderDate(LocalDateTime.now());
                order.setPaid(true);
                orderService.save(order);

                user.setStatus(Status.EMPTY);
                sendMessage(user.getChatId(),
                        properties.getProperty("bot.message.confirmSuccess"),
                        null);
            });
        }
        else {
            sendMessage(user.getChatId(),
                    properties.getProperty("bot.message.codeError"),
                    null);
        }
    }


    private void sendPaymentPage(TelegramUser user) {
        sendMessage(user.getChatId(),
                properties.getProperty("bot.message.payment"),
                keyboards.getPaymentButton());
    }


    private Message sendMessage(String chatId, String text, ReplyKeyboard keyboard) {
        SendMessage message = SendMessage.builder()
                .parseMode(ParseMode.HTML)
                .chatId(chatId)
                .text(text)
                .replyMarkup(keyboard)
                .build();
        try {
            Message newMessage = execute(message);
            log.info("RESPONSE: {}", newMessage);
            return newMessage;
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            return null;
        }
    }


    private Message sendPhoto(String chatId, String text, ReplyKeyboard keyboard, String photoName) {
        SendPhoto message = SendPhoto.builder()
                .parseMode(ParseMode.HTML)
                .chatId(chatId)
                .caption(text)
                .photo(new InputFile(new File(photoName)))
                .replyMarkup(keyboard)
                .build();
        try {
            Message newMessage = execute(message);
            log.info("RESPONSE: {}", newMessage);
            return newMessage;
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            return null;
        }
    }


    private void editMessageMedia(String chatId, Integer messageId, String caption, InlineKeyboardMarkup keyboard, String photoName) {
        String mediaName = UUID.randomUUID().toString();
        EditMessageMedia editMessage = EditMessageMedia.builder()
                .media(InputMediaPhoto.builder()
                        .newMediaFile(new File(photoName))
                        .media("attach://" + mediaName)
                        .mediaName(mediaName)
                        .isNewMedia(true)
                        .caption(caption)
                        .parseMode(ParseMode.HTML)
                        .build())
                .messageId(messageId)
                .chatId(chatId)
                .replyMarkup(keyboard)
                .build();
        try {
            execute(editMessage);
            log.info("RESPONSE: {}", editMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }


    private void editMessageCaption(String chatId, Integer messageId, String caption, ReplyKeyboard keyboard) {
        EditMessageCaption messageCaption = EditMessageCaption.builder()
                .messageId(messageId)
                .chatId(chatId)
                .caption(caption)
                .parseMode(ParseMode.HTML)
                .replyMarkup((InlineKeyboardMarkup) keyboard)
                .build();

        try {
            execute(messageCaption);
            log.info("RESPONSE: {}", messageCaption);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }


    private void deleteMessage(String chatId, Integer messageId) {
        DeleteMessage deleteMessage = DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build();

        try {
            execute(deleteMessage);
            log.info("Message deleted successfully!");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

}
