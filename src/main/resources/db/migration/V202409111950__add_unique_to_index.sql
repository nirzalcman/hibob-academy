DROP INDEX IF EXISTS idx_owner_company_employee;
CREATE UNIQUE INDEX IF NOT EXISTS idx_owner_company_employee ON owner(company_id, employee_id)