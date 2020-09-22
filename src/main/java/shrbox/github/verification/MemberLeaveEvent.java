package shrbox.github.verification;

import java.util.function.Consumer;

public class MemberLeaveEvent implements Consumer<net.mamoe.mirai.event.events.MemberLeaveEvent> {
    @Override
    public void accept(net.mamoe.mirai.event.events.MemberLeaveEvent event) {
        if(event.getGroup().getBotPermission().getLevel()==0|| !Main.config.getLongList("enable_group").contains(event.getGroup().getId())) {
            return;//如果机器人权限不足或该群不在开启的群列表内
        }
        long memberid = event.getMember().getId();
        long groupid = event.getGroup().getId();
        if(Main.checkverMember(memberid,groupid)) {
            Main.removeverMember(memberid,groupid);
        }
    }
}
