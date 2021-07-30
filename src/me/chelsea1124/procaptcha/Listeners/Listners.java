package me.chelsea1124.procaptcha.Listeners;

import me.chelsea1124.procaptcha.GUI.CaptchaGUI;
import me.chelsea1124.procaptcha.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Listners implements Listener {


    private Main main;
    public Listners (Main main)
    {
        this.main = main;
    }

    java.util.Date Time = new Date();
    SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
    Date Date = new Date();
    SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy");

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        Location loc = p.getLocation();
        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        CaptchaGUI gui = new CaptchaGUI();
        gui.build(p);

        if (p.hasPermission("procaptcha.bypass") || p.isOp() || main.data.getConfig().getInt(p.getUniqueId() + ".joins") == main.getConfig().getInt("JoinCounter.Amount")){

            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
                public void run() {
                    Main.instance.inventories.remove(p);
                    p.closeInventory();
                    return;
                }
            }, 1L);
            return;
        }

        if(!p.isOnGround()){
            p.teleport(p.getWorld().getHighestBlockAt(x,z).getLocation());
        }

        if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
            p.teleport(p.getWorld().getHighestBlockAt(x,z).getLocation());
        }

        if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.LAVA) {
            loc.add(0, -1, 0);
            Block block = loc.getBlock();
            block.setType(Material.GLASS);
        }

        if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.LEGACY_STATIONARY_LAVA) {
            loc.add(0, -1, 0);
            Block block = loc.getBlock();
            block.setType(Material.GLASS);
        }


        if (Main.instance.getConfig().getBoolean("Timer.KickTimer")) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
                public void run() {

                    if (Main.instance.inventories.containsKey(p)) {
                        p.kickPlayer(Main.instance.color(Main.instance.getConfig().getString(("Timer.KickMessage"))));
                    }
                }
            }, Main.instance.getConfig().getInt("Timer.KickTime"));
        }

    }

    @EventHandler
    public void onquit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        Main.instance.inventories.remove(p);
    }

    @EventHandler
    public void onkick(PlayerKickEvent e) {
        Player p = e.getPlayer();

        Main.instance.inventories.remove(p);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {

        Player p = (Player)e.getWhoClicked();
        if(Main.instance.inventories.containsKey(p)) {
            Inventory inv = Main.instance.inventories.get(p);

            if (e.getInventory().equals(inv)) {
                e.setCancelled(true);

                if (e.getCurrentItem() == null) {
                    return;
                }

                if (e.getCurrentItem().getType() == Material.matchMaterial(main.getConfig().getString("GUIItems.RightItem"))) {

                    if(p.isFlying() == true){
                        p.setFlying(false);
                    }

                    Main.instance.inventories.remove(p);
                    p.playSound(p.getLocation(), Sound.valueOf(main.getConfig().getString("Sounds.WhenPlayerCompletesCaptcha")), main.getConfig().getInt("AllSoundVolumes.Volume"), main.getConfig().getInt("AllSoundVolumes.Pitch"));
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Main.instance.color(main.getConfig().getString("GUIItems.RightCommand").replace("%player%", p.getPlayer().getDisplayName())));
                    p.sendMessage(Main.instance.color(main.getConfig().getString("Messages.CompleteCaptchaMessage").replace("%time%", format1.format(Time)).replace("%date%", format2.format(Date)).replace("%player%", p.getDisplayName())));

                    if (this.main.data.getConfig().contains(p.getUniqueId() + ".joins")) {
                        Integer AmountOfJoins = this.main.data.getConfig().getInt(p.getUniqueId() + ".joins") + 1;
                        this.main.data.getConfig().set(p.getUniqueId() + ".joins", AmountOfJoins);
                        this.main.data.SavethisConfig();
                    }else{
                        if (!(main.data.getConfig().contains(p.getUniqueId() + ".joins"))) {
                            this.main.data.getConfig().set(p.getUniqueId() + ".joins", 0);
                            this.main.data.SavethisConfig();
                        }
                    }

                    p.closeInventory();
                }

                if (e.getCurrentItem().getType() == Material.matchMaterial(main.getConfig().getString("GUIItems.WrongItem"))) {

                    if(p.isFlying() == true){
                        p.setFlying(false);
                    }

                    Main.instance.inventories.remove(p);
                    p.closeInventory();
                    p.kickPlayer(Main.instance.color(Main.instance.getConfig().getString("Messages.kickmessage")));
                }
            }
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        if(e.getPlayer() == null)
            return;
        if(!(e.getPlayer() instanceof Player))
            return;
        Player p = (Player) e.getPlayer();
        if(!p.isOnline())
            return;
        if (!Main.instance.inventories.containsKey(p))
            return;
        Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
            @Override
            public void run() {
                p.openInventory(Main.instance.inventories.get(p));
            }
        }, 1L);
    }


    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Location loc = e.getFrom();
        if ((Main.instance.inventories.containsKey(p) && (
            (e.getTo().getY() != loc.getY()) || (e.getTo().getX() != loc.getX()) || (e.getTo().getZ() != loc.getZ())))) {
            loc.setPitch(e.getTo().getPitch());
            loc.setYaw(e.getTo().getYaw());
            p.teleport(loc);
        }
    }

    @EventHandler
    public void OnDamage(EntityDamageEvent e) {
        Entity p = e.getEntity();
        if (((e.getEntity() instanceof Player)) && (Main.instance.inventories.containsKey(p))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void CantDamage(EntityDamageByEntityEvent e) {
        Entity p = e.getDamager();
        if (((e.getEntity() instanceof Player)) && (Main.instance.inventories.containsKey(p))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onchat(AsyncPlayerChatEvent e) {

        Player p = e.getPlayer();
        if (Main.instance.getConfig().getBoolean("Chatting")) {
            if (Main.instance.inventories.containsKey(p)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void OnCommand (PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();

        if (Main.instance.getConfig().getBoolean("commands")) {
            if (Main.instance.inventories.containsKey(p)) {
                e.setCancelled(true);
            }
        }
    }

}
