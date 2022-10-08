package me.koutachan.badpacketcheck.data;

import com.github.retrooper.packetevents.protocol.player.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataUtils {
    private static final Map<User, PlayerData> data = new HashMap<>();

    public static void createPlayerData(User user) {
        data.put(user, new PlayerData(user));
    }

    public static void removePlayerData(User user) {
        data.remove(user);
    }

    public static PlayerData getPlayerData(User user) {
        return data.get(user);
    }

    public static Map<User, PlayerData> getPlayerData() {
        return data;
    }
}
