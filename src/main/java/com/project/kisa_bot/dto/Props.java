package com.project.kisa_bot.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Props {

    private String card;

    public Props makeProps(String contentValue) {
        Props props = Props.builder()
                .card(contentValue)
                .build();

        return props;
    }

}
