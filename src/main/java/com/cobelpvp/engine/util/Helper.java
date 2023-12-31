package com.cobelpvp.engine.util;

import lombok.Getter;
import java.util.regex.Pattern;

@SuppressWarnings("RegExpRedundantEscape")
@Getter
public class Helper {

    @Getter private static Helper instance;

    private final Pattern pattern = Pattern.compile(".*\\$\\{[^}]*\\}.*");

    public Helper() {
        instance = this;
    }

    public boolean isInvalidJNDI(String message) {
        return pattern.matcher(message).matches();
    }
}
