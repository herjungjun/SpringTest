<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<script>
	const data = "${loginResult}";
	if(data === "idFail") {
		alert("존재하는 회원이 아닙니다. 회원가입을 먼저 진행해주세요!");
		history.back(); //뒤로가기 실행
	} else if(data === "pwFail") {
		alert("비밀번호가 틀렸습니다!");
		history.back();
	}
</script>

</body>
</html>







