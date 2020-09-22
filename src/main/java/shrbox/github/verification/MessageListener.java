package shrbox.github.verification;

import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageUtils;

import java.util.function.Consumer;

public class MessageListener implements Consumer <GroupMessageEvent> {
    @Override
    public void accept(GroupMessageEvent event) {
        if(event.getGroup().getBotPermission().getLevel()==0||!Main.config.getLongList("enable_group").contains(event.getGroup().getId())) {
            return;//如果机器人权限不足或该群不在开启的群列表内
        }
        //event.getGroup().sendMessage("JoinEvent权限足够");
        long memberid = event.getSender().getId();//获取QQ号
        long groupid = event.getGroup().getId();
        if(Main.checkverMember(memberid,groupid)) {
            int vercode = Main.getVerification(memberid,groupid);//获取对应验证码

            if(event.getMessage().contentToString().equals(String.valueOf(vercode))) {
                Main.removeverMember(memberid,groupid);
                event.getGroup().sendMessage(MessageUtils.newChain("恭喜你！")
                        .plus(new At(event.getSender()))
                        .plus("\n你已经通过了加群验证！"));
            } else {
                event.getGroup().sendMessage(MessageUtils.newChain("很遗憾，")
                        .plus(new At(event.getSender()))
                        .plus("\n你没有通过本群的加群验证，请重试！\n验证码："+vercode));
            }
        }
    }
}
