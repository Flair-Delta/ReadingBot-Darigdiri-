package com.Jin;

import com.Jin.com.Jin.Data.Guepsick;
import com.Jin.com.Jin.Data.KoreaGayo;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.List;
import java.util.Random;

public class JListener extends ListenerAdapter{

    public static boolean csbg=false;
    public static boolean sgbg=false;

    public static int lastReading=0;

    public boolean delete=false;

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        User user = e.getAuthor();
        TextChannel tc = e.getTextChannel();
        Message m = e.getMessage();

        if (user.isBot()) return;
        if (m.getContentRaw().equals("다링디리")) {
            tc.sendMessage("안녕하세요? " + user.getName() + " 님").queue();
            tc.sendMessage("위 두어렁셩 두어렁셩 다링디리").queue();
        }

        if(m.getContentRaw().endsWith("지워줘")){
            if(m.getContentRaw().startsWith("읽었던 거") || m.getContentRaw().startsWith("읽어준 거")){
                if(lastReading==0){
                    tc.sendMessage("음... 읽었던 게 없네요.").queue();
                }else{
                    m.delete().complete();
                    MessageHistory mh=new MessageHistory(tc);
                    List<Message> msgs=mh.retrievePast(lastReading).complete();
                    tc.deleteMessages(msgs).complete();
                    tc.sendMessage("다음에 다시 읽어드릴게요!").queue();
                    lastReading=0;

                }
            }else{
                tc.sendMessage("얼마나 지워드릴까요?").queue();
                delete=true;
            }
        }

        if (m.getContentRaw().equals("스웩!")){
            Random random=new Random();

            switch(random.nextInt(3)){
                case 0: tc.sendMessage(Guepsick.Geupsik1).queue();break;
                case 1: tc.sendMessage(Guepsick.Geupsik2).queue();break;
                case 2: tc.sendMessage(Guepsick.Geupsik3).queue();break;
            }

        }

        if(m.getContentRaw().endsWith("줄")){

            if(delete){

                String[] split=m.getContentRaw().split("줄");
                int line=0;

                try{
                    line=Integer.parseInt(split[0]);
                }catch(NumberFormatException ex){
                    tc.sendMessage("앗! 죄송하지만 "+String.valueOf(split[0])+"은 자연수가 아닙니다. 정신차려임마.").queue(); return;
                }

                if(line<1 | line>100){
                    tc.sendMessage("1에서 100 사이의 자연수를 입력해야 합니다. 정신차려임마.").queue();
                }else{
                    m.delete().complete();
                    MessageHistory mh=new MessageHistory(tc);
                    List<Message> msgs=mh.retrievePast(line).complete();
                    tc.deleteMessages(msgs).complete();
                    tc.sendMessage(line+"줄의 메세지를 모두 지웠습니다.").queue();
                }
                delete=false;
            }

        }

        if (m.getContentRaw().equals("청산별곡 읽어줘")) {
            if(!sgbg && !csbg) {csbg = true; lastReading=0;}
            else if(csbg) {tc.sendMessage("이미 청산별곡을 읽어달라고 하셨어요.").queue(); return;}
            else {tc.sendMessage("이미 다른 것을 읽어달라고 하셨어요.").queue();return;}
            tc.sendMessage("어디까지 읽어드릴까요?").queue();
        }else if(m.getContentRaw().equals("서경별곡 읽어줘")){
            if(!sgbg && !csbg) {sgbg = true; lastReading=0;}
            else if(sgbg) {tc.sendMessage("이미 서경별곡을 읽어달라고 하셨어요.").queue(); return;}
            else {tc.sendMessage("이미 다른 것을 읽어달라고 하셨어요.").queue(); return;}
            tc.sendMessage("어디까지 읽어드릴까요?").queue();
        }

        if (m.getContentRaw().equals("전부")) {
            if (csbg) {
                for (int i = 0; i < KoreaGayo.csbg.length; i++) {
                    tc.sendMessage(KoreaGayo.csbg[i]).queue(); lastReading++;
                }csbg=false;
            }if(sgbg){
                for(int i=0; i< KoreaGayo.sgbg.length; i++){
                    tc.sendMessage(KoreaGayo.sgbg[i]).queue(); lastReading++;
                }sgbg=false;
            }
        }

        if (m.getContentRaw().endsWith("연만")) {
            int yun=-256;
            int sangsua=0;
            int sangsub=0;
            String[] split=m.getContentRaw().split("연");

            try{
                yun=Integer.parseInt(split[0]);
            }catch(NumberFormatException ex){
               tc.sendMessage("앗! 죄송하지만 "+String.valueOf(split[0])+"은 자연수가 아닙니다. 정신차려임마.").queue(); return;
            }

            if (csbg) {
                if(yun==1){
                    sangsua=KoreaGayo.csbg_part;
                    for (int i = 0; i < sangsua; i++) {
                        tc.sendMessage(KoreaGayo.csbg[i]).queue(); lastReading++;
                    }
                }else if(yun>1 && yun<=KoreaGayo.csbg_all){
                    sangsua=(KoreaGayo.csbg_part*yun) + (yun-2);
                    sangsub=(KoreaGayo.csbg_part+1) * (yun-1);
                    for (int i = sangsub; i <= sangsua; i++) {
                        tc.sendMessage(KoreaGayo.csbg[i]).queue(); lastReading++;
                    }csbg=false;
                }else if(yun>KoreaGayo.csbg_all){
                    tc.sendMessage("청산별곡은 최대 8연까지 있습니다. 정신차려임마.").queue();
                }
                csbg=false;
            }else if(sgbg){
                if(yun==1){
                    sangsua=KoreaGayo.sgbg_part;
                    for (int i = 0; i < sangsua; i++) {
                        tc.sendMessage(KoreaGayo.sgbg[i]).queue(); lastReading++;
                    }
                }else if(yun>1 && yun<=KoreaGayo.sgbg_all){
                    sangsua=(KoreaGayo.sgbg_part*yun) + (yun-2);
                    sangsub=(KoreaGayo.sgbg_part+1) * (yun-1);
                    for (int i = sangsub; i <= sangsua; i++) {
                        tc.sendMessage(KoreaGayo.sgbg[i]).queue(); lastReading++;
                    }sgbg=false;
                }else if(yun>KoreaGayo.sgbg_all){
                    tc.sendMessage("청산별곡은 최대 8연까지 있습니다. 정신차려임마.").queue();
                }
                sgbg=false;
            }
        }

        if (m.getContentRaw().endsWith("연까지")) {
            int yun=-256;
            int sangsua=0;
            String[] split=m.getContentRaw().split("연");

            try{
                yun=Integer.parseInt(split[0]);
            }catch(NumberFormatException ex){
                tc.sendMessage("앗! 죄송하지만 "+String.valueOf(split[0])+"은 자연수가 아닙니다. 정신차려임마.").queue(); return;
            }

            if (csbg) {
                if(yun==1){
                    sangsua=KoreaGayo.csbg_part*1;
                    for (int i = 0; i < sangsua; i++) {
                        tc.sendMessage(KoreaGayo.csbg[i]).queue(); lastReading++;
                    }
                }else if(yun>1 && yun<=KoreaGayo.csbg_all){
                    sangsua=(KoreaGayo.csbg_part*yun) + (yun-2);
                    for (int i = 0; i <= sangsua; i++) {
                        tc.sendMessage(KoreaGayo.csbg[i]).queue(); lastReading++;
                    }csbg=false;
                }else if(yun>KoreaGayo.csbg_all){
                    tc.sendMessage("청산별곡은 8연까지입니다. 정신차려임마.").queue();
                }
                csbg=false;
            }else if (sgbg) {
                if(yun==1){
                    sangsua=KoreaGayo.sgbg_part*1;
                    for (int i = 0; i < sangsua; i++) {
                        tc.sendMessage(KoreaGayo.sgbg[i]).queue(); lastReading++;
                    }
                }else if(yun>1 && yun<=KoreaGayo.sgbg_all){
                    sangsua=(KoreaGayo.sgbg_part*yun) + (yun-2);
                    for (int i = 0; i <= sangsua; i++) {
                        tc.sendMessage(KoreaGayo.sgbg[i]).queue(); lastReading++;
                    }sgbg=false;
                }else if(yun>KoreaGayo.sgbg_all){
                    tc.sendMessage("서경별곡은 8연까지입니다. 정신차려임마.").queue();
                }
                sgbg=false;
            }

        }

    }
}
