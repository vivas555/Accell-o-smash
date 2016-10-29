DROP TABLE IF EXISTS `user` ;

CREATE TABLE IF NOT EXISTS `user` (
  `USER_Pseudo` VARCHAR(16) NOT NULL,
  `USER_Password` VARCHAR(16) NOT NULL,
  `USER_Name` VARCHAR(45) NOT NULL,
  `USER_FirstName` VARCHAR(45) NOT NULL,
  `USER_EmergencyNumber` VARCHAR(10) NOT NULL,
  `USER_Weight` FLOAT NOT NULL,
  `USER_SeatBeltAlwaysOn` TINYINT(1) NOT NULL,
  PRIMARY KEY (`USER_Pseudo`))
ENGINE = InnoDB;

DROP TABLE IF EXISTS `Collision` ;

CREATE TABLE IF NOT EXISTS `Collision` (
  `COL_ID` INT NOT NULL AUTO_INCREMENT,
  `COL_Date` DATETIME NOT NULL,
  `COL_Force` FLOAT NOT NULL,
  `COL_HelpCalled` TINYINT(1) NOT NULL,
  `COL_User` VARCHAR(16) NOT NULL,
  PRIMARY KEY (`COL_ID`),
    FOREIGN KEY (`COL_User`)
    REFERENCES `user` (`USER_Pseudo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

INSERT INTO `user` (USER_Pseudo, USER_Password, USER_Name, USER_FirstName, USER_EmergencyNumber, USER_Weight, USER_SeatBeltAlwaysOn)
VALUES
('Awe', 'qq', 'Tremblay', 'Philippe', '911', 50, 0),
('Vivas5', 'qq', 'Rivard', 'Felix', '911', 90, 0);

INSERT INTO `Collision` (COL_ID, COL_Date, COL_Force, COL_HelpCalled, COL_User)
VALUES
(NULL, '2016-05-20 13:15:23', 871, TRUE, 'Vivas5'),
(NULL, '2016-05-20 01:07:00', 656, TRUE, 'Vivas5'),
(NULL, '2016-05-20 13:15:13', 768, TRUE, 'Awe'),
(NULL, '2016-08-20 14:09:51', 744, TRUE, 'Awe');