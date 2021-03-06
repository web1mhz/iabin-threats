-- /////////////temp_land_1A///////////////////
DROP TABLE IF EXISTS temp_land_1A;

create table temp_land_1A as
select o.id,o.nub_concept_id,
FLOOR((o.longitude+180)/(5/60)) as xcell, FLOOR((o.latitude+90)/(5/60)) as ycell,
o.longitude,o.latitude,
o.iso_country_code, o.year
from occurrence_record o
where  geospatial_issue=0 
and latitude is not null and longitude is not null;


alter table temp_land_1A add index(nub_concept_id,longitude, latitude);

-- /////////////////////////////////////////////


-- /////////////temp_land_2A///////////////////
DROP TABLE IF EXISTS temp_land_2A;

create table temp_land_2A as
select id, nub_concept_id, xcell, ycell, longitude, latitude, iso_country_code, year, count(*) count
from temp_land_1A
group by 2,3,4;

alter table temp_land_2A add index(nub_concept_id);

-- /////////////////////////////////////////////


-- /////////////temp_land_3A///////////////////
DROP TABLE IF EXISTS temp_land_3A;

create table temp_land_3A as 
select t2.nub_concept_id
from temp_land_2A t2 inner join taxon_concept tc on t2.nub_concept_id=tc.id
where tc.rank>=7000
group by 1;
alter table temp_land_3A add index(nub_concept_id);

-- /////////////////////////////////////////////


-- /////////////temp_land_4A/////////////////// 
DROP TABLE IF EXISTS temp_land_4A;

create table temp_land_4A as
select t2.id, t2.nub_concept_id, t2.longitude, t2.latitude, t2.iso_country_code, t2.year
from temp_land_2A t2 inner join temp_land_3A t3 on t2.nub_concept_id=t3.nub_concept_id;

alter table temp_land_4A add index(nub_concept_id);
alter table temp_land_4A add index(id);
ALTER TABLE temp_land_4A add is_land tinyint(1) default '1';

-- /////////////////////////////////////////////


-- /////////////temp_land_5A/////////////////// 
DROP TABLE IF EXISTS temp_land_5A;

CREATE TABLE `temp_land_5A` (
  `id` int(10) unsigned NOT NULL,
  `nub_concept_id` int(10) unsigned default NULL,
  `longitude` float default NULL,
  `latitude` float default NULL,
  `iso_country_code` char(2) character set utf8 default NULL,
   UNIQUE KEY `id` (`id`),
  KEY `nub_concept_id` (`nub_concept_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- /////////////////////////////////////////////