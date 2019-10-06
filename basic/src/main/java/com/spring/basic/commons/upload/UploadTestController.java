package com.spring.basic.commons.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.spring.basic.commons.util.FileUtils;

@RestController
public class UploadTestController {
	
//	@Autowired
//	@Qualifier("uploadPath")
	@Resource(name="uploadPath")
	private String uploadPath;

	@GetMapping("/uploadForm")
	public ModelAndView uploadForm() {
		return new ModelAndView("test/upload-form");
	}
	
	//클라이언트가 전송한 파일데이터를 담을 객체 MultipartFile를 매개변수로 지정
	@PostMapping("/upload")
	public ModelAndView upload(MultipartFile file) throws Exception {
		
		System.out.println("original-name: " + file.getOriginalFilename());
		System.out.println("size: " + file.getSize());
		System.out.println("content-type: " + file.getContentType());
		
		ModelAndView mv = new ModelAndView();
		
		//실제로 서버의 업로드파일 디렉토리에 업로드를 수행하는 기능
		String fileName = FileUtils.uploadFile(file, uploadPath);
		mv.addObject("fileName", fileName);
		
		mv.setViewName("test/upload-form");
		return mv;
	}
	
	//drag & drop으로 전송된 파일 저장 요청처리
	@PostMapping(value="/uploadAjaxes")
	public ResponseEntity<String[]> uploadAjaxes(MultipartFile[] files) throws Exception {
//		System.out.println("original-name: " + file.getOriginalFilename());
//		System.out.println("size: " + file.getSize());
//		System.out.println("content-type: " + file.getContentType());
		
		int len = (files == null) ? 0 : files.length;
		
		try {
			String[] uploadedFileNames = new String[len];
			for(int i=0; i<len; i++) {
				uploadedFileNames[i] = FileUtils.uploadFile(files[i], uploadPath);
			}
			return new ResponseEntity<>(uploadedFileNames, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new String[] {e.getMessage()}, HttpStatus.BAD_REQUEST);
		}
	}

	//파일 로드 요청처리
	//요청 URI: /loadFile?fileName=~~~~~
	@GetMapping("/loadFile")
	public ResponseEntity<byte[]> loadFile(String fileName) throws Exception {
		
		System.out.println("요청 파일명: " + fileName);
		System.out.println("요청 파일 전체경로: " + uploadPath + fileName);
		
		//클라이언트가 요청한 파일의 전체경로의 객체
		File file = new File(uploadPath + fileName);
		if(!file.exists()) 
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
		//서버에서 파일명을 통해 파일을 로딩함.
		//파일 로딩시에는 InputStream객체 사용.
		try(InputStream in = new FileInputStream(file)) {
			//파일로딩 성공시 파일을 찾아서 로딩한 후 클라이언트에 전송
			String ext = FileUtils.getFileExtension(fileName);
			MediaType mType = FileUtils.getMediaType(ext);			
			
			//파일의 MimeType을 HttpHeader에 담아서 클라이언트에 전송
			HttpHeaders headers = new HttpHeaders();
			
			//이미지인지 여부 확인
			if(mType != null) { //이미지파일의 경우: 단순 썸네일을 읽어서 보냄.
				headers.setContentType(mType);
			} else { //이미지파일이 아닌 경우: 첨부파일 다운로드 기능을 붙임.
				
				//UUID로 생성한 파일명을 원래대로 되돌리는 작업.
				fileName = fileName.substring(fileName.lastIndexOf("_") + 1);
				
				//다운로드 기능을 추가한다.
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				
				//파일명이 한글일경우 인코딩을 재설정.
				String encodingName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
				
				//첨부파일 형태로 다운로드하겠다.				
				headers.add("Content-Disposition", 
						"attachment; filename=\""+ encodingName + "\"");
			}
			//1번째 매개값으로 InputStream으로 읽은 파일의 데이터를 바이트배열로 바꿔서 
			//클라이언트에 응답.
			//2번째 매개값으로 헤더정보를 전송.
			return new ResponseEntity<>(IOUtils.toByteArray(in), headers, HttpStatus.OK);
		} catch (Exception e) {
			//파일로딩 오류시 클라이언트에게 보낼 데이터 처리
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	//서버에 있는 파일 삭제 요청처리
	//URI: /deleteFile?fileName=/2019/09/22/s_djfksldfjs_abc.jpg
	@DeleteMapping("/deleteFile")
	public ResponseEntity<String> deleteFile(String fileName) throws Exception {
		
		try {			
			//파일 삭제 
			File delFile = new File(uploadPath + fileName);
			delFile.delete();
			
			//이미지파일이라면 원본이미지까지 지워야 한다!
			boolean isImage = FileUtils.getMediaType(FileUtils.getFileExtension(fileName)) != null;
			if(isImage) { //이미지라면~
				//fileName => 썸네일 이미지의 경로 ex) /2019/09/22/s_dfjlsfjl_monkey.gif
				//originalName => 원본 이미지의 경로 ex) /2019/09/22/dfjlsfjl_monkey.gif
				int lastSlash = fileName.lastIndexOf("/") + 1;
				String originalName = fileName.substring(0, lastSlash) + fileName.substring(lastSlash + 2);
				File orginalImageFile = new File(uploadPath + originalName);
				orginalImageFile.delete();
			}
			
			return new ResponseEntity<>("fileDelSuccess", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
}










