-- 1. Colleges
-- Drop dependent tables first
DROP TABLE IF EXISTS audit_logs, phase_evaluations, applications, screening_teams, screening_criteria, hiring_phases, placement_drives, resource_allocations, company_teams, companies, company_categories, resumes, reports, notifications, system_configs, faqs, profiles, students, placement_officers CASCADE;

-- Drop tables with no dependencies after
DROP TABLE IF EXISTS colleges, branches, users CASCADE;


CREATE TABLE colleges (
    clg_id SMALLINT PRIMARY KEY,
    clg_name VARCHAR(80) NOT NULL,
    clg_address VARCHAR(255),
    clg_contact VARCHAR(15)
);


INSERT INTO colleges (clg_id, clg_name, clg_address, clg_contact)
VALUES
(1, 'Aditya Engineering College', 'Surampalem, East Godavari District, Andhra Pradesh', '8801234567');

-- 2. Branches
CREATE TABLE branches (
    brn_id SMALLINT PRIMARY KEY,
    brn_clg_id SMALLINT NOT NULL REFERENCES colleges(clg_id),
    brn_name VARCHAR(50) NOT NULL,
    brn_desc VARCHAR(255)
);

INSERT INTO branches (brn_id, brn_clg_id, brn_name, brn_desc)
VALUES
(1, 1, 'CSE', 'Computer Science and Engineering focuses on computing, programming, and software development. Students learn algorithms, data structures, and develop skills in creating software systems and applications.'),
(2, 1, 'ECE', 'Electronics and Communication Engineering deals with the design and development of electronic systems and communication equipment. It covers subjects like digital electronics, signal processing, and communication systems.'),
(3, 1, 'MECH', 'Mechanical Engineering focuses on the design, analysis, and manufacturing of mechanical systems. It involves thermodynamics, fluid mechanics, materials science, and robotics, providing a foundation for various engineering applications.'),
(4, 1, 'IT', 'Information Technology focuses on the use of computers and telecommunications to store, retrieve, transmit, and manipulate data. It encompasses software development, network administration, and IT infrastructure management.'),
(5, 1, 'AIML', 'Artificial Intelligence and Machine Learning involves creating algorithms and systems that can learn and make decisions. This branch covers topics such as data science, neural networks, deep learning, and natural language processing.');


CREATE TABLE students (
    rollNo VARCHAR(50) PRIMARY KEY,
    fullName VARCHAR(255),
    branch_id INT,
    college_id INT,
    admYear INT,
    passoutYear INT,
    dob DATE,
    gender VARCHAR(10),
    address VARCHAR(255),
    status VARCHAR(10),
    cgpa numeric,
    backlogs INT,
    phoneNo VARCHAR(20),
    collegeEmail VARCHAR(255) NOT NULL UNIQUE,
    tenthScore numeric,
    interScore numeric,
    eamcetRank INT,
    CONSTRAINT fk_branch FOREIGN KEY (branch_id) REFERENCES branches(brn_id),
    CONSTRAINT fk_college FOREIGN KEY (college_id) REFERENCES colleges(clg_id)
);


INSERT INTO students (rollNo, college_id, branch_id, passoutYear, cgpa, backlogs, phoneNo, collegeEmail, tenthScore, interScore, eamcetRank, fullName)
VALUES
-- CSE Branch (Roll Number: 21A91A[05]01)
('21A91A0501', 1, 1, 2025, 8.5, 0, '9876543210', '21A91A0501@aec.edu.in', 9.1, 85.2, 18342, 'Aarav Reddy'),
('21A91A0502', 1, 1, 2025, 8.6, 1, '9865324102', '21A91A0502@aec.edu.in', 9.4, 87.5, 21000, 'Ananya Kumar'),
('21A91A0503', 1, 1, 2025, 8.9, 0, '9674123589', '21A91A0503@aec.edu.in', 9.3, 82.7, 15673, 'Ishaan Verma'),
('21A91A0504', 1, 1, 2025, 8.3, 2, '9776541234', '21A91A0504@aec.edu.in', 9.0, 88.2, 25000, 'Saanvi Yadav'),
('21A91A0505', 1, 1, 2025, 8.7, 0, '9761234578', '21A91A0505@aec.edu.in', 9.2, 86.1, 14562, 'Rohit Choudhury'),

-- ECE Branch (Roll Number: 21A91A[04]01)
('21A91A0401', 1, 2, 2025, 8.4, 1, '9854123789', '21A91A0401@aec.edu.in', 8.8, 75.6, 18500, 'Rajesh Kumar'),
('21A91A0402', 1, 2, 2025, 9.0, 0, '9761234567', '21A91A0402@aec.edu.in', 8.6, 90.4, 23000, 'Sanya Reddy'),
('21A91A0403', 1, 2, 2025, 8.2, 3, '9876543201', '21A91A0403@aec.edu.in', 9.1, 82.3, 29000, 'Harsha Mehta'),
('21A91A0404', 1, 2, 2025, 8.5, 1, '9801234567', '21A91A0404@aec.edu.in', 8.7, 91.2, 18000, 'Neha Patel'),
('21A91A0405', 1, 2, 2025, 8.3, 4, '9745623101', '21A91A0405@aec.edu.in', 9.0, 84.9, 21000, 'Aaditya Shetty'),

-- Mechanical Branch (Roll Number: 21A91A[03]01)
('21A91A0301', 1, 3, 2025, 8.8, 0, '9896721345', '21A91A0301@aec.edu.in', 9.2, 78.4, 23000, 'Vishal Kumar'),
('21A91A0302', 1, 3, 2025, 8.7, 2, '9764312587', '21A91A0302@aec.edu.in', 9.3, 80.1, 26000, 'Deepika Yadav'),
('21A91A0303', 1, 3, 2025, 8.5, 0, '9647384521', '21A91A0303@aec.edu.in', 9.0, 79.9, 22000, 'Shivam Agrawal'),
('21A91A0304', 1, 3, 2025, 8.6, 1, '9826457310', '21A91A0304@aec.edu.in', 9.2, 83.4, 17000, 'Siddhi Deshmukh'),
('21A91A0305', 1, 3, 2025, 8.9, 0, '9712348765', '21A91A0305@aec.edu.in', 9.4, 84.3, 16000, 'Nikhil Raj'),

-- IT Branch (Roll Number: 21A91A[12]01)
('21A91A1201', 1, 4, 2025, 9.0, 0, '9856743120', '21A91A1201@aec.edu.in', 8.8, 92.0, 28000, 'Harshit Gupta'),
('21A91A1202', 1, 4, 2025, 8.3, 2, '9736124857', '21A91A1202@aec.edu.in', 9.2, 89.1, 30000, 'Simran Kaur'),
('21A91A1203', 1, 4, 2025, 8.7, 0, '9603245876', '21A91A1203@aec.edu.in', 9.4, 85.7, 22000, 'Manoj Kumar'),
('21A91A1204', 1, 4, 2025, 8.5, 1, '9845123670', '21A91A1204@aec.edu.in', 8.9, 87.3, 24000, 'Ananya Pandey'),
('21A91A1205', 1, 4, 2025, 9.1, 0, '9713648520', '21A91A1205@aec.edu.in', 9.0, 90.0, 15000, 'Rahul Sharma'),

-- AIML Branch (Roll Number: 21A91A[42]01)
('21A91A4201', 1, 5, 2025, 8.6, 0, '9753124689', '21A91A4201@aec.edu.in', 9.3, 83.5, 25000, 'Ravi Kiran'),
('21A91A4202', 1, 5, 2025, 8.9, 1, '9667834521', '21A91A4202@aec.edu.in', 9.4, 88.0, 27000, 'Madhuri Joshi'),
('21A91A4203', 1, 5, 2025, 8.8, 0, '9647812345', '21A91A4203@aec.edu.in', 9.2, 82.7, 21000, 'Akhil Rao'),
('21A91A4204', 1, 5, 2025, 9.0, 0, '9675342189', '21A91A4204@aec.edu.in', 9.1, 86.2, 18000, 'Aaradhya Singh'),
('21A91A4205', 1, 5, 2025, 8.5, 2, '9712354678', '21A91A4205@aec.edu.in', 9.0, 84.5, 22000, 'Yashika Malhotra'),

-- CSE Branch (Roll Number: 21A91A[05]01) Again for more records
('21A91A0506', 1, 1, 2025, 8.8, 0, '9765432109', '21A91A0506@aec.edu.in', 9.2, 80.9, 15000, 'Varun Iyer'),
('21A91A0507', 1, 1, 2025, 8.4, 1, '9804561234', '21A91A0507@aec.edu.in', 8.8, 89.6, 18000, 'Swati Patil'),
('21A91A0508', 1, 1, 2025, 9.0, 0, '9812345670', '21A91A0508@aec.edu.in', 9.1, 92.4, 16000, 'Priya Rani'),
('21A91A0509', 1, 1, 2025, 8.7, 3, '9712348765', '21A91A0509@aec.edu.in', 9.0, 83.0, 20000, 'Ajay Joshi');


-- 3. Users
CREATE TABLE users (
    usr_id VARCHAR PRIMARY KEY REFERENCES students(rollno),
    usr_password VARCHAR(255) NOT NULL,
    usr_cdate DATE NOT NULL,
    usr_role CHAR(4) CHECK (usr_role IN ('STUD', 'ADMN'))
);

INSERT INTO users (usr_id, usr_password, usr_cdate, usr_role)
SELECT rollno, 'Aditya', CURRENT_DATE, 'STUD'
FROM students;


-- 4. Profiles
CREATE TABLE profiles (
    prf_id SMALLINT PRIMARY KEY,
    prf_usr_id SMALLINT NOT NULL REFERENCES users(usr_id),
    prf_brn_id SMALLINT NOT NULL REFERENCES branches(brn_id),
    prf_gpa DECIMAL(3,2),
    prf_updated DATE
);

-- 5. Company Categories
CREATE TABLE company_categories (
    cct_id SMALLINT PRIMARY KEY,
    cct_name VARCHAR(50) NOT NULL,
    cct_desc VARCHAR(255)
);

-- 6. Companies
CREATE TABLE companies (
    cmp_id SMALLINT PRIMARY KEY,
    cmp_cct_id SMALLINT NOT NULL REFERENCES company_categories(cct_id),
    cmp_name VARCHAR(80) NOT NULL,
    cmp_desc VARCHAR(255)
);

-- 7. Company Teams
CREATE TABLE company_teams (
    ctm_id SMALLINT PRIMARY KEY,
    ctm_cmp_id SMALLINT NOT NULL REFERENCES companies(cmp_id),
    ctm_name VARCHAR(80) NOT NULL,
    ctm_contact VARCHAR(100)
);

-- Inserting data into company_categories
INSERT INTO company_categories (cct_id, cct_name, cct_desc)
VALUES
(1, 'IT', 'Information Technology Companies'),
(2, 'Core', 'Core Engineering Companies (ECE, MECH, EEE)');

-- Inserting data into companies (20 IT companies)
INSERT INTO companies (cmp_id, cmp_cct_id, cmp_name, cmp_desc)
VALUES
(1, 1, 'TCS', 'Tata Consultancy Services, a leading IT services company'),
(2, 1, 'Infosys', 'Global leader in next-generation digital services and consulting'),
(3, 1, 'Wipro', 'Wipro Limited is a leading global information technology, consulting, and business process services company'),
(4, 1, 'Accenture', 'Accenture is a global professional services company with leading capabilities in digital, cloud, and security'),
(5, 1, 'Cognizant', 'Cognizant Technology Solutions is a global leader in IT services'),
(6, 1, 'IBM', 'IBM offers a wide range of technology solutions, including cloud, AI, and software'),
(7, 1, 'Tech Mahindra', 'Tech Mahindra provides IT services and solutions for businesses'),
(8, 1, 'Capgemini', 'Capgemini is a global leader in consulting, technology services, and digital transformation'),
(9, 1, 'Mindtree', 'Mindtree delivers digital transformation and technology services'),
(10, 1, 'L&T Infotech', 'L&T Infotech is a global IT services and consulting company'),
(11, 1, 'HCL Technologies', 'HCL Technologies is a multinational IT services company'),
(12, 1, 'Mphasis', 'Mphasis is a leading IT solutions provider offering technology services and consulting'),
(13, 1, 'Tech Mahindra', 'Tech Mahindra offers IT and business solutions to clients worldwide'),
(14, 1, 'Virtusa', 'Virtusa is a global provider of digital business strategy, digital engineering, and IT services'),
(15, 1, 'Hexaware', 'Hexaware Technologies offers IT services and consulting'),
(16, 1, 'Cognizant', 'A leader in IT services and business consulting'),
(17, 1, 'Hewlett-Packard (HP)', 'HP provides hardware, software, and IT services'),
(18, 1, 'Oracle', 'Oracle offers integrated cloud applications and platform services'),
(19, 1, 'SAP', 'SAP is a global leader in enterprise resource planning (ERP) software solutions'),
(20, 1, 'Dell Technologies', 'Dell Technologies provides a range of IT solutions, from software to hardware');

-- Inserting data into companies (10 Core companies for ECE, MECH, EEE)
INSERT INTO companies (cmp_id, cmp_cct_id, cmp_name, cmp_desc)
VALUES
(21, 2, 'BHEL', 'Bharat Heavy Electricals Limited is a leading manufacturer of heavy electrical equipment'),
(22, 2, 'L&T', 'Larsen & Toubro is a major technology, engineering, construction, manufacturing, and financial services conglomerate'),
(23, 2, 'ABB', 'ABB is a global leader in electrical equipment and industrial automation'),
(24, 2, 'Siemens', 'Siemens is a global powerhouse in electronics and electrical engineering'),
(25, 2, 'Schneider Electric', 'Schneider Electric specializes in energy management and automation solutions'),
(26, 2, 'GE India', 'GE India is a subsidiary of General Electric providing solutions in power, aviation, and renewable energy'),
(27, 2, 'Bosch', 'Bosch is a leading global supplier of technology and services in various industries'),
(28, 2, 'Ashok Leyland', 'Ashok Leyland is a leading manufacturer of commercial vehicles'),
(29, 2, 'Tata Motors', 'Tata Motors is a global automotive manufacturer'),
(30, 2, 'Maruti Suzuki', 'Maruti Suzuki is a leader in the Indian automotive industry, known for its cars');

-- Inserting data into company_teams
INSERT INTO company_teams (ctm_id, ctm_cmp_id, ctm_name, ctm_contact)
VALUES
(1, 1, 'HR Team', 'hr@tcs.com'),
(2, 1, 'Technical Recruitment (TR) Team', 'recruitment@tcs.com'),
(3, 1, 'Screening Test Team', 'tests@tcs.com'),
(4, 1, 'Interview Panel', 'interviews@tcs.com'),
(5, 1, 'Assessment or Evaluation Team', 'evaluation@tcs.com'),

(6, 2, 'HR Team', 'hr@infosys.com'),
(7, 2, 'Technical Recruitment (TR) Team', 'recruitment@infosys.com'),
(8, 2, 'Screening Test Team', 'tests@infosys.com'),
(9, 2, 'Interview Panel', 'interviews@infosys.com'),
(10, 2, 'Assessment or Evaluation Team', 'evaluation@infosys.com'),

(11, 3, 'HR Team', 'hr@wipro.com'),
(12, 3, 'Technical Recruitment (TR) Team', 'recruitment@wipro.com'),
(13, 3, 'Screening Test Team', 'tests@wipro.com'),
(14, 3, 'Interview Panel', 'interviews@wipro.com'),
(15, 3, 'Assessment or Evaluation Team', 'evaluation@wipro.com'),

(16, 4, 'HR Team', 'hr@accenture.com'),
(17, 4, 'Technical Recruitment (TR) Team', 'recruitment@accenture.com'),
(18, 4, 'Screening Test Team', 'tests@accenture.com'),
(19, 4, 'Interview Panel', 'interviews@accenture.com'),
(20, 4, 'Assessment or Evaluation Team', 'evaluation@accenture.com'),

(21, 21, 'HR Team', 'hr@bhel.com'),
(22, 21, 'Technical Recruitment (TR) Team', 'recruitment@bhel.com'),
(23, 21, 'Screening Test Team', 'tests@bhel.com'),
(24, 21, 'Interview Panel', 'interviews@bhel.com'),
(25, 21, 'Assessment or Evaluation Team', 'evaluation@bhel.com'),

(26, 22, 'HR Team', 'hr@lt.com'),
(27, 22, 'Technical Recruitment (TR) Team', 'recruitment@lt.com'),
(28, 22, 'Screening Test Team', 'tests@lt.com'),
(29, 22, 'Interview Panel', 'interviews@lt.com'),
(30, 22, 'Assessment or Evaluation Team', 'evaluation@lt.com');


-- 8. Placement Drives
CREATE TABLE placement_drives (
    pld_id SMALLINT PRIMARY KEY,
    pld_clg_id SMALLINT NOT NULL REFERENCES colleges(clg_id),
    pld_name VARCHAR(80) NOT NULL,
    pld_start_date DATE NOT NULL,
    pld_end_date DATE
);

-- 9. Hiring Phases
CREATE TABLE hiring_phases (
    hph_id SMALLINT PRIMARY KEY,
    hph_pld_id SMALLINT NOT NULL REFERENCES placement_drives(pld_id),
    hph_name VARCHAR(50) NOT NULL,
    hph_sequence SMALLINT NOT NULL
);

-- 10. Screening Criteria
CREATE TABLE screening_criteria (
    scr_id SMALLINT PRIMARY KEY,
    scr_hph_id SMALLINT NOT NULL REFERENCES hiring_phases(hph_id),
    scr_min_gpa DECIMAL(3,2),
    scr_skills VARCHAR(255)
);

-- 11. Screening Teams
CREATE TABLE screening_teams (
    stm_id SMALLINT PRIMARY KEY,
    stm_hph_id SMALLINT NOT NULL REFERENCES hiring_phases(hph_id),
    stm_ctm_id SMALLINT NOT NULL REFERENCES company_teams(ctm_id)
);

-- 12. Applications
CREATE TABLE applications (
    app_id SMALLINT PRIMARY KEY,
    app_usr_id SMALLINT NOT NULL REFERENCES users(usr_id),
    app_pld_id SMALLINT NOT NULL REFERENCES placement_drives(pld_id),
    app_cmp_id SMALLINT NOT NULL REFERENCES companies(cmp_id),
    app_date DATE NOT NULL,
    app_status CHAR(4) CHECK (app_status IN ('PEND', 'APPR', 'REJC'))
);

-- 13. Phase Evaluations
CREATE TABLE phase_evaluations (
    pev_id SMALLINT PRIMARY KEY,
    pev_app_id SMALLINT NOT NULL REFERENCES applications(app_id),
    pev_hph_id SMALLINT NOT NULL REFERENCES hiring_phases(hph_id),
    pev_score DECIMAL(5,2),
    pev_comments VARCHAR(255)
);

-- 14. Resumes
CREATE TABLE resumes (
    res_id SMALLINT PRIMARY KEY,
    res_usr_id SMALLINT NOT NULL REFERENCES users(usr_id),
    res_file VARCHAR(255) NOT NULL,
    res_upload_date DATE NOT NULL
);

-- 15. Resource Allocations
CREATE TABLE resource_allocations (
    ral_id SMALLINT PRIMARY KEY,
    ral_pld_id SMALLINT NOT NULL REFERENCES placement_drives(pld_id),
    ral_resource_type VARCHAR(50) NOT NULL,
    ral_quantity INTEGER NOT NULL,
    ral_date DATE NOT NULL
);

-- 16. Notifications
CREATE TABLE notifications (
    ntf_id SMALLINT PRIMARY KEY,
    ntf_usr_id SMALLINT NOT NULL REFERENCES users(usr_id),
    ntf_message VARCHAR(255) NOT NULL,
    ntf_date DATE NOT NULL,
    ntf_read BOOLEAN DEFAULT FALSE
);

-- 17. Reports
CREATE TABLE reports (
    rpt_id SMALLINT PRIMARY KEY,
    rpt_usr_id SMALLINT NOT NULL REFERENCES users(usr_id),
    rpt_type CHAR(4) CHECK (rpt_type IN ('PLAC', 'ANLY')),
    rpt_date DATE NOT NULL,
    rpt_file VARCHAR(255)
);

-- 18. System Configs
CREATE TABLE system_configs (
    cfg_id SMALLINT PRIMARY KEY,
    cfg_key VARCHAR(50) NOT NULL,
    cfg_value VARCHAR(255) NOT NULL,
    cfg_desc VARCHAR(255)
);

-- 19. Audit Logs
CREATE TABLE audit_logs (
    alg_id SMALLINT PRIMARY KEY,
    alg_app_id SMALLINT NOT NULL REFERENCES applications(app_id),
    alg_action VARCHAR(50) NOT NULL,
    alg_date DATE NOT NULL
);

-- 20. FAQs
CREATE TABLE faqs (
    faq_id SMALLINT PRIMARY KEY,
    faq_question VARCHAR(255) NOT NULL,
    faq_answer VARCHAR(255) NOT NULL
);


INSERT INTO students (rollNo, college_id, branch_id, admYear, passoutYear, dob, gender, address, status, cgpa, backlogs, passoutYear, phoneNo, collegeEmail, tenthScore, interScore)
VALUES
-- CSE Branch (Roll Number: 21A91A[05]01)
('21A91A0501', 1, 1, 2021, 2025, '2003-05-15', 'M', 'Kakinada, East Godavari', 'ACTV', 8.5, 0, 2025, '9876543210', '21A91A0501@aec.edu.in', 9.1, 85.2),
('21A91A0502', 1, 1, 2021, 2025, '2003-06-10', 'M', 'Rajahmundry, East Godavari', 'ACTV', 8.6, 0, 2025, '9865324102', '21A91A0502@aec.edu.in', 9.4, 87.5),
('21A91A0503', 1, 1, 2021, 2025, '2003-07-22', 'M', 'Samalkot, East Godavari', 'ACTV', 8.9, 0, 2025, '9674123589', '21A91A0503@aec.edu.in', 9.3, 82.7),
('21A91A0504', 1, 1, 2021, 2025, '2003-09-18', 'M', 'Peddapuram, East Godavari', 'ACTV', 8.3, 0, 2025, '9776541234', '21A91A0504@aec.edu.in', 9.0, 88.2),
('21A91A0505', 1, 1, 2021, 2025, '2003-11-05', 'M', 'Pitapuram, East Godavari', 'ACTV', 8.7, 0, 2025, '9761234578', '21A91A0505@aec.edu.in', 9.2, 86.1),

-- ECE Branch (Roll Number: 21A91A[04]01)
('21A91A0401', 1, 2, 2021, 2025, '2003-08-12', 'M', 'Ravulapalem, East Godavari', 'ACTV', 8.4, 0, 2025, '9854123789', '21A91A0401@aec.edu.in', 8.8, 75.6),
('21A91A0402', 1, 2, 2021, 2025, '2003-04-30', 'M', 'Kakinada, East Godavari', 'ACTV', 9.0, 0, 2025, '9761234567', '21A91A0402@aec.edu.in', 8.6, 90.4),
('21A91A0403', 1, 2, 2021, 2025, '2003-02-25', 'M', 'Rajahmundry, East Godavari', 'ACTV', 8.2, 0, 2025, '9876543201', '21A91A0403@aec.edu.in', 9.1, 82.3),
('21A91A0404', 1, 2, 2021, 2025, '2003-01-14', 'M', 'Samalkot, East Godavari', 'ACTV', 8.5, 0, 2025, '9801234567', '21A91A0404@aec.edu.in', 8.7, 91.2),
('21A91A0405', 1, 2, 2021, 2025, '2003-06-08', 'M', 'Peddapuram, East Godavari', 'ACTV', 8.3, 0, 2025, '9745623101', '21A91A0405@aec.edu.in', 9.0, 84.9),

-- Mechanical Branch (Roll Number: 21A91A[03]01)
('21A91A0301', 1, 3, 2021, 2025, '2003-10-21', 'M', 'Pitapuram, East Godavari', 'ACTV', 8.8, 0, 2025, '9896721345', '21A91A0301@aec.edu.in', 9.2, 78.4),
('21A91A0302', 1, 3, 2021, 2025, '2003-08-03', 'M', 'Ravulapalem, East Godavari', 'ACTV', 8.7, 0, 2025, '9764312587', '21A91A0302@aec.edu.in', 9.3, 80.1),
('21A91A0303', 1, 3, 2021, 2025, '2003-05-17', 'M', 'Rajahmundry, East Godavari', 'ACTV', 8.5, 0, 2025, '9647384521', '21A91A0303@aec.edu.in', 9.0, 79.9),
('21A91A0304', 1, 3, 2021, 2025, '2003-11-09', 'M', 'Kakinada, East Godavari', 'ACTV', 8.6, 0, 2025, '9826457310', '21A91A0304@aec.edu.in', 9.2, 83.4),
('21A91A0305', 1, 3, 2021, 2025, '2003-07-11', 'M', 'Samalkot, East Godavari', 'ACTV', 8.9, 0, 2025, '9712348765', '21A91A0305@aec.edu.in', 9.4, 84.3),

-- IT Branch (Roll Number: 21A91A[12]01)
('21A91A1201', 1, 4, 2021, 2025, '2003-06-21', 'M', 'Peddapuram, East Godavari', 'ACTV', 9.0, 0, 2025, '9856743120', '21A91A1201@aec.edu.in', 8.8, 92.0),
('21A91A1202', 1, 4, 2021, 2025, '2003-09-15', 'M', 'Pitapuram, East Godavari', 'ACTV', 8.3, 0, 2025, '9736124857', '21A91A1202@aec.edu.in', 9.2, 89.1),
('21A91A1203', 1, 4, 2021, 2025, '2003-03-28', 'M', 'Kakinada, East Godavari', 'ACTV', 8.7, 0, 2025, '9603245876', '21A91A1203@aec.edu.in', 9.4, 85.7),
('21A91A1204', 1, 4, 2021, 2025, '2003-10-30', 'M', 'Ravulapalem, East Godavari', 'ACTV', 8.5, 0, 2025, '9845123670', '21A91A1204@aec.edu.in', 8.9, 87.3),
('21A91A1205', 1, 4, 2021, 2025, '2003-01-08', 'M', 'Rajahmundry, East Godavari', 'ACTV', 9.1, 0, 2025, '9713648520', '21A91A1205@aec.edu.in', 9.0, 90.0),

-- AIML Branch (Roll Number: 21A91A[42]01)
('21A91A4201', 1, 5, 2021, 2025, '2003-04-02', 'M', 'Samalkot, East Godavari', 'ACTV', 8.6, 0, 2025, '9753124689', '21A91A4201@aec.edu.in', 9.3, 83.5),
('21A91A4202', 1, 5, 2021, 2025, '2003-02-18', 'M', 'Peddapuram, East Godavari', 'ACTV', 8.9, 0, 2025, '9667834521', '21A91A4202@aec.edu.in', 9.4, 88.0),
('21A91A4203', 1, 5, 2021, 2025, '2003-07-29', 'M', 'Pitapuram, East Godavari', 'ACTV', 8.8, 0, 2025, '9647812345', '21A91A4203@aec.edu.in', 9.2, 82.7),
('21A91A4204', 1, 5, 2021, 2025, '2003-11-06', 'M', 'Rajahmundry, East Godavari', 'ACTV', 9.0, 0, 2025, '9675342189', '21A91A4204@aec.edu.in', 9.1, 86.2),
('21A91A4205', 1, 5, 2021, 2025, '2003-05-24', 'M', 'Kakinada, East Godavari', 'ACTV', 8.5, 0, 2025, '9712354678', '21A91A4205@aec.edu.in', 9.0, 84.5)


-- 22. Placement Officers
CREATE TABLE placement_officers (
    pof_id SMALLINT PRIMARY KEY,
    pof_usr_id SMALLINT NOT NULL UNIQUE REFERENCES users(usr_id) ON DELETE CASCADE,
    pof_clg_id SMALLINT NOT NULL REFERENCES colleges(clg_id),
    pof_join_year SMALLINT NOT NULL,
    pof_department VARCHAR(100),
    pof_designation VARCHAR(100),
    pof_status CHAR(4) CHECK (pof_status IN ('ACTV', 'INAC'))
);

delete from colleges where clg_id = 1;
