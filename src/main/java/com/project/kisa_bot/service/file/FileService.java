package com.project.kisa_bot.service.file;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public interface FileService {

    File checkFileExist(String type) throws IOException;

    int fileRead(String type) throws IOException;

    void fileWrite(String type, int number) throws IOException;

}
