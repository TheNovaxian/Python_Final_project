<jsp:useBean id="order" scope="request" type="com.eventmanager.inventorymanagement.model.Order"/>
<%--
  Created by IntelliJ IDEA.
  User: Shaquille
  Date: 4/29/2025
  Time: 10:18 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Order Details</title>
    <link href='https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css' rel='stylesheet'>
</head>
<body class='container'>
<h1>Order #${order.orderId}</h1>
<p><strong>Customer ID:</strong> ${order.customerId}</p>
<p><strong>Shipping Address:</strong> ${order.shippingAddress}</p>
<p><strong>Shipping Method:</strong> ${order.shippingMethod}</p>
<p><strong>Payment Method:</strong> ${order.paymentMethod}</p>
<p><strong>Order Status:</strong> ${order.orderStatus}</p>
<p><strong>Order Date:</strong> ${order.orderDate}</p>
<p><strong>Total Price:</strong> $${order.totalPrice}</p>

<h3>Ordered Product</h3>
<ul>
    <li>${order.product.name} - Quantity: ${order.quantity} - Price: $${order.product.price}</li>
</ul>


<a href='inventory?action=viewOrders' class='btn btn-secondary'>Back to Orders</a>
</body>
</html>
