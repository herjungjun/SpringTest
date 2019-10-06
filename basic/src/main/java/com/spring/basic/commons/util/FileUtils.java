package com.spring.basic.commons.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
	
	//6. 이미지 구분을 위한 이미지 확장자 맵 만들기
	private static Map<String, MediaType> mediaMap;
	static {
		mediaMap = new HashMap<>();
		mediaMap.put("JPG", MediaType.IMAGE_JPEG);
		mediaMap.put("PNG", MediaType.IMAGE_PNG);
		mediaMap.put("GIF", MediaType.IMAGE_GIF);
	}
	//7. 미디어타입을 가져오는 메서드
	public static MediaType getMediaType(String ext) {
		return mediaMap.get(ext.toUpperCase());
	}

	//1. 파일 업로드 수행 후 저장파일명을 리턴하는 메서드
	public static String uploadFile(MultipartFile file, String uploadPath) throws IOException {

		//중복이 없는 파일명으로 변경하기
		//ex) abc.gif -> 3eddf23-dfsd3-dfsdf3-342_abc.gif
		String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		System.out.println("생성된 파일명: " + fileName);

		//uploadPath: ~~/workspace/~~~~/upload/ + 파일명
		//newUploadPath: ~~/workspace/~~~~/upload/2019/09/21/ + 파일명
		String newUploadPath = getNewUploadPath(uploadPath);
		
		//업로드할 파일을 객체로 포장 (생성자에 디렉토리와 파일명을 전달.)
		File target = new File(newUploadPath, fileName);
		//업로드한 파일을 복사하여 타겟경로에 저장.
		FileCopyUtils.copy(file.getBytes(), target);
		
		//만약에 업로드한 파일이 이미지라면?? 썸네일을 만든다.
		//이미지가 아니라면 해당 파일의 아이콘을 만든다.
		
		//확장자 정보 얻기
		String ext = getFileExtension(fileName);
		
		String uploadedFileName = null;
		if(getMediaType(ext) != null) { //만약에 이미지라면~
			uploadedFileName = makeCrop(uploadPath, newUploadPath, fileName);
		} else { //이미지가 아니라면~ 
			uploadedFileName = makeIcon(uploadPath, newUploadPath, fileName);
		}
		return uploadedFileName;
	}
	
	//8. 썸네일 이미지를 생성하는 메서드
	private static String makeThumbnail(String plainPath, String uploadDirPath, String fileName) throws IOException {
		
		//원본 이미지 읽어오기
		BufferedImage srcImg = ImageIO.read(new File(uploadDirPath, fileName));
		
		//# 썸네일 작업
		//1. 원본 이미지 리사이징하기
		BufferedImage destImg = Scalr.resize(srcImg, Scalr.Method.AUTOMATIC, 
						Scalr.Mode.FIT_TO_HEIGHT, 100);
		//2. 썸네일 이미지 경로 + 이름 생성
		String thumbnailName = uploadDirPath + File.separator + "s_" + fileName;
		
		//3. 썸네일 이미지파일 객체 생성
		File newFile = new File(thumbnailName);
		
		//4. 썸네일 이미지 생성 후 서버에 저장.
		ImageIO.write(destImg, getFileExtension(fileName), newFile);
		
		//썸네일 이미지파일 이름만 리턴하기
		// ~~~~/upload/2019/09/21/s_djfskfldjs_abc.jpg
		return thumbnailName.substring(plainPath.length()).replace(File.separatorChar, '/');
	}
	
	public static String makeCrop(String uploadRootPath, String dirname, String filename) throws IOException {
		BufferedImage srcImg = ImageIO.read(new File(dirname, filename));
		
		int w = srcImg.getWidth();  // 600
		int h = srcImg.getHeight(); // 400
		int min = Math.min(w, h);   // 400
		
		// (100, 0)에서 부터 가로 400, 세로 400 만큼 크롭하라!!
		//                                            100,          0,     ~400, ~400
		BufferedImage tmpImg = Scalr.crop(srcImg, (w - min)/2, (h - min)/2, min, min);
		
		BufferedImage destImg = Scalr.resize(tmpImg, Scalr.Method.AUTOMATIC,
				Scalr.Mode.FIT_TO_HEIGHT, 300);
		
		String ext = getFileExtension(filename);
		String cropName = dirname + File.separator + "c_" + filename;
		File newFile = new File(cropName);
		ImageIO.write(destImg, ext.toUpperCase(), newFile);
		
		return cropName.substring(uploadRootPath.length()).replace(File.separatorChar, '/');
	}
	
	//9. 아이콘 경로를 리턴하는 메서드
	private static String makeIcon(String plainPath, String uploadDirPath, String fileName) {
		String iconName = uploadDirPath + File.separator + fileName;
		return iconName.substring(plainPath.length()).replace(File.separatorChar, '/');
	}

	//5. 파일명에서 확장자 추출하는 메서드
	public static String getFileExtension(String fileName) {
		//ex) 324jdfkfjksdfsdjkflsdjlk_메롱.gif		
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

	//2. 날짜별 업로드 폴더 생성 후 그 새로운 저장경로를 리턴하는 메서드.
	//ex) uploadPath + /2019/09/21
	private static String getNewUploadPath(String uploadPath) {
		
		Calendar cal = Calendar.getInstance();
		int y = cal.get(Calendar.YEAR);
		int m = cal.get(Calendar.MONTH) + 1;
		int d = cal.get(Calendar.DATE);
		
		return makeDir(uploadPath, ""+y, len2(m), len2(d));
	}

	//4. 업로드 날짜별 폴더를 생성해주는 메서드.
	//uploadPath: ~~/workspace/~~~~/upload/
	//# 만약에 해당폴더가 존재하지 않는다면
	//a) upload폴더 아래에 2019폴더를 생성한다. ~~~/upload/2019
	//b) 2019폴더 아래에 09폴더를 생성한다. ~~~/upload/2019/09
	//c) 09폴더 아래에 21폴더를 생성한다. ~~~/upload/2019/09/21
	private static String makeDir(String uploadPath, String... dates) {
		
		for (String dateInfo : dates) {
			//File.separator : windows의 경우 \\, linux의 경우 /를 표현해줌.
			uploadPath += File.separator + dateInfo;
			File dirName = new File(uploadPath);
			if(!dirName.exists()) { //해당 디렉토리명이 존재하지 않는다면
				dirName.mkdir();//폴더를 만들어라!
			} else {
				continue;
			}
		}				
		return uploadPath;
	}
	
	//3. 월, 일을 항상 2자리로 표현해주는 메서드
	private static String len2(int n) {
		//숫자를 문자열형태로 포맷팅해주는 객체 DecimalFormat
		return new DecimalFormat("00").format(n).toString();
	}
	
	public static void main(String[] args) {
		//System.out.println(getNewUploadPath("D:\\spring_hsg\\workspace\\basic\\src\\main\\webapp\\resources\\upload"));
		System.out.println(getFileExtension("4509hfddds8334jdfdjf_메롱.hwp"));
	}

}








