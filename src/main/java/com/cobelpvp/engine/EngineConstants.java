package com.cobelpvp.engine;

import com.cobelpvp.atheneum.util.ColorText;
import org.apache.commons.lang.StringUtils;

public class EngineConstants {

    public static String getCenter(String message) {
        return ColorText.translate("&6&m" + StringUtils.repeat("-", 9) + "&r&6[&r" + message + "&6]&6&m" + StringUtils.repeat("-", 9));
    }
}
