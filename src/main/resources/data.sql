INSERT INTO ROLE VALUES (1,1, CURRENT_DATE(),1, CURRENT_DATE(),CURRENT_TIMESTAMP,'Client','Active'); 


INSERT INTO USER( user_id, first_name, mobile, email_id , gender, marital_status, gstp_number, status, created_by, created_date)  VALUES 
 (1,'bharath','9789944159','bharathkumar.feb14@gmail.com','Male', 'Single', '1345','InActive', 1, CURRENT_DATE());
 
 INSERT INTO USER_ROLE ( user_role_id,created_by,created_date,status,role_id,user_id ) VALUES
 (1,1, CURRENT_DATE(),'Active',1,1);
 
