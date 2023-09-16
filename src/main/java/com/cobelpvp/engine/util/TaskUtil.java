package com.cobelpvp.engine.util;

import com.cobelpvp.engine.Engine;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskUtil {

    public static void runTaskAsync(Runnable runnable) {
        Engine.getInstance().getServer().getScheduler().runTaskAsynchronously(Engine.getInstance(), runnable);
    }

    public static void runTaskLater(Runnable runnable, long delay) {
        Engine.getInstance().getServer().getScheduler().runTaskLater(Engine.getInstance(), runnable, delay);
    }

    public static void runTaskLaterAsync(Runnable runnable, long delay) {
        Engine.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(Engine.getInstance(), runnable, delay);
    }

    public static void runTaskTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(Engine.getInstance(), delay, timer);
    }

    public static void runTaskTimer(Runnable runnable, long delay, long timer) {
        Engine.getInstance().getServer().getScheduler().runTaskTimer(Engine.getInstance(), runnable, delay, timer);
    }

    public static void runTask(Runnable runnable) {
        Engine.getInstance().getServer().getScheduler().runTask(Engine.getInstance(), runnable);
    }
}
