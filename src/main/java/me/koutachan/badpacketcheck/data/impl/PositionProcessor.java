package me.koutachan.badpacketcheck.data.impl;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import me.koutachan.badpacketcheck.data.PlayerData;
import net.kyori.adventure.text.BlockNBTComponent;

public class PositionProcessor {
    private final PlayerData data;

    private double x, y, z;

    public PositionProcessor(PlayerData data) {
        this.data = data;
    }

    public void onPositionProcessor(WrapperPlayClientPlayerFlying position) {
        this.x = position.getLocation().getX();
        this.y = position.getLocation().getY();
        this.z = position.getLocation().getZ();

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
