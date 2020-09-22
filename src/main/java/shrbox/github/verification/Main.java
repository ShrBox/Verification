package shrbox.github.verification;

import net.mamoe.mirai.console.command.BlockingCommand;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.JCommandManager;
import net.mamoe.mirai.console.plugins.Config;
import net.mamoe.mirai.console.plugins.PluginBase;
import net.mamoe.mirai.message.GroupMessageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main extends PluginBase {
    private static Main plugin;
    public static Config config;
    public static Config verification;
    public static List<String> vlist = new ArrayList<>();
    public static Main getPlugin() {
        return plugin;
    }
    public static void removeverMember(long memberid,long groupid) {
        String s_memberid = String.valueOf(memberid);
        String s_groupid = String.valueOf(groupid);
        String checkid = s_memberid+"_"+s_groupid;
        vlist.remove(checkid);
        verification.set(checkid,0);
    }

    public static void addverMember(long memberid,long groupid,int code) {
        String s_memberid = String.valueOf(memberid);
        String s_groupid = String.valueOf(groupid);
        String checkid = s_memberid+"_"+s_groupid;
        vlist.add(checkid);//添加新成员到List
        verification.set(checkid,code);
    }

    public static boolean checkverMember(long memberid,long groupid) {
        String s_memberid = String.valueOf(memberid);
        String s_groupid = String.valueOf(groupid);
        return vlist.contains(s_memberid + "_" + s_groupid);
    }

    public static int getVerification(long memberid,long groupid) {
        String s_memberid = String.valueOf(memberid);
        String s_groupid = String.valueOf(groupid);
        String checkid = s_memberid+"_"+s_groupid;
        return verification.getInt(checkid);
    }

    public void registerCommands() {
        JCommandManager.getInstance().register(this, new BlockingCommand(
                "verreload", new ArrayList<>(),"重载Verification配置文件","/verreload"
        ) {
            @Override
            public boolean onCommandBlocking(@NotNull CommandSender commandSender, @NotNull List<String> list) {
                loadConfigFiles();
                commandSender.sendMessageBlocking("重载成功");
                return true;
            }
        });
    }

    public void onLoad() {}
    public static void loadConfigFiles() {
        config = getPlugin().loadConfig("config.yml");
        List<Long> templist = new ArrayList<>();
        Collections.addAll(templist, 114514L, 1919810L);
        config.setIfAbsent("enable_group",templist);
        config.setIfAbsent("timeout", 300);
        config.save();
        templist.clear();
    }

    public void onEnable() {
        plugin = this;
        registerCommands();
        verification = loadConfig("verification.yml");//并没有什么卵用，只为了对应验证码
        loadConfigFiles();

        getLogger().info("Plugin enabled!");
        this.getEventListener().subscribeAlways(net.mamoe.mirai.event.events.MemberJoinEvent.class,new MemberJoinEvent());
        this.getEventListener().subscribeAlways(GroupMessageEvent.class,new MessageListener());
        this.getEventListener().subscribeAlways(net.mamoe.mirai.event.events.MemberJoinEvent.class,new MemberJoinEvent());
    }
}          