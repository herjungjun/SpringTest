package com.spring.basic.commons.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spring.basic.commons.util.FileUtils;

@Controller
public class UploadController {
	
	@Autowired
	@Qualifier("uploadPath")
	private String uploadPath;
	
	@GetMapping("/uploadForm")
	public String upload() {
		return "/test/uploadForm";
	}
	
	//파일업로드 요청처리
	@PostMapping("/uploadForm")
	public String upload(MultipartFile file, Model model) throws Exception {
		System.out.println("original-name: " + file.getOriginalFilename());
		System.out.println("size: " + file.getSize());
		System.out.println("content-type: " + file.getContentType());
		
		String savedFileName = FileUtils.uploadFile(file, uploadPath);
		model.addAttribute("savedFileName", savedFileName);
		
		return "/test/uploadForm";
	}
	
	//파일업로드 비동기 요청처리
	@ResponseBody
	@PostMapping(value="/uploadAjaxes")
	public ResponseEntity<String[]> uploadAjaxes(MultipartFile[] file) throws Exception {
		//System.out.println(file);
//		System.out.println("original-name: " + file.getOriginalFilename());
//		System.out.println("size: " + file.getSize());
//		System.out.println("content-type: " + file.getContentType());
		
		int len = file == null ? 0 : file.length;
		
		try {
			String[] savedFileNames = new String[len];
			for (int i = 0; i < file.length; i++) {
				savedFileNames[i] = FileUtils.uploadFile(file[i], uploadPath);					
			}
			return new ResponseEntity<>(savedFileNames, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new String[] {e.getMessage()}, HttpStatus.BAD_REQUEST);
		}
	}

	
	//파일 로딩 요청처리
	@ResponseBody
	@GetMapping("/displayFile")
	public ResponseEntity<byte[]> displayFile(String fileName) throws Exception {
		
		File file = new File(uploadPath + fileName);
		if(!file.exists()) 
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
		try(InputStream in = new FileInputStream(file)) {
			String ext = FileUtils.getFileExtension(fileName);
			MediaType mType = FileUtils.getMediaType(ext);
			HttpHeaders headers = new HttpHeaders(); 
			
			if(mType != null) { //이미지파일일 경우
				headers.setContentType(mType);
			} else { //이미지가 아닌 경우
				fileName = fileName.substring(fileName.indexOf("_") + 1);
				//파일을 다운로드 시키겠다.
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				
				//첨부파일로 다운로드하겠다.
				String dsp = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
				headers.add("Content-Disposition", 
						"attachment; filename=\"" + dsp + "\"");
			}			
			return new ResponseEntity<>(IOUtils.toByteArray(in), headers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	//파일 삭제 요청처리
	@ResponseBody
	@DeleteMapping("/deleteFile")
	public ResponseEntity<String> deleteFile(String fileName) throws Exception {
		
		try {	
			//이미지파일 여부 확인
			boolean isImage = FileUtils.getMediaType(FileUtils.getFileExtension(fileName)) != null;
			File file = new File(uploadPath + fileName);
			file.delete();
			
			// image면 원본 이미지도 삭제!
			if (isImage) {
				// /2018/09/21/s_resaldsfjadfldsj_realname.jpg
				int lastSlash = fileName.lastIndexOf("/") + 1;
				String realName = fileName.substring(0, lastSlash) + fileName.substring(lastSlash + 2);
				File real = new File(uploadPath + realName);
				real.delete();
			}
			
			return new ResponseEntity<>("deleted", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	

}









