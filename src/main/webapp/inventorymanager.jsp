<<%@ page import="com.eventmanager.inventorymanagement.database.Database" %>
<%@ page import="com.eventmanager.inventorymanagement.model.*" %>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  Database.init(); // Initialize DB on JSP load (not recommended in real production setup)
  String action = request.getParameter("action");
  if (action == null) action = "home";
%>
<html>
<head>
  <title>Inventory Manager</title>
  <link href='https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css' rel='stylesheet'>
</head>
<body class='container'>
<h1 class='my-4'>Inventory Management</h1>

<nav class='navbar navbar-expand-lg navbar-light bg-light'>
  <a class='navbar-brand' href='inventory.jsp'>Home</a>
  <div class='collapse navbar-collapse'>
    <ul class='navbar-nav mr-auto'>
      <li class='nav-item'><a class='nav-link' href='inventory.jsp?action=viewProducts'>View Products</a></li>
      <li class='nav-item'><a class='nav-link' href='inventory.jsp?action=viewStock'>View Stock</a></li>
      <li class='nav-item'><a class='nav-link' href='inventory.jsp?action=placeOrderForm'>Place Order</a></li>
      <li class='nav-item'><a class='nav-link' href='inventory.jsp?action=viewOrders'>View Orders</a></li>
      <li class='nav-item'><a class='nav-link' href='inventory.jsp?action=addProductForm'>Add Product</a></li>
    </ul>
  </div>
</nav>
<hr>

<%
  switch (action) {
    case "viewProducts":
      List<Product> products = Database.getProducts();
%>
<h2>Product List</h2>
<%
  if (products.isEmpty()) {
%><p>No products available.</p><%
} else {
%>
<ul>
  <% for (Product p : products) { %>
  <li><%= p.getName() %> - $<%= p.getPrice() %> - Qty: <%= p.getQuantity() %>
    <a href='inventory.jsp?action=deleteProduct&id=<%= p.getId() %>'>Delete</a>
  </li>
  <% } %>
</ul>
<% } break; %>

<%
  case "viewStock":
    List<Stock> stocks = Database.getStocks();
%>
<h2>Stock Levels</h2>
<% if (stocks.isEmpty()) { %>
<p>No stock records found.</p>
<% } else { %>
<ul>
  <% for (Stock s : stocks) { %>
  <li><%= s.getStockName() %>: <%= s.getStockLevel() %> units available
    (Product ID: <%= s.getProduct().getId() %>, Stock ID: <%= s.getStockID() %>)
  </li>
  <% } %>
</ul>
<% } break; %>

<%
  case "placeOrderForm":
    products = Database.getProducts();
%>
<h2>Place Order</h2>
<form action="inventory.jsp?action=placeOrder" method="post">
  <div class='form-group'>
    <label>Select Product:</label>
    <select class='form-control' name='productId'>
      <% for (Product p : products) { %>
      <option value="<%= p.getId() %>"><%= p.getName() %> - $<%= p.getPrice() %></option>
      <% } %>
    </select>
  </div>
  <div class='form-group'>
    <label>Customer Name:</label>
    <input type='text' class='form-control' name='customerName' required>
  </div>
  <div class='form-group'>
    <label>Shipping Address:</label>
    <textarea class='form-control' name='shippingAddress' required></textarea>
  </div>
  <div class='form-group'>
    <label>Shipping Method:</label>
    <select class='form-control' name='shippingMethod'>
      <option>Standard</option>
      <option>Express</option>
    </select>
  </div>
  <div class='form-group'>
    <label>Payment Method:</label>
    <select class='form-control' name='paymentMethod'>
      <option>Credit Card</option>
      <option>PayPal</option>
    </select>
  </div>
  <div class='form-group'>
    <label>Quantity:</label>
    <input type='number' class='form-control' name='quantity' min='1' required>
  </div>
  <button class='btn btn-primary' type='submit'>Place Order</button>
</form>
<% break; %>

<%
  case "addProductForm":
%>
<h2>Add Product</h2>
<form action="inventory.jsp?action=addProduct" method="post">
  <div class='form-group'>
    <label>Product Name:</label>
    <input type='text' class='form-control' name='productName' required>
  </div>
  <div class='form-group'>
    <label>Product Price:</label>
    <input type='number' class='form-control' name='productPrice' step='0.01' required>
  </div>
  <div class='form-group'>
    <label>Product Quantity:</label>
    <input type='number' class='form-control' name='productQuantity' required>
  </div>
  <button class='btn btn-primary' type='submit'>Add Product</button>
</form>
<% break; %>

<%
  case "deleteProduct":
    try {
      int id = Integer.parseInt(request.getParameter("id"));
      Database.deleteProduct(id);
%>
<h3>Product Deleted Successfully!</h3>
<a href="inventory.jsp?action=viewProducts">Back to Product List</a>
<%
} catch (Exception e) {
%><p>Error deleting product.</p><%
    }
    break;
%>

<%
  case "placeOrder":
    try {
      int productId = Integer.parseInt(request.getParameter("productId"));
      int quantity = Integer.parseInt(request.getParameter("quantity"));
      String customerName = request.getParameter("customerName");
      String shippingAddress = request.getParameter("shippingAddress");
      String shippingMethod = request.getParameter("shippingMethod");
      String paymentMethod = request.getParameter("paymentMethod");

      Product product = null;
      for (Product p : Database.getProducts()) {
        if (p.getId() == productId) { product = p; break; }
      }

      Stock stock = null;
      for (Stock s : Database.getStocks()) {
        if (s.getProduct().getId() == productId) { stock = s; break; }
      }

      if (stock != null && stock.getStockLevel() >= quantity) {
        stock.reduceStock(quantity);
        Database.updateStock(stock);

        Order order = new Order(1, 123, productId, quantity, new Date(), "Processing",
                shippingAddress, shippingMethod, paymentMethod, product.getPrice() * quantity, product);
        Database.insertOrder(order);
%>
<h3>Order Placed Successfully!</h3>
<p>Customer: <%= customerName %></p>
<p>Product: <%= product.getName() %></p>
<p>Quantity: <%= quantity %></p>
<p>Total: $<%= order.getTotalPrice() %></p>
<p>Shipping: <%= shippingMethod %></p>
<p>Payment: <%= paymentMethod %></p>
<p>Remaining stock: <%= stock.getStockLevel() %></p>
<%
} else {
%><p>Not enough stock available.</p><%
  }
} catch (Exception e) {
%><p>Error placing order: <%= e.getMessage() %></p><%
    }
    break;
%>

<%
  case "addProduct":
    try {
      String name = request.getParameter("productName");
      double price = Double.parseDouble(request.getParameter("productPrice"));
      int quantity = Integer.parseInt(request.getParameter("productQuantity"));

      Product newProduct = new Product(0, name, price, quantity);
      Database.insertProduct(newProduct);

      Stock newStock = new Stock(0, quantity, name, newProduct);
      Database.insertStock(newStock);
%>
<h3>Product Added!</h3>
<p>Name: <%= name %>, Price: $<%= price %>, Quantity: <%= quantity %></p>
<%
} catch (Exception e) {
%><p>Error adding product.</p><%
    }
    break;
%>

<%
  case "viewOrders":
    List<Order> orders = Database.getOrders();
%>
<h2>Orders</h2>
<% if (orders.isEmpty()) { %>
<p>No orders found.</p>
<% } else { %>
<table class='table table-striped'>
  <thead><tr><th>ID</th><th>Product</th><th>Qty</th><th>Total</th><th>Status</th><th>Shipping</th><th>Payment</th></tr></thead>
  <tbody>
  <% for (Order o : orders) { %>
  <tr>
    <td><%= o.getOrderId() %></td>
    <td><%= o.getProduct().getName() %></td>
    <td><%= o.getQuantity() %></td>
    <td>$<%= o.getTotalPrice() %></td>
    <td><%= o.getOrderStatus() %></td>
    <td><%= o.getShippingMethod() %></td>
    <td><%= o.getPaymentMethod() %></td>
  </tr>
  <% } %>
  </tbody>
</table>
<% } break; %>

<% default: %>
<p>Welcome to the inventory system! Choose an action above.</p>
<% } %>

</body>
</html>
