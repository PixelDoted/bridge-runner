package me.pixeldots.Utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.bukkit.potion.PotionEffect;

import me.pixeldots.BridgeRunner;
import me.pixeldots.Game.data.PlayerStatistics;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;

public class Utils {

    public static String center(String s) {
        int maxWidth = 80, spaces = (int) Math.round((maxWidth-1.4*ChatColor.stripColor(s).length())/2);
        return repeat(' ', spaces)+s;
    }
    public static String repeat(char c, int length) {
        String output = "";
        for (int i = 0; i < length; i++) { output += c; }
        return output;
    }

    public static Component text(String text) {
        return Component.text(text);
    }

    public static Component text(String text, TextColor color) {
        return Component.text(text, color);
    }

    public static String text(Component component) {
        if (component == null || component instanceof TranslatableComponent) return "";
        String text = ((TextComponent)component).content();
        
        List<Component> children = component.children();
        for (int i = 0; i < children.size(); i++) {
            text += ((TextComponent)children.get(i)).content();
        }
        
        return text;
    }

    public static String text(ItemStack stack) {
        Component text = stack.getItemMeta().displayName();
        if (text == null) return "";
        return ((TextComponent)text).content();
    }

    public static ChatColor getChatColor(String color) {
        if (color.equals("orange")) return ChatColor.GOLD;
        else if (color.equals("pink")) return ChatColor.LIGHT_PURPLE;
        return ChatColor.valueOf(color.toUpperCase());
    }

    public static String getFirstString(String s) {
        return s.toUpperCase().substring(0, 1);
    }

    public static String upperCaseFirst(String s) {
        return s.toUpperCase().substring(0, 1) + s.substring(1, s.length());
    }

    public static int randomRange(float min, float max) {
		return (int)Math.round(Math.random() * (min + max) - min);
	}

    public static long getDateTime() {
        return Instant.now().toEpochMilli();
    }

    public static boolean equals(Vector a, Vector b) {
        return a.getBlockX() == b.getBlockX() && a.getBlockY() == b.getBlockY() && a.getBlockZ() == b.getBlockZ();
    }

    public static TextColor getTextColor(String color) {
        Color clr = getColorFromName(color.toLowerCase());
        return TextColor.color(clr.getRed(), clr.getGreen(), clr.getBlue());
    }

    public static Color getColorFromName(String s) {
        switch (s) {
            case "orange":
                return Color.ORANGE;
            case "white":
                return Color.WHITE;
            case "black":
                return Color.BLACK;
            case "red":
                return Color.RED;
            case "yellow":
                return Color.YELLOW;
            case "green":
                return Color.GREEN;
            case "pink":
                return Color.PURPLE;
            case "blue":
                return Color.BLUE;
            default:
                return Color.WHITE;
        }
    }

    public static long toMillisecondTime(String s) {
        s = s.toLowerCase();
        if (s.endsWith("ms")) return Long.parseLong(s.replace("ms", ""));
        else if (s.endsWith("s")) return Math.round(Float.parseFloat(s.replace("s", ""))*1000);
        else if (s.endsWith("m")) return Math.round(Float.parseFloat(s.replace("m", ""))*1000)*60;
        return 0;
    }
    public static int toTickTime(String s) {
        s = s.toLowerCase();
        if (s.endsWith("s")) return Math.round(Float.parseFloat(s.replace("s", ""))*20);
        else if (s.endsWith("m")) return Math.round(Float.parseFloat(s.replace("m", ""))*20)*60;
        return 0;
    }

    public static String formatTimer(long time) {
        return Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("mm:ss"));
    }

    public static String toRomanNumerics(int number) {
        switch (number) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            default:
                return "";
        }
    }

    public static void runDelayedTask(RunnableFunction runnable, long delay) {
        new BukkitRunnable() {
            @Override
            public void run() { runnable.run(); }  
        }.runTaskLater(BridgeRunner.instance, delay);
    }

    public static void sendTitle(Player player, Component title, Component subtitle) {
        PlayerStatistics stats = BridgeRunner.Variables.PlayerStats.get(player.getUniqueId());
        long date = Utils.getDateTime();
        boolean canSendTitle = true;
        if (stats != null && stats.lastTitle != null) {
            String[] s = stats.lastTitle.split(":");
            if (s[0].equals(Utils.text(title)) && s[1].equals(Utils.text(subtitle)) && Long.parseLong(s[2]) >= date-2*1000) canSendTitle = false;
        }
        if (canSendTitle) {
            player.showTitle(Title.title(title, subtitle));
            if (stats != null) stats.lastTitle = Utils.text(title) + ":" + Utils.text(subtitle) + ":" + date;
        }
    }

    public static void clearEffects(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    public static String formatNumber(int i) {
        switch (i) {
            case 1:
                return "1st";
            case 2:
                return "2nd";
            case 3:
                return "3rd";
            default:
                return i+"th";
        }
    }

    public static Logger Logger() {
        return BridgeRunner.logger;
    }

    public static interface RunnableFunction { public void run(); }

}
