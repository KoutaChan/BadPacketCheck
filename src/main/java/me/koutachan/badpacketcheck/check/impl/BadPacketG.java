package me.koutachan.badpacketcheck.check.impl;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import me.koutachan.badpacketcheck.check.Check;
import me.koutachan.badpacketcheck.check.CheckType;
import me.koutachan.badpacketcheck.check.PacketReceived;
import me.koutachan.badpacketcheck.data.PlayerData;

@CheckType(name = "BadPacket", type = "G")
public class BadPacketG extends Check {
    public BadPacketG(PlayerData data) {
        super(data);
    }

    //TODO: Create Teleport Manager

    private boolean shouldSkip;

    @Override
    public void onPacketReceived(PacketReceived event) {
        if (event.getType() == PacketType.Play.Client.USE_ITEM) {
            //TODO: Mojang????
            shouldSkip = true;
        } else if (shouldSkip && event.is(PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION)) {
            shouldSkip = false;
        } else if (event.isRotation()) {
            final float yaw = data.getRotationProcessor().getYaw();
            final float pitch = data.getRotationProcessor().getPitch();

            final float lastYaw = data.getRotationProcessor().getLastYaw();
            final float lastPitch = data.getRotationProcessor().getLastPitch();

            if (shouldSkip || (yaw == lastYaw && pitch == lastPitch) || Math.abs(pitch) > 90) {
                fail();
            }
        }

        if (event.isPosition()) {

        }
    }
}
