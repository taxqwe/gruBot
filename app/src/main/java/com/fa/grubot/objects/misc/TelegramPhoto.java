package com.fa.grubot.objects.misc;

import com.github.badoualy.telegram.api.utils.InputFileLocation;

public class TelegramPhoto {
    private InputFileLocation fileLocation;
    private long photoId;

    public TelegramPhoto(InputFileLocation fileLocation, long photoId) {
        this.fileLocation = fileLocation;
        this.photoId = photoId;
    }

    public InputFileLocation getFileLocation() {
        return fileLocation;
    }

    public long getPhotoId() {
        return photoId;
    }
}
