
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>

<!DOCTYPE html>
<html>
<head>
    <title>Affectation parcours/filière 3ème année Centrale Marseille</title>
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/bootstrap-responsive.min.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/js/jquery-final/jquery-1.8.3.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/highcharts/highcharts.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/highcharts/exporting.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/admin/run/main/statistics/choice/column.js" type="text/javascript"></script>

</head>
<body>
    <div class="container">
        <br />
        <div>
            <center><h2>Résumé des choix des élèves</h2></center>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger pull-right"><i class="icon-white icon-off"></i> Deconnexion</a>
        </div>
        <div class="span10 offset1">

            <br />
            <center>
                <ul class="nav nav-pills">

                    <c:forEach var="i" begin="1" end="5" step="1">
                        <c:choose>
                            <c:when test="${i == choiceNumber}">
                                <li class="active"><a href="${pageContext.request.contextPath}/responsable/resume/choice${i}">Choix ${i}</a></li>
                            </c:when>
                            <c:otherwise>
                                <li><a href="${pageContext.request.contextPath}/responsable/resume/choice${i}">Choix ${i}</a></li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                </ul>
            </center>
            <div id="containerColumnChartIc" style="min-width: 400px; height: 400px; margin: 0 auto"></div>
            <legend></legend>
            <div id="containerColumnChartJs" style="min-width: 400px; height: 400px; margin: 0 auto"></div>
        </div>



        <div id="inputsIc">
            <c:forEach var="improvementCourse" items="${simpleImprovementCourses}" varStatus="status">
                <input id="ic${status.index}" value="${improvementCourse.abbreviation};${improvementCourse.number};${improvementCourse.name}" style="display: none">
            </c:forEach>
        </div>

        <div id="inputsJs">
            <c:forEach var="jobSector" items="${simpleJobSectors}" varStatus="status">
                <input id="js${status.index}" value="${jobSector.abbreviation};${jobSector.number};${jobSector.name}" style="display: none">
            </c:forEach>
        </div>

    </div>


</body>
</html>
