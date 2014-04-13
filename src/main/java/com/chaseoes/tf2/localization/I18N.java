package com.chaseoes.tf2.localization;

import com.chaseoes.tf2.localization.replacer.Replacer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class I18N {

    private static final Pattern VAR_PATTERN = Pattern.compile("\\{([a-zA-Z]+)\\}");

    private final Localizer localizer;
    final String configNode;
    private final Replacer<?>[] replacers;
    private final String[] vars;
    private final int[] originalIndexes;

    public I18N(Localizer localizer, String configNode, Replacer<?>... replacers) {
        this.localizer = localizer;
        this.configNode = configNode;
        this.replacers = Arrays.copyOf(replacers, replacers.length);
        vars = new String[this.replacers.length];
        originalIndexes = new int[this.replacers.length];
        if (replacers.length == 0) {
            return;
        }
        Arrays.sort(this.replacers, new ReplacerComparator());
        for (int i = 0; i < this.replacers.length; i++) {
            vars[i] = this.replacers[i].getReplacedVar();
            if (!VAR_PATTERN.matcher("{" + vars[i] + "}").matches()) {
                throw new RuntimeException("Illegal variable '" + vars[i] + "'");
            }
            int newIndex = Arrays.binarySearch(this.replacers, replacers[i], new ReplacerComparator());
            originalIndexes[i] = newIndex;
        }
    }

    public void send(CommandSender sender, Object... objects) {
        String message = getString(objects);
        sender.sendMessage(message);
    }

    public void sendPrefixed(CommandSender sender, Object... objects) {
        String message = getPrefixedString(objects);
        sender.sendMessage(message);
    }

    public void broadcast(Object... objects) {
        String message = getString(objects);
        Bukkit.broadcastMessage(message);
    }

    public void broadcastPrefixed(Object... objects) {
        String message = getPrefixedString(objects);
        Bukkit.broadcastMessage(message);
    }

    public String getString(Object... objects) {
        if (!(replacers.length == objects.length)) {
            throw new IllegalStateException("The number of passed parameters is not valid for " + configNode + ", " + replacers.length + " != " + objects.length);
        }
        if (objects.length == 0) {
            String message = localizer.getConfigString(configNode);
            return ChatColor.translateAlternateColorCodes('&', message);
        }
        Matcher matcher = VAR_PATTERN.matcher(localizer.getConfigString(configNode));
        StringBuffer buffer = new StringBuffer();
        boolean[] used = new boolean[replacers.length];
        while (matcher.find()) {
            int index = Arrays.binarySearch(vars, matcher.group(1));
            if (index < 0) {
                continue;
            }
            used[index] = true;
            matcher.appendReplacement(buffer, replacers[index].getReplacement(objects[originalIndexes[index]]));
        }
        for (int i = 0; i < used.length; i++) {
            if (!used[i]) {
                throw new RuntimeException("Variable not used! '" + replacers[i].getReplacedVar() + "'");
            }
        }

        matcher.appendTail(buffer);
        String message = buffer.toString();
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    public String getPrefixedString(Object... objects) {
        String message = getString(objects);
        message = ChatColor.YELLOW + "[TF2] " + message;
        return message;
    }

    private class ReplacerComparator implements java.util.Comparator<Replacer<?>> {
        @Override
        public int compare(Replacer<?> o1, Replacer<?> o2) {
            return o1.getReplacedVar().compareTo(o2.getReplacedVar());
        }
    }

}
