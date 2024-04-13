-- 17
SELECT productCode, SUM(quantityOrdered) AS Total
FROM orderdetails
GROUP BY productCode
HAVING SUM(quantityOrdered) = (
    SELECT SUM(quantityOrdered) AS highest_total
    FROM orderdetails
    GROUP BY productCode
    ORDER BY highest_total DESC
    LIMIT 1
);