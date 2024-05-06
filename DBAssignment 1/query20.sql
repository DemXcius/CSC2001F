-- 20
SELECT e.firstName, e.lastName, SUM(p.amount) as total
FROM employees e
JOIN customers c ON e.employeeNumber = c.salesRepEmployeeNumber
JOIN payments p ON c.customerNumber = p.customerNumber
WHERE e.officeCode = 7
GROUP BY e.firstName, e.lastName;