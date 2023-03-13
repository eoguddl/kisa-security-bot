package com.project.kisa_bot.dto;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@Setter
@Builder
public class Field {

    @SerializedName("short")
    private Boolean shorts;

    private String title;

    private String value;

    private transient int number;

    private transient String type;

    public String getLink() {
        System.out.println(value.split(" \\|")[0].substring(1));
        return value.split(" \\|")[0].substring(1);
    }

}
