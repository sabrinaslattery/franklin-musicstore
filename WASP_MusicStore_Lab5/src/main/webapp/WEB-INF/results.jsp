<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Franklin Music - Search Results</title>
<link rel="stylesheet" href="styles/main.css">
<script type="text/javascript" src="jquery-jsl-3.5.1.js"></script>
</head>
<body>
	<h1>Search Results</h1>

	<table>
		<tr>
			<th style="width: 75px">Code</th>
			<th style="width: 500px">Description</th>
			<th style="width: 75px; text-align: right">Price</th>
			<th></th>
		</tr>

		<c:forEach items="${product}" var="prod">
			<tr>
				<td>${prod.code}</td>
				<td>${prod.description}</td>
				<td style="text-align: right">${prod.priceCurrencyFormat}</td>
				<td>
					<form action="addtocart" method="POST">
						<input type="hidden" name="productCode" value="${prod.code}"></input>
						<input type="submit" value="Add To Cart"></input>
					</form>
				</td>
			</tr>
		</c:forEach>

		<tr>
			<td colspan="3"></td>
			<td>
				<form action="viewcart" method="GET">
					<input type="submit" value="View Cart"></input>
				</form>
			</td>
		</tr>

	</table>
</body>
</html>