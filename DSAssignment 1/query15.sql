-- 15
SELECT  c.customerName, c.phone
FROM customers c
LEFT JOIN payments p ON c.customerNumber = p.customerNumber
WHERE p.customerNumber IS NULL;