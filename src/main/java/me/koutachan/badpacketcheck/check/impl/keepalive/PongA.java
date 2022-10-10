package me.koutachan.badpacketcheck.check.impl.keepalive;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import me.koutachan.badpacketcheck.check.Check;
import me.koutachan.badpacketcheck.check.CheckType;
import me.koutachan.badpacketcheck.check.PacketReceived;
import me.koutachan.badpacketcheck.data.PlayerData;

@CheckType(name = "Pong", type = "A")
public class PongA extends Check {
    public PongA(PlayerData data) {
        super(data);
    }

    private boolean received;

    @Override
    public void onPacketReceived(PacketReceived event) {
        if (event.is(PacketType.Play.Client.TELEPORT_CONFIRM)) {
            if (!received) {
                fail();
            }

            received = false;
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.PLAYER_POSITION_AND_LOOK) {
            data.getKeepAliveProcessor().ready(v -> received = true);
        }
    }
}
