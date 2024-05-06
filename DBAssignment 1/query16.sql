-- 16
SELECT pro.productName, pro.quantityInStock, line.textDescription
FROM products pro
JOIN productlines line ON pro.productLine = line.productLines
WHERE pro.quantityInStock < 100;