<%@ taglib prefix="InsertNewPlayerResult" uri="/WEB-INF/CustomTagHandler.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Result!</title>
</head>
<body>
<h1>Result</h1>
<InsertNewPlayerResult:InsertNewPlayerResultForm newPlayerFirstName="${param.first_name}"
newPlayerLastName ="${param.last_name}"/>
</body>
</html>