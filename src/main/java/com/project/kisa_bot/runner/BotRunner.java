package com.project.kisa_bot.runner;

import com.google.gson.Gson;
import com.internship.yhdatabase.project.config.BotProperties;
import com.internship.yhdatabase.project.dto.Attachment;
import com.internship.yhdatabase.project.dto.Attachments;
import com.internship.yhdatabase.project.dto.Field;
import com.internship.yhdatabase.project.service.bot.BotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BotRunner {

    @Value("${mattermost.webhook-url}")
    private String webhookUrl;

    private final RestTemplate restTemplate;

    private final BotProperties mmProperties;

    private final BotService botService;

    @Scheduled(fixedDelay = 300000)
    public void mattermostRunner() {
        log.info("### Start ###");

        try {
            List<Field> fields = botService.getFieldData();

            if (!fields.isEmpty()) {
                for (Field field : fields) {
                    int number = field.getNumber();
                    String type = field.getType();

                    log.info("### FIELD TYPE: {} ###", type);

                    Attachment attachment = Attachment.builder()
                            .color(mmProperties.getColor())
                            .title(mmProperties.getTitle(number, type))
                            .fields(new ArrayList<>() {{
                                add(field);
                            }})
                            .footer(mmProperties.getFooter())
                            .build();

                    Attachments attachments = new Attachments(attachment);

                    String URL = field.getLink();

                    if (botService.checkType(type)) {
                        attachments.setProps(botService.makeSecInfoProps(URL));
                    } else {
                        attachments.setProps(botService.makeGuideProps(URL));
                    }

                    String payload = new Gson().toJson(attachments);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<String> entity = new HttpEntity<>(payload, headers);

                    restTemplate.exchange(webhookUrl, HttpMethod.POST, entity, String.class);
                    log.info("### SEND OK ###");
                }
            } else {
                log.info("### FIELD NULL ###");
            }
            log.info("### END ###");
        } catch (Exception exception) {
            log.info("### ERROR: {}", exception.getMessage());
        }
    }

}
