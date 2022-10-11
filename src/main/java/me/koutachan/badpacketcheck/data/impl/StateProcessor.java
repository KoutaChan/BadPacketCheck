package me.koutachan.badpacketcheck.data.impl;

import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChangeGameState;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerJoinGame;
import me.koutachan.badpacketcheck.data.PlayerData;

public class StateProcessor {
    private PlayerData data;

    public StateProcessor(PlayerData data) {
        this.data = data;
    }

    private GameMode gameMode = null;
    private boolean enabledRespawnScreen;
    private boolean sprint, sneak;

    public void handleJoinGame(WrapperPlayServerJoinGame wrapper) {
        data.getKeepAliveProcessor().ready(v -> {
            gameMode = wrapper.getGameMode();
            enabledRespawnScreen = wrapper.isRespawnScreenEnabled();
        });
    }

    public void handleChangeGameMode(WrapperPlayServerChangeGameState state) {
        if (state.getReason() == WrapperPlayServerChangeGameState.Reason.CHANGE_GAME_MODE) {
            data.getKeepAliveProcessor().ready(v -> gameMode = GameMode.getById(((Float) state.getValue()).intValue()));
        } else if (state.getReason() == WrapperPlayServerChangeGameState.Reason.ENABLE_RESPAWN_SCREEN) {
            data.getKeepAliveProcessor().ready(v -> enabledRespawnScreen = state.getValue() == .0F);
        }
    }

    public void handleAction(WrapperPlayClientEntityAction action) {
        switch (action.getAction()) {
            case START_SPRINTING:
                sprint = true;
                break;
            case STOP_SPRINTING:
                sprint = false;
                break;
            case START_SNEAKING:
                sneak = true;
                break;
            case STOP_SNEAKING:
                sneak = false;
                break;
        }
    }


    public GameMode getGameMode() {
        return gameMode;
    }

    public boolean isEnabledRespawnScreen() {
        return enabledRespawnScreen;
    }

    public boolean isSprint() {
        return sprint;
    }

    public boolean isSneak() {
        return sneak;
    }
}
