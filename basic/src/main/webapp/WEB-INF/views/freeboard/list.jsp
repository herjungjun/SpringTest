<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../include/static-head.jsp" />
<style>
	.article-link {
		font-weight: 700;
		color: brown;
	}
</style>
</head>
<body>

<jsp:include page="../include/header.jsp" />

	<div class="virtual-box"></div>

 	<div class="container">
        <div class="row">
            <h2 class="page-header"><span class="title">Spring</span> 자유 게시판</h2>

            <table class="table table-secondary table-hover table-border">
                <thead class="thead-dark">
                    <tr>
                        <th># 번호</th>
                        <th>작성자</th>
                        <th>제목</th>
                        <th>작성일</th>
                        <th>조회수</th>
                    </tr>
                </thead>
				
				<c:forEach var="b" items="${articles}" >
	                <!--게시물들이 들어갈 공간-->
	                <tr>
	                    <td>${b.boardId}</td>
	                    <td>${b.writer}</td>
	                    
	                    <td>
	                    	<a class="article-link" href="/freeboard/${b.boardId}?page=${pc.paging.page}&keyword=${search.keyword}&condition=${search.condition}">
	                    		${b.title}&nbsp;[${b.replyCnt}]
	                    	</a>	                    	
	                    </td>
	                    
	                    <td>
	                    	<fmt:formatDate value="${b.regDate}" pattern="yyyy년 MM월 dd일 a hh:mm" />	                    
	                    </td>
	                    
	                    <td>${b.viewCnt}</td>
	                </tr>
                </c:forEach>
            </table>
        </div>
        <!-- 글목록 부분 /end row -->

        <!--페이징 처리 부분-->
        <ul class="pagination justify-content-center">
			<c:if test="${pc.prev}">
            	<li class="page-item">
            		<a id="page-prev" class="page-link page-custom" href="/freeboard/all/${pc.beginPage-1}?keyword=${search.keyword}&condition=${search.condition}">이전</a>
           		</li>
			</c:if>
			
			<c:forEach var="pageNum" begin="${pc.beginPage}" end="${pc.endPage}">
            	<li class="page-item">
            		<a class="page-link page-custom ${(pageNum == pc.paging.page) ? 'page-active' : ''}" href="/freeboard/all/${pageNum}?keyword=${search.keyword}&condition=${search.condition}">${pageNum}</a>
            	</li>
            </c:forEach>
            
			<c:if test="${pc.next}">
            	<li class="page-item">
            		<a id="page-next" class="page-link page-custom" href="/freeboard/all/${pc.endPage+1}?keyword=${search.keyword}&condition=${search.condition}">다음</a>
            	</li>
        	</c:if>
        </ul>

        <!--검색 창 영역-->
        <div class="row">
            <div class="col-md-3">
                <div class="form-group">
                    <select class="form-control" name="condition" id="condition">
                        <option value="title" ${param.condition == 'title' ? 'selected' : ''}>제목</option>
                        <option value="content" ${param.condition == 'content' ? 'selected' : ''}>내용</option>
                        <option value="writer" ${param.condition == 'writer' ? 'selected' : ''}>작성자</option>
                        <option value="titleContent" ${param.condition == 'titleContent' ? 'selected' : ''}>제목+내용</option>
                    </select>
                </div>
            </div>
            <div class="col-md-6">
                <div class="input-group">
                    <input id="keywordInput" class="form-control" name="keyword" type="text" placeholder="검색어 입력" value="${param.keyword}">
                    <span class="input-group-btn">
                        <button id="searchBtn" class="btn btn-dark">검색</button>
                    </span>
                </div>
            </div>
            <div class="offset-md-1 col-md-2">
                <a class="btn btn-secondary" href="/freeboard">글쓰기</a>
            </div>
        </div>
    </div>
    <!-- /end container -->


<jsp:include page="../include/footer.jsp" />
<jsp:include page="../include/plugin-js.jsp" />

<script>
	$(function() {
		
		//게시글 처리 알림창 띄우기
		const result = "${message}";
		if(result === 'regSuccess') {
			alert("게시글 등록이 완료되었습니다.");
		} else if(result === 'delSuccess') {
			alert("게시글 삭제가 완료되었습니다.");
		}
		
		//검색창 엔터키 이벤트
		$("#keywordInput").keydown((key) => {
			if(key.keyCode === 13) {
				$("#searchBtn").click();
			}
		});
		
		//검색 버튼 클릭 이벤트
		$("#searchBtn").click(() => {
			//console.log("검색 버튼 클릭함~~");
			const keyword = $("#keywordInput").val();
			//console.log("검색어: " + keyword);
			const condition = $("#condition option:selected").val();
			//console.log("검색조건: " + condition);
			location.href = "/freeboard/all/1?keyword="+ keyword					
						+"&condition=" + condition; //링크요청을 보냄.
		});
	});
</script>
</body>
</html>








