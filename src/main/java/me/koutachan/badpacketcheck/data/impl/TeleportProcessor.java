package me.koutachan.badpacketcheck.data.impl;

import me.koutachan.badpacketcheck.data.PlayerData;

public class TeleportProcessor {
    private PlayerData data;

    public TeleportProcessor(PlayerData data) {
        this.data = data;
    }

    private boolean isTeleported;

    private long lastTeleport;

    public void handleTeleport() {
        lastTeleport = 0;
    }

    public  void handleFlying()  {
        lastTeleport++;
    }

    public long getLastTeleport() {
        return lastTeleport;
    }
}
