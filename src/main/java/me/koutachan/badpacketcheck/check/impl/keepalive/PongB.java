package me.koutachan.badpacketcheck.check.impl.keepalive;

import me.koutachan.badpacketcheck.check.Check;
import me.koutachan.badpacketcheck.check.CheckType;
import me.koutachan.badpacketcheck.data.PlayerData;

@CheckType(name = "Pong", type = "B")
public class PongB extends Check {
    public PongB(PlayerData data) {
        super(data);
    }
}