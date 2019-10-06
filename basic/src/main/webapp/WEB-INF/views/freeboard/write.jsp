<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<jsp:include page="../include/static-head.jsp" />
<style>
.virtual-box {
	margin-bottom: 120px;
}
</style>
</head>
<body>

<jsp:include page="../include/header.jsp" />
   
   <div class="virtual-box"></div>
   
   <div class="container">
       <div class="row">
           <div class="offset-md-1 col-md-10">
               <div class="card">
                   <div class="card-header text-white" style="background: #343A40;">
                       <h2>게시글 등록</h2>
                   </div>
                   <div class="card-body">
                       <form id="regForm" action="/freeboard" method="post">
                           <div class="form-group">
                               <label for="writer" hidden>작성자</label>
                               <input id="writer" name="writer" value="${loginUser.account}" 
                               type="text" class="form-control" placeholder="작성자" readonly>
                           </div>
   
                           <div class="form-group">
                               <label for="title" hidden>작성자</label>
                               <input id="title" name="title" type="text" class="form-control" placeholder="글제목">
                           </div>
   
                           <div class="form-group">
                               <label for="content" hidden>글내용</label>
                               <textarea id="content" name="content" class="form-control" placeholder="게시글 내용을 입력하세요." rows="5"></textarea>
                           </div>
                           
                           <!-- 첨부파일 드래그앤 드롭 영역 -->
                           <div class="form-group">
                               <label for="">File Drop Here!!</label>
                               <div class="fileDrop"></div>
                           </div>
                           
                           <!-- 첨부파일 목록 영역 -->
                           <div class="box-footer" style="margin-bottom: 20px;">
                           	<ul class="uploadedList">
                           		<!-- 리스트들 배치 영역 -->
                           	</ul>
                           </div>
   						
   						<div>
                            <button id="regBtn" type="button" class="btn btn-dark form-control">등록</button>
                            <button type="button" class="btn btn-danger form-control">취소</button>
                       	</div>
                       </form>
                       <form action="/uploadAjaxes" id="form-attach" method="POST" enctype="multipart/form-data">
				        <input type="file" name="files" id="ajax-file" style="display: none;">
				       </form>
                   </div>
               </div>
           </div>
       </div>
   </div>
   
   <!-- 원본이미지 모달 -->
	<div id="original-image-modal" class="modal fade">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <img src="" alt="" />
	    </div>
	  </div>
	</div>

<jsp:include page="../include/footer.jsp" />
<jsp:include page="../include/plugin-js.jsp" />

<script>
	$(function() {
		//글쓰기 버튼 클릭 이벤트
		$("#regBtn").click(() => {
			let chk = confirm("게시글 등록을 완료하시겠습니까?");
			if(chk) {
				$("#regForm").submit();
			}
		});
		
		//////////////////////////////////////////////////////////////////		
		///////////////////////  파일첨부 처리  ///////////////////////////////
		
		const fileDrop = $("div.fileDrop");

		fileDrop.on("dragover dragenter", (e) => {
			e.preventDefault();
			fileDrop.css("background", "lightgrey");
		});
		fileDrop.on("dragleave", (e) => {
			e.preventDefault();
			fileDrop.css("background", "none");
		});
		fileDrop.on("drop", (e) => {
			e.preventDefault();
			let files = e.originalEvent.dataTransfer.files;
			console.log("drop>>", files);
			fileDrop.css("background", "none");
			
			$("#ajax-file").prop("files", e.originalEvent.dataTransfer.files);
			$('#form-attach').submit();
		});
		
		const uploadedList = $("ul.uploadedList");
		$('#form-attach').ajaxForm({
		    
		    complete: function(result) {
		    	let fileNameList = result.responseJSON;
		    	
		    	fileNameList.forEach(fileName => {
		    		let fileInfo = getFileInfo(fileName);
			    	let tag = '<li id="'+fileInfo.fileId+'">'
			    			+ '  <input type="hidden" name="files" value="'+ fileInfo.fullName +'">'
			    			+ '  <a class="img-link" href="javascript:;">'
			    			+ '     <img class="has-img" src="' + fileInfo.imgsrc + '" alt="attachment">'
			    			+ '  </a>'
			    			+ '  <a class="btn btn-sm btn-danger delBtn" href="javascript:;">'
			    			+ '    <i class="fa fa-trash" aria-hidden="true"></i>'
			    			+ '  </a>'
			    			+ '  <div class="fileName">'
			    			+       fileInfo.originalfileName
			    			+ '  </div>'	    		
			    			+ '</li>';
			    	uploadedList.append(tag);
		    	}); 
		    }
		});		

		//fullName: /2019/09/22/s_fdjksfjdsjlfs_abcd.png
		//originalfileName: abcd.png
		//imgsrc: 이미지로딩을 위한 요청URI를 만듦.
		//getLink: 다운로드를 위한 URI를 만듦.
		//uploadedFileName: s_fdjksfjdsjlfs_abcd.png
		function getFileInfo(fullName) {
			
			let originalfileName, imgsrc, getLink, uploadedFileName;
			
			if(checkImageType(fullName)) {
				imgsrc = "/loadFile?fileName=" + fullName;
				uploadedFileName = fullName.substring(14); //why?) /2019/09/22/s_ -> 14글자
				
				let front = fullName.substring(0, 12); // /2019/09/22/만 추출
				let end = uploadedFileName;
				
				getLink = "/loadFile?fileName=" + front + end; //원본이미지 경로(썸네일 X)
			} else {
				imgsrc = "/vendor/images/file_icon.jpg";
				uploadedFileName = fullName.substring(12); //why?) /2019/09/22/ -> 12글자
				getLink = "/loadFile?fileName=" + fullName;
			}
			
			//uploadedFileName: fdjksfjdsjlfs_abcd.png
			originalfileName = uploadedFileName.substring(uploadedFileName.indexOf("_") + 1);
			fileId = uploadedFileName.substring(0, uploadedFileName.indexOf("_"));
			
			return {
				fileId : fileId,
				originalfileName : originalfileName,
				imgsrc : imgsrc,
				getLink : getLink,
				fullName : fullName
			}
		}

		function checkImageType(fileName) {
			let pattern = /jpg$|png$|gif$/i;
			return fileName.match(pattern);
		}
		
		//삭제버튼 이벤트
		$(uploadedList).on("click", "a.delBtn", function(e) {
			e.preventDefault();
			const liTag = $(this).parent();
			const fileName = liTag.find("input[name=files]").val();
			console.log("삭제버튼 클릭됨!! : ", fileName);
			//let fileInfo = getFileInfo(fileName);			
			$.ajax({
				type : "DELETE",
		        url : "/deleteFile?fileName=" + fileName,
		        headers : {
		            "Content-type" : "application/json",
		            "X-HTTP-Method-Override" : "DELETE"
		        },
		        dataType : "text",
		        success : function(result) {
		            console.log("result : " + result);
		            if (result === "fileDelSuccess") {
		                alert("파일 삭제 완료!");
		                liTag.remove();
		            }
		        }
				
			});
		});
		
		//이미지링크 클릭이벤트
		$(uploadedList).on("click", "a.img-link", function(e) {
			e.preventDefault();
			const liTag = $(this).parent();
			const fileName = liTag.find("input[name=files]").val();
			//원본 이미지 경로
			const originalImageLink = getFileInfo(fileName).getLink;
			let originalImageModal = $('#original-image-modal');
			   
		    if (checkImageType(originalImageLink)) {
			    originalImageModal.find('img').attr('src', originalImageLink);
			    originalImageModal.modal('show');
			    
		    } else {
			   window.location.href = originalImageLink;
		    }
		});
		
	});//end jQuery
</script>

</body>
</html>







