package me.koutachan.badpacketcheck.check.impl;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;
import me.koutachan.badpacketcheck.check.Check;
import me.koutachan.badpacketcheck.check.CheckType;
import me.koutachan.badpacketcheck.check.PacketReceived;
import me.koutachan.badpacketcheck.data.PlayerData;

@CheckType(name = "BadPacket", type = "H")
public class BadPacketH extends Check {
    public BadPacketH(PlayerData data) {
        super(data);
    }

    private int last;

    @Override
    public void onPacketReceived(PacketReceived event) {
        if (event.is(PacketType.Play.Client.HELD_ITEM_CHANGE)) {
            WrapperPlayClientHeldItemChange wrapper = new WrapperPlayClientHeldItemChange(event.getPacket());

            final int slot = wrapper.getSlot();

            if (last == slot) {
                fail();
            }

            this.last = slot;
        }
    }
}