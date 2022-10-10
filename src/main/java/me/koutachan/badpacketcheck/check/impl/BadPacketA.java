package me.koutachan.badpacketcheck.check.impl;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import me.koutachan.badpacketcheck.check.Check;
import me.koutachan.badpacketcheck.check.CheckType;
import me.koutachan.badpacketcheck.check.PacketReceived;
import me.koutachan.badpacketcheck.data.PlayerData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@CheckType(name = "BadPacket", type = "A")
public class BadPacketA extends Check {
    public BadPacketA(PlayerData data) {
        super(data);
    }

    private final Map<PacketTypeCommon, Long> map = new HashMap<>();

    private final static Set<PacketTypeCommon> ignoredType = new HashSet<PacketTypeCommon>() {{
        add(PacketType.Play.Client.ANIMATION);
        add(PacketType.Play.Client.USE_ITEM);
        add(PacketType.Play.Client.ENTITY_ACTION);
        add(PacketType.Play.Client.CLICK_WINDOW);
        add(PacketType.Play.Client.PLAYER_DIGGING);
        add(PacketType.Play.Client.INTERACT_ENTITY);
        add(PacketType.Play.Client.HELD_ITEM_CHANGE);
        add(PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT);
        add(PacketType.Play.Client.PONG);
        add(PacketType.Play.Client.KEEP_ALIVE);
    }};


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