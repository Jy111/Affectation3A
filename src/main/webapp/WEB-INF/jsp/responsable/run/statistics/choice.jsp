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
<script src="${pageContext.request.contextPath}/js/admin/run/main/statistics/choice/pie-chart.js" type="text/javascript"></script>
</head>
<body>
	<div class="container">

		<tags:headerResponsible title="${specialization.name} (${specialization.abbreviation})" />

		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">

					<li class="nav-header">${specialization.abbreviation}</li>
					<c:forEach var="i" begin="1" end="5" step="1">
						<li><a href="${pageContext.request.contextPath}/responsable/${i}">Choix ${i}</a></li>
					</c:forEach>

					<li class="nav-header">Statistiques</li>
					<li class="active"><a href="${pageContext.request.contextPath}/responsable/run/statistics/choice1">Parcours/filières</a></li>
					<c:choose>
						<c:when test="${specialization.type == specialization.JOB_SECTOR }">
							<li><a href="${pageContext.request.contextPath}/responsable/run/statistics/repartition-other-choice2">Répartition filières</a></li>
							<li><a href="${pageContext.request.contextPath}/responsable/run/statistics/inverse-repartition">Répartition parcours</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="${pageContext.request.contextPath}/responsable/run/statistics/repartition-other-choice2">Répartition parcours</a></li>
							<li><a href="${pageContext.request.contextPath}/responsable/run/statistics/inverse-repartition">Répartition filières</a></li>
						</c:otherwise>
					</c:choose>
				</ul>
			</div>

			<div class="span7">
				<ul class="nav nav-pills">
					<c:forEach var="i" begin="1" end="5" step="1">
						<c:choose>
							<c:when test="${i == choiceNumber}">
								<li class="active"><a href="${pageContext.request.contextPath}/responsable/run/statistics/choice${i}">Choix ${i}</a></li>
							</c:when>
							<c:otherwise>
								<li><a href="${pageContext.request.contextPath}/responsable/run/statistics/choice${i}">Choix ${i}</a></li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</ul>

				<div id="containerPieChartIc" style="min-width: 400px; height: 400px; margin: 0 auto"></div>

				<div id="containerPieChartJs" style="min-width: 400px; height: 400px; margin: 0 auto"></div>

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
			
			<div class="span3">
				<tags:rightColumnResponsible specialization="${specialization}"></tags:rightColumnResponsible>
			</div>
		</div>
	</div>
</body>
</html>