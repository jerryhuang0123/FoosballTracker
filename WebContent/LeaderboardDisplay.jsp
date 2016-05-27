<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>LeaderBoard</title>
</head>
<body>
<table border="1">
<tr>
<th>Rank</th>
<th>ID</th>
<th>First Name</th>
<th>Last Name</th>
<th>Win Percentage </th>
<th>Total Wins </th>
<th>Total Loss </th>
<th>Point Differential</th>
<th>Total Points</th>
<th>Opponent Total Points</th>
</tr>
<c:set var="rank" value="1" scope="page"/>
<c:forEach var="player" items="${requestScope.leaderboardList }">
<tr>
<td><c:out value="${rank}"/></td>
<c:set var="rank" value="${rank + 1}" scope="page"/>
<td><c:out value="${player.playerID }"/></td>
<td><c:out value="${player.firstName }"/></td>
<td><c:out value="${player.lastName }"/></td>
<td><c:out value="${player.winPercentage }"/></td>
<td><c:out value="${player.winTotal }"/></td>
<td><c:out value="${player.lossTotal }"/></td>
<td><c:out value="${player.pointDifference }"/></td>
<td><c:out value="${player.pointTotal }"/></td>
<td><c:out value="${player.givenUpPointTotal }"/></td>
</tr>
</c:forEach>
</table>
<br><br>

<a href="/JSPExample"><h2>Go Back to Home Page</h2></a>
</body>
</html>