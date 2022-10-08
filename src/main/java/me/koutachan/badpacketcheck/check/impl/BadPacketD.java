package me.koutachan.badpacketcheck.check.impl;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientStatus;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityStatus;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateHealth;
import me.koutachan.badpacketcheck.check.Check;
import me.koutachan.badpacketcheck.check.CheckType;
import me.koutachan.badpacketcheck.check.PacketReceived;
import me.koutachan.badpacketcheck.data.PlayerData;
import org.bukkit.Bukkit;

/**
 * Check Wrong Respawn Packet
 */
@CheckType(name = "BadPacket", type = "D")
public class BadPacketD extends Check {
    public BadPacketD(PlayerData data) {
        super(data);
    }

    private boolean dead;

    @Override
    public void onPacketReceived(PacketReceived event) {
        if (event.is(PacketType.Play.Client.CLIENT_STATUS)) {
            WrapperPlayClientClientStatus status = new WrapperPlayClientClientStatus(event.getPacket());

            if (status.getAction() == WrapperPlayClientClientStatus.Action.PERFORM_RESPAWN) {
                if (!dead) {
                    fail();
                }

                dead = false;
            }
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.UPDATE_HEALTH) {
            WrapperPlayServerUpdateHealth health = new WrapperPlayServerUpdateHealth(event);

            if (health.getHealth() <= 0) {
                dead = true;
            }
        } else if (event.getPacketType() == PacketType.Play.Server.RESPAWN) {
            data.getKeepAliveProcessor().ready(v -> dead = false);
        }
    }
}