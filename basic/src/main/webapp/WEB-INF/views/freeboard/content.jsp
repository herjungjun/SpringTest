<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <jsp:include page="../include/static-head.jsp" />
</head>
<body>
	<jsp:include page="../include/header.jsp" />

    <div class="virtual-box"></div>

    <div class="container">
        <div class="row">
            <div class="offset-md-1 col-md-10">
                <div class="card">
                    <div class="card-header" style="background: #343A40; color: white;">
                        <div class="row">
                            <div class="col-md-6">
                                <h2>${article.title}</h2>
                            </div>
                            <div class="offset-md-3 col-md-3">
                                <div>${article.writer}</div>
                                <div>
                                	<fmt:formatDate value="${article.regDate}" pattern="yyyy-MM-dd"/> 
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="card-body">
                        <div class="article-content">
                            <p>${article.content}</p>
                        </div>

                        <hr>
                        
                        <!-- 첨부파일 목록 -->
                        <div class="row">
                        	<ul class="uploadedList">
                           		<!-- 첨부파일 리스트들 배치 영역 -->
                           	</ul>
                        </div>
                        <hr>

                        <div class="row">
                            <div class="col-md-5">
                                <p class="last-update">
                                	최종 수정일: 
                                	<c:if test="${article.updateDate != null}">
                                		<fmt:formatDate value="${article.updateDate}" pattern="yyyy-MM-dd a hh:mm"/>
                                	</c:if>
                                	<c:if test="${article.updateDate == null}">
                                		수정되지 않은 게시글입니다.
                                	</c:if>
                                </p>
                            </div>
                            <div class="btn-group offset-md-2 col-md-5">
                                <a class="btn btn-outline-dark" href="/freeboard/all/${search.page}?keyword=${search.keyword}&condition=${search.condition}">목록</a>
                                
                                <c:if test="${loginUser.auth eq 'admin' || loginUser.account eq article.writer}">
	                                <a class="btn btn-outline-dark" href="#modifyModal" data-toggle="modal">수정</a>
	                                <a id="delBtn" class="btn btn-outline-dark" href="#">삭제</a>
                                </c:if>
                                
                                <a class="btn btn-dark" href="/freeboard">새 글 쓰기</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div> <!-- end content row -->
        
        <!-- 댓글 영역 -->
        <div id="replies" class="row">
        	<div class="offset-md-1 col-md-10">
        		<!-- 댓글 쓰기 영역 -->
                <div class="card">
                	<div class="card-body">
                        <div class="row">
                        
                        <c:if test="${not empty loginUser}">
                            <div class="col-md-9">
                                <div class="form-group">
                                    <label for="newReplyText" hidden>댓글 내용</label>
                                    <textarea rows="3" id="newReplyText" name="replyText" class="form-control"
                                        placeholder="댓글을 입력해주세요."></textarea>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label for="newReplyWriter" hidden>댓글 작성자</label>
                                    <input id="newReplyWriter" name="replyWriter" value="${loginUser.account}" type="text" class="form-control"
                                        placeholder="작성자 이름" style="margin-bottom: 6px;" readonly>
                                    <button id="replyAddBtn" type="button" class="btn btn-dark form-control">등록</button>
                                </div>
                            </div>
                        </c:if>
                        
                        <c:if test="${empty loginUser}">
                        	<p style="text-size: 22px; margin: 0 auto;">
                        		<a style="color: gray;" href="/login?check=${article.boardId}">댓글은 로그인 후 작성 가능합니다.</a>
                        	</p>
                        </c:if>
                            
                        </div>
                    </div>
                </div> <!-- end reply write -->
                
                <!--댓글 내용 영역-->
                <div class="card">
                	<!-- 댓글 내용 헤더 -->
                	<div class="card-header text-white m-0" style="background: #343A40;">
                        <div class="float-left">댓글 (<span id="replyCnt"></span>)</div>
                        <div class="float-right">
                            <a id="plusMinusBtn" class="text-white" data-toggle="collapse" href="#replyCollapse"
                                aria-expanded="false" aria-controls="collapseExample">+</a>
                        </div>
                    </div>
                    
                    <!-- 댓글 내용 바디 -->
                    <div id="replyCollapse" class="card-collapse collapse">
                    	<div id="replyData">
	                    	<!-- 
	                    		< JS로 댓글 정보 DIV삽입 > 
	                    	-->
                    	</div>
                    	
	                    <!-- 댓글 페이징 영역 -->
	                    <ul class="pagination justify-content-center">
	                    	<!-- 
	                    		< JS로 댓글 페이징 DIV삽입 > 
	                    	-->
	                    </ul>
                    </div>
                    
                </div> <!-- end reply content -->
                
        	</div>
        </div> <!-- end replies row -->
    </div>   <!-- end content container -->
    
    
    <!--게시글 수정창 모달-->
    <div class="modal fade bd-example-modal-lg" id="modifyModal">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">

                <!-- Modal Header -->
                <div class="modal-header" style="background: #343A40; color: white;">
                    <h4 class="modal-title">게시글 수정하기</h4>
                    <button type="button" class="close text-white" data-dismiss="modal">&times;</button>
                </div>

                <!-- Modal body -->
                <form id="modForm" action="/freeboard/modify/${article.boardId}" method="post">    
                	<div class="modal-body">
                      
                      <input name="boardId" type="hidden" value="${article.boardId}">
                      
                      <div class="form-group">
                          <label for="title" hidden>글 제목</label>
                          <input id="title" name="title" type="text" class="form-control" placeholder="글제목" value="${article.title}">
                      </div>

                      <div class="form-group">
                          <label for="content" hidden>글내용</label>
                          <textarea id="content" name="content" class="form-control" placeholder="게시글 내용을 입력하세요." rows="10">${article.content}</textarea>
                      </div>
                </div>

                <!-- Modal footer -->
                <div class="modal-footer">
                    <button id="modBtn" type="button" class="btn btn-dark">수정</button>
                    <button type="button" class="btn btn-danger" data-dismiss="modal">닫기</button>
                </div>
              </form>

            </div>
        </div>
    </div> <!-- end modifyModal -->
    
    <!-- 댓글 수정 모달 -->
    
    <div class="modal fade bd-example-modal-lg" id="replyModifyModal">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">

                <!-- Modal Header -->
                <div class="modal-header" style="background: #343A40; color: white;">
                    <h4 class="modal-title">댓글 수정하기</h4>
                    <button type="button" class="close text-white" data-dismiss="modal">&times;</button>
                </div>

                <!-- Modal body -->
                <div class="modal-body">
               	   <div class="form-group">
               	   	   <input id="modReplyId" type="hidden">
                       <label for="modReplyText" hidden>댓글내용</label>
                       <textarea id="modReplyText" class="form-control" placeholder="수정할 댓글 내용을 입력하세요." rows="3"></textarea>
                   </div>
                </div>

                <!-- Modal footer -->
                <div class="modal-footer">
                    <button id="replyModBtn" type="button" class="btn btn-dark">수정</button>
                    <button type="button" class="btn btn-danger" data-dismiss="modal">닫기</button>
                </div>
              

            </div>
        </div>
    </div>    
    <!-- end replyModifyModal -->
    
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
<!-- 게시글 처리 JS -->
<script>
$(function() {
	const message = "${message}";
	if(message === "modSuccess") {
		alert("게시글 수정이 완료되었습니다.");
	}
	
	//글수정 버튼 클릭 이벤트
	$("#modBtn").click(() => {
		if(confirm("게시글 수정을 완료하시겠습니까?")) {
			$("#modForm").submit();
		}
	});
	
	//글 삭제 비동기 처리
	$("#delBtn").click(function() {
		if(!confirm("게시물을 삭제하시겠습니까??")) {
			return;
		}
		const boardId = "${article.boardId}";
		
		$.getJSON("/freeboard/file/"+ boardId, (result) => {
			result.forEach(fileName => {
				deleteFile(fileName);
			});
		});
		
		$.ajax({
	        type : "DELETE",
	        url : "/freeboard/" + boardId,
	        headers : {
	            "Content-type" : "application/json",
	            "X-HTTP-Method-Override" : "DELETE"
	        },
	        dataType : "text",
	        success : function (result) {
	            if (result === "delSuccess") {
	                alert("게시글 삭제 완료!");
	                location.href="/freeboard/all/1";
	            }
	        }
	    });
	});
	
	//서버에있는 첨부파일 삭제처리 함수
	function deleteFile(fileName) {
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
	        }			
		});
	}
	
});//end JQuery
</script>

<!-- 댓글 처리 JS -->
<script>
$(function() {	
	const boardNo = "${article.boardId}"; //원본글 번호
	let page = 1;
	getReplies(page); //페이지 첫 진입 시 댓글 목록 불러오기
	
	//댓글창 컬랩스 이벤트(부트스트랩 기능)
	$('#replyCollapse').on('shown.bs.collapse', function () {
        //console.log('댓글 열림');
        $("#plusMinusBtn").text("-");
    });
    $('#replyCollapse').on('hidden.bs.collapse', function () {
        //console.log('댓글 닫힘');
        $("#plusMinusBtn").text("+");
    });
    
    //댓글 날짜 포맷팅처리 함수
    function formatDate(time) {
    	let dateObj = new Date(time); //밀리초를 날짜객체로 변환
    	//날짜객체에서 각 정보들 얻어오기
		let year = dateObj.getFullYear();
		let month = dateObj.getMonth() + 1;
		let date = dateObj.getDate();
		let hours = dateObj.getHours();
		let minutes = dateObj.getMinutes();		
		
		//오전 오후 시간체크
		let amPm;
		
		if(hours < 12 && hours >= 6) {
			amPm = "오전";
		} else if(hours >= 12 && hours < 21) {
			amPm = "오후";
			if(hours !== 12){
				hours -= 12;				
			}
		} else if(hours >= 21 && hours <= 24) {
			amPm = "밤";
			hours -= 12;
		} else {
			amPm = "새벽";
		}
		
		//2자리 숫자로 변환
		(month < 10) ? month = '0' + month : month;
		(date < 10) ? date = '0' + date : date;
		(hours < 10) ? hours = '0' + hours : hours;
		(minutes < 10) ? minutes = '0' + minutes : minutes;
		
		return year + "-" + month + "-" + date + " " + amPm + " " + hours + ":" + minutes;
    }
    
    //regTime = DB에 저장된 댓글등록시간(밀리초)
	function formatDate2(regTime) {
    	
    	//now = 현재날짜 객체
    	const now = new Date();
    	//oneDayTime = 하루의 초변환
    	const oneDayTime = 60 * 60 * 24;
    	//now.getTime() = 현재시간의 밀리초
    	//diff = 댓글 등록 경과시간의 초 표현
    	let diff = (now.getTime() - regTime) / 1000;
		
    	//day_diff = 댓글 등록 경과시간의 일표현
    	let day_diff = Math.floor(diff / oneDayTime); 
    	
    	console.log(diff);
    	console.log(day_diff);
    	console.log('=================');

        return day_diff == 0 && 
           (
	   	    diff < 60 && "방금전" ||
	   	    diff < 120 && "1분전" ||
	   	    diff < 3600 && Math.floor( diff / 60 ) + " 분전" ||
	   	    diff < 7200 && "1 시간전" ||
	   	    diff < 86400 && Math.floor( diff / 3600 ) + " 시간전"
	   	   ) 
	   	    
	   	    || day_diff == 1 && "어제" 
	   	    || day_diff < 7 && day_diff + " 일전" 
	   	    || day_diff < 31 && Math.floor( day_diff / 7 ) + " 주전" 
	   	    || day_diff < 365 && Math.floor( day_diff / 30 ) + " 개월 전" 
	   	    || day_diff >= 365 && (Math.floor( day_diff / 365 ) === 0 ? 1 : Math.floor( day_diff / 365 )) + " 년 전";

    }
	
	//댓글 목록 불러오기 비동기 통신처리
	function getReplies(pageNum) {
		//GET방식 비동기 통신 함수
		//$.getJSON(요청url, 콜백함수(통신 성공시 수행할 로직에 대한 함수));
		$.getJSON("/reply/"+boardNo+"/"+pageNum, (result) => {			
			
			let tag = "";
			
			result.replies.forEach((reply) => {
				tag += "<div id='replyContent' class='card-body' data-replyId='"+reply.replyId+"'>"
					+ "    <div class='row user-block'>"
					+ "       <span class='col-md-3'>"
					+ "         <b>"+ reply.replyWriter +"</b>"
					+ "       </span>"
					+ "       <span class='offset-md-6 col-md-3 text-right'>"
					+"           <b>"+ formatDate(reply.regDate) +"</b><br>" + formatDate2(reply.regDate) 
					+"        </span>"
					+ "    </div><br>"
					+ "    <div class='row'>"
					+ "       <div class='col-md-6'>"+ reply.replyText +"</div>"
					+ "       <div class='offset-md-2 col-md-4 text-right'>"
					+           makeModDelBtn(reply.replyWriter)
					+ "       </div>"
					+ "    </div><hr style='border-bottom: 1px solid gray;'>"
					+ " </div>";
			});			
			
			$("#replyData").html(tag);
			$("#replyCnt").html(result.replyCnt);
			
			printPageElement(result.pc);
			
		});
	}
	
	//댓글 수정삭제 버튼 인증처리
	function makeModDelBtn(replyWriter) {
		
		const auth = "${loginUser.auth}";
		const account = "${loginUser.account}";
		
		let tag;
		if(auth === "admin" || account === replyWriter) { //만약에 관리자이거나 자기 댓글이라면 2개의 버튼을 만든다.
			tag = "<a id='replyModBtn' class='btn btn-sm btn-outline-dark' href='#replyModifyModal' data-toggle='modal'>수정</a>&nbsp;"
				+ "<a id='replyDelBtn' class='btn btn-sm btn-outline-dark' href='#'>삭제</a>"
		} else { //아니면 빈문자열을 리턴한다.
			tag = "";
		}
		return tag;
	}
	
	//페이지 태그를 만들어주는 함수
	function printPageElement(pageInfo) {
		
		let tag = "";
		
		const begin = pageInfo.beginPage;
		const end = pageInfo.endPage;
		
		//이전 버튼 만들기
		if(pageInfo.prev) {
			tag += "<li class='page-item'><a class='page-link page-active' href='" + (begin-1) + "'>이전</a></li>";
		}
		
		//페이지 번호 리스트 만들기
		for(let i=begin; i<=end; i++) {
			const active = (pageInfo.paging.page === i) ? 'page-active' : '';
			tag += "<li class='page-item'><a class='page-link page-custom "+ active +"' href='" + i + "'>" + i + "</a></li>";
		}
		
		//다음 버튼 만들기
		if(pageInfo.next) {
			tag += "<li class='page-item'><a class='page-link page-active' href='" + (end+1) + "'>다음</a></li>";
		}
		
		//태그 삽입하기
		$(".pagination").html(tag);
	}
	
	//페이지 클릭 이벤트	
	$(".pagination").on("click", "li a", function(e) {
		e.preventDefault(); //a태그의 본래 기능을 차단함.
		page = $(this).attr("href");
		getReplies(page);
	});
	
	//댓글 등록 비동기 통신처리
	$("#replyAddBtn").click(() => {
		
		const replyData = {
				boardId : boardNo,
				replyText : $("#newReplyText").val(),
				replyWriter: $("#newReplyWriter").val()
		};
		
		//비동기 통신 방법(화면이 전환되지 않고 부분적 렌더링을 실행하기 위한 방법.)
		$.ajax({
			type: "POST", //통신 방법(GET, POST, PUT, DELETE)
			url: "/reply", //요청 URI
			headers: { //요청 헤더정보
				"Content-type" : "application/json",
				"X-HTTP-Method-Override": "POST" //PUT, DELETE를 지원하지 않는 브라우저에게 대체로 쓸 메서드를 지정
			},
			dataType: "text", //서버가 돌려준 데이터의 타입
			data: JSON.stringify(replyData), //서버로 전송할 데이터
			success: (result) => { //통신 성공시 실행할 로직을 함수로 작성
				//통신이 성공했을 때 서버가 리턴한 데이터가 result에 저장됨.
				console.log("통신 성공: " + result);
				if(result === "replyRegSuccess") {
					alert("댓글 등록 성공!");
					$("#newReplyText").val("");
					//$("#newReplyWriter").val("");
					$('#replyCollapse').show(); //댓글창 오픈
					getReplies(1);
				}
			},
			error: (e) => { //통신 실패시 실행할 로직을 함수로 작성
				console.log("통신 실패: " + e);
			}
		});
		
	});//end insert reply event
	
	//댓글 수정 모달버튼 클릭 이벤트
	$("#replyData").on("click", "#replyModBtn", function() {
		//console.log("댓글번호: " + $(this).parent().parent().parent().attr("data-replyId"));		
		//console.log("내용: " + $(this).parent().prev().text());
		const replyId = $(this).parent().parent().parent().attr("data-replyId");
		const replyText = $(this).parent().prev().text();
		
		$("#modReplyId").val(replyId);
		$("#modReplyText").val(replyText);
	});
	
	//댓글 수정 비동기 통신처리
	$("#replyModBtn").click(function() {
		
		const replyId = $("#modReplyId").val();
		//console.log("댓글번호: " + replyId);
		const newReplyText = $("#modReplyText").val();
		
		$.ajax({
			type: "PUT",
			url: "/reply/"+replyId,
			headers: {
				"Content-type" : "application/json",
				"X-HTTP-Method-Override": "PUT"
			},
			dataType: "text",
			data: JSON.stringify({replyText: newReplyText}),
			success: (result) => {
				if(result === "replyModSuccess") {
					alert("댓글 수정 완료!");
					//모달창을 닫아주는 함수
					$("#replyModifyModal").modal("hide");
					getReplies(page);
				}
			},
			error: () => {
				console.log("통신 실패!");
			}
		});
	});
	
	//댓글 삭제 비동기 통신처리
	$("#replyData").on("click", "#replyDelBtn", function(e) {
		e.preventDefault();
		const replyId = $(this).parent().parent().parent().attr("data-replyId");
		//console.log("삭제 버튼 클릭! : " + replyId);
		if(!confirm("진짜로 삭제할거니??")) {
			return;
		}			

	    $.ajax({
	        type : "DELETE",
	        url : "/reply/" + replyId + "?boardId=" + boardNo,
	        headers : {
	            "Content-type" : "application/json",
	            "X-HTTP-Method-Override" : "DELETE"
	        },
	        dataType : "text",
	        success : function(result) {
	            //console.log("result : " + result);
	            if (result === "replyDelSuccess") {
	                alert("댓글 삭제 완료!");
	                $('#replyCollapse').show();
	                getReplies(page);
	            }
	        },
	        error : function() {
	        	console.log("통신 실패!");
	        }
	    });

	});
	
	
	
}); //end JQuery

</script>

<!-- 첨부파일 목록 처리 JS -->
<script>
$(function() {
	
	//페이지 로딩시 해당 게시글에 맞는 첨부파일을 불러오는 로직
	const boardId = "${article.boardId}"; //현재 게시물 글 번호
	const uploadedList = $("ul.uploadedList");
	
	//첨부파일 이름목록 불러오는 비동기 통신
	$.getJSON("/freeboard/file/" + boardId, (result) => {
		
		result.forEach(fileName => {
			let fileInfo = getFileInfo(fileName);
	    	let tag = '<li id="'+fileInfo.fileId+'">'
	    			+ '  <input type="hidden" name="files" value="'+ fileInfo.fullName +'">'
	    			+ '  <a class="img-link" href="javascript:;">'
	    			+ '     <img class="has-img" src="' + fileInfo.imgsrc + '" alt="attachment">'
	    			+ '  </a>'	    			
	    			+ '  <div class="fileName">'
	    			+       fileInfo.originalfileName
	    			+ '  </div>'	    		
	    			+ '</li>';
	    	uploadedList.append(tag);
		});
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
	
}); //end JQuery
</script>

</body>
</html>











