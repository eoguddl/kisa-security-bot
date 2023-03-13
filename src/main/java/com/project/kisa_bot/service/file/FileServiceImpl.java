package com.project.kisa_bot.service.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private final String filePath =
            System.getProperty("user.dir") + "\\src\\main\\resources\\static";

    private final File secInfoFile = new File(filePath + "\\SecInfo.txt");

    private final File guideFile = new File(filePath + "\\GuideInfo.txt");

    @Override
    public File checkFileExist(String type) throws IOException {
        log.info("### CHECK FILE EXIST: {} ###", type);

        if (!secInfoFile.exists()) {
            log.info("### CREATE NEW SecInfo FILE ###");
            secInfoFile.createNewFile();
        }

        if (!guideFile.exists()) {
            log.info("### CREATE NEW Guide File ###");
            guideFile.createNewFile();
        }

        if (type.equals("SecInfo")) {
            return secInfoFile;
        } else {
            return guideFile;
        }
    }

    @Override
    public int fileRead(String type) throws IOException {
        File file = checkFileExist(type);
        BufferedReader reader = new BufferedReader(new FileReader(file));

        log.info("### READ START ###");
        int parseNumber = Integer.parseInt(reader.readLine());;
        log.info("### READ END ###");

        return parseNumber;
    }

    @Override
    public void fileWrite(String type, int number) throws IOException {
        BufferedWriter writer = null;

        if (type.equals("SecInfo")) {
            writer = Files.newBufferedWriter(Path.of(filePath + "\\SecInfo.txt"));
        } else if (type.equals("Guide")) {
            writer = Files.newBufferedWriter(Path.of(filePath + "\\GuideInfo.txt"));
        }

        writer.write(String.valueOf(number));
        writer.newLine();

        writer.close();
    }

}
