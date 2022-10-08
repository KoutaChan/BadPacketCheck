package me.koutachan.badpacketcheck.event;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPosition;
import me.koutachan.badpacketcheck.check.PacketReceived;
import me.koutachan.badpacketcheck.data.PlayerData;
import me.koutachan.badpacketcheck.data.PlayerDataUtils;

public class PacketEvent implements PacketListener {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        PlayerData data = PlayerDataUtils.getPlayerData(event.getUser());

        if (data != null) {
            PacketReceived packetReceived = new PacketReceived(event);

            if (packetReceived.isFlying()) {
                WrapperPlayClientPlayerFlying flying = new WrapperPlayClientPlayerFlying(event);

                if (flying.hasPositionChanged()) {
                    data.getPositionProcessor().onPositionProcessor(flying);
                }
            } else if (event.getPacketType() == PacketType.Play.Client.PONG) {
                data.getKeepAliveProcessor().onPongEvent(packetReceived);
            }

            data.getCheckProcessor().getChecks().forEach(check -> check.onPacketReceived(packetReceived));
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        PlayerData data =  PlayerDataUtils.getPlayerData(event.getUser());

        if (data != null)  {
            data.getCheckProcessor().getChecks().forEach(check -> check.onPacketSend(event));
        }
    }

    @Override
    public void onUserConnect(UserConnectEvent event) {
        PlayerDataUtils.createPlayerData(event.getUser());
    }

    @Override
    public void onUserDisconnect(UserDisconnectEvent event) {
        PlayerDataUtils.removePlayerData(event.getUser());
    }
}
