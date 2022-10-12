package me.koutachan.badpacketcheck.check.impl.keepalive;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import me.koutachan.badpacketcheck.check.Check;
import me.koutachan.badpacketcheck.check.CheckType;
import me.koutachan.badpacketcheck.check.PacketReceived;
import me.koutachan.badpacketcheck.data.PlayerData;

@CheckType(name = "Pong", type = "B")
public class PongB extends Check {
    public PongB(PlayerData data) {
        super(data);
    }

    private PacketTypeCommon previousPacket;
    private int lastSize, lastId;

    @Override
    public void onPacketReceived(PacketReceived event) {
        if (event.is(PacketType.Play.Client.KEEP_ALIVE)) {
            final int currentSize = data.getKeepAliveProcessor().getSize();

            if (currentSize > 0) {
                final int v = (currentSize - lastSize) - (data.getKeepAliveProcessor().getCurrentId() - lastId);

                if (v == 0 && v != lastSize) {
                    fail();
                }
            }
        }

        this.previousPacket = event.getType();
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.KEEP_ALIVE) {
            lastSize = data.getKeepAliveProcessor().getSize();
            lastId = data.getKeepAliveProcessor().getCurrentId();

            data.getKeepAliveProcessor().ready(v -> {
               if (previousPacket != PacketType.Play.Client.KEEP_ALIVE) {
                   fail();
               }
            });
        }
    }
}
