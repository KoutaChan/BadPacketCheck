package me.koutachan.badpacketcheck.check;

import me.koutachan.badpacketcheck.check.impl.*;
import me.koutachan.badpacketcheck.data.PlayerData;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class CheckProcessor {

    private final List<Check> checks = new ArrayList<>();
    private static final List<Class<? extends Check>> classes = new ArrayList<Class<? extends Check>>() {{
        //add(BadPacketA.class); too many false positive
        //add(BadPacketB.class); not implement!
        add(BadPacketC.class);
        add(BadPacketD.class);
        add(BadPacketE.class);
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