package me.simplecoder.ezchat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EZChat
  extends JavaPlugin
  implements Listener
{
  private String line;
  
  public void onEnable()
  {
    saveDefaultConfig();
    Bukkit.getPluginManager().registerEvents(this, this);
    String line = color(getConfig().getString("Messages.line"));
    this.line = line;
  }
  
  boolean off = false;
  public String disabler = "CONSLE";
  String message = color("None");
  
  private String color(String message)
  {
    String string = ChatColor.translateAlternateColorCodes('&', message);
    return string;
  }
  
  @EventHandler
  public void playerChat(AsyncPlayerChatEvent e)
  {
    if ((this.off) && 
      (!e.getPlayer().hasPermission("ec.bypass")))
    {
      e.setCancelled(true);
      e.getPlayer().sendMessage(color(getConfig().getString("Messages.disabled").replace("$locker", this.disabler)));
    }
  }
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    if (command.getLabel().equalsIgnoreCase("ec")) {
      if (args.length == 0) {
        showHelp(sender);
      } else if (args.length == 1)
      {
        if (args[0].equalsIgnoreCase("lock"))
        {
          if (sender.hasPermission("ec.lock"))
          {
            if (!this.off)
            {
              this.off = true;
              this.disabler = sender.getName();
              Bukkit.broadcastMessage(this.line);
              Bukkit.broadcastMessage(color(getConfig().getString("Messages.disabled").replace("$locker", this.disabler)));
              Bukkit.broadcastMessage(this.line);
            }
            else
            {
              sender.sendMessage(ChatColor.RED + "Chat is already disabled");
            }
          }
          else {
            sender.sendMessage(ChatColor.RED + "You do not have permission to perform that command.");
          }
        }
        else if (args[0].equalsIgnoreCase("unlock"))
        {
          if (sender.hasPermission("ec.lock"))
          {
            if (this.off)
            {
              this.off = false;
              this.disabler = sender.getName();
              Bukkit.broadcastMessage(this.line);
              Bukkit.broadcastMessage(color(getConfig().getString("Messages.unlocked").replace("$locker", this.disabler)));
              Bukkit.broadcastMessage(this.line);
            }
            else
            {
              sender.sendMessage(ChatColor.RED + "Chat is already enabled");
            }
          }
          else {
            sender.sendMessage(ChatColor.RED + "You do not have permission to perform that command.");
          }
        }
        else if (args[0].equalsIgnoreCase("clock"))
        {
          if (sender.hasPermission("ec.clock"))
          {
            if (!this.off)
            {
              clear(sender.getName());
              this.off = true;
              this.disabler = sender.getName();
              Bukkit.broadcastMessage(this.line);
              Bukkit.broadcastMessage(color(getConfig().getString("Messages.disabled").replace("$locker", this.disabler)));
              Bukkit.broadcastMessage(this.line);
            }
            else
            {
              sender.sendMessage(ChatColor.RED + "Chat is already disabled");
            }
          }
          else {
            sender.sendMessage(ChatColor.RED + "You do not have permission to perform that command.");
          }
        }
        else if (args[0].equalsIgnoreCase("clear"))
        {
          if (sender.hasPermission("ec.clear")) {
            clear(sender.getName());
          } else {
            sender.sendMessage(ChatColor.RED + "You do not have permission to perform that command.");
          }
        }
        else {
          showHelp(sender);
        }
      }
      else if (args.length == 2) {
        if (sender.hasPermission("ec.clear"))
        {
          if (args[0].equalsIgnoreCase("clear"))
          {
            String target = Bukkit.getPlayer(args[1]).getName();
            playerClear(sender.getName(), target);
          }
        }
        else {
          sender.sendMessage(ChatColor.RED + "You do not have permission to perform that command.");
        }
      }
    }
    return true;
  }
  
  private void showHelp(CommandSender sender)
  {
    sender.sendMessage(ChatColor.YELLOW  + "============================================");
    sender.sendMessage("");
    sender.sendMessage(ChatColor.AQUA  + "EZChat Commands: ");
    sender.sendMessage("");
    sender.sendMessage(ChatColor.GOLD  + " * " + ChatColor.GREEN + "/ec lock: " + ChatColor.AQUA + "Locks Chat");
    sender.sendMessage(ChatColor.GOLD  + " * " + ChatColor.GREEN + "/ec unlock: " + ChatColor.AQUA + "Unlocks Chat");
    sender.sendMessage(ChatColor.GOLD  + " * " + ChatColor.GREEN + "/ec clock: " + ChatColor.AQUA + "Clears and locks chat");
    sender.sendMessage(ChatColor.GOLD  + " * " + ChatColor.GREEN + "/ec clear: " + ChatColor.AQUA + "Clears everyone's chat");
    sender.sendMessage(ChatColor.GOLD  + " * " + ChatColor.GREEN + "/ec clear <player>: " + ChatColor.AQUA + "Clears players chat");
    sender.sendMessage("");
    sender.sendMessage(ChatColor.YELLOW  + "============================================");
  }
  
  public void clear(String name)
  {
    for (int i = 0; i < 101; i++) {
      Bukkit.broadcastMessage("");
    }
    Bukkit.broadcastMessage(this.line);
    Bukkit.broadcastMessage(color(getConfig().getString("Messages.cleared").replace("$locker", name)));
    Bukkit.broadcastMessage(this.line);
  }
  
  public void playerClear(String name, String target)
  {
    Bukkit.getPlayer(name).sendMessage(ChatColor.RED + "Cleared " + target + "'s Chat");
    Player targ = Bukkit.getPlayer(target);
    for (int i = 0; i < 101; i++) {
      Bukkit.broadcastMessage("");
    }
    Bukkit.broadcastMessage(this.line);
    targ.sendMessage(color(getConfig().getString("Messages.personaldisabled").replace("$locker", name)));
    Bukkit.broadcastMessage(this.line);
  }
}

