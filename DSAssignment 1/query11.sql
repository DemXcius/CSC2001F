-- 11
SELECT officeCode,
	CASE
		WHEN state IS NOT NULL THEN state
        WHEN country IS NOT NULL THEN country
        ELSE city
	END AS location
FROM offices;