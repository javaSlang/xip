package com.xip.impl;

import com.xip.iface.XipGrayScaleImage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.xip.impl.XipFileEventProcessingImpl.XIP_PREFIX;
import static com.xip.impl.XipPublishingSessionImpl.JAR_FOLDER;

public class XipGrayScaleImageImpl implements XipGrayScaleImage {

    private final static Logger LOGGER = Logger.getLogger(XipGrayScaleImageImpl.class);

    private final String fileName;

    public XipGrayScaleImageImpl(String fileName) {
        this.fileName = fileName;
    }

    public void prepare() {
        File input = new File(JAR_FOLDER + fileName);
        try (FileInputStream fis = new FileInputStream(input);
             FileOutputStream fos = new FileOutputStream(new File(JAR_FOLDER + XIP_PREFIX + fileName))) {
            BufferedImage image = ImageIO.read(fis);
            int width = image.getWidth();
            int height = image.getHeight();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    Color c = new Color(image.getRGB(j, i));
                    int red = (int) (c.getRed() * 0.299);
                    int green = (int) (c.getGreen() * 0.587);
                    int blue = (int) (c.getBlue() * 0.114);
                    Color newColor = new Color(red + green + blue,
                            red + green + blue, red + green + blue);
                    image.setRGB(j, i, newColor.getRGB());
                }
            }
            ImageIO.write(image, "jpg", fos);
        } catch (IOException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        FileUtils.deleteQuietly(input);
    }
}