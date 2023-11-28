package com.paper.util;

import com.paper.exceptions.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.Binary;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ServiceUtil {

    private static final Logger logger = LogManager.getLogger(ServiceUtil.class);


    public static Binary toBinary(MultipartFile file) {
        try {
            return new Binary(file.getBytes());
        } catch (IOException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }
}
