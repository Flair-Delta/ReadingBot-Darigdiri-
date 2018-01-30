package com.Jin;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


import java.io.*;
import java.net.URLEncoder;
import java.util.*;

public class JDialogue extends ListenerAdapter {

    public static HashMap<String, String> music=new HashMap<String, String>();

    //음악 기억
    public boolean teachme=false;
    public String teachmu="";
    public User teacher;

    //음악 기억 제거
    public boolean music_remove=false;
    public User remover;

    //번역
    public boolean tran_ask=false;
    public User tran_asker;
    public String Source_lang="";
    public String Target_lang="";

    //중복방지
    public boolean dupl=false;

    //외국인
    public ArrayList<User> japanese=new ArrayList<User>();
    public ArrayList<User> american=new ArrayList<User>();
    public ArrayList<User> chinese=new ArrayList<User>();

    public static File file=new File("MusicData.yml");

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
     if(!dupl) {
         User user = e.getAuthor();
         TextChannel tc = e.getTextChannel();
         Message msg = e.getMessage();

         String m = msg.getContentRaw();

         if (user.isBot()) return;

         //간단한 의사 표현
         if (m.equals("안녕") || m.equals("헬로") || m.equals("하이")) {
             tc.sendMessage("안녕하세요. " + user.getName() + " 님").queue();
         }
         if (m.equals("사랑해") || m.equals("I love you") || m.equals("아이시테루")) {
             tc.sendMessage("금지된 사랑입니다.").queue();
         }
         if (m.endsWith("못 생겼지?")) {
             String[] spl = m.split(" ");

             if (spl[0].equals("찬희") || spl[0].equals("이찬희")) {
                 tc.sendMessage("원래라면 잘 생겼다고 해야하지만, 이 분은 좀...").queue();
             } else {
                 tc.sendMessage("그럴 리가요. " + spl[0] + " 님은 잘 생기셨을거에요.").queue();
             }

         }


         //노래 기억 및 잊기
         if (m.endsWith("들려줘")) {
             String[] spl = m.split(" ");

             String name = "";
             for (int i = 0; i < spl.length - 2; i++) {
                 name += spl[i] + " ";
             }
             name += spl[spl.length - 2];

             if (name.equals("뇌장작렬걸")) {
                 tc.sendMessage("https://youtu.be/vOt2u7WcV9A").queue();
                 tc.sendMessage("아는 노래라 다행이에요!").queue();
             } else if (music.keySet().contains(name)) {
                 tc.sendMessage(music.get(name)).queue();
                 tc.sendMessage("알려주신 노래라 다행이에요!").queue();
             } else {
                 String search = name.replace(" ", "+");
                 tc.sendMessage("음... 죄송하지만 이건 모르는 노래에요.").queue();
                 try{
                     tc.sendMessage("그래서 대신에 검색해봤습니다.").queue();
                     tc.sendMessage("https://www.youtube.com/results?search_query=" + URLEncoder.encode(search, "UTF-8")).queue();
                 }catch (Exception ex){}

                 tc.sendMessage(name + "을 들을 수 있는 주소를 알려주세요.").queue();
                 teachme = true;
                 teachmu = name;
                 teacher = user; dupl=true;
             }
         }
         if (m.endsWith("바꿔줘")) {
             String[] spl = m.split(" ");

             String name = "";
             for (int i = 0; i < spl.length - 2; i++) {
                 name += spl[i] + " ";
             }
             name += spl[spl.length - 2];

             if (music.keySet().contains(name)) {
                 tc.sendMessage(name + "을 들을 수 있는 주소를 다시 알려주세요.").queue();
                 teachme = true;
                 teachmu = name;
                 teacher = user; dupl=true;
             } else {
                 tc.sendMessage("음... 죄송하지만 아예 모르는 노래에요.").queue();
             }
         }

         if (m.equals("노래를 전부 잊어줘")) {
             music_remove = true;
             remover = user;
             tc.sendMessage("정말이요?").queue(); dupl=true;
         }

         if (m.equals("알고 있는 노래 알려줘")) {
             tc.sendMessage("제가 알고 있는 노래들이에요.");
             for (String k : music.keySet()) {
                 tc.sendMessage(k + " " + music.get(k));
             }
         }

         //번역
         //조건부 응답, 순차 처리 때문에 옮김 (배열 바꾸지 말 것)
         if (tran_ask && user.equals(tran_asker)) {

             String getResult = JParser.JTranslate(m, tc, Source_lang, Target_lang);
             tc.sendMessage(getResult).queue();

             tran_ask = false; dupl=false;

         }

         if (m.endsWith("번역해줘") && !tran_ask) {


             String[] spl = m.split(" ");

             if (spl.length < 3) {
                 tc.sendMessage("<언어>에서 <언어>로 번역해줘  라고 말해주세요.").queue();
                 tc.sendMessage("[예시] 한국어에서 일본어로 번역해줘").queue();
                 tc.sendMessage("중국어는 '중국어번체에서'와 같이 번체와 간체는 '중국어'와 붙여 써주세요.").queue();
                 return;
             }

             if (spl[0].replace("에서", "").equals(spl[1].replace("로", ""))) {
                 tc.sendMessage("같은 언어로 번역할 수는 없습니다. 정신차려임마.").queue();
             }

             if (m.startsWith("한국어에서")) {
                 Source_lang = "ko";
             } else if (m.startsWith("영어에서")) {
                 Source_lang = "en";
             } else if (m.startsWith("일본어에서")) {
                 Source_lang = "ja";
             } else if (m.startsWith("중국어간체에서")) {
                 Source_lang = "zh-CN";
             } else if (m.startsWith("중국어번체에서")) {
                 Source_lang = "zh-TW";
             } else {
                 tc.sendMessage("다음 언어 외의 언어는 지원하지 않습니다.").queue();
                 tc.sendMessage("[지원 언어 목록] 한국어, 영어, 일본어, 중국어(간체·번체)").queue();
                 return;
             }

             String t = spl[1];
             if (t.equals("한국어로")) {
                 Target_lang = "ko";
             } else if (t.equals("영어로")) {
                 Target_lang = "en";
             } else if (t.equals("일본어로")) {
                 Target_lang = "ja";
             } else if (t.equals("중국어간체로")) {
                 Target_lang = "zh-CN";
             } else if (t.equals("중국어번체로")) {
                 Target_lang = "zh-TW";
             } else {
                 tc.sendMessage("다음 언어 외의 언어는 지원하지 않습니다.").queue();
                 tc.sendMessage("[지원 언어 목록] 한국어, 영어, 일본어, 중국어(간체·번체)").queue();
                 return;
             }

             tc.sendMessage("번역할 문장을 입력해주세요.").queue();
             tran_ask = true;
             tran_asker = user; dupl=true;

         }

         if(m.endsWith("의 뜻을 알려줘")){

         }

         //외국인을 위한 자동 번역
        if(m.equals("I use English")){
             tc.sendMessage("Yes, sir. I'll translate their message.").queue();
             american.add(user);
         }if(m.equals("私は日本語を書きます") || m.equals("I use Japanese")){
             tc.sendMessage("はい、私が日本語で翻訳してあげます。").queue();
             japanese.add(user);
         }if(m.equals("我用中文") || m.equals("I use Chinese")){
             tc.sendMessage("是的,我来给您翻译。").queue();
             chinese.add(user);
         }

         // 조건부 응답
         if (music_remove && user.equals(remover)) {
             if (m.equals("응")) {
                 tc.sendMessage("알겠습니다. 전부 잊을게요.").queue();
                 music.clear();
                 BufferedWriter bw;
                 try {
                     bw = new BufferedWriter(new FileWriter(file));
                     for (String k : music.keySet()) {
                         bw.write(k + " " + music.get(k));
                         bw.newLine();
                     }
                     bw.close(); dupl=false;
                 } catch (FileNotFoundException ex) {
                 } catch (IOException ex) {
                 } catch (Exception ex) {
                 }
             } else if (m.equals("아니")) {
                 tc.sendMessage("역시 그런 거였죠?").queue();
             }
         }

         if (teachme && user.equals(teacher)) {
             if (m.startsWith("http://") || m.startsWith("https://")) {

                 tc.sendMessage("감사합니다. 잘 기억하고 있을게요.").queue();
                 music.put(teachmu, m);
                 teachmu = "";
                 teachme = false;
                 BufferedWriter bw;
                 try {
                     bw = new BufferedWriter(new FileWriter(file));
                     for (String k : music.keySet()) {
                         bw.write(k + " " + music.get(k));
                         bw.newLine();
                     }
                     bw.close(); dupl=false;
                 } catch (FileNotFoundException ex) {
                     ex.printStackTrace();
                 } catch (IOException ex) {
                     ex.printStackTrace();
                 }


             } else return;
         }
     }
    }

    public void onMeesageUpdate(MessageUpdateEvent e){

        TextChannel tc=e.getTextChannel();
        String m=e.getMessage().getContentRaw();

        //찾기
        if(!japanese.isEmpty()){
            tc.sendMessage("[日本語] "+JParser.JTranslate(m, tc, "ko", "ja")).queue();
        }if(!american.isEmpty()){
            tc.sendMessage("[English] "+JParser.JTranslate(m, tc, "ko", "en")).queue();
        }if(!chinese.isEmpty()){
            tc.sendMessage("[中文(简体)] "+JParser.JTranslate(m, tc, "ko", "zh-CN")).queue();
        }

    }

}
