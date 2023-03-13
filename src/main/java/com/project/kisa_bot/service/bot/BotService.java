package com.project.kisa_bot.service.bot;

import com.internship.yhdatabase.project.dto.Field;
import com.internship.yhdatabase.project.dto.Props;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface BotService {

    Field makeFieldByCrawling(String type);

    Props makeSecInfoProps(String URL) throws IOException;

    Props makeGuideProps(String URL) throws IOException;

    List<Field> getFieldData();

    Boolean checkType(String type);

}
