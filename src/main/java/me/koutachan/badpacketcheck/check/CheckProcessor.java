package me.koutachan.badpacketcheck.check;

import me.koutachan.badpacketcheck.check.impl.*;
import me.koutachan.badpacketcheck.check.impl.keepalive.PongA;
import me.koutachan.badpacketcheck.check.impl.keepalive.PongB;
import me.koutachan.badpacketcheck.data.PlayerData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CheckProcessor {

    private final List<Check> checks = new ArrayList<>();
    private static final Set<Class<? extends Check>> classes = new HashSet<Class<? extends Check>>() {{
        //add(BadPacketA.class); too many false positive
        //add(BadPacketB.class); not implement!
        add(BadPacketC.class);
        add(BadPacketD.class);
        add(BadPacketE.class);
        add(BadPacketF.class);
        //
        //add(BadPacketG.class);
        add(BadPacketH.class);
        add(BadPacketI.class);
        add(PongA.class);
        add(PongB.class);
    }};

    private final PlayerData data;

    public CheckProcessor(PlayerData data) {
        this.data = data;

        register();
    }

    public void register() {
        for (Class<?> checkClass : classes) {
            try {
                checks.add((Check) checkClass.getConstructor(PlayerData.class)
                        .newInstance(data));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<Check> getChecks() {
        return checks;
    }
}