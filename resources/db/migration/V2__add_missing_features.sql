-- Add new columns to incharges table
ALTER TABLE incharges
ADD COLUMN employee_id VARCHAR(50) UNIQUE,
ADD COLUMN verified BOOLEAN NOT NULL DEFAULT FALSE;

-- Create status_history table
CREATE TABLE IF NOT EXISTS status_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    complaint_id BIGINT NOT NULL,
    old_status VARCHAR(20) NOT NULL,
    new_status VARCHAR(20) NOT NULL,
    updated_by VARCHAR(20) NOT NULL,
    updated_by_name VARCHAR(100) NOT NULL,
    updated_by_id BIGINT NOT NULL,
    remarks VARCHAR(500),
    timestamp TIMESTAMP NOT NULL,
    FOREIGN KEY (complaint_id) REFERENCES complaints(id) ON DELETE CASCADE
);

-- Create resolution_proofs table
CREATE TABLE IF NOT EXISTS resolution_proofs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    complaint_id BIGINT NOT NULL UNIQUE,
    image_url VARCHAR(500) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    uploaded_by BIGINT NOT NULL,
    remarks VARCHAR(500),
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (complaint_id) REFERENCES complaints(id) ON DELETE CASCADE,
    FOREIGN KEY (uploaded_by) REFERENCES incharges(id)
);

-- Add indexes for performance
CREATE INDEX idx_status_history_complaint ON status_history(complaint_id);
CREATE INDEX idx_status_history_timestamp ON status_history(timestamp);
CREATE INDEX idx_resolution_proof_complaint ON resolution_proofs(complaint_id);

-- Add updated_at column to complaints if not exists
ALTER TABLE complaints
ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
