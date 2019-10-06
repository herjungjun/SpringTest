-- 댓글 테이블 생성
CREATE TABLE IF NOT EXISTS `spring8`.`free_board_reply` (
  `reply_id` INT NOT NULL AUTO_INCREMENT,
  `reply_text` VARCHAR(1000) NOT NULL,
  `reply_writer` VARCHAR(80) NOT NULL,
  `reg_date` TIMESTAMP NOT NULL DEFAULT NOW(),
  `update_date` TIMESTAMP NOT NULL DEFAULT NOW(),
  `board_id` INT(11) NOT NULL,
  PRIMARY KEY (`reply_id`),
  CONSTRAINT `fk_free_board_reply_free_board`
    FOREIGN KEY (`board_id`)
    REFERENCES `spring8`.`free_board` (`board_id`))
ENGINE = InnoDB;

-- 댓글 참조키 설정
ALTER TABLE free_board_reply ADD CONSTRAINT FK_BOARD 
FOREIGN KEY (board_id) REFERENCES free_board (board_id);






