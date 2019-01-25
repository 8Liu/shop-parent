<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<form action="/demo/addOrderPage" method="post">
		<input type="hidden" name="token" value="${token}"> <span>订单名称</span><input
			type="text" name="orderName"><br> <span>订单描述</span><input
			type="text" name="orderDes"><br> <input type="submit">
	</form>
</body>
</html>