package com.xip.impl;

import com.xip.iface.XipPost;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramSearchTagsRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUploadPhotoRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchTagsResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchTagsResultTag;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.xip.impl.XipPublishingSessionImpl.JAR_FOLDER;

public class XipPostImpl implements XipPost {

    private final static Logger LOGGER = Logger.getLogger(XipPostImpl.class);

    private static final int TAGS_LIMIT = 20;
    private static final String HASH = "#";

    private final Instagram4j iClient;
    private final String fileName;
    private final String tag;

    public XipPostImpl(Instagram4j iClient, String fileName, String tag) {
        this.iClient = iClient;
        this.fileName = fileName;
        this.tag = tag;
    }

    @Override
    public void publish() throws IOException {
        iClient.setup();
        iClient.login();
        File imageToPublish = new File(JAR_FOLDER + fileName);
        InstagramUploadPhotoRequest request = new InstagramUploadPhotoRequest(
                imageToPublish,
                hashTagsDescription());
        iClient.sendRequest(request);
        FileUtils.deleteQuietly(imageToPublish);
    }

    private String hashTagsDescription() throws IOException {
        StringBuilder desc = new StringBuilder();
        InstagramSearchTagsResult tagsResult = iClient.sendRequest(new InstagramSearchTagsRequest(tag));
        List<InstagramSearchTagsResultTag> results = tagsResult.getResults();
        results.stream()
                .sorted((o1, o2) -> o2.media_count - o1.media_count)
                .limit(TAGS_LIMIT)
                .forEach(r -> {
                    desc.append(HASH)
                            .append(r.getName())
                            .append(" ");
                });
        LOGGER.info("HashTags :: " + desc);
        return desc.toString();
    }
}
