<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
<jsp:include page="../include/static-head.jsp" />
</head>
<body class="hold-transition skin-blue sidebar-mini layout-boxed">

<div class="wrapper">
    

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                AJAX 댓글 테스트 페이지
            </h1>
            <ol class="breadcrumb">
                <li><i class="fa fa-edit"></i> reply test</li>
            </ol>
        </section>

        <!-- Main content -->
        <!-- 댓글 입력창 -->
        <section class="content container-fluid">

            <div class="col-lg-12">
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">댓글 작성</h3>
                    </div>
                    <div class="box-body">
                        <div class="form-group">
                            <label for="newReplyText">댓글 내용</label>
                            <input class="form-control" id="newReplyText" name="replyText" placeholder="댓글 내용을 입력해주세요">
                        </div>
                        <div class="form-group">
                            <label for="newReplyWriter">댓글 작성자</label>
                            <input class="form-control" id="newReplyWriter" name="replyWriter" placeholder="댓글 작성자를 입력해주세요">
                        </div>
                        <div class="pull-right">
                            <button type="button" id="replyAddBtn" class="btn btn-primary"><i class="fa fa-save"></i> 댓글 저장</button>
                        </div>
                    </div>
                    
                    <!-- 댓글 목록이 배치될 박스 -->
                    <div class="box-footer">
                        <ul id="replies">
							
                        </ul>
                    </div>
                    
                    <!-- 댓글 페이지 화면이 들어올 박스 -->
                    <div class="box-footer">
                        <div class="text-center">
                            <ul class="pagination pagination-sm no-margin">

                            </ul>
                        </div>
                    </div>
                </div>
            </div>
			
			<!-- 댓글 수정 모달 화면 창 -->
            <div class="modal fade" id="modifyModal" role="dialog">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">댓글 수정창</h4>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <label for="replyNo">댓글 번호</label>
                                <input class="form-control" id="replyNo" name="replyNo" readonly>
                            </div>
                            <div class="form-group">
                                <label for="replyText">댓글 내용</label>
                                <input class="form-control" id="replyText" name="replyText" placeholder="댓글 내용을 입력해주세요">
                            </div>
                            <div class="form-group">
                                <label for="replyWriter">댓글 작성자</label>
                                <input class="form-control" id="replyWriter" name="replyWriter" readonly>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default pull-left" data-dismiss="modal">닫기</button>
                            <button type="button" class="btn btn-success modalModBtn">수정</button>
                            <button type="button" class="btn btn-danger modalDelBtn">삭제</button>
                        </div>
                    </div>
                </div>
            </div>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
	
	<jsp:include page="../include/plugin-js.jsp" />
</div>

<script>
$(function() {
	
	const boardNo = 340; //원본글 번호
	let page = 1;
	getReplies(page); //페이지 첫 진입 시 댓글 목록 불러오기
	
	//댓글 목록 불러오기 비동기 통신처리
	function getReplies(pageNum) {
		//GET방식 비동기 통신 함수
		//$.getJSON(요청url, 콜백함수(통신 성공시 수행할 로직에 대한 함수));
		$.getJSON("/reply/"+boardNo+"/"+pageNum, (result) => {
			//console.log("1번째 댓글의 작성자: " + result[0].replyWriter);
			//console.log("3번째 댓글의 내용: " + result[2].replyText);
			
			let tag = "";
			
			result.replies.forEach((reply) => {
				tag += "<li class='replyLi' data-replyId='"+reply.replyId+"'>"
						+ "<p class='replyText'>" + reply.replyText + "</p>"
						+ "<p class='replyWriter'>" + reply.replyWriter + "</p>"
						+ "<button type='button' class='btn btn-warning' data-toggle='modal' data-target='#modifyModal'>댓글 수정</button>"
					+ "</li><hr>";
			});
			/*$(result).each(function() {
				tag += "<li class='replyLi' data-replyId='"+this.replyId+"'>"
						+ "<p class='replyText'>" + this.replyText + "</p>"
						+ "<p class='replyWriter'>" + this.replyWriter + "</p>"
						+ "<button type='button' class='btn btn-warning' data-toggle='modal' data-target='#modifyModal'>댓글 수정</button>"
					+ "</li><hr>";
			}); *///JQuery 배열 반복문
			
			$("#replies").html(tag);
			
			printPageElement(result.pc);
			
		});
	}
	
	//페이지 태그를 만들어주는 함수
	function printPageElement(pageInfo) {
		
		let tag = "";
		
		const begin = pageInfo.beginPage;
		const end = pageInfo.endPage;
		
		//이전 버튼 만들기
		if(pageInfo.prev) {
			tag += "<li><a href='" + (begin-1) + "'>이전</a></li>";
		}
		
		//페이지 번호 리스트 만들기
		for(let i=begin; i<=end; i++) {
			tag += "<li><a href='" + i + "'>" + i + "</a></li>";
		}
		
		//다음 버튼 만들기
		if(pageInfo.next) {
			tag += "<li><a href='" + (end+1) + "'>다음</a></li>";
		}
		
		//태그 삽입하기
		$(".pagination").html(tag);
	}
	
	//페이지 클릭 이벤트
	/*
		# 클래스이름이 pagination인 ul태그는 실존하는 요소이지만
		 ul태그의 자식요소들인 li요소와 a요소는 자바스크립트 구문으로 만든
		 가상요소입니다. 따라서 직접적인 클릭이벤트처리를 할 수 없습니다.
	 	# 그래서 실존요소인 ul태그에 이벤트 처리를 걸고 on함수의 2번째 매개값으로
	 	실제로 이벤트 처리할 가상 요소를 적습니다.
	*/
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
					$("#newReplyWriter").val("");
					getReplies(1);
				}
			},
			error: (e) => { //통신 실패시 실행할 로직을 함수로 작성
				console.log("통신 실패: " + e);
			}
		});
		
	});//end insert reply event
	
	//댓글 수정 모달버튼 클릭 이벤트
	$("#replies").on("click", "li button", function() {
		//console.log("수정 모달버튼 클릭됨!");
		console.log("댓글번호: " + $(this).parent().attr("data-replyId"));
		console.log("작성자: " + $(this).parent().find(".replyWriter").text());
		console.log("내용: " + $(this).parent().find(".replyText").text());
		
		const replyId = $(this).parent().attr("data-replyId");
		const replyWriter = $(this).parent().find(".replyWriter").text();
		const replyText = $(this).parent().find(".replyText").text();
		
		$("#replyNo").val(replyId);
		$("#replyWriter").val(replyWriter);
		$("#replyText").val(replyText);
	});
	
	//댓글 수정 비동기 통신처리
	$(".modalModBtn").click(function() {
		
		const replyId = $(this).parent().parent().find("#replyNo").val();
		//console.log("댓글번호: " + replyId);
		const newReplyText = $(this).parent().parent().find("#replyText").val();
		
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
					$("#modifyModal").modal("hide");
					getReplies(page);
				}
			},
			error: () => {
				console.log("통신 실패!");
			}
		});
	});
	
	//댓글 삭제 비동기 통신처리
	$(".modalDelBtn").on("click", function() {
		if(!confirm("진짜로 삭제할거니??")) {
			return;
		}			
	    const replyNo = $("#replyNo").val();

	    $.ajax({
	        type : "DELETE",
	        url : "/reply/" + replyNo + "?boardId=" + boardNo,
	        headers : {
	            "Content-type" : "application/json",
	            "X-HTTP-Method-Override" : "DELETE"
	        },
	        dataType : "text",
	        success : function(result) {
	            console.log("result : " + result);
	            if (result === "replyDelSuccess") {
	                alert("댓글 삭제 완료!");
	                $("#modifyModal").modal("hide");
	                getReplies(page);
	            }
	        }
	    });

	});
	
	
	
}); //end JQuery

</script>

</body>
</html>






