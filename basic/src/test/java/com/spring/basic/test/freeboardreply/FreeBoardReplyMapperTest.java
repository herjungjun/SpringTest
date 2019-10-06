package com.spring.basic.test.freeboardreply;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.spring.basic.freeboardreply.domain.FreeBoardReply;
import com.spring.basic.freeboardreply.repository.IFreeBoardReplyMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"classpath:/spring/mvc-config.xml"})
public class FreeBoardReplyMapperTest {
	
	@Autowired
	private IFreeBoardReplyMapper mapper;
	
	@Test
	public void createTest() {
		
		FreeBoardReply reply = new FreeBoardReply();
		reply.setBoardId(100);
		reply.setReplyText("100번에 글에 대한 댓글입니다.");
		reply.setReplyWriter("댓글킹");
		
		mapper.create(reply);
		System.out.println("댓글 등록 완료!");
		
	}
	
	@Test
	public void updateTest() {
		FreeBoardReply reply = new FreeBoardReply();
		reply.setReplyId(5);
		reply.setReplyText("댓글 수정함~");
		
		mapper.update(reply);
		System.out.println("댓글 수정 완료!");
		
	}
	
	@Test
	public void deleteTest() {
		
		mapper.delete(15);
		System.out.println("댓글 삭제 완료!");
	}
	
	@Test
	public void selectAllTest() {
		
		/*
		List<FreeBoardReply> list = mapper.selectAll(100);
		for (FreeBoardReply re : list) {
			System.out.println(re);
		}*/
		
		//mapper.selectAll(100).forEach(re -> System.out.println(re));
	}

}










