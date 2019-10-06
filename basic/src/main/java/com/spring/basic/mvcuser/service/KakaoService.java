package com.spring.basic.mvcuser.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spring.basic.commons.util.OAuthValue;

@Service
public class KakaoService implements IOAuthLoginService, OAuthValue {

	@Override
	public String getAccessToken(String authCode) throws Exception {

		//액세스 토큰을 발급받는 요청 URL
		String reqURL = "https://kauth.kakao.com/oauth/token";

		//자바 언어로 Http요청 보내기
		//Url정보를 가질 URL객체 생성
		URL url = new URL(reqURL);
		//해당 요청 URL을 연결하고 연결정보를 가질 Connection객체 생성.
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();

		conn.setRequestMethod("POST"); //POST 요청 설정
		conn.setDoOutput(true); //출력결과를 받을것이냐 ? 에 대한 설정

		BufferedWriter bw = null;
		BufferedReader br = null;
		try {
			//필요로하는 파라미터를 스트림을 통해 전송.
			//문자열 출력스트림 OutputStreamWriter객체 생성
			//OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

			//성능향상을 위한 BufferStream사용// 문자열전용 스트림 BufferedWriter
			bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

			String queryParam = "grant_type=authorization_code"
					+ "&client_id=" + KAKAO_APP_KEY
					+ "&redirect_uri=http://localhost" + KAKAO_REDIRECT_PATH
					+ "&code=" + authCode;

			bw.write(queryParam); //스트림으로 파라미터 전송
			bw.flush(); //출력버퍼를 비움

			//응답상태 코드가 200이라면 요청 성공
			int resCode = conn.getResponseCode();
			//System.out.println("응답상태: " + resCode);

			//요청을 통해 응답받은 JSON객체를 읽어오기

			//응답데이터를 스트림을 통해 읽기.
			//문자열 입력스트림 InputStreamReader객체 생성
			//InputStreamReader isr = new InputStreamReader(conn.getInputStream());
			//성능향상을 위한 BufferStream사용// 문자열전용 스트림 BufferedReader
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String line = ""; //스트림에서 한줄씩 읽은 문자열데이터를 저장
			String responseData = ""; //line을 모두 결합하여 최종데이터 저장.

			while((line = br.readLine()) != null) {
				responseData += line;
			}
			System.out.println("responseData: " + responseData);

			//Gson라이브러리를 사용하여 JSON문자열을 자바 객체로 변환
			JsonParser parser = new JsonParser();
			//element는 자바로 변환된 JSON객체
			JsonElement element = parser.parse(responseData);

			String accessToken = element.getAsJsonObject().get("access_token").getAsString();
			String refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

			//System.out.println("accessToken: " + accessToken);
			//System.out.println("refreshToken: " + refreshToken);
			return accessToken;

		} catch(Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			bw.close();
			br.close();
		}
	}

	public Map<String, Object> getKakaoUserInfo(String accessToken) throws Exception {

		//사용자 정보를 얻어오는 요청 URL
		String reqURL = "https://kapi.kakao.com/v2/user/me";

		URL url = new URL(reqURL);		
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();

		conn.setRequestMethod("POST"); //POST 요청 설정
		conn.setDoOutput(true); //출력결과를 받을것이냐 ? 에 대한 설정
		
		//요청 헤더정보 설정
		conn.setRequestProperty("Authorization", "Bearer " + accessToken);

		BufferedWriter bw = null;
		BufferedReader br = null;
		try {			
			bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

			//응답상태 코드가 200이라면 요청 성공
			int resCode = conn.getResponseCode();
			System.out.println("응답상태: " + resCode);

			//요청을 통해 응답받은 JSON객체를 읽어오기
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String line = ""; //스트림에서 한줄씩 읽은 문자열데이터를 저장
			String responseData = ""; //line을 모두 결합하여 최종데이터 저장.

			while((line = br.readLine()) != null) {
				responseData += line;
			}
			System.out.println("responseData: " + responseData);
			
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(responseData);
			
			JsonObject kAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
			
			String nickname = kAccount.get("profile").getAsJsonObject().get("nickname").getAsString();
			String profileImagePath = kAccount.get("profile").getAsJsonObject().get("profile_image_url").getAsString();
			String email = kAccount.get("email").getAsString();
			String gender = kAccount.get("gender").getAsString();
			
			//System.out.println("카톡 닉네임: " + nickname);
			//System.out.println("카톡 프로필 경로: " + profileImagePath);
			//System.out.println("카톡 이메일: " + email);
			//System.out.println("성별: " + gender);
			
			Map<String, Object> kakaoUserMap = new HashMap<>();
			kakaoUserMap.put("nickname", nickname);
			kakaoUserMap.put("profileImagePath", profileImagePath);
			kakaoUserMap.put("email", email);
			kakaoUserMap.put("gender", gender);
			
			return kakaoUserMap;

		} catch(Exception e) {
			e.printStackTrace();	
			return null;
		} finally {
			bw.close();
			br.close();
		}

	}

	public void kakaoLogout(String accessToken) throws Exception {
		
		String reqUrl = "https://kapi.kakao.com/v1/user/logout";
		
		BufferedReader br = null;
		try {
			URL url = new URL(reqUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);
			
			int responseCode = conn.getResponseCode();
			System.out.println("응답코드: " + responseCode);
			
			//요청을 통해 응답받은 JSON객체를 읽어오기
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String line = ""; //스트림에서 한줄씩 읽은 문자열데이터를 저장
			String responseData = ""; //line을 모두 결합하여 최종데이터 저장.

			while((line = br.readLine()) != null) {
				responseData += line;
			}
			System.out.println("responseData: " + responseData);
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(br != null) br.close();
		}
	}

}









