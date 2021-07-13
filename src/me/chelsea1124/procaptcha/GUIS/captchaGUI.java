package me.chelsea1124.procaptcha.GUIS;

import io.netty.util.internal.ThreadLocalRandom;
import me.chelsea1124.procaptcha.Main;
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
import java.util.*;


public class captchaGUI implements Listener {

    private Main main;

    public captchaGUI ()
    {
        this.main = Main.instance;
    }

    public void build(Player p) {

        Inventory inv = Bukkit.createInventory(null, 27, Main.color(Main.instance.getConfig().getString("GUIItems.Title")));

        int randomNum = ThreadLocalRandom.current().nextInt(0, 27);

        ItemStack wrong = new ItemStack(Material.getMaterial(String.valueOf(main.getConfig().getInt("GUIItems.WrongItem"))));
        ItemMeta wrongtmeta = wrong.getItemMeta();
        wrongtmeta.setDisplayName(Main.color(main.getConfig().getString("Messages.WrongTitle")));
        wrong.setItemMeta(wrongtmeta);
        if (main.getConfig().getBoolean("GUIItems.WrongItemGlow") == true) {
            wrong.addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);
        }

        ItemStack right = new ItemStack(Material.getMaterial(String.valueOf(main.getConfig().getInt("GUIItems.RightItem"))));
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
        ArrayList<String> lore = new ArrayList();
        lore.add("");
        lore.add("");
        lore.add("§6§l* §e§lInformation");
        lore.add(Main.color("&c● &eThe time is &7» &7&l" + format1.format(Time) ));
        lore.add(Main.color("&c● &eThe date is &7» &7&l" + format2.format(Date) ));
        headmeta.setLore(lore);
        headmeta.setDisplayName(Main.color("&c" + p.getDisplayName()));
        head.setItemMeta(headmeta);

        if (main.getConfig().getBoolean("GUIItems.setitem")) {
            for (int i = 0; i < 27; i++) {
                inv.setItem(i, wrong);
            }
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
