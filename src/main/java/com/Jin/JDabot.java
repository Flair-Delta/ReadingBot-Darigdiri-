package com.Jin;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;

import javax.security.auth.login.LoginException;
import java.io.*;

public class JDabot {

    public static JDA jda;

    public static void main(String[] args){

        JDABuilder jb = new JDABuilder(AccountType.BOT);
        jb.setAutoReconnect(true);
        jb.setStatus(OnlineStatus.ONLINE);
        jb.setToken("NDA1OTk5OTk2NzgyMTE2ODY1.DUskhA.-XQO1e2iApHeFCUhad5s-MxMjPM");
        jb.addEventListener(new JListener());
        jb.addEventListener(new JDialogue());
        jb.addEventListener(new JInfo());
        jb.setBulkDeleteSplittingEnabled(true);


        FileInputStream fis;
        InputStreamReader isr;
        BufferedReader br;
        try {
            fis = new FileInputStream(JDialogue.file);
            isr=new InputStreamReader(fis);
            br=new BufferedReader(isr);
            String line;

            System.out.println("[Daringdiri] 봇이 켜졌습니다.");
            System.out.println("[Daringdiri] 다음은 제가 기억하고 있는 곡의 목록입니다.");

            while((line=br.readLine())!=null){
                System.out.println("[Daringdiri] "+line);
                String[] spl=line.split(" ");
                String name="";

                for(int i=0;i<spl.length-2;i++){
                    name+=spl[i]+" ";
                }name+=spl[spl.length-2];

                JDialogue.music.put(name, spl[spl.length-1]);
            }

        }catch(FileNotFoundException ex){
            System.out.println("처음 켜셨나요? MusicData 파일이 없네요.");
        }catch(IOException ex){
            System.out.println("앗.. 뭔가 오류가..");
        }

        try{
            jda=jb.buildBlocking();
        }catch(LoginException e){
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }

    }

}
