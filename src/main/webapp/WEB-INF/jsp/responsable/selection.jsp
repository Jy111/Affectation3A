<%--
  Created by IntelliJ IDEA.
  User: jean
  Date: 1/29/14
  Time: 4:24 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>


<!DOCTYPE html>
<html>
<head>
    <title>Selection Page</title>
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/bootstrap-responsive.min.css" rel="stylesheet">
</head>
<body>

    <div class="container">
        <tags:headerResponsible title="Selection"></tags:headerResponsible>
        <div class="span4 offset4">
            <center>
                <ul class="nav nav-pills nav-stacked span3">
                    <li>Choisissez votre parcours ou filière</li>
                    <c:forEach var="abbreviation" items="${allAbbreviations}" varStatus="status">
                        <p>
                            <li><a href ="${pageContext.request.contextPath}/responsable/${abbreviation}/1" >${abbreviation}</a>  </li>
                        </p>
                    </c:forEach>
                </ul>
            </center>
        </div>
    </div>

</body>
</html>
