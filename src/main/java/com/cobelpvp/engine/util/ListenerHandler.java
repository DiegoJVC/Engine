package com.cobelpvp.engine.util;

import com.cobelpvp.atheneum.util.ClassUtils;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class ListenerHandler {

    public static void loadListenersFromPackage(Plugin plugin, String packageName) {
        for (Class<?> clazz : ClassUtils.getClassesInPackage(plugin, packageName)) {
            if (isListener(clazz)) {
                try {
                    plugin.getServer().getPluginManager().registerEvents((Listener) clazz.newInstance(), plugin);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isListener(Class<?> clazz) {
        for (Class<?> interfaze : clazz.getInterfaces()) {
            if (interfaze == Listener.class) {
                return true;
            }
        }

        return false;
    }
}
