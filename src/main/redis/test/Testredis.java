package main.redis.test;

import functions.hypernite.mc.Functions;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.hypernite.mc.RedisManager;

import java.io.File;
import java.util.List;

public class Testredis extends JavaPlugin {
    public static Plugin plugin;
    @Override
    public void onEnable() {
        plugin = this;
        getLogger().info("Testredis enabled.");
        new Functions(this).addNewFile("config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(this.getDataFolder(),"config.yml"));
        List<String> channels = config.getStringList("Channels");
        Jedis redis = RedisManager.getInstance().getRedis();
        Bukkit.getScheduler().runTaskAsynchronously(this,()->{
            JedisPubSub sub = Subscriber.getInstance().getSubscribe();
            redis.subscribe(sub, channels.toArray(new String[0]));
            redis.close();
        });
    }

    @Override
    public void onDisable() {
        getLogger().info("Testredis disable");
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(this.getDataFolder(),"config.yml"));
        List<String> channels = config.getStringList("Channels");
        Subscriber.getInstance().getSubscribe().unsubscribe(channels.toArray(new String[0]));
        RedisManager.getInstance().closePool();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Jedis redis = RedisManager.getInstance().getRedis();
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(this.getDataFolder(),"config.yml"));
        List<String> channels = config.getStringList("Channels");
        if (args.length < 2) {
            sender.sendMessage("use /send <channel> <msg>");
            return false;
        }
        String channel = args[0];
        String message = args[1];
        /*if (!channels.contains(channel)) {
            sender.sendMessage("Channel can only be "+channels);
            return false;
        }*/
        if (command.getName().equals("send")){
            redis.publish(channel,message);
        }
        redis.close();
        return true;
    }
}
