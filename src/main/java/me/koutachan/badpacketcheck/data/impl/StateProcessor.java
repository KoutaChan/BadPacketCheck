package me.koutachan.badpacketcheck.data.impl;

import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChangeGameState;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerJoinGame;
import me.koutachan.badpacketcheck.data.PlayerData;

public class StateProcessor {
    private PlayerData data;

    public StateProcessor(PlayerData data) {
        this.data = data;
    }

    private GameMode gameMode = null;

    public void handleJoinGame(WrapperPlayServerJoinGame wrapper) {
        data.getKeepAliveProcessor().ready(v -> gameMode = wrapper.getGameMode());
    }

    public void handleChangeGameMode(WrapperPlayServerChangeGameState state) {
        if (state.getReason() == WrapperPlayServerChangeGameState.Reason.CHANGE_GAME_MODE) {
            data.getKeepAliveProcessor().ready(v -> gameMode = GameMode.getById(((Float) state.getValue()).intValue()));
        }
    }

    public GameMode getGameMode() {
        return gameMode;
    }
}
