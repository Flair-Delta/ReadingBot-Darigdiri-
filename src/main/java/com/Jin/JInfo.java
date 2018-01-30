package com.Jin;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class JInfo extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        User user = e.getAuthor();
        TextChannel tc = e.getTextChannel();
        Message m = e.getMessage();

        if (m.getContentRaw().equals("SWVersion 알려줘")) {
            tc.sendMessage("제 소프트웨어 버전은 v1.0.1 (20180129Z)입니다.").queue();
        }

    }

}
