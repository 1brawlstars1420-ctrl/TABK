package com.brawlstars1420.tab4k;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    private static Main instance;
    private Logger log;

    @Override
    public void onEnable() {
        instance = this;
        log = getLogger();

        log.info("========================================");
        log.info("  Activando TAB4K v" + getDescription().getVersion());
        log.info("  Autor: 1brawlstars1420-ctrl");
        log.info("========================================");

        // 1. Guardar configuración por defecto si no existe
        saveDefaultConfig();

        // 2. Registrar el comando principal
        if (getCommand("tab4k") != null) {
            getCommand("tab4k").setExecutor(this);
        }

        // 3. Iniciar tarea asíncrona del TAB
        iniciarTareaTab();

        log.info("✓ TAB4K habilitado correctamente.");
    }

    @Override
    public void onDisable() {
        log.info("TAB4K deshabilitado.");
    }

    public static Main getInstance() {
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("tab4k")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("recargar")) {
                if (sender.hasPermission("tab4k.admin")) {
                    reloadConfig();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&bTAB4K&8] &a¡Configuración recargada con éxito!"));
                } else {
                    sender.sendMessage(ChatColor.RED + "No tienes permisos para ejecutar este comando.");
                }
                return true;
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&bTAB4K&8] &fVersión actual: &b" + getDescription().getVersion()));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &7/tab4k recargar &8- &fRecarga la configuración del plugin"));
            return true;
        }
        return false;
    }

    private void iniciarTareaTab() {
        // Actualización asíncrona cada 1 segundo (20 ticks) para evitar lag
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            List<String> headerLines = getConfig().getStringList("cabecera");
            List<String> footerLines = getConfig().getStringList("pie");

            for (Player player : Bukkit.getOnlinePlayers()) {
                actualizarTabJugador(player, headerLines, footerLines);
            }
        }, 0L, 20L);
    }

    private void actualizarTabJugador(Player player, List<String> headerLines, List<String> footerLines) {
        if (player == null || !player.isOnline()) return;

        String header = colorize(String.join("\n", headerLines));
        String footer = colorize(String.join("\n", footerLines));

        player.setPlayerListHeaderFooter(header, footer);
    }

    private String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
