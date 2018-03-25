package com.fa.grubot.helpers;

import com.fa.grubot.objects.chat.Chat;
import com.fa.grubot.objects.pojos.VkMessagePOJO;
import com.fa.grubot.util.DataType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.ReplaySubject;

/**
 * Created by ni.petrov on 23/03/2018.
 */

public class VkDialogParser {

    private ReplaySubject<Chat> dialogsSubscription = ReplaySubject.create();

    private VKResponse dialogsResponse;

    private Gson gson = new Gson();

    public VkDialogParser(VKResponse dialogsResponse){
        this.dialogsResponse = dialogsResponse;
        try {
            parse(this.dialogsResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parse(VKResponse dialogsResponse) throws JSONException {
        JSONObject json = dialogsResponse.json;
        JSONArray messagesArrayJson = json.getJSONObject("response").getJSONArray("items");

        for (int i = 0; i < messagesArrayJson.length(); i++){
            VkMessagePOJO messagePOJO =
                    gson.fromJson(messagesArrayJson.getJSONObject(i)
                            .getJSONObject("message").toString(), VkMessagePOJO.class);

            boolean isGroupDialog = messagesArrayJson.getJSONObject(i).getJSONObject("message").has("chat_id");

            Map<String, Boolean> usersInDialog = new HashMap<>();

            if (isGroupDialog && messagesArrayJson.getJSONObject(i).getJSONObject("message").has("chat_active")){
                usersInDialog = getUsersFromChatActiveJsonObject(
                        messagesArrayJson.getJSONObject(i).getJSONObject("message")
                                .getJSONArray("chat_active"));
            } else {
                usersInDialog.put(messagePOJO.getId().toString(), true);
            }

            Chat chat = new Chat(messagePOJO.getUserId().toString(),
                    isGroupDialog ? messagePOJO.getTitle() : VkHelper.getVkUserInfoById(messagePOJO.getUserId().toString()).first_name,
                    usersInDialog,
                    "http://nonsoc.com/uploads/posts/2017-04/1492002153_vk-zen-dlya-google-chrome.jpg",
                    messagePOJO.getBody(),
                    DataType.VK,
                    messagePOJO.getDate(),
                    VkHelper.getVkUserInfoById(messagePOJO.getUserId().toString()).first_name); //TODO may be combained into batch request
            dialogsSubscription.onNext(chat);
        }

        dialogsSubscription.onComplete();
    }

    private Map<String,Boolean> getUsersFromChatActiveJsonObject(JSONArray chat_active) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        Map<String, Boolean> users = new HashMap<>();
        List<String> userList = new Gson().fromJson(chat_active.toString(), listType);
        for (String s : userList) {
            users.put(s, true);
        }
        return users;
    }

    public Observable<Chat> getDialogsSubscription() {
        return dialogsSubscription;
    }
}
