<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">

<head>
  <jsp:include page="./include/static-head.jsp" />
</head>

<body>

  <jsp:include page="./include/header.jsp" />

  <!--background image-->
  <div class="container-fluid bg-main">
    <img src="http://img.chuing.net/i/QuyNVV/Preview.x.jpg" alt="">
  </div>

  <!-- Page Content -->
  <div class="container">
    <div class="row">
      <div class="col-lg-12 text-center">
        <h1 class="mt-5">A Bootstrap 4 Starter Template</h1>
        <p class="lead">Complete with pre-defined file paths and responsive navigation!</p>
        <ul class="list-unstyled">
          <li>Bootstrap 4.3.1</li>
          <li>jQuery 3.4.1</li>
        </ul>
      </div>
    </div>
  </div>

  <jsp:include page="./include/footer.jsp" />

  <jsp:include page="./include/plugin-js.jsp" />

</body>

</html>







