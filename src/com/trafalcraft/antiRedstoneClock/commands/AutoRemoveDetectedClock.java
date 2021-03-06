package com.trafalcraft.antiRedstoneClock.commands;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.util.Msg;
import org.bukkit.command.CommandSender;

public class AutoRemoveDetectedClock {
        private static AutoRemoveDetectedClock ourInstance = new AutoRemoveDetectedClock();

        public static AutoRemoveDetectedClock getInstance() {
                return ourInstance;
        }

        private AutoRemoveDetectedClock() {
        }

        public void performCMD(CommandSender sender, String... args) {
                if (args.length == 1) {
                        if (Main.getInstance().getConfig().getBoolean("AutomaticallyBreakDetectedClock")) {
                                Main.getInstance().getConfig().set("AutomaticallyBreakDetectedClock", false);
                                Main.getInstance().saveConfig();
                                sender.sendMessage(Msg.Prefix + Msg.newValueInConfig.toString()
                                        .replace("$setting", "\"AutomaticallyBreakDetectedClock\"")
                                        .replace("$value", "false"));
                        } else {
                                Main.getInstance().getConfig().set("AutomaticallyBreakDetectedClock", true);
                                Main.getInstance().saveConfig();
                                sender.sendMessage(Msg.Prefix + Msg.newValueInConfig.toString()
                                        .replace("$setting", "\"AutomaticallyBreakDetectedClock\"")
                                        .replace("$value", "true"));
                        }
                } else {
                        if (Boolean.parseBoolean(args[1])) {
                                Main.getInstance().getConfig().set("AutomaticallyBreakDetectedClock", true);
                                Main.getInstance().saveConfig();
                                sender.sendMessage(Msg.Prefix + Msg.newValueInConfig.toString()
                                        .replace("$setting", "\"AutomaticallyBreakDetectedClock\"")
                                        .replace("$value", args[1]));
                        } else if (!Boolean.parseBoolean(args[1])) {
                                Main.getInstance().getConfig().set("AutomaticallyBreakDetectedClock", false);
                                Main.getInstance().saveConfig();
                                sender.sendMessage(Msg.Prefix + Msg.newValueInConfig.toString()
                                        .replace("$setting", "\"AutomaticallyBreakDetectedClock\"")
                                        .replace("$value", args[1]));
                        }
                }
        }
}
