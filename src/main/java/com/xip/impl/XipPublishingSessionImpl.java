package com.xip.impl;

import org.brunocvcunha.instagram4j.Instagram4j;

import java.io.IOException;
import java.nio.file.*;

public class XipPublishingSessionImpl implements com.xip.iface.XipPublishingSession {

    static final String JAR_FOLDER = "./";

    private final String iLogin;
    private final String iPassword;
    private final String tag;
    private final WatchService watchService;

    public XipPublishingSessionImpl(String iLogin, String iPassword, String tag) throws IOException {
        this.iLogin = iLogin;
        this.iPassword = iPassword;
        this.tag = tag;
        this.watchService = FileSystems.getDefault().newWatchService();
        Paths.get(JAR_FOLDER).register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE);
    }

    public void goAhead() throws InterruptedException {
        WatchKey key;
        while ((key = watchService.take()) != null) {
            key.pollEvents().forEach(event -> {
                new XipFileEventProcessingImpl(
                        event,
                        Instagram4j.builder().username(iLogin).password(iPassword).build(),
                        tag
                ).doNow();
            });
            key.reset();
        }
    }
}
