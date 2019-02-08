package com.xip.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.brunocvcunha.instagram4j.Instagram4j;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;

import static com.xip.impl.XipPublishingSessionImpl.JAR_FOLDER;

public class XipFileEventProcessingImpl implements com.xip.iface.XipFileEventProcessing {

    private final static Logger LOGGER = Logger.getLogger(XipFileEventProcessingImpl.class);

    static final String XIP_PREFIX = "xip_____";

    private final WatchEvent<?> fileEvent;
    private final Instagram4j iClient;
    private final String tag;

    public XipFileEventProcessingImpl(WatchEvent<?> fileEvent, Instagram4j iClient, String tag) {
        this.fileEvent = fileEvent;
        this.iClient = iClient;
        this.tag = tag;
    }

    public void doNow() {
        String fileName = fileEvent.context().toString();
        if (isImage(fileName)) {
            try {
                if (fileEvent.kind().equals(StandardWatchEventKinds.ENTRY_CREATE)) {
                    if (fileName.startsWith(XIP_PREFIX)) {
                        LOGGER.info("Going to publish...");
                        new XipPostImpl(iClient, fileName, tag).publish();
                        LOGGER.info("^^ published...");
                    } else {
                        LOGGER.info("Converting input image...");
                        new XipGrayScaleImageImpl(fileName).prepare();
                        LOGGER.info("^^ converted...");
                    }
                }
            } catch (Throwable t) {
                LOGGER.error(ExceptionUtils.getStackTrace(t));
            }
        }
    }

    private boolean isImage(String fileName) {
        boolean isImage = false;
        try (FileInputStream fis = new FileInputStream(new File(JAR_FOLDER + fileName))) {
            isImage = ImageIO.read(fis) != null;
        } catch (IOException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return isImage;
    }
}
