package com.trafalcraft.antiRedstoneClock.listener;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClock;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClockController;
import com.trafalcraft.antiRedstoneClock.util.Msg;
import com.trafalcraft.antiRedstoneClock.util.WorldGuardLink;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

class Util {
        static void checkAndUpdateRedstoneClockState(Block block) {
                if (!RedstoneClockController.contains(block.getLocation())) {
                        try {
                                RedstoneClockController.addRedstone(block.getLocation());
                        } catch (Exception e1) {
                                e1.printStackTrace();
                        }
                }else{
                        if (!RedstoneClockController.getRedstoneClock(block.getLocation()).isEnd()) {
                                if (RedstoneClockController.getRedstoneClock(block.getLocation()).getNumberOfClock()
                                        >= Main.getInstance().getConfig().getInt("MaxPulses")) {
                                        removeRedstoneClock(block);
                                }else{
                                        RedstoneClockController.getRedstoneClock(block.getLocation()).addOneToClock();
                                }
                        }
                }
        }

        static boolean checkIgnoreWorldsAndRegions(Block block) {
                for(String ignoreWorld: Main.getIgnoredWorlds()){
                        if (block.getWorld().getName().equals(ignoreWorld)) {
                                return true;
                        }
                }
                return WorldGuardLink.checkAllowedRegion(block.getLocation());
        }

        static void removeRedstoneClock(Block block) {
                if (Main.getInstance().getConfig().getBoolean("AutomaticallyBreakDetectedClock")) {
                        if (Main.getInstance().getConfig().getBoolean("DropItems")) {
                                block.breakNaturally();
                        } else {
                                block.setType(Material.AIR);
                        }
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                                if (Main.getInstance().getConfig().getBoolean("CreateSignWhenClockIsBreak")) {
                                        block.setType(Material.SIGN_POST, false);
                                        BlockState blockState = block.getState();
                                        Sign sign = (Sign) blockState;
                                        sign.setLine(0, Main.getInstance().getConfig().getString("Sign.Line1")
                                                .replace("&", "§"));
                                        sign.setLine(1, Main.getInstance().getConfig().getString("Sign.Line2")
                                                .replace("&", "§"));
                                        sign.setLine(2, Main.getInstance().getConfig().getString("Sign.Line3")
                                                .replace("&", "§"));
                                        sign.setLine(3, Main.getInstance().getConfig().getString("Sign.Line4")
                                                .replace("&", "§"));
                                        sign.update(false, false);
                                } else {
                                        block.setType(Material.AIR);
                                }
                                Bukkit.getLogger()
                                        .info(Msg.Prefix + Msg.MsgToAdmin.toString()
                                                .replace("$X", block.getX() + "")
                                                .replace("$Y", block.getY() + "").replace("$Z", block.getZ() + "")
                                                .replace("$World", block.getWorld().getName()));
                                if (Main.getInstance().getConfig().getBoolean("NotifyAdmins")) {
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                                if (p.isOp() || p.hasPermission("antiRedstoneClock.NotifyAdmin")) {
                                                        p.sendMessage(
                                                                Msg.Prefix + Msg.MsgToAdmin.toString()
                                                                        .replace("$X", block.getX() + "")
                                                                        .replace("$Y", block.getY() + "")
                                                                        .replace("$Z", block.getZ() + "")
                                                                        .replace("$World", block.getWorld().getName()));
                                                }
                                        }
                                }
                                RedstoneClockController.removeRedstoneByLocation(block.getLocation());
                        }, 1L);
                } else {
                        RedstoneClock redstoneClock = RedstoneClockController.getRedstoneClock(block.getLocation());
                        if (!redstoneClock.getDetected()) {
                                redstoneClock.setDetected(true);
                                Bukkit.getLogger()
                                        .info(Msg.Prefix + Msg.MsgToAdmin.toString()
                                                .replace("$X", block.getX() + "")
                                                .replace("$Y", block.getY() + "")
                                                .replace("$Z", block.getZ() + "")
                                                .replace("$World", block.getWorld().getName()));
                                if (Main.getInstance().getConfig().getBoolean("NotifyAdmins")) {
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                                if (p.isOp() || p.hasPermission("antiRedstoneClock.NotifyAdmin")) {
                                                        p.sendMessage(
                                                                Msg.Prefix + Msg.MsgToAdmin.toString()
                                                                        .replace("$X", block.getX() + "")
                                                                        .replace("$Y", block.getY() + "")
                                                                        .replace("$Z", block.getZ() + "")
                                                                        .replace("$World", block.getWorld().getName()));
                                                }
                                        }
                                }

                        }
                }
        }
}