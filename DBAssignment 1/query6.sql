-- 6
SELECT ROUND(AVG(amount), 2) as mean, ROUND(SUM(amount), 2) as total
FROM payments;
