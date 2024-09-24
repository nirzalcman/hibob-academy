CREATE UNIQUE INDEX IF NOT EXISTS idx_feedback_response_company_feedback_response_by ON feedback_response(company_id, feedback_id, response_by);



