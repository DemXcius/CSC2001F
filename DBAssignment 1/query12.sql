-- 12
SELECT customerNumber, ROUND(SUM(amount),2) as total
FROM payments
GROUP BY customerNumber;