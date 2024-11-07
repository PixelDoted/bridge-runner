package me.pixeldots.SaveData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.Gson;

import org.bukkit.util.Vector;

import me.pixeldots.BridgeRunner;
import me.pixeldots.Game.data.DirectionVector;

public class DataHandler {

    public static void Save(String path) {
        BufferedWriter writer = null;
        try {
            Gson gson = new Gson();
            writer = new BufferedWriter(new FileWriter(new File(path)));
            
            VariablesData data = new VariablesData(BridgeRunner.Variables);
            writer.write(gson.toJson(data));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { writer.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    public static void Load(String path) {
        if (!new File(path).exists()) return;
        BufferedReader reader = null;
        try {
            Gson gson = new Gson();
            reader = new BufferedReader(new FileReader(new File(path)));

            VariablesData data = gson.fromJson(reader.readLine(), VariablesData.class);

            BridgeRunner.Variables.AreaStart = new Vector(data.AreaStart[0], data.AreaStart[1], data.AreaStart[2]);
            BridgeRunner.Variables.AreaEnd = new Vector(data.AreaEnd[0], data.AreaEnd[1], data.AreaEnd[2]);
            BridgeRunner.Variables.LobbyPosition = new Vector(data.LobbyPosition[0], data.LobbyPosition[1], data.LobbyPosition[2]);
            
            BridgeRunner.Variables.redCageCenter = new DirectionVector(data.redCage[0], data.redCage[1], data.redCage[2], data.redCage[3], data.redCage[4]);
            BridgeRunner.Variables.redSpawn = new DirectionVector(data.redSpawn[0], data.redSpawn[1], data.redSpawn[2], data.redSpawn[3], data.redSpawn[4]);
            BridgeRunner.Variables.redGoal = new Vector(data.redGoal[0], data.redGoal[1], data.redGoal[2]);
            
            BridgeRunner.Variables.blueCageCenter = new DirectionVector(data.blueCage[0], data.blueCage[1], data.blueCage[2], data.blueCage[3], data.blueCage[4]);
            BridgeRunner.Variables.blueSpawn = new DirectionVector(data.blueSpawn[0], data.blueSpawn[1], data.blueSpawn[2], data.blueSpawn[3], data.blueSpawn[4]);
            BridgeRunner.Variables.blueGoal = new Vector(data.blueGoal[0], data.blueGoal[1], data.blueGoal[2]);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { reader.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

}
