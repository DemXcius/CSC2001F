-- 18
SELECT e.email
FROM employees e
JOIN customers c ON e.employeeNumber = c.salesRepEmployeeNumber
WHERE e.jobTitle = 'Sales Rep' 
GROUP BY e.employeeNumber, e.email
HAVING COUNT(c.customerNumber) < (
	SELECT COUNT(DISTINCT customerNumber)
    FROM customers
    WHERE salesRepEmployeeNumber = 1166);