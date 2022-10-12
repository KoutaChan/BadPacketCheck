package me.koutachan.badpacketcheck.check.impl;

import me.koutachan.badpacketcheck.check.Check;
import me.koutachan.badpacketcheck.check.PacketReceived;
import me.koutachan.badpacketcheck.data.PlayerData;

public class BadPacketJ extends Check {
    public BadPacketJ(PlayerData data) {
        super(data);
    }

    @Override
    public void onPacketReceived(PacketReceived event) {
        if (event.isPosition()) {

        }
    }
}
