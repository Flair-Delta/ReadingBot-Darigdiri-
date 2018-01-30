package com.Jin;

import net.dv8tion.jda.core.entities.TextChannel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class JParser {

    public static String JTranslate(String message, TextChannel tc, String Source_lang, String Tageet_lang){
        String str2="";
        String clientId = "GyXDVvMAll7vG2Lf8wUw";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "bqm7HQ6GW4";//애플리케이션 클라이언트 시크릿값";
        try {
            String text = URLEncoder.encode(message, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/language/translate";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            // post request
            String postParams = "source="+Source_lang+"&target="+Tageet_lang+"&text=" + text;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            JSONParser parser = new JSONParser();
            JSONObject ob=(JSONObject)parser.parse(response.toString());

            try{
                String s=String.valueOf(ob.get("message"));

                JSONObject ob1=(JSONObject)parser.parse(String.valueOf(s));

                String str=String.valueOf(ob1.get("result"));

                JSONObject ob2=(JSONObject)parser.parse(String.valueOf(str));

                str2=String.valueOf(ob2.get("translatedText"));
            }catch(NullPointerException ex){
                String er=String.valueOf(ob.get("errorCode"));
                if(er.equals("TR06")){
                    str2="해당 언어의 조합은 지원하지 않습니다.";
                }
            }

        } catch (Exception ex) {
            tc.sendMessage("죄송해요. 오류가 났네요.").queue();
            ex.printStackTrace();
        }

        return str2;
    }

}
