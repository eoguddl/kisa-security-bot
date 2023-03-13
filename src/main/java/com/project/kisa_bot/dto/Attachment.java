package com.project.kisa_bot.dto;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
public class Attachment {

    private String channel;

    private String pretext;

    private String color;

    @SerializedName("author_name")
    private String authorName;

    @SerializedName("author_icon")
    private String authorIcon;

    private String title;

    private String text;

    private List<Field> fields;

    private String footer;

}
