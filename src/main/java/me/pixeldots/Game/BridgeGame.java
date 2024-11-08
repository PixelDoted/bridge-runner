package me.pixeldots.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import me.pixeldots.BridgeConf;
import me.pixeldots.BridgeRunner;
import me.pixeldots.API.APIEventCaller;
import me.pixeldots.Game.data.DirectionVector;
import me.pixeldots.Game.data.PlayerStatistics;
import me.pixeldots.Game.data.WorldBlockData;
import me.pixeldots.Scoreboard.GameScoreboardUtils;
import me.pixeldots.Utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;

public class BridgeGame {

    public static Component scoredTitle = Utils.text("");

    public static void start() {
        BridgeRunner.Variables.PlayerStats.clear();
        
        List<Player> players = BridgeRunner.world.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            players.get(i).getInventory().clear(); // Clear the player's inventory

            Vector pos = BridgeRunner.Variables.LobbyPosition; // lobby position
            players.get(i).teleport(new Location(BridgeRunner.world, pos.getX(), pos.getY(), pos.getZ())); // teleport player to the lobby
            players.get(i).setGameMode(GameMode.ADVENTURE); // set player's gamemode to adventure
            players.get(i).sendMessage("The game will start soon"); // send starting soon message
        }
        Utils.Logger().info("[DEBUG] Setting up the Lobby Scoreboard");
        
        //ScoreboardUtils.RegisterLobbyBoard();
        BridgeRunner.StartingTime = Utils.getDateTime()+(30*1000);
        BridgeRunner.isStarting = true;
    }

    public static void run() {
        List<Player> players = BridgeRunner.world.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            players.get(i).getInventory().clear(); // Clear the player's inventory
        }

        // Calculate Minimum and Maximum Area
        Vector areaStart = BridgeRunner.Variables.AreaStart; 
        Vector areaEnd = BridgeRunner.Variables.AreaEnd;
        BridgeRunner.Variables.AreaMin = new Vector(Math.min(areaStart.getBlockX(), areaEnd.getBlockX()), Math.min(areaStart.getBlockY(), areaEnd.getBlockY()), Math.min(areaStart.getBlockZ(), areaEnd.getBlockZ()));
        BridgeRunner.Variables.AreaMax = new Vector(Math.max(areaStart.getBlockX(), areaEnd.getBlockX()), Math.max(areaStart.getBlockY(), areaEnd.getBlockY()), Math.max(areaStart.getBlockZ(), areaEnd.getBlockZ()));
        // Calculate Minimum and Maximum Area

        try {
            BridgeRunner.Variables.bluePoints = 0;
            BridgeRunner.Variables.redPoints = 0;

            CageBuilder.build(BridgeRunner.world, BridgeRunner.Variables.redCageCenter.pos, Material.GLASS, Material.GRAY_TERRACOTTA);
            CageBuilder.build(BridgeRunner.world, BridgeRunner.Variables.blueCageCenter.pos, Material.GLASS, Material.GRAY_TERRACOTTA);

            Utils.Logger().info("[DEBUG] Setting up Players");
            int nextAddTeam = 0;
            BridgeRunner.Variables.PlayersInGame.clear();
            for (int i = 0; i < players.size(); i++) {
                int teamID = nextAddTeam;
                while (teamID >= 2) {
                    teamID -= 2;
                }

                Player player = players.get(i);
                UUID uuid = player.getUniqueId();
                if (!BridgeRunner.Variables.PlayerStats.containsKey(uuid)) { // check if the player hasn't selected a team
                    BridgeRunner.Variables.PlayerStats.put(uuid, new PlayerStatistics(teamID)); // create Player's Statistics for this Game
                    nextAddTeam++; // add 1 to nextAddTeam
                }
                BridgeRunner.Variables.PlayersInGame.add(uuid);

                //player.customName()
                //PlayerUtils.setPlayerNameColor(player, (teamID == 0 ? "red" : "blue")); // update the player name

                BridgeRunner.ReleaseTime = Utils.getDateTime()+(5*1000);
                BridgeRunner.FallDamageDelay = Utils.getDateTime()+(7*1000);
                teleportPlayerToCage(teamID, player);
                player.setGameMode(GameMode.SURVIVAL); // set player's gamemode to survival
                player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()); // set the player's health
                givePlayerDefaultItems(player, player.getInventory(), teamID); // give the player defualt items
            }

            Utils.Logger().info("[DEBUG] Setting up the Scoreboard");
            GameScoreboardUtils.Register(); // create the games scoreboard
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        BridgeRunner.isRunning = true;
        if (BridgeConf.endless) BridgeRunner.GameStartedTime = Utils.getDateTime();
        else BridgeRunner.GameEndTime = Utils.getDateTime()+Utils.toMillisecondTime(BridgeConf.gameTime);
    }

    public static void stop() {
        BridgeRunner.isRunning = false;

        List<Player> players = BridgeRunner.world.getPlayers();
        for (int i = 0; i < players.size(); i++) { // Reset player data
            Player player = players.get(i);
            player.displayName(Utils.text(players.get(i).getName())); // reset the players displayName
            player.playerListName(Utils.text(players.get(i).getName())); // reset the players playerListName
            player.customName(Utils.text(players.get(i).getName())); // reset the players customName
            player.getInventory().clear(); // clear the players inventory

            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType()); // remove all potion effects
            }
        }

        for (int i = 0; i < BridgeRunner.Variables.BlocksPlaced.size(); i++) { // Remove placed blocks
            Vector pos = BridgeRunner.Variables.BlocksPlaced.get(i);
            BridgeRunner.world.getBlockAt(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()).setType(Material.AIR);
        }

        for (int i = 0; i < BridgeRunner.Variables.WorldBlocksDestroyed.size(); i++) {
            WorldBlockData data = BridgeRunner.Variables.WorldBlocksDestroyed.get(i);
            Block block = data.blockPos.getBlock();
            block.setType(data.blockType);
            data.blockPos.getWorld().setBlockData(data.blockPos, data.blockData);
        }
        BridgeRunner.Variables.WorldBlocksDestroyed.clear();
        

        List<Entity> entities = BridgeRunner.world.getEntities();
        for (int i = 0; i < entities.size(); i++) { // Clear Entitys
            EntityType type = entities.get(i).getType();
            if (type == EntityType.DROPPED_ITEM || type == EntityType.ARROW || type == EntityType.IRON_GOLEM || 
                type == EntityType.EGG || type == EntityType.SNOWBALL || type == EntityType.SILVERFISH || 
                type == EntityType.PRIMED_TNT || type == EntityType.ENDER_DRAGON || type == EntityType.AREA_EFFECT_CLOUD) {
                entities.get(i).remove();
            }
        }
        // Clear all Game based Variables
        BridgeRunner.Variables.PlayerStats.clear();
        BridgeRunner.Variables.PlayersInGame.clear();
    }

    public static void RandomEndGame() {
        int winTeam = -1;
        if (BridgeRunner.Variables.bluePoints > BridgeRunner.Variables.redPoints) winTeam = 1;
        else if (BridgeRunner.Variables.bluePoints < BridgeRunner.Variables.redPoints) winTeam = 0;

        List<UUID> playersWon = new ArrayList<>();
        List<UUID> playersLost = new ArrayList<>();
        for (int i = 0; i < BridgeRunner.Variables.PlayersInGame.size(); i++) {
            UUID uuid = BridgeRunner.Variables.PlayersInGame.get(i);
            Player player = Bukkit.getPlayer(uuid);
            if (winTeam != -1) {
                PlayerStatistics stats = BridgeRunner.Variables.PlayerStats.get(uuid);
                if (stats.team == winTeam) {
                    Utils.sendTitle(player, Utils.text("VICTORY!", TextColor.color(255, 255, 0)), Utils.text(""));
                    playersWon.add(uuid);
                }
                else {
                    Utils.sendTitle(player, Utils.text("GAME OVER", TextColor.color(255, 0, 0)), Utils.text(""));
                    playersLost.add(uuid);
                }
            } else {
                Utils.sendTitle(player, Utils.text("DRAW"), Utils.text(""));
            }
        }

        APIEventCaller.gameEnd(winTeam, playersWon, playersLost);
        BridgeRunner.endGame();
    }

    public static void TickPlayers(long currentTime) throws Exception {
        for (int i = 0; i < BridgeRunner.Variables.PlayersInGame.size(); i++) {
            Player player = Bukkit.getPlayer(BridgeRunner.Variables.PlayersInGame.get(i)); // get player from bukkit
            if (player == null || !BridgeRunner.Variables.PlayerStats.containsKey(player.getUniqueId())) continue; // skip player if they don't exsist or arn't in the game
            PlayerStatistics stats = BridgeRunner.Variables.PlayerStats.get(player.getUniqueId()); // get the players stats
            int TeamID = stats.team; // get the players team ID

            Location goalLocation;
            if (TeamID == 1)
                goalLocation = new Location(player.getLocation().getWorld(), BridgeRunner.Variables.redGoal.getX(), BridgeRunner.Variables.redGoal.getY(), BridgeRunner.Variables.redGoal.getZ());
            else goalLocation = new Location(player.getLocation().getWorld(), BridgeRunner.Variables.blueGoal.getX(), BridgeRunner.Variables.blueGoal.getY(), BridgeRunner.Variables.blueGoal.getZ());
            
            if (player.getLocation().distance(goalLocation) <= 3 && player.getLocation().getBlock().getType() == Material.END_PORTAL) {
                playerScored(player, TeamID);
            }

            // Void Handler
            if (player.getLocation().getY() < BridgeRunner.Variables.AreaMin.getBlockY()) player.damage(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*2);
            if (!stats.hasArrow && stats.usedArrow <= currentTime) { 
                player.getInventory().setItem(8, new ItemStack(Material.ARROW));
                stats.hasArrow = true; 
            } else if (!stats.hasArrow) {
                int level = (int)Math.ceil((stats.usedArrow-currentTime)/1000);
                player.setExp((stats.usedArrow-currentTime)/3000f);
                player.setLevel(level);
            }

            if (BridgeRunner.ReleaseTime != -1) {
                Utils.sendTitle(player, scoredTitle, Utils.text("cages open in " + (BridgeRunner.ReleaseTime-currentTime+1000)/1000 + "..."));
            }
        }
    }

    public static void TickExtras(long currentTime) throws Exception {
        if (!BridgeConf.endless && currentTime >= BridgeRunner.GameEndTime) {
            RandomEndGame();
            BridgeRunner.GameEndTime = 0;
        }
        if (BridgeRunner.ReleaseTime != -1 && currentTime >= BridgeRunner.ReleaseTime) {
            CageBuilder.destroy(BridgeRunner.world, BridgeRunner.Variables.blueCageCenter.pos, Material.GLASS, Material.GRAY_TERRACOTTA);
            CageBuilder.destroy(BridgeRunner.world, BridgeRunner.Variables.redCageCenter.pos, Material.GLASS, Material.GRAY_TERRACOTTA);
            BridgeRunner.ReleaseTime = -1;
        }
        if (BridgeRunner.FallDamageDelay != -1 && currentTime >= BridgeRunner.FallDamageDelay) {
            BridgeRunner.FallDamageDelay = -1;
        }
    }

    public static void playerScored(Player scorer, int TeamID) {
        boolean endGame = false;

        if (TeamID == 1) {
            scoredTitle = Utils.text(scorer.getName() + " has scored!", TextColor.color(0, 0, 255));
            BridgeRunner.Variables.bluePoints++;
            if (BridgeRunner.Variables.bluePoints >= BridgeConf.totalPoints) endGame = true;
        } else {
            scoredTitle = Utils.text(scorer.getName() + " has scored!", TextColor.color(255, 0, 0));
            BridgeRunner.Variables.redPoints++;
            if (BridgeRunner.Variables.redPoints >= BridgeConf.totalPoints) endGame = true;
        }
        int scorerGoals = BridgeRunner.Variables.PlayerStats.get(scorer.getUniqueId()).goals++;
        AsyncBridgeGameTicker.actions.add(() -> {
            GameScoreboardUtils.UpdateStatistics(scorer);
        });
        if (!endGame) {
            CageBuilder.build(scorer.getWorld(), BridgeRunner.Variables.redCageCenter.pos, Material.GLASS, Material.GRAY_TERRACOTTA);
            CageBuilder.build(scorer.getWorld(), BridgeRunner.Variables.blueCageCenter.pos, Material.GLASS, Material.GRAY_TERRACOTTA);
        }

        for (int i = 0; i < BridgeRunner.Variables.PlayersInGame.size(); i++) {
            UUID uuid = BridgeRunner.Variables.PlayersInGame.get(i);
            if (BridgeRunner.Variables.PlayerStats.containsKey(uuid)) {
                int team = BridgeRunner.Variables.PlayerStats.get(uuid).team;
                Player player = Bukkit.getPlayer(uuid);
                AsyncBridgeGameTicker.actions.add(() -> {
                    GameScoreboardUtils.UpdateTeamsboard(player);
                });

                sendScoredMessage(player, scorer, scorerGoals+1, TeamID);

                player.getInventory().clear();
                Utils.clearEffects(player);
                givePlayerDefaultItems(player, player.getInventory(), team);
                player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                teleportPlayerToCage(team, player);

                APIEventCaller.playerScore(TeamID, scorer);
            }
        }

        if (endGame) RandomEndGame();
        else {
            BridgeRunner.ReleaseTime = Utils.getDateTime()+(5*1000);
            BridgeRunner.FallDamageDelay = Utils.getDateTime()+(7*1000);
        }
    }

    public static void teleportPlayerToCage(int team, Player player) {
        DirectionVector cagePos;
        if (team == 0) cagePos = BridgeRunner.Variables.redCageCenter;
        else  cagePos = BridgeRunner.Variables.blueCageCenter;
        player.setFallDistance(0f);
        player.teleport(new Location(player.getWorld(), cagePos.pos.getX(), cagePos.pos.getY(), cagePos.pos.getZ(), cagePos.yaw, cagePos.pitch));
    }

    public static void sendScoredMessage(Player player, Player scorer, int scorerGoals, int teamID) {
        String scorerName = scorer.getName()+ChatColor.RESET;
        if (teamID == 1) scorerName = ChatColor.BLUE+scorerName;
        else scorerName = ChatColor.RED+scorerName;

        String scorerHP = " ("+ChatColor.GREEN+Math.round(((scorer.getHealth())*10)/10f)+"HP"+ChatColor.RESET+") ";
        String scored = ChatColor.YELLOW+"scored!"+ChatColor.RESET;
        String goals = " ("+ChatColor.GOLD+Utils.formatNumber(scorerGoals)+" Goal"+ChatColor.RESET+")";

        String bluePoints = ChatColor.BLUE+""+BridgeRunner.Variables.bluePoints;
        String redPoints = ChatColor.RED+""+BridgeRunner.Variables.redPoints;
        String buffer = Utils.repeat('-', 53);
        
        player.sendMessage(Utils.text(buffer, TextColor.color(255, 255, 0)));
        player.sendMessage(Utils.text("", null));
        player.sendMessage(Utils.text(Utils.center(scorerName+scorerHP+scored+goals)));
        player.sendMessage(Utils.text(Utils.center(bluePoints+ChatColor.RESET+" - "+redPoints)));
        player.sendMessage(Utils.text("", null));
        player.sendMessage(Utils.text(buffer, TextColor.color(255, 255, 0)));
    }

    public static void givePlayerDefaultItems(Player player, PlayerInventory inventory, int team) {
        ItemStack chestplate = colorLeatherArmor(team, new ItemStack(Material.LEATHER_CHESTPLATE));
        ItemStack leggings = colorLeatherArmor(team, new ItemStack(Material.LEATHER_LEGGINGS));
        ItemStack boots = colorLeatherArmor(team, new ItemStack(Material.LEATHER_BOOTS));
        ItemStack ironSword = makeUnbreakable(new ItemStack(Material.IRON_SWORD));
        ItemStack blocksOne = new ItemStack(getColoredBlocks(team), 64);
        ItemStack blocksTwo = new ItemStack(getColoredBlocks(team), 64);
        ItemStack gapples = new ItemStack(Material.GOLDEN_APPLE, 8);
        ItemStack pickaxe = makeUnbreakable(new ItemStack(Material.DIAMOND_PICKAXE));
        ItemStack bow = makeUnbreakable(new ItemStack(Material.BOW));
        ItemStack arrow = new ItemStack(Material.ARROW);

        ItemMeta pickaxeMeta = pickaxe.getItemMeta();
        pickaxeMeta.addEnchant(Enchantment.DIG_SPEED, 2, true);
        pickaxeMeta.setUnbreakable(true);
        pickaxe.setItemMeta(pickaxeMeta);

        inventory.setChestplate(chestplate); inventory.setLeggings(leggings); inventory.setBoots(boots);
        inventory.setItem(0, ironSword);
        inventory.setItem(1, bow);
        inventory.setItem(2, pickaxe);
        inventory.setItem(3, blocksOne);
        inventory.setItem(4, blocksTwo);
        inventory.setItem(5, gapples);
        inventory.setItem(8, arrow);
    } 

    public static ItemStack colorLeatherArmor(int team, ItemStack armor) {
        LeatherArmorMeta meta = (LeatherArmorMeta)armor.getItemMeta();
        if (team == 0) meta.setColor(Color.RED);
        else meta.setColor(Color.BLUE);
        meta.setUnbreakable(true);
        armor.setItemMeta(meta);
        return armor;
    }

    public static Material getColoredBlocks(int team) {
        if (team == 0) return Material.RED_TERRACOTTA;
        else return Material.BLUE_TERRACOTTA;
    }

    public static ItemStack makeUnbreakable(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta(); meta.setUnbreakable(true);
        stack.setItemMeta(meta);
        return stack;
    }

}
