create table georeferenced_records as
select o.id, o.iso_country_code, r.country,
r.state_province, r.county, r.locality,
r.latitude, r.longitude
from occurrence_record o, raw_occurrence_record r
where o.id=r.id
and (( o.latitude is null
		or o.longitude is null
        or o.geospatial_issue!=0
) or (o.id in (select bad.id from temp_bad_records bad)));

ALTER TABLE georeferenced_records add blatitude float default NULL;
ALTER TABLE georeferenced_records add blongitude float default NULL;
ALTER TABLE georeferenced_records add uncertainty int(10) default '-1';
ALTER TABLE georeferenced_records add is_fixed tinyint(1) default '0';
alter table georeferenced_records add index(id);



create table temp_georeferenced_records as
select o.id, o.iso_country_code, r.country,
r.state_province, r.county, r.locality,
r.latitude, r.longitude
from occurrence_record o, raw_occurrence_record r
where o.id=r.id
and o.id in (select good.id from temp_good_records good);

ALTER TABLE temp_georeferenced_records add blatitude float default NULL;
ALTER TABLE temp_georeferenced_records add blongitude float default NULL;
ALTER TABLE temp_georeferenced_records add uncertainty int(10) default '-1';
ALTER TABLE temp_georeferenced_records add is_fixed tinyint(1) default '0';
alter table temp_georeferenced_records add index(id);


#estos updates modifican el nombre del pais y ponen el equivalente al nombre del iso country en ingles
update temp_georeferenced_records o
set o.country=(select n.name from country_name n where n.iso_country_code=o.iso_country_code and n.locale='en' limit 1);

update georeferenced_records o
set o.country=(select n.name from country_name n where n.iso_country_code=o.iso_country_code and n.locale='en' limit 1);
