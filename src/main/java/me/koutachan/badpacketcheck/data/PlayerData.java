package me.koutachan.badpacketcheck.data;

import com.github.retrooper.packetevents.protocol.player.User;
import me.koutachan.badpacketcheck.check.Check;
import me.koutachan.badpacketcheck.check.CheckProcessor;
import me.koutachan.badpacketcheck.data.impl.KeepAliveProcessor;
import me.koutachan.badpacketcheck.data.impl.PositionProcessor;
import me.koutachan.badpacketcheck.data.impl.RotationProcessor;
import me.koutachan.badpacketcheck.data.impl.StateProcessor;

public class PlayerData {

    private final User user;
    private final CheckProcessor checkProcessor = new CheckProcessor(this);
    private final KeepAliveProcessor keepAliveProcessor = new KeepAliveProcessor(this);
    private final PositionProcessor positionProcessor = new PositionProcessor(this);
    private final RotationProcessor rotationProcessor = new RotationProcessor(this);
    private final StateProcessor stateProcessor = new StateProcessor(this);

    public PlayerData(User user) {
        this.user = user;

        checkProcessor.getChecks().forEach(Check::onInitialEvent);
    }

    public User getUser() {
        return user;
    }

    public CheckProcessor getCheckProcessor() {
        return checkProcessor;
    }

    public KeepAliveProcessor getKeepAliveProcessor() {
        return keepAliveProcessor;
    }

    public PositionProcessor getPositionProcessor() {
        return positionProcessor;
    }

    public RotationProcessor getRotationProcessor() {
        return rotationProcessor;
    }

    public StateProcessor getStateProcessor() {
        return stateProcessor;
    }
}
