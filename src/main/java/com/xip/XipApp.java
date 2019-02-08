package com.xip;

import com.xip.impl.XipPublishingSessionImpl;

import java.io.IOException;

public class XipApp {

    public static void main(String[] args) throws IOException, InterruptedException {
        new XipPublishingSessionImpl(args[0], args[1], args[2]).goAhead();
    }

}
