package me.koutachan.badpacketcheck.check.impl;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerAbilities;
import me.koutachan.badpacketcheck.check.Check;
import me.koutachan.badpacketcheck.check.CheckType;
import me.koutachan.badpacketcheck.check.PacketReceived;
import me.koutachan.badpacketcheck.data.PlayerData;

@CheckType(name = "BadPacket", type = "B")
public class BadPacketB extends Check {
    public BadPacketB(PlayerData data) {
        super(data);
    }

    @Override
    public void onPacketReceived(PacketReceived event) {
        if (event.is(PacketType.Play.Client.PLAYER_ABILITIES)) {
            WrapperPlayClientPlayerAbilities wrapper = new WrapperPlayClientPlayerAbilities(event.getPacket());

           // wrapper.
           // wrapper.isInCreativeMode().ifPresent(c -> );
        }
    }
}