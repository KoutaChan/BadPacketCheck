package me.koutachan.badpacketcheck;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import me.koutachan.badpacketcheck.event.PacketEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class BadPacketCheck extends JavaPlugin {

    public static BadPacketCheck INSTANCE;

    @Override
    public void onLoad() {
        INSTANCE = this;

        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        PacketEvents.getAPI()
                .getEventManager().registerListener(new PacketEvent(), PacketListenerPriority.LOWEST);
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ServerVersion getServerVersion( ) {
        return PacketEvents.getAPI().getServerManager().getVersion();
    }
}
