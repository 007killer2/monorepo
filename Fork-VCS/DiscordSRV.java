package com.adt.discordsrv;

import com.adt.discordsrv.cmd.*;
import com.adt.discordsrv.cmd.newcmd.*;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ml.luxinfine.config.api.ConfigAPI;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

import javax.security.auth.login.LoginException;
import java.util.Map;
import java.util.Set;

@Mod(modid = "discordsrv", name = "DiscordSRV", version = "@VERSION@", acceptableRemoteVersions = "*")
public class DiscordSRV {
    @Mod.EventHandler
    public void prePreInit(FMLConstructionEvent e) {
//        String token = DConfigNew.TOKEN;
//        new DiscordThread(token).start();
        String token = System.getProperty("discordsrv.bot_token");//DConfigGeneral.jsonObject.get("bot_token").getAsString();
        if (token == null) {
            System.out.println("DiscordSRV: bot_token is not set. Please set it in the config file.");
            return;
        }

        String guildId = System.getProperty("discordsrv.guild_id");
        //System.out.println("guildId1 = " + guildId);
        new DiscordSRV.DiscordThread(token, "Запуск сервера...").start();
    }


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {}

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        FMLCommonHandler.instance().bus().register(new DEventHandler());
        MinecraftForge.EVENT_BUS.register(new DEventHandler());
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent e) {

        if (DConfigNew.enableRTP) {
            e.registerServerCommand(new RTPCommand());
            //RTP.initRTP();
        }
        if (DConfigNew.enableSetBlockCommand) {
            //e.registerServerCommand(new SetBlockCommand());
            //e.registerServerCommand(new GetBlockCommand());
            GetBlock.initGetBlock();
            SetBlock.initSetBlock();

        }
        if (DConfigNew.enableSetStatusCommand) {
            //e.registerServerCommand(new SetStatusCommand());
            SetStatus.initSetStatus();
        }
        if (DConfigNew.enableGetInfoCommand) {
            //e.registerServerCommand(new GetInfoCommand());
            GetInfo.initGetInfo();
        }
        if (DConfigNew.enableTeleportCommnad) {
            //e.registerServerCommand(new TeleportCommand());
            Teleport.initTeleport();
        }
        int online = 0;
        boolean isWhitelist = false;
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        OnlineListener listener = new OnlineListener();
        if (server.getConfigurationManager() != null) {
            online = server.getMaxPlayers();
            if (server.getConfigurationManager().isWhiteListEnabled()) {
                isWhitelist = true;
            }
        }
        try {
            OnlineListener.init(online, isWhitelist);
        } catch (LoginException ex) {
            ex.printStackTrace();
        }

    }


    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent e) {
        OnlineListener listener = new OnlineListener();
        listener.sendStopServer();
        listener.killBot();

    }

    @SideOnly(Side.SERVER)
    public static class DiscordThread extends Thread {
        public DiscordThread(final String token, final String status) {
            try {
                OnlineListener.preInit(token, status);
                if (OnlineListener.getJda() != null) {
                    OnlineListener.setStatus(false);
                    //listener.sendLaunchServer();
                }
            } catch (LoginException e) {
                e.printStackTrace();
            }
        }
    }
}
