package me.koutachan.badpacketcheck.check.impl;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import me.koutachan.badpacketcheck.check.Check;
import me.koutachan.badpacketcheck.check.CheckType;
import me.koutachan.badpacketcheck.check.PacketReceived;
import me.koutachan.badpacketcheck.data.PlayerData;
import net.kyori.adventure.text.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CheckType(name = "BadPacket", type = "A")
public class BadPacketA extends Check {
    public BadPacketA(PlayerData data) {
        super(data);
    }

    private final Map<PacketTypeCommon, Long> map = new HashMap<>();

    private final List<PacketTypeCommon> ignoredType = Arrays
            .asList(PacketType.Play.Client.ANIMATION,
                    PacketType.Play.Client.USE_ITEM,
                    PacketType.Play.Client.ENTITY_ACTION,
                    PacketType.Play.Client.CLICK_WINDOW,
                    PacketType.Play.Client.PLAYER_DIGGING,
                    PacketType.Play.Client.INTERACT_ENTITY,
                    PacketType.Play.Client.HELD_ITEM_CHANGE,
                    PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT,
                    PacketType.Play.Client.PONG,
                    PacketType.Play.Client.KEEP_ALIVE);

    @Override
    public void onPacketReceived(PacketReceived event) {
        if (event.isFlying()) {
            for (Map.Entry<PacketTypeCommon, Long> packet : map.entrySet()) {
                if (packet.getValue() < 5) {
                    map.replace(packet.getKey(), packet.getValue() + 1);
                }
            }
        } else if (!ignoredType.contains(event.getType())) {
            final long ratio = map.getOrDefault(event.getType(), 1L);

            if (ratio == 0) {
                fail();
            }

            map.put(event.getType(), 0L);
        }
    }
}