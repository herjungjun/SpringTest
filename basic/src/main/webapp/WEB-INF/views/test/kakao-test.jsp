<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<c:if test="${empty loginUser}">
<a href="https://kauth.kakao.com/oauth/authorize?client_id=${app_key}&redirect_uri=http://localhost${redirect_uri}&response_type=code">
	<img src="//mud-kage.kakao.com/14/dn/btqbjxsO6vP/KPiGpdnsubSq3a0PHEGUK1/o.jpg" alt="카카오 로그인">
</a>
</c:if>

<c:if test="${not empty loginUser}">
	<h1>${loginUser.nickname}님 카카오톡 로그인 성공!</h1>
	<img src="${loginUser.profileImagePath}" alt="프로필사진">
	<br>
	<p>
		# 이메일: ${loginUser.email} <br>
		# 성별: ${loginUser.gender}
	</p>
	
	<a href="/kakao/logout">카카오 로그아웃하기</a>
</c:if>

</body>
</html>










