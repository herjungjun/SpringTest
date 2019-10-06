package com.spring.basic.commons.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.basic.freeboard.domain.FreeBoard;

@RestController
public class RestTestController {
	
	@GetMapping("/rest/hello")	
	//리턴데이터를 ViewResolver에게 주지않고
	//즉시 클라이언트로 전송함.
	public String hello() {
		return "hello world!!!!!@";
	}
	
	@GetMapping("/rest/obj")
	public FreeBoard testObj() {
		FreeBoard article = new FreeBoard();
		
		article.setBoardId(99);
		article.setTitle("JSON 전송테스트");
		article.setContent("RestController사용중~");
		article.setWriter("테스트왕");
		
		return article;
	}
	
	@GetMapping("/rest/list")
	public List<String> testList() {
		List<String> list = new ArrayList<>();		
		list.add("야옹이");list.add("멍멍이");list.add("짹짹이");		
		return list;
	}
	
	@GetMapping("/rest/map")
	public Map<String, Object> testMap() {
		Map<String, Object> map = new HashMap<>();
		
		map.put("이름", "김철수");
		map.put("나이", 34);
		map.put("취미", new String[] {"축구", "게임", "음악감상"});
		
		return map;
	}
	
}










