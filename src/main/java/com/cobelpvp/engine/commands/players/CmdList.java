package com.cobelpvp.engine.commands.players;

import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.rank.Rank;
import com.cobelpvp.atheneum.command.Command;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CmdList {

    @Command(names = {"list"})
    public static void listCommand(CommandSender sender) {
        List<Player> sortedPlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        sortedPlayers.sort(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                Profile p1 = Profile.getByUuid(o1.getUniqueId());
                Profile p2 = Profile.getByUuid(o2.getUniqueId());
                return p2.getActiveRank().getDisplayWeight() - p1.getActiveRank().getDisplayWeight();
            }
        });

        List<String> playerNames = new ArrayList<>();

        for (Player player : sortedPlayers) {
            Profile profile = Profile.getByUsername(player.getName());
            playerNames.add(profile.getActiveGrant().getRank().getGameColor() + player.getName());
        }

        List<Rank> sortedRanks = new ArrayList<>(Rank.getRanksMap().values());
        sortedRanks.sort(new Comparator<Rank>() {
            @Override
            public int compare(Rank o1, Rank o2) {
                return o2.getDisplayWeight() - o1.getDisplayWeight();
            }
        });

        List<String> rankNames = new ArrayList<>();

        for (Rank rank : sortedRanks) {
            rankNames.add(rank.getGameColor() + rank.getDisplayName());
        }

        sender.sendMessage(StringUtils.join(rankNames, ChatColor.WHITE + ", "));
        sender.sendMessage("(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + "): " + StringUtils.join(playerNames, ChatColor.WHITE + ", "));
    }
}
