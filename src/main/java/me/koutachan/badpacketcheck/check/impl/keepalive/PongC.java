package me.koutachan.badpacketcheck.check.impl.keepalive;

import me.koutachan.badpacketcheck.check.Check;
import me.koutachan.badpacketcheck.check.CheckType;
import me.koutachan.badpacketcheck.data.PlayerData;

@CheckType(name = "Pong", type = "C")
public class PongC extends Check {
    public PongC(PlayerData data) {
        super(data);
    }

    @Override
    public void onPongEvent(final short id) {
        final int f = Math.abs(data.getKeepAliveProcessor().getCurrentId() - data.getKeepAliveProcessor().getSize() - id);

        //wrong transaction/pong timing
        if (f != 1) {
            fail();
        }
    }
}