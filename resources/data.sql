-- Insert sample in-charges for testing
INSERT INTO incharges (name, ward, designation, mobile_number, email, employee_id, active, verified, created_at)
VALUES
('Rajesh Kumar', 'Ward-1', 'Sanitation Inspector', '9876543211', 'rajesh.ward1@swachh.gov', 'EMP001', true, false, NOW()),
('Priya Sharma', 'Ward-2', 'Waste Management Officer', '9876543212', 'priya.ward2@swachh.gov', 'EMP002', true, false, NOW()),
('Amit Patel', 'Ward-3', 'Chief Sanitation Officer', '9876543213', 'amit.ward3@swachh.gov', 'EMP003', true, false, NOW()),
('Sunita Reddy', 'Ward-4', 'Sanitation Supervisor', '9876543214', 'sunita.ward4@swachh.gov', 'EMP004', true, false, NOW()),
('Vijay Singh', 'Ward-5', 'Waste Inspector', '9876543215', 'vijay.ward5@swachh.gov', 'EMP005', true, false, NOW());
