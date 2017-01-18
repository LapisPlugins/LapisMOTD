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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class LapisMOTD extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPing(ServerListPingEvent e) {
        reloadConfig();
        String configMOTD = ChatColor.translateAlternateColorCodes('&', getConfig().getString("MOTD"));
        String MOTD = "";
        if (configMOTD.contains("\n")) {
            String[] lines = configMOTD.split("\n");
            for (String s : lines) {
                MOTD = MOTD + Chat.centerMotD(s) + "\n";
            }
        } else {
            MOTD = Chat.centerMotD(configMOTD);
        }
        e.setMotd(MOTD);
    }

}
