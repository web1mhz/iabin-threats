 DROP TABLE IF EXISTS I3N_BR_occurrence_record;

create table I3N_BR_occurrence_record as 
(select d.city_id as id,
d.species_id as nub_concept_id,
d.coord_utm_y as latitude,
d.coord_utm_x as longitude,
'BR' as iso_country_code
 from data_places d); 