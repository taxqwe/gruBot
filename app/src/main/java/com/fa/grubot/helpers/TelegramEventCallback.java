package com.fa.grubot.helpers;

import com.fa.grubot.App;
import com.fa.grubot.objects.events.telegram.TelegramAbstractEvent;
import com.fa.grubot.objects.events.telegram.TelegramMessageEvent;
import com.fa.grubot.objects.events.telegram.TelegramUpdateUserNameEvent;
import com.fa.grubot.objects.events.telegram.TelegramUpdateUserPhotoEvent;
import com.fa.grubot.objects.misc.TelegramPhoto;
import com.github.badoualy.telegram.api.TelegramClient;
import com.github.badoualy.telegram.api.UpdateCallback;
import com.github.badoualy.telegram.api.utils.InputFileLocation;
import com.github.badoualy.telegram.api.utils.TLMediaUtilsKt;
import com.github.badoualy.telegram.tl.api.TLAbsFileLocation;
import com.github.badoualy.telegram.tl.api.TLAbsMessage;
import com.github.badoualy.telegram.tl.api.TLAbsUpdate;
import com.github.badoualy.telegram.tl.api.TLAbsUserProfilePhoto;
import com.github.badoualy.telegram.tl.api.TLChat;
import com.github.badoualy.telegram.tl.api.TLMessage;
import com.github.badoualy.telegram.tl.api.TLPeerChannel;
import com.github.badoualy.telegram.tl.api.TLPeerChat;
import com.github.badoualy.telegram.tl.api.TLPeerUser;
import com.github.badoualy.telegram.tl.api.TLUpdateNewChannelMessage;
import com.github.badoualy.telegram.tl.api.TLUpdateNewMessage;
import com.github.badoualy.telegram.tl.api.TLUpdateShort;
import com.github.badoualy.telegram.tl.api.TLUpdateShortChatMessage;
import com.github.badoualy.telegram.tl.api.TLUpdateShortMessage;
import com.github.badoualy.telegram.tl.api.TLUpdateShortSentMessage;
import com.github.badoualy.telegram.tl.api.TLUpdateUserName;
import com.github.badoualy.telegram.tl.api.TLUpdateUserPhoto;
import com.github.badoualy.telegram.tl.api.TLUpdates;
import com.github.badoualy.telegram.tl.api.TLUpdatesCombined;
import com.github.badoualy.telegram.tl.api.TLUser;
import com.github.badoualy.telegram.tl.api.TLUserFull;
import com.github.badoualy.telegram.tl.api.messages.TLAbsMessages;
import com.github.badoualy.telegram.tl.core.TLIntVector;
import com.github.badoualy.telegram.tl.exception.RpcErrorException;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TelegramEventCallback implements UpdateCallback {

    public static final Logger LOG = LoggerFactory.getLogger(TelegramEventCallback.class);
    private TelegramEventListener listener;

    public TelegramEventCallback(TelegramEventListener listener) {
        this.listener = listener;
    }

    public static TelegramAbstractEvent getTelegramAbstractEvent(TelegramClient telegramClient, int messageId) {
        //create vector with one element
        TLIntVector messageIds = new TLIntVector();
        messageIds.add(messageId);
        try {
            TLAbsMessages tlAbsMessages = telegramClient.messagesGetMessages(messageIds);
            LOG.debug("create telegramAbstractEvent. sizes of vectors: messages={}, chats={}, users={}", tlAbsMessages.getMessages().size(), tlAbsMessages.getChats().size(), tlAbsMessages.getUsers().size());

            //Fill the telegramAbstractEvent
            int fromUserId = -1;
            TelegramAbstractEvent telegramAbstractEvent = new TelegramAbstractEvent();
            if (!tlAbsMessages.getMessages().isEmpty()) {
                telegramAbstractEvent.setMessage(tlAbsMessages.getMessages().get(0));
                if (tlAbsMessages.getMessages().get(0) instanceof TLMessage) {
                    fromUserId = ((TLMessage) tlAbsMessages.getMessages().get(0)).getFromId();
                }
            }
            if (!tlAbsMessages.getChats().isEmpty())
                telegramAbstractEvent.setChat(tlAbsMessages.getChats().get(0));
            if (!tlAbsMessages.getUsers().isEmpty()) {
                int finalFromUserId = fromUserId;
                tlAbsMessages.getUsers().forEach(tlAbsUser -> {
                    if (tlAbsUser.getId() == finalFromUserId) {
                        telegramAbstractEvent.setUser(tlAbsUser);
                    }
                });
            }
            return telegramAbstractEvent;
        } catch (RpcErrorException e) {
            LOG.error("converting message failed: error on on get message. message id was {}", messageId, e);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    /*public static TelegramMessageEvent getTelegramMessageEvent(TelegramClient telegramClient, TelegramAbstractEvent telegramAbstractEvent) {
        TelegramMessageEvent resultMessageEvent = new TelegramMessageEvent();
        if (telegramAbstractEvent.getMessage() instanceof TLMessage) {
            TLMessage tlMessage = (TLMessage) telegramAbstractEvent.getMessage();
            resultMessageEvent.setMessage(tlMessage.getMessage())
                    .setDate(tlMessage.getDate())
                    .setFromId(tlMessage.getFromId())
                    //.setFrom(Users.getName(telegramClient,tlMessage.getFromId()))
                    .setToId(App.INSTANCE.getCurrentUser().getTelegramUser().getId());

//            if(tlMessage.getToId() instanceof TLPeerUser) {
//                LOG.debug("TLMessage toId was TLPeerUser");
//                telegramMessageEvent.setToId(Users.getName(telegramClient,((TLPeerUser) tlMessage.getToId()).getUserId()))
//                    .setToId(((TLPeerUser) tlMessage.getToId()).getUserId());
//            } else if(tlMessage.getToId() instanceof TLPeerChat) {
//                LOG.debug("TLMessage toId was TLPeerChat (setting toId and to with Chats-Title)");
//                //telegramMessageEvent.setToId(Users.getName)
//                telegramMessageEvent.setToId(((TLPeerChat) tlMessage.getToId()).getChatId())
//                        .setToNumber(Chats.getTitle(telegramClient,((TLPeerChat) tlMessage.getToId()).getChatId()));
//
//            } else {
//                LOG.warn("TLMessage toId was not instance of TLPeerUser or TLPeerChat! It was instance of {}. use fallback selfUser!", tlMessage.getToId().getClass().getName());
//                telegramMessageEvent.setToId(Users.getSelfName(telegramClient))
//                        .setToId(Users.getSelfId(telegramClient));
//            }
        }

        if (telegramAbstractEvent.getChat() != null) {
            resultMessageEvent.setGroupId(telegramAbstractEvent.getChat().getId())
                    .setGroup(TelegramHelper.Chats.extractChatTitle(telegramAbstractEvent.getChat()));
        }

        if (telegramAbstractEvent.getUser() instanceof TLUser) {
            TLUser tlUser = (TLUser) telegramAbstractEvent.getUser();
            resultMessageEvent.setFromId(tlUser.getId())
                    .setFrom(TelegramHelper.Users.extractName(tlUser));
        }
        return resultMessageEvent;
    }*/

    @Override
    public void onUpdates(@NotNull TelegramClient telegramClient, @NotNull TLUpdates tlUpdates) {
        //Wird bei GIFs in Gruppenchats getriggert
        LOG.debug("TLUpdates called");

        if (tlUpdates.getUpdates().isEmpty())
            LOG.info("Update was empty");

        tlUpdates.getUpdates().forEach(absUpdate -> this.processUpdate(absUpdate, telegramClient));
        tlUpdates.getChats().forEach(tlAbsChat -> {
            if (tlAbsChat instanceof TLChat) {
                LOG.info("Chats title was {} ", ((TLChat) tlAbsChat).getTitle());
            }
        });

        tlUpdates.getUsers().forEach(tlAbsUser -> {
            if (tlAbsUser instanceof TLUser) {
                if (((TLUser) tlAbsUser).getSelf())
                    LOG.info("Users was username: {}, firstname: {}", ((TLUser) tlAbsUser).getUsername(), ((TLUser) tlAbsUser).getFirstName());
            }
        });

    }

    @Override
    public void onUpdatesCombined(@NotNull TelegramClient telegramClient, @NotNull TLUpdatesCombined tlUpdatesCombined) {
        LOG.debug("TLUpdatesCombined called");
    }

    @Override
    public void onUpdateShort(@NotNull TelegramClient telegramClient, @NotNull TLUpdateShort tlUpdateShort) {
        LOG.debug("TLUpdateShort called");
        processUpdate(tlUpdateShort.getUpdate(), telegramClient);
    }

    @Override
    public void onShortChatMessage(@NotNull TelegramClient telegramClient, @NotNull TLUpdateShortChatMessage tlUpdate) {
        //Wird in gruppen getriggert
//        TelegramEvent event = new TelegramEvent();
//        //Setting Time & Message
//        event
//                .setTime((long) tlUpdate.getDate())
//                .setMessage(tlUpdate.getTelegramAbstractEvent());
//
//
//        //Setting SendingUser
//        String userName = Users.getName(telegramClient, tlUpdate.getFromId());
//        event.setSendingUserName(userName)
//                .setSendingUserId(tlUpdate.getFromId());
//        //Setting Group
//        String groupName = Chats.getTitle(telegramClient, tlUpdate.getChatId());
//        event.setGroupId(tlUpdate.getChatId())
//                .setGroupName(groupName);
//        //Setting ReceivingUser
//        String selfName = Users.getSelfName(telegramClient);
//        Integer selfId = Users.getSelfId(telegramClient);
//        event.setReceivingUserName(selfName)
//                .setReceivingUserId(selfId);
//
//        if(event.getReceivingUserId().equals(event.getSendingUserId()))
//            event.setSelfMessage(true);

        TelegramAbstractEvent telegramAbstractEvent = getTelegramAbstractEvent(telegramClient, tlUpdate.getId());
        LOG.debug("TLUpdateShortChatMessage called");
        listener.onMessage(telegramAbstractEvent);
        //listener.onMessage(getTelegramMessageEvent(telegramClient, telegramAbstractEvent));
    }

    @Override
    public void onShortMessage(@NotNull TelegramClient telegramClient, @NotNull TLUpdateShortMessage tlUpdateShortMessage) {
        //Wird in einzelnen chats getriggert
//        TelegramEvent event = new TelegramEvent();
//
//        event.setMessage(tlUpdateShortMessage.getMessage())
//                .setTime((long) tlUpdateShortMessage.getDate());
//
//        String userName = Users.getName(telegramClient, tlUpdateShortMessage.getUserId());
//        event.setSendingUserName(userName)
//                .setSendingUserId(tlUpdateShortMessage.getUserId());
//        event.setReceivingUserName(Users.getSelfName(telegramClient))
//                .setReceivingUserId(Users.getSelfId(telegramClient));
//
//        if(event.getReceivingUserId().equals(event.getSendingUserId()))
//            event.setSelfMessage(true);

        TelegramAbstractEvent telegramAbstractEvent = getTelegramAbstractEvent(telegramClient, tlUpdateShortMessage.getId());
        LOG.debug("TLUpdateShortMessage called");
        listener.onMessage(telegramAbstractEvent);
        //listener.onMessage(getTelegramMessageEvent(telegramClient, telegramAbstractEvent));
        //LOG.debug("ALT Message: {} ", Message.getMessage(telegramClient,tlUpdateShortMessage.getId()));
    }

    @Override
    public void onShortSentMessage(@NotNull TelegramClient telegramClient, @NotNull TLUpdateShortSentMessage tlUpdateShortSentMessage) {
        LOG.debug("TLUpdateShortSentMessage called");
    }

    @Override
    public void onUpdateTooLong(@NotNull TelegramClient telegramClient) {
        LOG.debug("UpdateTooLong called");
    }

    private void processUpdate(TLAbsUpdate update, TelegramClient client) {
        if (update instanceof TLUpdateNewMessage) {
            TLAbsMessage message = ((TLUpdateNewMessage) update).getMessage();
            if (message instanceof TLMessage) {
                TLMessage tlMessage = (TLMessage) message;

                int messageToId = -1;
                String fromName = null;

                if (tlMessage.getToId() instanceof TLPeerUser) {
                    TLPeerUser peerUser = (TLPeerUser) tlMessage.getToId();
                    messageToId = peerUser.getUserId();
                } else if (tlMessage.getToId() instanceof TLPeerChat) {
                    TLPeerChat peerChat = (TLPeerChat) tlMessage.getToId();
                    messageToId = peerChat.getChatId();

                    if (tlMessage.getFromId() != App.INSTANCE.getCurrentUser().getTelegramUser().getId()) {
                        TLUser user = TelegramHelper.Users.getUser(client, tlMessage.getFromId()).getUser().getAsUser();
                        fromName = user.getFirstName() + " " + user.getLastName();
                        fromName = fromName.replace("null", "").trim();
                    }
                } else if (tlMessage.getToId() instanceof TLPeerChannel) {
                    TLPeerChannel peerChat = (TLPeerChannel) tlMessage.getToId();
                    messageToId = peerChat.getChannelId();
                }

                TelegramMessageEvent event = new TelegramMessageEvent(tlMessage.getMessage(), tlMessage.getFromId(), messageToId, tlMessage.getDate() * 1000, fromName);
                listener.onMessage(event);
            }
        } else if (update instanceof TLUpdateNewChannelMessage) {
            TLAbsMessage message = ((TLUpdateNewChannelMessage) update).getMessage();
            if (message instanceof TLMessage) {
                TLMessage tlMessage = (TLMessage) message;

                int messageToId = -1;
                String fromName = null;

                if (tlMessage.getToId() instanceof TLPeerUser) {
                    TLPeerUser peerUser = (TLPeerUser) tlMessage.getToId();
                    messageToId = peerUser.getUserId();
                } else if (tlMessage.getToId() instanceof TLPeerChat) {
                    TLPeerChat peerChat = (TLPeerChat) tlMessage.getToId();
                    messageToId = peerChat.getChatId();

                    if (tlMessage.getFromId() != App.INSTANCE.getCurrentUser().getTelegramUser().getId()) {
                        TLUser user = TelegramHelper.Users.getUser(client, tlMessage.getFromId()).getUser().getAsUser();
                        fromName = user.getFirstName() + " " + user.getLastName();
                        fromName = fromName.replace("null", "").trim();
                    }
                } else if (tlMessage.getToId() instanceof TLPeerChannel) {
                    TLPeerChannel peerChat = (TLPeerChannel) tlMessage.getToId();
                    messageToId = peerChat.getChannelId();
                }

                TelegramMessageEvent event = new TelegramMessageEvent(tlMessage.getMessage(), tlMessage.getFromId(), messageToId, tlMessage.getDate() * 1000, fromName);
                listener.onMessage(event);
            }
        } else if (update instanceof TLUpdateUserName) {
            TLUpdateUserName updateUserName = (TLUpdateUserName) update;
            TelegramUpdateUserNameEvent event = new TelegramUpdateUserNameEvent(updateUserName.getUserId(),
                    updateUserName.getFirstName(),
                    updateUserName.getLastName(),
                    updateUserName.getUsername());
            listener.onUserNameUpdate(event);
        } else if (update instanceof TLUpdateUserPhoto) {
            TLUpdateUserPhoto updateUserPhoto = (TLUpdateUserPhoto) update;

            TLAbsUserProfilePhoto absPhoto = updateUserPhoto.getPhoto();
            InputFileLocation inputFileLocation = null;
            long photoId = 0;

            if (absPhoto != null) {
                TLAbsFileLocation fileLocation = absPhoto.getAsUserProfilePhoto().getPhotoBig();
                inputFileLocation = TLMediaUtilsKt.toInputFileLocation(fileLocation);
                photoId = absPhoto.getAsUserProfilePhoto().getPhotoId();
            }

            TelegramPhoto telegramPhoto = new TelegramPhoto(inputFileLocation, photoId);

            TelegramUpdateUserPhotoEvent event = new TelegramUpdateUserPhotoEvent(updateUserPhoto.getUserId(), telegramPhoto);
            listener.onUserPhotoUpdate(event);
        }
    }

    public interface TelegramEventListener {
        void onMessage(TelegramAbstractEvent telegramAbstractEvent);

        void onMessage(TelegramMessageEvent telegramMessageEvent);

        void onUserNameUpdate(TelegramUpdateUserNameEvent telegramUpdateUserNameEvent);

        void onUserPhotoUpdate(TelegramUpdateUserPhotoEvent telegramUpdateUserPhotoEvent);
    }
}