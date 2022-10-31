package me.koutachan.badpacketcheck.event;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChangeGameState;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerJoinGame;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerPositionAndLook;
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
                    data.getPositionProcessor().onFlying(flying.getLocation().getX(), flying.getLocation().getY(), flying.getLocation().getZ());
                }

                if (flying.hasRotationChanged()) {
                    data.getRotationProcessor().onFlying(flying.getLocation().getYaw(), flying.getLocation().getPitch());
                }

                data.getTeleportProcessor().handleFlying();
            } else if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION) {
                WrapperPlayClientEntityAction action = new WrapperPlayClientEntityAction(event);

                data.getStateProcessor().handleAction(action);
            }

            data.getKeepAliveProcessor().onPongEvent(packetReceived);

            data.getCheckProcessor().getChecks().forEach(check -> check.onPacketReceived(packetReceived));
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        PlayerData data =  PlayerDataUtils.getPlayerData(event.getUser());

        if (data != null)  {
            data.getCheckProcessor().getChecks().forEach(check -> check.onPacketSend(event));

            if (event.getPacketType() == PacketType.Play.Server.JOIN_GAME) {
                WrapperPlayServerJoinGame wrapper = new WrapperPlayServerJoinGame(event);

                data.getStateProcessor().handleJoinGame(wrapper);
            } else if (event.getPacketType() == PacketType.Play.Server.CHANGE_GAME_STATE) {
                WrapperPlayServerChangeGameState wrapper = new WrapperPlayServerChangeGameState(event);

                data.getStateProcessor().handleChangeGameMode(wrapper);
            } else if (event.getPacketType() == PacketType.Play.Server.PLAYER_POSITION_AND_LOOK) {
                WrapperPlayServerPlayerPositionAndLook wrapper = new WrapperPlayServerPlayerPositionAndLook(event);

                data.getKeepAliveProcessor().ready(v -> {
                    data.getPositionProcessor().onFlying(wrapper.getX(), wrapper.getY(), wrapper.getZ());
                    if (data.getRotationProcessor().isRotationChanged(wrapper.getYaw(), wrapper.getPitch())) {
                        data.getRotationProcessor().onFlying(wrapper.getYaw(), wrapper.getPitch());
                    }
                    data.getTeleportProcessor().handleTeleport();
                });
            } else if (event.getPacketType() == PacketType.Play.Server.DISCONNECT) {
                event.setCancelled(true);
            }
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
