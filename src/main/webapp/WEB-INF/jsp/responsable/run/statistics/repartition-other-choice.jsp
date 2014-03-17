<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>

<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filière 3ème année Centrale
	Marseille</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/bootstrap-responsive.min.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/js/jquery-final/jquery-1.8.3.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/highcharts/highcharts.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/highcharts/exporting.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/responsible/repartition-other-choice.js" type="text/javascript"></script>
</head>
<body>
	<div class="container">

		<tags:headerResponsible
			title="${specialization.name} (${specialization.abbreviation})" />

		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">

					<li class="nav-header">${specialization.abbreviation}</li>
					<c:forEach var="i" begin="1" end="5" step="1">
						<li><a href="${pageContext.request.contextPath}/responsable/${specialization.abbreviation}/${i}">Choix ${i}</a></li>
					</c:forEach>

					<li class="nav-header">Statistiques</li>
					<li><a href="${pageContext.request.contextPath}/responsable/run/statistics/${specialization.abbreviation}/choice1">Parcours/filière</a></li>
					<c:choose>
						<c:when test="${specialization.type == specialization.JOB_SECTOR }">
							<li class="active"><a
								href="${pageContext.request.contextPath}/responsable/run/statistics/${specialization.abbreviation}/repartition-other-choice2">Répartition
									filières</a></li>
							<li><a
								href="${pageContext.request.contextPath}/responsable/run/statistics/${specialization.abbreviation}/inverse-repartition">Répartition
									parcours</a></li>
						</c:when>
						<c:otherwise>
							<li class="active"><a
								href="${pageContext.request.contextPath}/responsable/run/statistics/${specialization.abbreviation}/repartition-other-choice2">Répartition
									parcours</a></li>
							<li><a
								href="${pageContext.request.contextPath}/responsable/run/statistics/${specialization.abbreviation}/inverse-repartition">Répartition
									filières</a></li>
						</c:otherwise>
					</c:choose>
                    <li class="nav-header">Parcours associés</li>
                    <c:forEach var="abb" items="${abbFromSuper}">
                        <li><a href = "${pageContext.request.contextPath}/responsable/${abb}/1">${abb}</a></li>
                    </c:forEach>
				</ul>
			</div>

			<div class="span7">
				<ul class="nav nav-pills">
					<c:forEach var="i" begin="2" end="5" step="1">
						<c:choose>
							<c:when test="${i == choiceNumber}">
								<li class="active"><a
									href="${pageContext.request.contextPath}/responsable/run/statistics/${specialization.abbreviation}/repartition-other-choice${i}">Choix
										${i}</a></li>
							</c:when>
							<c:otherwise>
								<li><a
									href="${pageContext.request.contextPath}/responsable/run/statistics/${specialization.abbreviation}/repartition-other-choice${i}">Choix
										${i}</a></li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</ul>

				<div id="container"
					style="min-width: 400px; height: 400px; margin: 0 auto"></div>

				<div id="results">
					<c:forEach var="spec" items="${specializations}" varStatus="status">
						<div>
							<input value="${spec.name};${spec.abbreviation}"
								style="display: none">
							<c:forEach var="student" items="${spec.students}">
								<input value="${student}" style="display: none">
							</c:forEach>
						</div>
					</c:forEach>
				</div>
				<input id="spec" value="${specialization.abbreviation}"
					style="display: none"> <input id="number"
					value="${choiceNumber}" style="display: none">

			</div>
			
			<div class="span3">
				<tags:rightColumnResponsible specialization="${specialization}"></tags:rightColumnResponsible>
			</div>
		</div>
	</div>
</body>
</html>