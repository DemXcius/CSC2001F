-- 19
SELECT SUM(det.priceEach*det.quantityOrdered) as totalCost
FROM customers c
JOIN orders o ON c.customerNumber = o.customerNumber
JOIN orderdetails det ON o.orderNumber = det.orderNumber
WHERE c.customerNumber = 121;