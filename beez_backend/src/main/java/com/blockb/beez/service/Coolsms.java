package com.blockb.beez.service;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

@Service
public class Coolsms {
    public static final String api_key = "NCSCBMGMP2MCQA2I";
    public static final String api_secret = "BLT2ENWSMKPPFP4W563SOT9JITAG3KVU";

    public int sendCoolsms(String phone){
        Message coolsms = new Message(api_key, api_secret);

        // 휴대폰 인증번호 생성
        int authNum = randomRange(100000, 999999);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phone);
		params.put("from", "01083289772");
		params.put("type", "SMS");
		params.put("text", "[BEEZ] 인증번호 "+ authNum +" 를 입력하세요.");
		params.put("app_version", "test app 1.2"); 

        try {
			JSONObject obj = (JSONObject) coolsms.send(params);
			System.out.println(obj.toString());
		} catch (CoolsmsException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCode());
		}

        return authNum;
    }

    // 인증번호 범위 지정
    public static int randomRange(int n1, int n2) {
        return (int) (Math.random() * (n2 - n1 + 1)) + n1;
    }
}
