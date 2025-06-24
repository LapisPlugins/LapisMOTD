/*
 * Copyright 2017 Benjamin Martin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.lapismc.lapismotd;

import net.lapismc.lapiscore.LapisCorePlugin;
import net.lapismc.lapiscore.utils.LapisCoreFileWatcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.map.MinecraftFont;

public final class LapisMOTD extends LapisCorePlugin implements Listener {

    LapisCoreFileWatcher watcher;
    String serverVersion;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        //Register the watcher to make sure that edits to the config are reloaded live
        watcher = new LapisCoreFileWatcher(this);
        //Get the server version for replacement in MOTD
        serverVersion = Bukkit.getServer().getVersion().split("-")[0];
        super.onEnable();
    }

    @EventHandler
    public void onPing(ServerListPingEvent e) {
        //Get the MOTD and translate the colors
        String configMOTD = ChatColor.translateAlternateColorCodes('&', getConfig().getString("MOTD"));
        //Translate the server version from the string
        configMOTD = configMOTD.replace("%ServerVersion%", serverVersion);
        //Handle if there are 2 lines, centre them both after split
        StringBuilder MOTD = new StringBuilder();
        if (configMOTD.contains("\n")) {
            String[] lines = configMOTD.split("\n");
            MOTD.append(centerText(lines[0])).append("\n");
            MOTD.append(centerText(lines[1])).append("\n");
        } else {
            MOTD = new StringBuilder(centerText(configMOTD));
        }
        //Set the MOTD
        e.setMotd(MOTD.toString());
    }

    public String centerText(String text) {
        //Strip colors since they don't have width
        String colorlessText = ChatColor.stripColor(text);
        //Get the desired width, if it's set
        int pixelsToUse = getConfig().getInt("Pixels", 195);
        //Get the width of the text
        int pixelsWide = MinecraftFont.Font.getWidth(colorlessText);
        //Calculate how many pixels we need in front
        int pixelsToAdd = (int) Math.rint((double) (pixelsToUse - pixelsWide) / 2);
        //Calculate how many spaces it takes to make that many pixels
        int spaces = (int) Math.rint((double) pixelsToAdd / MinecraftFont.Font.getChar(' ').getWidth());
        StringBuilder builder = new StringBuilder();
        //Add said spaces
        builder.append(" ".repeat(Math.max(0, spaces)));
        //Put the spaces in front of the original text
        return builder + text;
    }

}
