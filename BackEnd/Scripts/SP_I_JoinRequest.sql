CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_I_JoinRequest`(
   IN p_joinReqId VARCHAR(255),
    IN p_pasName VARCHAR(255),
    IN p_desplace_id VARCHAR(255),
    IN p_startLon FLOAT,
    IN p_startLat FLOAT,
    IN p_destLon FLOAT,
    IN p_destLat FLOAT
)
BEGIN
DECLARE new_id VARCHAR(255);

set new_id = (select next_val from joinreq_sequence);
SET SQL_SAFE_UPDATES = 0;
update joinreq_sequence set next_val = next_val + 1  where next_val = new_id;
SET SQL_SAFE_UPDATES = 1;
      
    -- SET new_id = (SELECT next_val from passenger_Sequence);
INSERT INTO tbl_Join_Request (id,join_req_id, pas_name, desplace_id, start_lon, start_lat, dest_lon, dest_lat)
    VALUES (new_id,p_joinReqId, p_pasName, p_desplace_id, p_startLon, p_startLat, p_destLon, p_destLat);

END