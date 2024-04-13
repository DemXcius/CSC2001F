-- 13
SELECT CustomerName, ROUND(SUM(p.amount),2) as total
FROM customers c
JOIN payments p ON c.CustomerNumber = p.customerNumber
GROUP BY c.CustomerName;