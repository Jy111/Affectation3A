
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
    <link href="${pageContext.request.contextPath}/css/student-admin-page.css" rel="stylesheet">
</head>>
<body>
    <div class="container">

        <p>
            ${choiceIc.choice1}
        </p>
        <p>
            ${choiceIc.choice2}
        </p>
        <p>
            ${choiceIc.choice3}
        </p>
        <p>
            ${choiceIc.choice4}
        </p>
        <p>
            ${choiceIc.choice5}
        </p>
    </div>


</body>
</html>
