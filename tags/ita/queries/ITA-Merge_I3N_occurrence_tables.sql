DROP TABLE IF EXISTS I3N_occurrence_record;
 
 create table I3N_occurrence_record as 
 (SELECT id+11100000 as id ,nub_concept_id, latitude as I3N_latitude, longitude as I3N_longitude, iso_country_code
   from I3N_BR_occurrence_record);
 
  insert into I3N_occurrence_record
SELECT id+22200000,nub_concept_id, latitude, longitude, iso_country_code
   from  I3N_AR_occurrence_record;
 
  insert into I3N_occurrence_record
SELECT id+33300000,nub_concept_id, latitude, longitude, iso_country_code
   from  I3N_CL_occurrence_record;
 
 insert into I3N_occurrence_record
SELECT id+44400000,nub_concept_id, latitude, longitude, iso_country_code
   from  I3N_CO_occurrence_record;
 
  insert into I3N_occurrence_record
SELECT id+55500000,nub_concept_id, latitude, longitude, iso_country_code
   from  I3N_CR_occurrence_record;
 
  insert into I3N_occurrence_record
SELECT id+66600000,nub_concept_id, latitude, longitude, iso_country_code
   from  I3N_DO_occurrence_record;
 
  insert into I3N_occurrence_record
SELECT id+77700000,nub_concept_id, latitude, longitude, iso_country_code
   from  I3N_EC_occurrence_record;
 
  insert into I3N_occurrence_record
SELECT id+88800000,nub_concept_id, latitude, longitude, iso_country_code
   from  I3N_GT_occurrence_record;
   
ALTER TABLE I3N_occurrence_record add latitude float(12) default null;
ALTER TABLE I3N_occurrence_record add longitude float(12) default null;
