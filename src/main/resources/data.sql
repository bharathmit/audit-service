INSERT INTO ROLE VALUES (1,1, CURRENT_DATE(),1, CURRENT_DATE(),CURRENT_TIMESTAMP,'User','Active'); 


INSERT INTO USER( user_id, first_name, mobile, email_id , gender, marital_status, gstp_number, status, created_by, created_date)  VALUES 
 (1,'bharath','9789944159','bharathkumar.feb14@gmail.com','Male', 'Single', '1345','InActive', 1, CURRENT_DATE());
 
 INSERT INTO USER_ROLE ( user_role_id,created_by,created_date,status,role_id,user_id ) VALUES
 (1,1, CURRENT_DATE(),'Active',1,1);
 
INSERT INTO USER( user_id, first_name, mobile, email_id , gender, marital_status, gstp_number, status, created_by, created_date)  VALUES 
 (2,'mani','9789944150','veluvijay1804@gmail.com','Male', 'Single', '12345','InActive', 1, CURRENT_DATE());
 
 INSERT INTO USER_ROLE ( user_role_id,created_by,created_date,status,role_id,user_id ) VALUES
 (2,1, CURRENT_DATE(),'Active',1,2);
 
INSERT INTO USER( user_id, first_name, mobile, email_id , gender, marital_status, gstp_number, status, created_by, created_date)  VALUES 
 (3,'Mohan','9884173633','m.mohaanraj@gmail.com','Male', 'Single', '123456','InActive', 1, CURRENT_DATE());
 
 INSERT INTO USER_ROLE ( user_role_id,created_by,created_date,status,role_id,user_id ) VALUES
 (3,1, CURRENT_DATE(),'Active',1,3);
 
