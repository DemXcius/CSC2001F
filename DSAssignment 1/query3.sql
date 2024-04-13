-- 3
SELECT productVendor, productName, quantityInStock
FROM products
WHERE quantityInStock < 2000
    AND (productVendor LIKE '%diecast%' 
    OR productVendor LIKE '%dyecast%');