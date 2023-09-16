package com.cobelpvp.engine.commands.players;

import com.cobelpvp.engine.profile.Conversation;
import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CmdReply {

    @Command(names = {"reply", "r"})
    public static void replyCommand(Player sender, @Param(name = "message", wildcard = true) String message) {
        Profile senderProfile = Profile.getByUsername(sender.getName());
        Conversation conversation = senderProfile.getConversations().getLastRepliedConversation();

        if (conversation != null) {
            if (conversation.validate()) {
                conversation.sendMessage(sender, Bukkit.getPlayer(conversation.getPartner(sender.getUniqueId())), message);
            } else {
                sender.sendMessage(ChatColor.RED + "You can no longer reply to that player.");
            }
        } else {
            sender.sendMessage(ChatColor.GOLD + "You have nobody to reply to.");
        }
    }
}
