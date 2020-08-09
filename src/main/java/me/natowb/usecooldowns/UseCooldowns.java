package me.natowb.usecooldowns;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public final class UseCooldowns extends JavaPlugin implements Listener {

    private HashMap<UUID, HashSet<String>> cooldowns = new HashMap<>();


    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this,this);
        setupConfig();

    }


    public void msg(Player player, String message) {
        String pre = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix"));
        String msg = ChatColor.translateAlternateColorCodes('&', message);
        player.sendMessage(pre + " " +msg);
    }


    public void setupConfig() {
        getConfig().options().copyDefaults(true);
        getConfig().options().header(
                "#########################\n" +
                        "Created by NatoWB\n" +
                        "https://github.com/natowb\n" +
                        "#########################\n" +
                        "Prefix: sets the display of the plugin when it messages players\n" +
                        "itemDelays: is the list of items to give delays to\n" +
                        "Examples:\n" +
                        "birch_sapling:100\n" +
                        "horse_spawn_egg:100\n" +
                        "Examples:\n" +
                        "jump:100 -- Gives delay of 5 seconds to potions of type jump\n" +
                        "fire_resistance:100 -- Gives delay of 5 seconds to potions of type fire resistance");
        saveConfig();
    }


    public boolean checkPotion(Player player, String potion) {


        if(player.hasPermission("usedelay.bypass")) {
            return false;
        }


        UUID pID = player.getUniqueId();
        boolean potionHasCooldown = false;
        int delay = 0;
        try {
            for(String s : getConfig().getStringList("consumeCooldowns.potions")) {
                String[] sArr = s.split(":");
                if(sArr[0].equalsIgnoreCase(potion)) {
                    potionHasCooldown = true;
                    delay = Integer.parseInt(sArr[1]);
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if(potionHasCooldown) {
            if(cooldowns.containsKey(pID)) {
                if(cooldowns.get(pID).contains(potion)) {
                    String p = String.valueOf(potion.charAt(0)).toUpperCase();
                    String msg = "&6 <"+ p + potion.substring(1).toLowerCase()+">&c Currently on Cooldown";
                    msg(player, msg);
                    return true;
                }
                cooldowns.get(pID).add(potion);
            }
            HashSet<String> arr = new HashSet<>();
            arr.add(potion);
            cooldowns.put(pID, arr);
            getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    cooldowns.get(pID).remove(potion);
                }
            }, delay);

        }

        return false;
    }


    @EventHandler
    public void consumeItem(PlayerItemConsumeEvent event) {

        Player player = event.getPlayer();
        if(event.getItem().getType() == Material.POTION) {
            String potion = ((PotionMeta)event.getItem().getItemMeta()).getBasePotionData().getType().toString();
            event.setCancelled(checkPotion(player, potion));
        } else{
            event.setCancelled(checkFood(event.getPlayer(), event.getItem().getType().toString()));
        }





    }

    @EventHandler
    public void projectileLaunch(ProjectileLaunchEvent event) {
        Projectile proj = event.getEntity();
        if(proj.getShooter() instanceof Player) {
            Player player = (Player) proj.getShooter();

            if(proj.getType()== EntityType.SPLASH_POTION) {
                ThrownPotion pot = (ThrownPotion) proj;
                String potion = ((PotionMeta)pot.getItem().getItemMeta()).getBasePotionData().getType().toString();
                event.setCancelled(checkPotion(player, potion));
            }

        }


    }

    public boolean checkFood(Player player, String food) {


        if(player.hasPermission("usedelay.bypass")) {
            return false;
        }

        UUID pID = player.getUniqueId();
        boolean itemHasCooldown = false;
        int delay = 0;
        try {
            for(String s : getConfig().getStringList("consumeCooldowns.food")) {
                String[] sArr = s.split(":");
                if(sArr[0].equalsIgnoreCase(food)) {
                    itemHasCooldown = true;
                    delay = Integer.parseInt(sArr[1]);
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if(itemHasCooldown) {
            if(cooldowns.containsKey(pID)) {
                if(cooldowns.get(pID).contains(food)) {
                    String p = String.valueOf(food.charAt(0)).toUpperCase();
                    String msg = "&6 <"+ p + food.substring(1).toLowerCase()+">&c Currently on Cooldown";
                    msg(player, msg);
                    return true;
                }
                cooldowns.get(pID).add(food);
            }
            HashSet<String> arr = new HashSet<>();
            arr.add(food);
            cooldowns.put(pID, arr);
            getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    cooldowns.get(pID).remove(food);
                }
            }, delay);

        }

        return false;
    }

    public boolean checkItem(Player player, String item) {


        if(player.hasPermission("usedelay.bypass")) {
            return false;
        }

        UUID pID = player.getUniqueId();
        boolean itemHasCooldown = false;
        int delay = 0;
        try {
            for(String s : getConfig().getStringList("useCooldowns")) {
                String[] sArr = s.split(":");
                if(sArr[0].equalsIgnoreCase(item)) {
                    itemHasCooldown = true;
                    delay = Integer.parseInt(sArr[1]);
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if(itemHasCooldown) {
            if(cooldowns.containsKey(pID)) {
                if(cooldowns.get(pID).contains(item)) {
                    String p = String.valueOf(item.charAt(0)).toUpperCase();
                    String msg = "&6 <"+ p + item.substring(1).toLowerCase()+">&c Currently on Cooldown";
                    msg(player, msg);
                    return true;
                }
                cooldowns.get(pID).add(item);
            }
            HashSet<String> arr = new HashSet<>();
            arr.add(item);
            cooldowns.put(pID, arr);
            getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    cooldowns.get(pID).remove(item);
                }
            }, delay);

        }

        return false;
    }
    @EventHandler()
    public void useItem(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND && event.getItem()!=null) {
            event.setCancelled(checkItem(event.getPlayer(), event.getItem().getType().toString()));
        }
    }




}
