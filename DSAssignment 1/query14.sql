-- 14
SELECT c.CustomerName, ROUND(SUM(p.amount),2) as total
FROM customers c
JOIN payments p ON c.CustomerNumber = p.CustomerNumber
WHERE c.city = 'Paris' 
GROUP BY c.CustomerName
HAVING COUNT(p.CustomerNumber) > 4;