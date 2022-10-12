package me.koutachan.badpacketcheck.check.impl;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import me.koutachan.badpacketcheck.check.Check;
import me.koutachan.badpacketcheck.check.CheckType;
import me.koutachan.badpacketcheck.check.PacketReceived;
import me.koutachan.badpacketcheck.data.PlayerData;

/**
 * Wrong BlockPlacement Packet
 */
@CheckType(name = "BadPacket", type = "E")
public class BadPacketE extends Check {
    public BadPacketE(PlayerData data) {
        super(data);
    }

    @Override //TODO: add PacketLocation
    public void onPacketReceived(PacketReceived event) {
        if (event.is(PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT)) {
            WrapperPlayClientPlayerBlockPlacement placement = new WrapperPlayClientPlayerBlockPlacement(event.getPacket());

            final int x = (int) data.getPositionProcessor().getX();
            final int y = (int) data.getPositionProcessor().getY();
            final int z = (int) data.getPositionProcessor().getZ();

            final double distance = placement.getBlockPosition().toVector3d().subtract(x, y, z).length();

            if (distance > 8) {
                fail();
            }
        }
    }
}