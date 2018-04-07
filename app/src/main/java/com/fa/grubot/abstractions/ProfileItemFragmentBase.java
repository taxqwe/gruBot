package com.fa.grubot.abstractions;

import com.fa.grubot.objects.pojos.VkUserResponseWithPhoto;
import com.fa.grubot.objects.users.User;

public interface ProfileItemFragmentBase {
    void showVkUser(VkUserResponseWithPhoto userVk);
    void showTelegramUser(User telegramUser);
}
