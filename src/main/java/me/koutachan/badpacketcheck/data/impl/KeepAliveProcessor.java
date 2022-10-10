package me.koutachan.badpacketcheck.data.impl;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPong;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPing;
import me.koutachan.badpacketcheck.check.PacketReceived;
import me.koutachan.badpacketcheck.data.PlayerData;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class KeepAliveProcessor {
    private final PlayerData data;

    public KeepAliveProcessor(PlayerData data) {
        this.data = data;
    }

    private final Map<Integer, Consumer<Integer>> hold = new HashMap<>();
    private int id;

    public int ready(Consumer<Integer> consumer) {
        final int id = this.id++;

        data.getUser().sendPacket(new WrapperPlayServerPing(id));
        hold.put(id, consumer);

        return id;
    }

    public void onPongEvent(PacketReceived event) {
        WrapperPlayClientPong pong = new WrapperPlayClientPong(event.getPacket());

        Consumer<Integer> consumer = hold.remove(pong.getId());

        if (consumer != null) {
            consumer.accept(pong.getId());
        }

        data.getCheckProcessor().getChecks().forEach(v -> v.onPongEvent(pong));
    }
}
