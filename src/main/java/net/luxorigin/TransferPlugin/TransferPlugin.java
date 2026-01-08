package net.luxorigin.TransferPlugin;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.luxorigin.TransferPlugin.command.TransferCommand;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.nio.MappedByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class TransferPlugin extends JavaPlugin {

    public static String prefix = "§l§a[!] §r§7";

    private File file;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private Map<String, Map<String, String>> cache = new HashMap<>();

    protected NamespacedKey key;

    @Override
    public void onEnable() {
        key = new NamespacedKey(this, "transfer_server");

        getServer().getPluginManager().registerEvents(new EventListener(this), this);

        getServer().getCommandMap().register("luxorigin", new TransferCommand(this));

        file = new File(getDataFolder(), "cache.json");
        getDataFolder().mkdirs();

        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Type type = new TypeToken<Map<String, Map<String, String>>>() {}.getType();
                Map<String, Map<String, String>> loaded = gson.fromJson(reader, type);

                if (loaded != null) {
                    cache = loaded;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisable() {
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(cache, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NamespacedKey getKey() {
        return key;
    }

    public Map<String, Map<String, String>> getCache() {
        return cache;
    }

    public Map<String, String> getTransferData(String name) {
        return cache.get(name.toLowerCase());
    }

    public boolean hasTransferData(String name) {
        return cache.containsKey(name.toLowerCase());
    }

    public void createTransferData(String name, String address, String port) {
        Map<String, String> object = new HashMap<>();
        object.put("address", address);
        object.put("port", port);
        cache.put(name.toLowerCase(), object);
    }

    public void transfer(Player player, String name) {
        Map<String, String> data = getTransferData(name);

        if (data == null) {
            return;
        }
        String address = data.get("address");
        String port = data.get("port");

        if (address == null || port == null) return;

        try {
            player.transfer(address, Integer.parseInt(port));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void deleteTransferData(String name) {
        cache.remove(name.toLowerCase());
    }
}