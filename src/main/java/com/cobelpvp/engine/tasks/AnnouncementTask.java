package com.cobelpvp.engine.tasks;

import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.engine.Engine;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.List;

public class AnnouncementTask {

    private List<String> announcements = Engine.getInstance().getConfig().getStringList("Announcements.List");
    private int count = 0;

    public AnnouncementTask() {
        Engine.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(Engine.getInstance(), () -> {
            String announce = getNextAnnouncerMessage();
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.sendMessage(announce);
            }
        }, 20L * 60, 20L * 60 * Engine.getInstance().getConfig().getInt("Announcements.Time"));
    }

    private String getNextAnnouncerMessage() {
        if (this.count >= this.announcements.size()) {
            this.count = 0;
        }

        String message = this.announcements.get(count);
        message = message.replace("\\n", System.lineSeparator());
        this.count++;

        return ColorText.translate(message);
    }
}
