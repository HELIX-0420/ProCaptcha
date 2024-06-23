package me.helix.procaptcha.GUI;

import me.helix.procaptcha.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CaptchaGUI implements Listener {

    private Main main;
    private Player p;

    public CaptchaGUI () {
        this.main = Main.instance;
    }

    public void build(Player p) {
        this.p = p;

        Inventory inv = Bukkit.createInventory(null, 27, Main.color(Main.instance.getConfig().getString("GUIItems.Title")));

        int randomNum = ThreadLocalRandom.current().nextInt(0, 27);

        ItemStack wrong = new ItemStack(Material.matchMaterial(main.getConfig().getString("GUIItems.WrongItem")));
        ItemMeta wrongtmeta = wrong.getItemMeta();
        wrongtmeta.setDisplayName(Main.color(main.getConfig().getString("Messages.WrongTitle")));
        wrong.setItemMeta(wrongtmeta);
        if (main.getConfig().getBoolean("GUIItems.WrongItemGlow") == true) {
            wrong.addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);
        }

        ItemStack right = new ItemStack(Material.matchMaterial(main.getConfig().getString("GUIItems.RightItem")));
        ItemMeta righttmeta = right.getItemMeta();
        righttmeta.setDisplayName(Main.color(main.getConfig().getString("Messages.RightTitle")));
        right.setItemMeta(righttmeta);
        if (main.getConfig().getBoolean("GUIItems.RightItemGlow") == true) {
            right.addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);
        }

        Date Time = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
        Date Date = new Date();
        SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy");

        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1, (short)3);
        SkullMeta headmeta = (SkullMeta)head.getItemMeta();
        headmeta.setOwner(p.getName());
        List Lore = new ArrayList();
        for (String Headlore : this.main.getConfig().getStringList("GUIsPlayerHead.Lore")) {
            Lore.add(Main.instance.color(Headlore).replace("%time%", format1.format(Time)).replace("%date%", format2.format(Date)).replace("%player%", p.getDisplayName()));
        }
        headmeta.setLore(Lore);
        headmeta.setDisplayName(Main.instance.color(main.getConfig().getString("GUIsPlayerHead.Title")).replace("%time%", format1.format(Time)).replace("%date%", format2.format(Date)).replace("%player%", p.getDisplayName()));
        head.setItemMeta(headmeta);

        for (int i = 0; i < 27; i++) {
            inv.setItem(i, wrong);
        }

        inv.setItem(13, head);

        inv.setItem(randomNum, right);

        this.main.inventories.put(p, inv);
        this.main.inventories.put(p, inv);

        Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
            @Override
            public void run() {
                p.openInventory(inv);
            }
        }, 1L);
    }


}
