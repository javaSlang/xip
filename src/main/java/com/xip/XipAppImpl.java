package com.xip;

import com.xip.iface.XipApp;
import com.xip.iface.XipPublishingSession;
import com.xip.impl.XipPublishingSessionImpl;

import java.io.IOException;

public class XipAppImpl implements XipApp {

    private final XipPublishingSession publishingSession;

    public XipAppImpl(String[] args) throws IOException {
        if (args.length != 3) {
            throw new IllegalArgumentException(
                    "Failed to run :: the app requires exactly 3 args :: login, password, key tag :: but actually passed " + args.length + " args.");
        }
        this.publishingSession = new XipPublishingSessionImpl(args[0], args[1], args[2]);
    }

    @Override
    public void run() throws IOException, InterruptedException {
        publishingSession.goAhead();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new XipAppImpl(args).run();
    }
}
