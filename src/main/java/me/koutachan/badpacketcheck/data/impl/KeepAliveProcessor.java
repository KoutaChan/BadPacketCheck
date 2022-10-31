package me.koutachan.badpacketcheck.data.impl;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPong;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientWindowConfirmation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPing;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import me.koutachan.badpacketcheck.BadPacketCheck;
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

    private final Map<Short, Consumer<Short>> waiting = new HashMap<>();
    private short shiftedNumber;

    public void ready(Consumer<Short> onPong) {
        final short currentId = (short) Math.abs(shiftedNumber++);

        if (BadPacketCheck.INSTANCE.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_17)) {
            data.getUser().sendPacket(new WrapperPlayServerPing(currentId));
        } else {
            data.getUser().sendPacket(new WrapperPlayServerWindowConfirmation(0, currentId, false));
        }

        waiting.put(currentId, onPong);
    }

    public void onPongEvent(PacketReceived event) {
        label: {
            short receivedId;

            if (event.is(PacketType.Play.Client.PONG)) {
                receivedId = (short) new WrapperPlayClientPong(event.getPacket()).getId();
            } else if (event.is(PacketType.Play.Client.WINDOW_CONFIRMATION)) {
                receivedId = new WrapperPlayClientWindowConfirmation(event.getPacket()).getActionId();
            } else {
                break label;
            }

            final Consumer<Short> onPong = waiting.remove(receivedId);

            if (onPong != null) {
                onPong.accept(receivedId);

                data.getCheckProcessor().getChecks().forEach(v -> v.onPongEvent(receivedId));
            }
        }
    }

    public int getCurrentId() {
        return shiftedNumber;
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
