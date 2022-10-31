package me.koutachan.badpacketcheck.check.impl.reach;


import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import com.google.common.base.Preconditions;
import me.koutachan.badpacketcheck.check.Check;
import me.koutachan.badpacketcheck.check.CheckType;
import me.koutachan.badpacketcheck.check.PacketReceived;
import me.koutachan.badpacketcheck.data.PlayerData;

import java.util.HashMap;
import java.util.Map;

//Ping -> Pong -> Position -> Ping -> Pong
//Ping -> Pong -> Position -> Ping -> Pong
//Ping -> Pong -> Position -> Ping -> Pong
//Ping -> Pong -> Position -> Ping -> Pong

//WRONG ENTITY TRACKING :(
//間違ってるトラッキング
//TODO: Add Player Tracking
@CheckType(name = "Reach", type = "A")
public class ReachA extends Check {
    public ReachA(PlayerData data) {
        super(data);
    }

    private boolean accept;

    @Override
    public void onPacketReceived(PacketReceived event) {
        if (event.isFlying()) {
            if (accept) {
                data.getUser().sendMessage("accepted");
            }

            /*data.getKeepAliveProcessor().ready(v -> {
                accept = true;
                data.getKeepAliveProcessor().ready(c -> {
                    accept = false;
                });
            });*/
        }

        if (event.is(PacketType.Play.Client.INTERACT_ENTITY)) {
            WrapperPlayClientInteractEntity interact = new WrapperPlayClientInteractEntity(event.getPacket());

            if (interact.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                System.out.println(interact.getTarget().toString());
                System.out.println(interact.getEntityId());

                EntityTracking tracking = this.tracking.get(interact.getEntityId());

                data.getUser().sendMessage("attacked entity pos=" + tracking.getVector3d());
                data.getUser().sendMessage("attacked entity yaw=" + tracking.getYaw());
                data.getUser().sendMessage("attacked entity pitch=" + tracking.getPitch());
                data.getUser().sendMessage("attacked entity isOnGround=" + tracking.isGround());
                data.getUser().sendMessage("attacked entity type=" + tracking.getEntityType().getName());

                final double distnace = new Vector3d(data.getPositionProcessor().getX(), data.getPositionProcessor().getY(), data.getPositionProcessor().getZ())
                        .distance(tracking.getVector3d());

                data.getUser().sendMessage("simple distance=" + distnace);
            }

        }

        if (!event.is(PacketType.Play.Client.PONG))
            System.out.println("C:" + event.getType());
    }

    private Map<Integer, EntityTracking> tracking = new HashMap<>();

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY) {
            WrapperPlayServerSpawnEntity entity = new WrapperPlayServerSpawnEntity(event);

            data.getKeepAliveProcessor().ready(v -> {
                tracking.put(entity.getEntityId(), new EntityTracking(entity.getUUID().orElse(null), entity.getEntityId(), entity.getEntityType(), entity.getPosition(), entity.getYaw(), entity.getPitch()));
            });
        } else if (event.getPacketType() == PacketType.Play.Server.SPAWN_LIVING_ENTITY) {
            WrapperPlayServerSpawnLivingEntity entity = new WrapperPlayServerSpawnLivingEntity(event);

            data.getKeepAliveProcessor().ready(v -> {
                tracking.put(entity.getEntityId(), new EntityTracking(entity.getEntityUUID(), entity.getEntityId(), entity.getEntityType(), entity.getPosition(), entity.getYaw(), entity.getPitch()));
            });
        } else if (event.getPacketType() == PacketType.Play.Server.ENTITY_RELATIVE_MOVE) {
            WrapperPlayServerEntityRelativeMove relative = new WrapperPlayServerEntityRelativeMove(event);

            data.getKeepAliveProcessor().ready(v -> {
                EntityTracking tracking = Preconditions.checkNotNull(this.tracking.get(relative.getEntityId()));

                tracking.setVector3d(updateEntityPosition(tracking.getPacketLocation(), relative.getDeltaX(), relative.getDeltaY(), relative.getDeltaZ()));
                tracking.setGround(relative.isOnGround());
            });
        } else if (event.getPacketType() == PacketType.Play.Server.ENTITY_HEAD_LOOK) {
            WrapperPlayServerEntityHeadLook relative = new WrapperPlayServerEntityHeadLook(event);

            data.getKeepAliveProcessor().ready(v -> {
                EntityTracking tracking = Preconditions.checkNotNull(this.tracking.get(relative.getEntityId()));

                tracking.setYaw(relative.getHeadYaw());
            });
        } else if (event.getPacketType() == PacketType.Play.Server.ENTITY_RELATIVE_MOVE_AND_ROTATION) {
            WrapperPlayServerEntityRelativeMoveAndRotation relative = new WrapperPlayServerEntityRelativeMoveAndRotation(event);

            data.getKeepAliveProcessor().ready(v -> {
                EntityTracking tracking = Preconditions.checkNotNull(this.tracking.get(relative.getEntityId()));

                tracking.setVector3d(updateEntityPosition(tracking.getPacketLocation(), relative.getDeltaX(), relative.getDeltaY(), relative.getDeltaZ()));
                tracking.setYaw(relative.getYaw());
                tracking.setPitch(relative.getPitch());
                tracking.setGround(relative.isOnGround());
            });
        } else if (event.getPacketType() == PacketType.Play.Server.ENTITY_TELEPORT) {
            WrapperPlayServerEntityTeleport teleport = new WrapperPlayServerEntityTeleport(event);

            data.getKeepAliveProcessor().ready(v -> {
                EntityTracking tracking = Preconditions.checkNotNull(this.tracking.get(teleport.getEntityId()));

                tracking.setVector3d(teleport.getPosition());
                tracking.setYaw(teleport.getYaw());
                tracking.setPitch(teleport.getPitch());
                tracking.setGround(teleport.isOnGround());
            });
        } else if (event.getPacketType() == PacketType.Play.Server.ENTITY_VELOCITY) {
            //現在これらを処理できない...
            WrapperPlayServerEntityVelocity velocity = new WrapperPlayServerEntityVelocity(event);

            data.getKeepAliveProcessor().ready(v -> {
                EntityTracking tracking = Preconditions.checkNotNull(this.tracking.get(velocity.getEntityId()));

                tracking.getVector3d().add(velocity.getVelocity());
            });
        } else if (event.getPacketType() == PacketType.Play.Server.DESTROY_ENTITIES) {
            WrapperPlayServerDestroyEntities destroyEntities = new WrapperPlayServerDestroyEntities(event);

            data.getKeepAliveProcessor().ready(v -> {
                for (int entityId : destroyEntities.getEntityIds()) {
                    tracking.remove(entityId);
                }
            });
        } else if (event.getPacketType() == PacketType.Play.Server.ENTITY_ROTATION) {
        }


        System.out.println("S:" + event.getPacketType());
    }

    public Vector3d updateEntityPosition(Vector3d vec3d, final double deltaX, final double deltaY, final double deltaZ) {
        final double x = deltaX == 0d ? vec3d.x : vec3d.x + deltaX;
        final double y = deltaY == 0d ? vec3d.y : vec3d.y + deltaY;
        final double z = deltaZ == 0d ? vec3d.z : vec3d.z + deltaZ;

        return new Vector3d(x, y, z);
    }

    public static long longFloor(double d) {
        long i = (long) d;
        return d < (double) i ? i - 1L : i;
    }
}