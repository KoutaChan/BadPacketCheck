package me.koutachan.badpacketcheck.check;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CheckType {
    String name();

    String type();
}
