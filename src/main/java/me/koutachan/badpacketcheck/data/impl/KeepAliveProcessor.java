package me.koutachan.badpacketcheck.data.impl;

import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
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

    private final Map<Integer, Consumer<Integer>> waiting = new HashMap<>();
    private int id;

    public int ready(Consumer<Integer> onPong) {
        final int currentId = this.id++;

        data.getUser().sendPacket(new WrapperPlayServerPing(currentId));
        waiting.put(currentId, onPong);

        return currentId;
    }

    public void onPongEvent(PacketReceived event) {
        WrapperPlayClientPong pong = new WrapperPlayClientPong(event.getPacket());

        Consumer<Integer> onPong = waiting.remove(pong.getId());

        if (onPong != null) {
            onPong.accept(pong.getId());

            data.getCheckProcessor().getChecks().forEach(v -> v.onPongEvent(pong));
        }
    }

    public int getCurrentId() {
        return id;
    }

    public int getSize() {
        return waiting.size();
    }

    public static class PacketHolder {
        private final PacketTypeCommon packetType;
        private final Consumer<Integer> onPong;

        public PacketHolder(final Consumer<Integer> onPong, PacketTypeCommon packetType) {
            this.onPong = onPong;
            this.packetType = packetType;
        }

        public PacketTypeCommon getPacketType() {
            return packetType;
        }

        public Consumer<Integer> getOnPong() {
            return onPong;
        }
    }
}
