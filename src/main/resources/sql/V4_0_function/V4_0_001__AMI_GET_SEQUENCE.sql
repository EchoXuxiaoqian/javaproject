DELIMITER $$


DROP FUNCTION IF EXISTS `AMI_GET_SEQUENCE`$$

CREATE FUNCTION `AMI_GET_SEQUENCE`(seq_name VARCHAR (50)) RETURNS BIGINT(20)
BEGIN
  DECLARE SEQ BIGINT ;
  SET SEQ = 0 ;
  UPDATE 
    sequence 
  SET
    current_value = current_value + increment 
  WHERE NAME = seq_name ;
  IF ROW_COUNT() = 0 
  THEN 
  INSERT INTO sequence (NAME, current_value, increment) 
  VALUES
    (seq_name, 1, 1) ;
  END IF ;
  SELECT 
    current_value INTO SEQ 
  FROM
    sequence 
  WHERE NAME = seq_name ;
  RETURN SEQ ;
END$$

DELIMITER ;