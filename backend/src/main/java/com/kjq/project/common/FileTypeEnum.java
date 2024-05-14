package com.kjq.project.common;

public enum FileTypeEnum {
    JPG("jpg"),
    PNG("png"),
    JPEG("jpeg"),
    WEBP("webp");

    private final String type;

    FileTypeEnum(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
