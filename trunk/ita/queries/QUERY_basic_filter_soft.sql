-- /////////////temp_land_4A///////////////////
DROP TABLE IF EXISTS temp_land_4A;

create table temp_land_4A as
select id,nub_concept_id,longitude,latitude, iso_country_code
from occurrence_record
where  geospatial_issue=0 
and latitude is not null and longitude is not null;

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
  `iso_country_code` char(2) character set utf8 default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE temp_land_5A ADD PRIMARY KEY (id);

-- /////////////////////////////////////////////