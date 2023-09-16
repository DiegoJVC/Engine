package com.cobelpvp.engine.commands.players;

import com.cobelpvp.engine.profile.Conversation;
import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CmdMessage {

    @Command(names = {"m", "message", "msg", "tell"})
    public static void messageCommand(Player sender, @Param(name = "target") Player target, @Param(name = "message", wildcard = true) String message) {
        Profile targetProfile = Profile.getByUsername(target.getName());

        Profile senderProfile = Profile.getByUsername(sender.getName());

        if (targetProfile.getConversations().canBeMessagedBy(sender)) {
            Conversation conversation = senderProfile.getConversations().getOrCreateConversation(target);

            if (sender.isOp()) {
                conversation.sendMessage(sender, target, message);
            } else {
                if (conversation.validate()) {
                    conversation.sendMessage(sender, target, message);
                } else {
                    sender.sendMessage(ChatColor.RED + "This player has toggled private messages!");
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "This player has toggled private messages!");
        }
    }
}
