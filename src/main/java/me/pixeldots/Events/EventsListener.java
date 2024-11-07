package me.pixeldots.Events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import me.pixeldots.BridgeConf;
import me.pixeldots.BridgeRunner;
import me.pixeldots.Game.data.PlayerStatistics;
import me.pixeldots.Game.data.WorldBlockData;
import me.pixeldots.Utils.BlockUtils;
import me.pixeldots.Utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class EventsListener implements Listener {

    // Block

    @EventHandler
    public void onBlockForm(BlockFormEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (BridgeRunner.isStarting) e.setCancelled(true);
        if (!BridgeRunner.isRunning) return;

        Material type = e.getBlock().getType();
        Block block = e.getBlock();
        if (!BridgeRunner.Variables.BlocksPlaced.contains(BlockUtils.getVector(block.getLocation()))) {
            if (BridgeConf.canDestroyWorld || e.getBlock().getType().name().endsWith("_TERRACOTTA")) 
                BridgeRunner.Variables.WorldBlocksDestroyed.add(new WorldBlockData(type, block.getLocation(), block.getBlockData()));
            else e.setCancelled(true);
        } else {
            Location location = block.getLocation();
            Vector pos = new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
            BridgeRunner.Variables.BlocksPlaced.remove(pos);
        }
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent e) {
        if (BridgeRunner.isStarting) e.setCancelled(true);
        if (!BridgeRunner.isRunning) return;
        
        if (BlockUtils.isBlockOutOfBounds(e.getBlock().getLocation())) {
            e.getPlayer().sendMessage(Utils.text("You have reached the build limit!", TextColor.color(255, 22, 22))); 
            e.setCancelled(true);
        } else {
            Location location = e.getBlockPlaced().getLocation();
            Vector pos = new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
            BridgeRunner.Variables.BlocksPlaced.add(pos);
        }
    }

    @EventHandler
    public void onEntityAttackEntity(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player)e.getEntity();

        PlayerStatistics stats = BridgeRunner.Variables.PlayerStats.get(player.getUniqueId());
        if (e.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow)e.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player shooter = ((Player)arrow.getShooter());
                String teamColor = "red";
                if (stats.team == 1) teamColor = "blue";

                String health = String.valueOf(Math.round( (((Player)e.getEntity()).getHealth()-e.getFinalDamage())*10)/10f);
                Component message = Utils.text(player.getName(), Utils.getTextColor(teamColor)).append(Utils.text(" is on ", TextColor.color(255, 255, 255))).append(Utils.text(health, TextColor.color(255, 22, 22))).append(Utils.text(" HP!", TextColor.color(255, 255, 255)));
                shooter.sendMessage(message);
                shooter.sendActionBar(Utils.text(Utils.text(player.displayName()), Utils.getTextColor(teamColor)).append(Utils.text(health)));
            }
        }
        if (e.getDamager() instanceof Player) {
            String teamColor = "red";
            if (stats.team == 1) teamColor = "blue";
            String health = String.valueOf(Math.round( (((Player)e.getEntity()).getHealth()-e.getFinalDamage())*10)/10f);
            e.getDamager().sendActionBar(Utils.text(Utils.text(player.displayName()), Utils.getTextColor(teamColor)).append(Utils.text(health)));
        }
    }

}
