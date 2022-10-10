package me.koutachan.badpacketcheck.data.impl;

import me.koutachan.badpacketcheck.data.PlayerData;

public class PositionProcessor {
    private final PlayerData data;

    private double x, y, z;

    public PositionProcessor(PlayerData data) {
        this.data = data;
    }

    public void onFlying(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
