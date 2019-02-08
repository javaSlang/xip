package com.xip.iface;

import java.io.IOException;

public interface XipPublishingSession {
    void goAhead() throws IOException, InterruptedException;
}
