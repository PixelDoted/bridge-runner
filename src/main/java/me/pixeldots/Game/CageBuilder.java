package me.pixeldots.Game;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class CageBuilder {

    public static void destroy(World world, Vector pos, Material glass, Material solid) {
        for (int x = -2; x <= 2; x++) {
            for (int y = -1; y <= 3; y++) {
                for (int z = -2; z <= 2; z++) {
                    Block block = world.getBlockAt(pos.getBlockX()+x, pos.getBlockY()+y, pos.getBlockZ()+z);
                    if (block.getType() == solid || block.getType() == glass) block.setType(Material.AIR);
                }
            }
        }
    }

    public static void build(World world, Vector pos, Material glass, Material solid) {
        buildFloor(world, pos, glass);
        buildLayer(world, pos, glass, solid);
        buildLayer(world, pos.clone().add(new Vector(0,1,0)), glass, solid);
        buildLayer(world, pos.clone().add(new Vector(0,2,0)), glass, solid);
    }

    public static void buildFloor(World world, Vector pos, Material glass) {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                placeBlock(world, pos.getBlockX()+x, pos.getBlockY()-1, pos.getBlockZ()+z, glass);
            }
        }
    }

    public static void buildLayer(World world, Vector pos, Material glass, Material solid) {
        placeBlock(world, pos.getBlockX()+1, pos.getBlockY(), pos.getBlockZ()+2, glass);
        placeBlock(world, pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()+2, glass);
        placeBlock(world, pos.getBlockX()-1, pos.getBlockY(), pos.getBlockZ()+2, glass);
        placeBlock(world, pos.getBlockX()-2, pos.getBlockY(), pos.getBlockZ()+2, solid);

        placeBlock(world, pos.getBlockX()+2, pos.getBlockY(), pos.getBlockZ()+1, glass);
        placeBlock(world, pos.getBlockX()+2, pos.getBlockY(), pos.getBlockZ(), glass);
        placeBlock(world, pos.getBlockX()+2, pos.getBlockY(), pos.getBlockZ()-1, glass);
        placeBlock(world, pos.getBlockX()+2, pos.getBlockY(), pos.getBlockZ()-2, solid);

        placeBlock(world, pos.getBlockX()+1, pos.getBlockY(), pos.getBlockZ()-2, glass);
        placeBlock(world, pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()-2, glass);
        placeBlock(world, pos.getBlockX()-1, pos.getBlockY(), pos.getBlockZ()-2, glass);
        placeBlock(world, pos.getBlockX()-2, pos.getBlockY(), pos.getBlockZ()-2, solid);

        placeBlock(world, pos.getBlockX()-2, pos.getBlockY(), pos.getBlockZ()-1, glass);
        placeBlock(world, pos.getBlockX()-2, pos.getBlockY(), pos.getBlockZ(), glass);
        placeBlock(world, pos.getBlockX()-2, pos.getBlockY(), pos.getBlockZ()+1, glass);
        placeBlock(world, pos.getBlockX()+2, pos.getBlockY(), pos.getBlockZ()+2, solid);
    }

    public static void placeBlock(World world, int x, int y, int z, Material mat) {
        Block block = world.getBlockAt(x, y, z);
        if (block.getType() == Material.AIR) block.setType(mat);
    }
    
}
