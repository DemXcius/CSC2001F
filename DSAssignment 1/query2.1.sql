-- 1
-- Find an available employee number and update the record
UPDATE 
	employees
SET 
	employeeNumber = (SELECT MAX(employeeNumber) + 1 FROM employees)
WHERE 
	employeeNumber = 1313;

