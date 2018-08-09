create database cat3;

use cat3;


 CREATE TABLE `cat3`.`login_details` (
    `login_id` INT  NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `role` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`login_id`)
   
  );
  
CREATE TABLE `cat3`.`skill_details` (
  `skill_id` INT  NOT NULL AUTO_INCREMENT,
  `skill_name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(255) NULL,
  `creation_date` DATE NOT NULL,
  `updation_date` DATE NOT NULL,
`skill_category` VARCHAR(255) NULL,
`skill_sub_category` VARCHAR(255) NULL,
  PRIMARY KEY (`skill_id`),
  UNIQUE INDEX `skill_name_UNIQUE` (`skill_name` ASC));


CREATE TABLE `cat3`.`employee_details` (
 `id` INT  NOT NULL AUTO_INCREMENT,
  `emp_id` VARCHAR(255) NOT NULL,
  `first_name` VARCHAR(255) NOT NULL,
  `last_name` VARCHAR(255) NOT NULL,
  `department` VARCHAR(255) NULL,
  `manager_id` VARCHAR(255) ,
  `email_id` VARCHAR(255) NOT NULL ,
  `creation_date` DATE NOT NULL,
  `updation_date` DATE NOT NULL,
  `SECURITY_QUESTION_1` VARCHAR(255) NOT NULL,
  `SECURITY_ANSWER_1` VARCHAR(255) NOT NULL,
  `SECURITY_QUESTION_2` VARCHAR(255) NOT NULL,
  `SECURITY_ANSWER_2` VARCHAR(255) NOT NULL,
  `APPROVAL_STATUS` VARCHAR(255),
  PRIMARY KEY (`id`),
  constraint fk1
  foreign key(`id`) references `login_details`(`login_id`)
  );




 
  
CREATE  TABLE `cat3`.`employee_skills` ( 
    `id` INT  NOT NULL AUTO_INCREMENT, 
   `emp_id` VARCHAR(255) NOT NULL , 
   `skill_id` INT NOT NULL , 
   `sdlc_category` INT NOT NULL ,
   `week_number` INT NOT NULL ,
   `rating` VARCHAR(45) NOT NULL ,
   `rating_done_by` VARCHAR(45) NOT NULL ,
   `comment` VARCHAR(255),
   `RATING_DONE_BY_EMP_ID` VARCHAR(45) NOT NULL ,
   `creation_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`) 
  );
  
  ALTER TABLE `cat3`.`employee_skills` CHANGE COLUMN `sdlc_category` `sdlc_category` VARCHAR(255) NOT NULL  ;


  
  CREATE  TABLE `cat3`.`profile_details` ( 
    `id` INT  NOT NULL AUTO_INCREMENT, 
   `emp_id_profile` VARCHAR(255) NOT NULL , 
   `project_role` VARCHAR(255),
   `description` VARCHAR(255),
   `creation_date` DATETIME,
  PRIMARY KEY (`id`), 
  constraint emp_profile
  foreign key(`id`) 
  references `employee_details`(`id`)
  );
  
  ALTER TABLE `cat3`.`profile_details` ADD COLUMN `firstname` VARCHAR(255) NULL  AFTER `emp_id_profile` , ADD COLUMN `lastname` VARCHAR(255) NULL  AFTER `firstname` , ADD COLUMN `email` VARCHAR(255) NULL  AFTER `lastname` ;
  
  




  
CREATE  TABLE `cat3`.`skill_profile` ( 
    `id` INT  NOT NULL AUTO_INCREMENT, 
   `profile_id` INT NOT NULL , 
   `skill_id` INT NOT NULL,
   `creation_date` DATETIME,
  PRIMARY KEY (`id`), 
  constraint skills_profile
  foreign key(`profile_id`) 
  references `profile_details`(`id`)
  );
  


ALTER TABLE cat3.skill_profile
ADD CONSTRAINT skills_profile_sk
FOREIGN KEY (skill_id) REFERENCES skill_details(skill_id);



CREATE  TABLE `cat3`.`security_questions` (

  `id` INT NOT NULL ,

  `question` VARCHAR(255) NOT NULL ,

  PRIMARY KEY (`id`) );


CREATE  TABLE `cat3`.`feedback_details` (

  `id` INT NOT NULL AUTO_INCREMENT ,

  `emp_id` VARCHAR(145) NOT NULL ,

  `rating_done_by` VARCHAR(145) NOT NULL ,

  `rating_done_by_empid` VARCHAR(145) NOT NULL ,

  `comment` VARCHAR(356) NULL ,

  `creation_date` DATETIME NOT NULL ,

  PRIMARY KEY (`id`) );

ALTER TABLE `cat3`.`feedback_details` ADD COLUMN `week_number` INT NULL  AFTER `creation_date` ;
ALTER TABLE `cat3`.`feedback_details` CHANGE COLUMN `rating_done_by_empid` `rating_done_by_emp_id` VARCHAR(145) NOT NULL  ;

