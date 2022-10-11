package me.koutachan.badpacketcheck.check.impl;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindowButton;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCloseWindow;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenWindow;
import me.koutachan.badpacketcheck.check.Check;
import me.koutachan.badpacketcheck.check.CheckType;
import me.koutachan.badpacketcheck.check.PacketReceived;
import me.koutachan.badpacketcheck.data.PlayerData;

@CheckType(name = "BadPacket", type = "I")
public class BadPacketI extends Check {
    public BadPacketI(PlayerData data) {
        super(data);
    }

    private boolean open;
    private int containerId = 0;

    @Override
    public void onPacketReceived(PacketReceived event) {
        if (event.is(PacketType.Play.Client.CLOSE_WINDOW)) {
            containerId = 0;
            open = false;
        } else if (event.is(PacketType.Play.Client.CLICK_WINDOW)) {
            WrapperPlayClientClickWindow window = new WrapperPlayClientClickWindow(event.getPacket());
            final int windowId = window.getWindowId();
            if (windowId == 0) {
                open = true;
            } else if (windowId != containerId) {
                fail();
            }
        } else if (event.is(PacketType.Play.Client.CLICK_WINDOW_BUTTON)) {
            WrapperPlayClientClickWindowButton window = new WrapperPlayClientClickWindowButton(event.getPacket());
            final int windowId = window.getWindowId();

            if (windowId == 0) {
                open = true;
            } else if (windowId != containerId) {
                fail();
            }
        } else if (event.isPosition() && (containerId != 0 || open) && (data.getStateProcessor().isSprint() || data.getStateProcessor().isSneak())) {
            //additional check
            fail();
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.OPEN_WINDOW) {
            WrapperPlayServerOpenWindow window = new WrapperPlayServerOpenWindow(event);

            data.getKeepAliveProcessor().ready(v -> {
               containerId = window.getContainerId();
            });
        } else if (event.getPacketType() == PacketType.Play.Server.CLOSE_WINDOW) {
            WrapperPlayServerCloseWindow window = new WrapperPlayServerCloseWindow(event);

            final int windowId = window.getWindowId();

            if (windowId != 0 && windowId != containerId) {
                fail();
            } else {
                data.getKeepAliveProcessor().ready(v -> containerId = 0);
            }

            open = false;
        }
    }
}