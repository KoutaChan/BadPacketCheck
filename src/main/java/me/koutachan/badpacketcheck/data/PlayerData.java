package me.koutachan.badpacketcheck.data;

import com.github.retrooper.packetevents.protocol.player.User;
import me.koutachan.badpacketcheck.check.CheckProcessor;
import me.koutachan.badpacketcheck.data.impl.KeepAliveProcessor;
import me.koutachan.badpacketcheck.data.impl.PositionProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {

    private final User user;
    private final CheckProcessor checkProcessor = new CheckProcessor(this);
    private final KeepAliveProcessor keepAliveProcessor = new KeepAliveProcessor(this);
    private final PositionProcessor positionProcessor = new PositionProcessor(this);

    public PlayerData(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public CheckProcessor getCheckProcessor() {
        return checkProcessor;
    }

    public KeepAliveProcessor getKeepAliveProcessor() {
        return keepAliveProcessor;
    }

    public PositionProcessor getPositionProcessor() {
        return positionProcessor;
    }
}
