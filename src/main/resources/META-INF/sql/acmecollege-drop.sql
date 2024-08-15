-- -----------------------------------------------------
-- Drop Schema for ACMECollege Application
--
-- 
--
-- -----------------------------------------------------

--DROP SCHEMA IF EXISTS `acmecollege`;
use acmecollege;

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE acmecollege.peer_tutor;

TRUNCATE TABLE acmecollege.student_club;

TRUNCATE TABLE acmecollege.club_membership;

TRUNCATE TABLE acmecollege.peer_tutor_registration;

TRUNCATE TABLE acmecollege.membership_card;

TRUNCATE TABLE acmecollege.student;

TRUNCATE TABLE acmecollege.course;

SET FOREIGN_KEY_CHECKS = 1;