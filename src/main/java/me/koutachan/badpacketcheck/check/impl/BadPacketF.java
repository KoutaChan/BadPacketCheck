package me.koutachan.badpacketcheck.check.impl;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import me.koutachan.badpacketcheck.check.Check;
import me.koutachan.badpacketcheck.check.CheckType;
import me.koutachan.badpacketcheck.check.PacketReceived;
import me.koutachan.badpacketcheck.data.PlayerData;

/**
 * sending spectate packet while not spectator
 */
@CheckType(name = "BadPacket", type = "F")
public class BadPacketF extends Check {
    public BadPacketF(PlayerData data) {
        super(data);
    }

    @Override
    public void onPacketReceived(PacketReceived event) {
        if (event.is(PacketType.Play.Client.SPECTATE) && data.getStateProcessor().getGameMode() != GameMode.SPECTATOR) {
            fail();
        }
    }
}