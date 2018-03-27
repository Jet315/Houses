package me.jet315.houses.commands;

import org.bukkit.command.CommandSender;

/**
 * Created by Jet on 28/01/2018.
 */
public abstract class CommandExecutor {
    private String command;
    private String permission;
    private String usage;
    private boolean console;
    private boolean player;
    private int length;

    public abstract void execute(CommandSender sender, String[] args);

    public void setBoth(){
        this.player = true;
        this.console = true;
    }

    public boolean isBoth(){
        return player && console;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean isConsole() {
        return console;
    }

    public void setConsole() {
        this.console = true;
        this.player = false;
    }

    public boolean isPlayer() {
        return player;
    }

    public void setPlayer() {
        this.player = true;
        this.console = false;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
}
