-- /////////////filtered_records///////////////////
-- filter record with outliers equal or bigger than 16
DROP TABLE IF EXISTS filtered_records;

create table filtered_records as
select id,nub_concept_id,longitude,latitude, iso_country_code, canonical,
bio_1,bio_2,bio_3,bio_4,bio_5,bio_6,bio_7,bio_8,bio_9,bio_10,bio_11,bio_12,bio_13,bio_14,bio_15,bio_16,bio_17,bio_18,bio_19,alt 
from temp_good_records
where  outlier<=16;


alter table filtered_records add index(nub_concept_id);
alter table filtered_records add index(id);

-- /////////////////////////////////////////////
 
