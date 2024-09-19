package com.test.project.chatbot.controller;

import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

// chatbot 사용시 config.properties 에서 Secret Key, Invoke URL 가져와서 쓸 거
@PropertySource("classpath:/config.properties")
@Controller
@RequestMapping("chatbot")
@Slf4j
public class ChatbotController {

	// lombok 아니고 springFramework
	// ${} 으로 가져와야 함.
	@Value("${my.chatbot.invoke.url}")
	private String invokeUrl;
	
	@Value("${my.chatbot.Secret.key}")
	private String secretKey;
	
	@GetMapping("chatbotMain")
	public String chatbotMain() {
		return "chatbot/chatbotMain";
	}
	
    @PostMapping("/question")
    @ResponseBody
    public String sendQuestionToChatbot(@RequestBody String requestBody) {
    	
        try {
        	
        	log.info("Received Request Body: " + requestBody);
            // 2. 요청 본문을 기반으로 서명 생성
            String signature = makeSignature(requestBody, secretKey);
            log.info("Generated Signature: " + signature);

            // 3. HttpHeaders 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("X-NCP-CHATBOT_SIGNATURE", signature);
            log.info("Headers: " + headers);

            // 4. 요청 본문과 헤더를 함께 요청 객체로 생성
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            // 5. RestTemplate을 사용해 API 호출
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(invokeUrl, HttpMethod.POST, entity, String.class);

            log.info("API Response: " + response.getBody());
            
            // 6. 응답 처리
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error during API call", e);
            return "Error: " + e.getMessage();
        }
    }

    // 서명 생성 함수 (HMAC-SHA256 사용)
    private String makeSignature(String requestBody, String secretKey) throws Exception {
        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(requestBody.getBytes("UTF-8"));
        // Java 기본 Base64 클래스를 사용해 인코딩
        return Base64.getEncoder().encodeToString(rawHmac);
    }

}
