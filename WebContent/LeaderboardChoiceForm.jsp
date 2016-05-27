<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Leaderboard Choice Selection</title>
</head>
<h1>Sort Leaderboard By:</h1><br><br>
<body>
<form name = "LeaderboardChoiceForm" method="post" action="LeaderboardDisplay">
	<input type="radio" name="LeaderboardChoice" value="WinPercentage"> Win Percentage <br>
	<input type="radio" name="LeaderboardChoice" value="WinTotal"> Win Total <br>
	<input type="radio" name="LeaderboardChoice" value="PointDifference"> Point Difference <br>
	<input type="submit" value="submit"><br>
</form>
</body>
</html>