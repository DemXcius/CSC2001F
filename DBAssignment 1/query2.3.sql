-- This query uses a recursive common table expression to traverse the hierarchical structure of employees.
-- It starts with an initial employee (jobTitle = 'President') and recursively selects all employees who report to that employee,
-- building a hierarchy based on the reportsTo relationship.

-- Define a recursive CTE named 'recursive_hierarchy'.
WITH RECURSIVE recursive_hierarchy AS (
  -- Anchor member: Select details of the initial employee (jobTitle = 'President') with level 0.
  SELECT
    e.employeeNumber,
    e.firstName,
    e.lastName,
    e.jobTitle,
    e.reportsTo,
    o.city AS City, -- Include the office city
    0 AS level
  FROM
    classicModels.employees e
  JOIN
    classicModels.offices o ON e.officeCode = o.officeCode
  WHERE
    e.jobTitle = 'President'

  UNION ALL

  -- Recursive member: Select details of employees who report to the previously selected employees,
  -- incrementing the level by 1 each time.
  SELECT
    e.employeeNumber,
    e.firstName,
    e.lastName,
    e.jobTitle,
    e.reportsTo,
    o.city AS City, -- Include the office city
    level + 1
  FROM
    classicModels.employees e
  JOIN
    classicModels.offices o ON e.officeCode = o.officeCode
  INNER JOIN
    recursive_hierarchy rh ON e.reportsTo = rh.employeeNumber
)
-- Select employee details (employeeNumber, firstName, lastName, jobTitle, officeCity, level) from the recursive_hierarchy CTE.
SELECT
  employeeNumber,
  firstName,
  lastName,
  jobTitle,
  City,
  level
FROM
  recursive_hierarchy;