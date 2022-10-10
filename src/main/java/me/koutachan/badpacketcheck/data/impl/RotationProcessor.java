package me.koutachan.badpacketcheck.data.impl;

import me.koutachan.badpacketcheck.data.PlayerData;

public class RotationProcessor {
    private PlayerData data;

    public RotationProcessor(PlayerData data) {
        this.data = data;
    }

    private float lastYaw;
    private float lastPitch;
    private float yaw;
    private float pitch;

    public void onFlying(final float yaw, final float pitch) {
        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;

        this.yaw = yaw;
        this.pitch = pitch;
     }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float getLastYaw() {
        return lastYaw;
    }

    public float getLastPitch() {
        return lastPitch;
    }

    public boolean isRotationChanged(final float yaw, final float pitch) {
        return yaw != this.yaw || pitch != this.pitch;
    }
}
