package me.koutachan.badpacketcheck.check;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import me.koutachan.badpacketcheck.data.PlayerData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class Check {

    protected PlayerData data;
    protected long verbose;

    public Check(PlayerData data) {
        this.data = data;
    }

    public void onPacketReceived(PacketReceived event) {}

    public void onPacketSend(PacketSendEvent event) {}

    private CheckType getCheckType() {
        return this.getClass().getAnnotation(CheckType.class);
    }

    public void fail() {
        verbose++;

        for (Player player : Bukkit.getOnlinePlayers()) {
            CheckType type = getCheckType();

            //TODO: better alerts
            player.sendMessage("§b[!]" + data.getUser().getName() + " failed " + type.name() + " (" + type.type() + ") x" + verbose);
        }
    }
}