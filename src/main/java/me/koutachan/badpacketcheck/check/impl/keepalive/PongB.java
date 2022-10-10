package me.koutachan.badpacketcheck.check.impl.keepalive;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import me.koutachan.badpacketcheck.check.Check;
import me.koutachan.badpacketcheck.check.CheckType;
import me.koutachan.badpacketcheck.check.PacketReceived;
import me.koutachan.badpacketcheck.data.PlayerData;

@CheckType(name = "Pong", type = "B")
public class PongB extends Check {
    public PongB(PlayerData data) {
        super(data);
    }

    private long balance = 0;

    @Override
    public void onPacketReceived(PacketReceived event) {
        if (event.is(PacketType.Play.Client.KEEP_ALIVE)) {
            balance--;

            System.out.println("balance=" + balance);
        }
        System.out.println(event.getType());
        //System.out.println(event.getType());
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.KEEP_ALIVE) {
            balance++;
        }
    }
}
