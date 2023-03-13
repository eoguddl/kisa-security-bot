package com.project.kisa_bot.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Attachments {

    private Props props;

    private List<Attachment> attachments = new ArrayList<>();

    public Attachments(Attachment attachment) {
        this.attachments.add(attachment);
    }

}
