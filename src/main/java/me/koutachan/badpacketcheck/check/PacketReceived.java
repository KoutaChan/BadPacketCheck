package me.koutachan.badpacketcheck.check;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class PacketReceived {

    private final PacketReceiveEvent event;
    private final PacketTypeCommon type;

    public PacketReceived(PacketReceiveEvent event) {
        this.event = event;
        this.type = event.getPacketType();
    }

    public boolean is(PacketTypeCommon... packetTypes) {
        return Arrays.stream(packetTypes)
                .anyMatch(type -> this.type == type);
    }

    public boolean isFlying() {
        return type == PacketType.Play.Client.PLAYER_FLYING
                || type == PacketType.Play.Client.PLAYER_POSITION
                || type == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION
                || type == PacketType.Play.Client.PLAYER_ROTATION;
    }

    public boolean isPosition() {
        return type == PacketType.Play.Client.PLAYER_POSITION
                || type == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION
                || type == PacketType.Play.Client.PLAYER_ROTATION;
    }

    public PacketTypeCommon getType() {
        return type;
    }

    public PacketReceiveEvent getPacket() {
        return event;
    }

    public Player getAsPlayer() {
        return (Player) event.getPlayer();
    }

}