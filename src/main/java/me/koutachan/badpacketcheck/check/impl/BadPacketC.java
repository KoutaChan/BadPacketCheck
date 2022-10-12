package me.koutachan.badpacketcheck.check.impl;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import me.koutachan.badpacketcheck.check.Check;
import me.koutachan.badpacketcheck.check.CheckType;
import me.koutachan.badpacketcheck.check.PacketReceived;
import me.koutachan.badpacketcheck.data.PlayerData;

import java.util.HashMap;
import java.util.Map;

@CheckType(name = "BadPacket", type = "C")
public class BadPacketC extends Check {
    public BadPacketC(PlayerData data) {
        super(data);
    }

    private int streak;
    private final Map<DiggingAction, Integer> map = new HashMap<>();

    private final Map<DiggingAction, Integer> limits = new HashMap<DiggingAction, Integer>() {{
        put(DiggingAction.DROP_ITEM, 35);
        put(DiggingAction.DROP_ITEM_STACK, 35);
        put(DiggingAction.RELEASE_USE_ITEM, 35);
        put(DiggingAction.SWAP_ITEM_WITH_OFFHAND, 35);

        put(DiggingAction.START_DIGGING, 25);
        put(DiggingAction.FINISHED_DIGGING, 25);
        put(DiggingAction.CANCELLED_DIGGING, 25);
    }};

    @Override
    public void onPacketReceived(PacketReceived event) {
        if (event.is(PacketType.Play.Client.PLAYER_DIGGING)) {
            WrapperPlayClientPlayerDigging wrapper = new WrapperPlayClientPlayerDigging(event.getPacket());

            final int typeStreak = map.getOrDefault(wrapper.getAction(), 0) + 1;

            if (streak++ > 50 || typeStreak > limits.getOrDefault(wrapper.getAction(), Integer.MAX_VALUE)) {
                fail();
            }

            map.put(wrapper.getAction(), typeStreak);
        } else if (event.isFlying()) {
            map.clear();

            streak = 0;
        }
    }
}