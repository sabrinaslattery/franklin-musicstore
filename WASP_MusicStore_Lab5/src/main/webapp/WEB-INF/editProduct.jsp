<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Edit - Product Maintenance</title>
	<link rel="stylesheet" href="styles/main.css">
	<script type="text/javascript" src="jquery-jsl-3.5.1.js"></script>
</head>
<body>
	<h1>Product</h1>

	<div class="error">${error}</div>

	<form action="updateproduct" method="POST">
		
		<input type="hidden" name="isNew" value="${isNew}" />
		<c:if test="${!isNew}">
			<input type="hidden" name="productCode" value="${product.code}" />
		</c:if>
		
		<label for="productCode">Code:</label>
			<input type="text" name="productCode" size="10" value="${product.code}"
				${isNew ? '' : 'disabled'} /> <br /> 
		<label for="description">Description:</label>
			<input type="text" name="description" size="50" value="${product.description}" /> <br /> 
		<label for="price">Price:</label>
			<input type="text" name="price" size="10" value="${product.price}" /> <br />
		
		<input type="submit" value="Update Product" />
	</form>

	<form action="maintenance" method="GET">
		<input type="submit" value="View Products" />
	</form>

</body>
</html>