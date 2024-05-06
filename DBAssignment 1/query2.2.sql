
-- This query retrieves information about sales representatives, including the total number of customers they handle,
-- the total payments received from those customers, and the average payment per customer.
SELECT 
    -- Concatenate the first name and last name of each employee to create the employee's full name,
    -- and alias it as "employeeName".
    CONCAT(e.firstName, ' ', e.lastName) AS employeeName,
    
    -- Count the number of distinct customer numbers associated with each sales rep and alias the result as "totalCustomers".
    COUNT(DISTINCT c.customerNumber) AS totalCustomers,
    
    -- Calculate the total payments received from customers associated with each sales rep,
    -- rounding the result to 2 decimal places, and alias it as "totalPayments".
    ROUND(SUM(p.amount), 2) AS totalPayments,
    
    -- Calculate the average payment per customer for each sales rep,
    -- rounding the result to 2 decimal places, and alias it as "averagePayment".
    CASE 
        WHEN COUNT(DISTINCT c.customerNumber) > 0 THEN ROUND(SUM(p.amount) / COUNT(DISTINCT c.customerNumber), 2)
        ELSE 0
    END AS averagePayment
FROM 
    employees e
LEFT JOIN 
    customers c ON e.employeeNumber = c.salesRepEmployeeNumber
LEFT JOIN 
    payments p ON c.customerNumber = p.customerNumber
-- Group the results by the concatenated full name of each sales rep.
GROUP BY 
    CONCAT(e.firstName, ' ', e.lastName)
-- Order the results by the total number of customers in descending order, then by the total payments in descending order.
ORDER BY 
    totalCustomers DESC, totalPayments DESC
-- Limit the output to the top 5 sales reps.
LIMIT 5;

