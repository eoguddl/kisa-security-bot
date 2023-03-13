package com.project.kisa_bot.service.bot;

import com.internship.yhdatabase.project.dto.Field;
import com.internship.yhdatabase.project.dto.Props;
import com.internship.yhdatabase.project.service.file.FileService;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BotServiceImpl implements BotService {

    private static final String baseURL = "https://www.krcert.or.kr/";

    private static final String secInfoURL = "https://www.krcert.or.kr/data/secInfoList.do";

    private static final String guideURL = "https://www.krcert.or.kr/data/guideList.do";

    @Autowired
    private FileService fileService;

    @Override
    public Field makeFieldByCrawling(String type) {
        try {
            Boolean checkType = checkType(type);

            Connection conn;
            if (checkType) {
                conn = Jsoup.connect(secInfoURL);
            } else {
                conn = Jsoup.connect(guideURL);
            }

            Elements contents = conn.get().select("table tbody tr");
            String contentNumber = contents.get(0).getElementsByClass("gray").get(0).toString();
            int number = Integer.parseInt(contentNumber.split(">")[1].split("<")[0]);

            if (fileService.fileRead(type) >= number) {
                return null;
            }

            fileService.fileWrite(type, number);

            String contentValue = contents.get(0).getElementsByClass("colTit").toString();
            String title = contentValue.split(">")[2].split("<")[0];
            String url = baseURL + contentValue.split("=\"")[2].split("\">")[0];

            Field field;
            if (checkType) {
                field = Field.builder()
                        .shorts(false)
                        .title(title.substring(0, 14))
                        .value("<" + url + " || " + StringUtils.left(title.substring(17), 40) + ">\n")
                        .number(number)
                        .type("SecInfo")
                        .build();
            } else {
                field = Field.builder()
                        .shorts(false)
                        .value("<" + url + " || " + title + ">\n")
                        .number(number)
                        .type("Guide")
                        .build();
            }
            return field;
        } catch (Exception exception) {
            log.info("### ERROR: {} ###", exception.getMessage());
            return null;
        }
    }

    @Override
    public Props makeSecInfoProps(String URL) throws IOException {
        Connection conn = Jsoup.connect(URL);
        String[] contents = conn.get().select("table tbody tr td table tbody tr td").toString().split("\n");
        String contentValue = "";

        boolean status = false;

        for (String i : contents) {
            if (i.contains("<caption>")) {
                status = true;
            } else if (i.contains("</caption>")) {
                status = false;
            }

            if (!status) {
                contentValue += i + "\n";
            }
        }

        contentValue = contentValue.substring(0, contentValue.lastIndexOf("□ 기타"));
        String[] markdownCode = FlexmarkHtmlConverter.builder().build().convert(contentValue).split("\n");
        contentValue = "";

        for (String i : markdownCode) {
            if (i.equals("<br />")) {
                continue;
            }
            contentValue += i + "\n";
        }

        contentValue = contentValue.replaceAll("□ ", "### ")
                .replaceAll("o ", "- ");

        return new Props()
                .makeProps(contentValue);
    }

    @Override
    public Props makeGuideProps(String URL) throws IOException {
        Connection conn = Jsoup.connect(URL);
        String contents = conn.get().select("table tbody tr td table tbody tr td").toString();

        String[] downloadFile = conn.get().select("table tbody tr td a").toString().split("\n");
        String downloadFileHref = "";

        for (String i : downloadFile) {
            if (i.contains("/filedownload")) {
                i = i.split("href=\"")[0] + "href=\"https://www.krcert.or.kr" + i.split("href=\"")[1];
                downloadFileHref += FlexmarkHtmlConverter.builder().build().convert(i);
            }
        }

        String[] markdownCode = FlexmarkHtmlConverter.builder().build().convert(contents).split("\n");
        String contentValue = "";

        for (String i : markdownCode) {
            if (i.equals("<br />")) {
                continue;
            } else if (i.contains("/contentsImgView")) {
                i = i.split("/contentsImgView")[0] + "https://www.krcert.or.kr/contentsImgView" + i.split("/contentsImgView")[1];
            }
            contentValue += i + "\n";
        }

        if (contentValue.contains("□ 작성")) {
            contentValue = contentValue.substring(0, contentValue.lastIndexOf("□ 작성"));
        }

        if (downloadFileHref.length() != 0) {
            contentValue += "\n" + downloadFileHref + "\n";
        }

        return new Props()
                .makeProps(contentValue);
    }

    @Override
    public List<Field> getFieldData() {
        log.info("### GET FIELD DATA ###");

        List<Field> fields = new ArrayList<>();
        Field field;

        field = makeFieldByCrawling("SecInfo");
        if (field != null) {
            fields.add(field);
        } else {
            log.info("### SECINFO FIELD IS NULL ###");
        }

        field = makeFieldByCrawling("Guide");
        if (field != null) {
            fields.add(field);
        } else {
            log.info("### GUIDE FIELD IS NULL ###");
        }
        return fields;
    }

    @Override
    public Boolean checkType(String type) {
        if (type.equals("SecInfo")) {
            return true;
        } else {
            return false;
        }
    }
}
