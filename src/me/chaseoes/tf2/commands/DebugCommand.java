package me.chaseoes.tf2.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.utilities.PastebinPoster;
import me.chaseoes.tf2.utilities.PastebinPoster.PasteCallback;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class DebugCommand {

    private TF2 plugin;
    static DebugCommand instance = new DebugCommand();

    private DebugCommand() {

    }

    public static DebugCommand getCommand() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void execDebugCommand(final CommandSender cs, String[] strings, Command cmnd) {
        cs.sendMessage(ChatColor.YELLOW + "[TF2] Uploading debug information to Pastebin...");
        PastebinPoster.paste(getDebugInformation(), new PasteCallback() {

            @Override
            public void handleSuccess(String url) {
                cs.sendMessage(ChatColor.YELLOW + "[TF2] Debug information available for 1 day at:\n" + url);
            }

            @Override
            public void handleError(String err) {
                cs.sendMessage(ChatColor.YELLOW + "[TF2] Error encountered while uploading to Pastebin.");
            }
        });
    }

    public String getDebugInformation() {
        StringBuilder info = new StringBuilder();
        info.append("====== SERVER INFORMATION ======\n");
        info.append("TF2 Version: ").append(plugin.getDescription().getVersion()).append("\n");
        info.append("Bukkit Version: ").append(plugin.getServer().getVersion()).append("\n");
        StringBuilder plugins = new StringBuilder();
        for (Plugin plug : plugin.getServer().getPluginManager().getPlugins()) {
            plugins.append(plug.getName()).append(", ");
        }
        info.append("Plugins: ").append(plugins.toString().substring(0, plugins.toString().length() - 1)).append("\n\n");
        info.append("====== TF2 CONFIGURATION ======\n");
        File config = new File(plugin.getDataFolder() + "/config.yml").getAbsoluteFile();
        File data = new File(plugin.getDataFolder() + "/data.yml").getAbsoluteFile();
        try {
            String confi = new Scanner(config).useDelimiter("\\A").next();
            String df = new Scanner(data).useDelimiter("\\A").next();
            info.append(confi);
            info.append("\n====== DATA CONFIGURATION ======\n");
            info.append(df);
            for (String map : MapUtilities.getUtilities().getEnabledMaps()) {
                File mf = new File(plugin.getDataFolder() + "/" + map + ".yml").getAbsoluteFile();
                String mapfile = new Scanner(mf).useDelimiter("\\A").next();
                info.append("\n====== ").append(map).append(" MAP CONFIGURATION ======\n");
                info.append(mapfile);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return info.toString();
    }

    private static String readFile(File path) throws IOException {
        FileInputStream stream = new FileInputStream(path);
        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            /* Instead of using default, pass in a decoder. */
            fc.close();
            return Charset.defaultCharset().decode(bb).toString();
        } finally {
            stream.close();
        }
    }

}
