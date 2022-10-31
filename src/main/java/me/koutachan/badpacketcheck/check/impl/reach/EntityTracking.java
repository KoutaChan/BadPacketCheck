package me.koutachan.badpacketcheck.check.impl.reach;

import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.util.Vector3d;

import java.util.UUID;

public class EntityTracking {
    private Vector3d vector3d;
    private Vector3d packetLocation;
    private float yaw;
    private float pitch;
    private boolean ground = true;

    private final UUID uuid;
    private final long entityId;
    private final EntityType entityType;

    public EntityTracking(UUID uuid, final long entityId, EntityType type, Vector3d vector3d, float yaw, float pitch) {
        this.uuid = uuid;
        this.entityId = entityId;
        this.entityType = type;

        this.vector3d = vector3d;
        this.packetLocation = new Vector3d(vector3d.x, vector3d.y, vector3d.z);
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Vector3d getVector3d() {
        return vector3d;
    }

    public void setVector3d(Vector3d vector3d) {
        this.vector3d = vector3d;

        this.packetLocation = new Vector3d(vector3d.x, vector3d.y, vector3d.z);
    }

    public Vector3d getPacketLocation() {
        return packetLocation;
    }

    public void setPacketLocation(Vector3d packetLocation) {
        this.packetLocation = packetLocation;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean isGround() {
        return ground;
    }

    public void setGround(boolean ground) {
        this.ground = ground;
    }

    public UUID getUUID() {
        return uuid;
    }

    public long getEntityId() {
        return entityId;
    }

    public EntityType getEntityType() {
        return entityType;
    }
}
