package com.project.kisa_bot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Component
public class BotProperties {

    private String channel;

    private String pretext;

    private String authorName;

    private String authorIcon;

    private String color = "#00BFFE";

    private String title;

    private String text;

    private String footer = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));

    public String getTitle(int number, String type) {
        if (type.equals("SecInfo")) {
            return "취약점 정보 No." + number;
        } else if (type.equals("Guide")) {
            return "가이드 및 메뉴얼 No." + number;
        }
        return null;
    }
}
