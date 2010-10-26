-- /////////////temp_land_1A///////////////////
DROP TABLE IF EXISTS temp_land_1A;

create table temp_land_1A as
select id,nub_concept_id,longitude,latitude, iso_country_code
from occurrence_record
where  geospatial_issue=0 
and basis_of_record=2
and latitude is not null and longitude is not null;

alter table temp_land_1A add index(nub_concept_id,longitude, latitude);

-- /////////////////////////////////////////////


-- /////////////temp_land_2A///////////////////
DROP TABLE IF EXISTS temp_land_2A;

create table temp_land_2A as
select id, nub_concept_id, longitude, latitude, iso_country_code, count(*) as count
from temp_land_1A
group by 2,3,4;

alter table temp_land_2A add index(nub_concept_id);

-- /////////////////////////////////////////////


-- /////////////temp_land_3A///////////////////
DROP TABLE IF EXISTS temp_land_3A;

create table temp_land_3A as 
select t2.nub_concept_id, count(*) as num_sites
from temp_land_2A t2 inner join taxon_concept tc on t2.nub_concept_id=tc.id
where tc.rank>=7000
group by 1;
alter table temp_land_3A add index(num_sites);
alter table temp_land_3A add index(nub_concept_id);

-- /////////////////////////////////////////////


-- /////////////temp_land_4A/////////////////// 
DROP TABLE IF EXISTS temp_land_4A;

create table temp_land_4A as
select t2.id, t2.nub_concept_id, t2.longitude, t2.latitude, t2.iso_country_code
from temp_land_2A t2 inner join temp_land_3A t3 on t2.nub_concept_id=t3.nub_concept_id 
where t3.num_sites>10;

alter table temp_land_4A add index(nub_concept_id);
ALTER TABLE temp_land_4A add is_land tinyint(1) default '1';
ALTER TABLE temp_land_4A ADD PRIMARY KEY (id);

-- /////////////////////////////////////////////


-- /////////////temp_land_5A/////////////////// 
DROP TABLE IF EXISTS temp_land_5A;

CREATE TABLE `temp_land_5A` (
  `id` int(10) unsigned NOT NULL,
  `nub_concept_id` int(10) unsigned default NULL,
  `longitude` float default NULL,
  `latitude` float default NULL,
  `iso_country_code` char(2) character set utf8 default NULL,
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE temp_land_5A ADD PRIMARY KEY (id);

-- /////////////////////////////////////////////