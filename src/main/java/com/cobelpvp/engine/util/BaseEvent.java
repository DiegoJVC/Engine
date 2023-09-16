package com.cobelpvp.engine.util;

import com.cobelpvp.engine.Engine;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BaseEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public void call() {
        Engine.getInstance().getServer().getPluginManager().callEvent(this);
    }

}
