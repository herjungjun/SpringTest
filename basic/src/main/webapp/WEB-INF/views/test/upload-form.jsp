<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<%@ include file="../include/static-head.jsp" %>
</head>
<body>


<form action="/upload" method="post" enctype="multipart/form-data">
	<input type="file" name="file">
	<button type="submit">확인</button>
</form>

# Uploaded FileName: ${fileName}

<br>

<%-- AJAX비동기 통신을 통한 파일업로드 --%>
<div class="fileDrop" style="height:60px;">
	<span><i class="fa fa-hand-o-right" aria-hidden="true"></i></span>Drop Here!!
</div>

<div class="uploadedList">
	<%-- 
	<img src="/loadFile?fileName=/2019/09/21/s_102ba101-b22c-4d5e-a21d-50a634c1b85c_facebook.PNG" alt="">
	--%>
</div>

<form action="/uploadAjaxes" id="ajaxForm" method="POST" enctype="multipart/form-data">
	<input type="file" name="files" id="ajax-file" style="display: none;">
</form>
<div id="percent">0 %</div>
<div id="status">ready...</div>

<%@ include file="../include/plugin-js.jsp" %>

<script>
const dropBox = $("div.fileDrop");
const uploadedList = $("div.uploadedList");

//drag & drop 이벤트 함수
dropBox.on("dragover dragenter", (e) => {
	e.preventDefault();
	dropBox.css("border", "1px dotted gray");
});

dropBox.on("dragleave", (e) => {
	e.preventDefault();
	dropBox.css("border", "none");
});

dropBox.on("drop", (e) => {
	e.preventDefault();
	let files = e.originalEvent.dataTransfer.files;
	console.log("drop file data: ", files);
	dropBox.html(files[0].name);
	
	//파일이 드롭되었을 때 파일정보를 input[type="file"]에 저장
	//prop()은 객체를 넣을 때 사용합니다.
	$("#ajax-file").prop("files", files);
	//폼을 서버로 전송
	$("#ajaxForm").submit();
});

//파일업로드 비동기 전송처리
const percent = $("#percent");
const status = $("#status");

$("#ajaxForm").ajaxForm({
	//서버 전송 전에 해야할 처리들...
	beforeSend: function() {
		status.empty();
		percent.html("0%");
	},
	//업로드 진행상황 처리
	uploadProgress: function(event, position, total, percentComplete) {
		status.html("uploading....");
		percent.html(percentComplete + "%");
	},
	//파일 업로드 통신 성공시 해야할 로직
	complete: function(result) {	
		console.log("return text: ", result);
		
		//filePath : /2019/09/21/s_dsjfdsjlfksj_lalal.gif
		//const filePath = result.responseText;
		const filePathList = result.responseJSON;		
		
		filePathList.forEach( filePath => {
			//파일경로에서 원본파일이름만 추출
			//이미지라면?? 이미지링크로 가져오고
			//이미지가 아니라면 원본파일이름으로 가져와라~
			let originalFileName = getoriginalFileName(filePath);
			
			//다운로드 링크 생성
			let delFunc = "deleteFile('"+ filePath +"')";
			let ufLink = '<a href="/loadFile?fileName='+ filePath +'">'+ originalFileName +'</a>'   
						+'<a href="javascript:;" onclick="'+ delFunc +'">x</a>';
			status.html(ufLink + " Uploaded!!");
			//업로드리스트에 링크 지속적으로 추가
			uploadedList.append("<div>"+ ufLink +"</div>");
		});	
		
	}
});

//원본파일명 또는 이미지소스를 가져오는 함수
function getoriginalFileName(filePath) {
	//filePath : /2019/09/21/dsjfdsjlfksj_lalal.docx
	//str : lalal.docx
	let str = filePath.substring(filePath.indexOf("_") + 1);
	
	//파일이 이미지파일이라면 <img>를 리턴할 것이고,
	//아니면 단순히 잘라낸 파일명만 리턴할 것이다.
	if(isImageFile(str)) { //이미지인가?
		//filePath : /2019/09/21/s_dsjfdsjlfksj_dog.gif
		//str : dsjfdsjlfksj_dog.gif
		str = str.substring(str.indexOf("_") + 1);
		return '<img src="/loadFile?fileName='+ filePath +'" alt="' + str  + '">';
	} else { //아닌가?
		return '<img src="/vendor/images/file_icon.jpg" alt="파일 아이콘"><br>' + str;
	}
}

//이미지파일인지 여부 확인하는 함수
function isImageFile(fileName) {
	//정규표현식
	let pattern = /jpg$|gif$|png$/i;
	return fileName.match(pattern);
}

//파일 삭제 비동기요청을 보내는 함수
function deleteFile(fileName) {
	
	$.ajax({
		type: "DELETE",
		url: "/deleteFile?fileName=" + fileName,
		headers: {
			"Content-type" : "application/json",
            "X-HTTP-Method-Override" : "DELETE"
		},
		dataType: "text",
		success: function(result) {
			console.log("result: ", result);
			if(result === "fileDelSuccess") {
				alert("첨부파일 삭제 완료!");
				const fileLink = $("div.uploadedList a[href='/loadFile?fileName="+ fileName +"']");
				fileLink.parent().remove();
			}
		}
	});
	
}

</script>
</body>
</html>









