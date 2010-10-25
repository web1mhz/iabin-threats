-- /////////////temp_good_1A///////////////////
-- filter record with outliers equal or bigger than 16
DROP TABLE IF EXISTS temp_good_1A;

create table temp_good_1A as
select id,nub_concept_id,longitude,latitude, iso_country_code, canonical,
bio_1,bio_2,bio_3,bio_4,bio_5,bio_6,bio_7,bio_8,bio_9,bio_10,bio_11,bio_12,bio_13,bio_14,bio_15,bio_16,bio_17,bio_18,bio_19,alt 
from temp_good_records
where  outlier<=16;

alter table temp_good_1A add index(nub_concept_id,longitude, latitude);

-- /////////////////////////////////////////////


-- /////////////temp_good_2A///////////////////
DROP TABLE IF EXISTS temp_good_2A;

create table temp_good_2A as
select id, nub_concept_id, longitude, latitude, iso_country_code, canonical, count(*) as count,
bio_1,bio_2,bio_3,bio_4,bio_5,bio_6,bio_7,bio_8,bio_9,bio_10,bio_11,bio_12,bio_13,bio_14,bio_15,bio_16,bio_17,bio_18,bio_19,alt 
from temp_good_1A
group by 2,3,4,5;

alter table temp_good_2A add index(nub_concept_id);

-- /////////////////////////////////////////////


-- /////////////temp_good_3A///////////////////
DROP TABLE IF EXISTS temp_good_3A;

create table temp_good_3A as 
select t2.nub_concept_id, count(*) as num_sites
from temp_good_2A t2 inner join taxon_concept tc on t2.nub_concept_id=tc.id
where tc.rank>=7000
group by 1;
alter table temp_good_3A add index(num_sites);
alter table temp_good_3A add index(nub_concept_id);

--/////////////////////////////////////////////


-- /////////////filtered_records/////////////////// 
DROP TABLE IF EXISTS filtered_records;

create table filtered_records as
select t2.id, t2.nub_concept_id, t2.longitude, t2.latitude, t2.iso_country_code, t2.canonical,
bio_1,bio_2,bio_3,bio_4,bio_5,bio_6,bio_7,bio_8,bio_9,bio_10,bio_11,bio_12,bio_13,bio_14,bio_15,bio_16,bio_17,bio_18,bio_19,alt 
from temp_good_2A t2 inner join temp_good_3A t3 on t2.nub_concept_id=t3.nub_concept_id 
where t3.num_sites>10;

alter table filtered_records add index(nub_concept_id);
alter table filtered_records add index(id);

-- /////////////////////////////////////////////
 
