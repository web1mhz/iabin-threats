-- SQL scripts to extract data for johannes


-- Kingdom:Plantae
select 'specie_id', 'specie','genus_id','genus','family_id','family', 'lat', 'lon', 'database'
union
(select f.nub_concept_id, f.nub_name, f.genus_concept_id, f.genus_name,
 f.family_concept_id, f.family_name, f.latitude, f.longitude, f.database
into outfile "/data/IABIN/Resources/LA/LA_filtered_Plantae.csv"
FIELDS TERMINATED BY ','
from LAC_occurrence_record f
where f.kingdom_name='Plantae'
and f.rank=7000
and f.iso_country_code in (select iso_country_code from LA_countries)
group by f.nub_concept_id,f.latitude, f.longitude);

-- Kingdom:Animalia Phylum:Chordata Class:Aves
select 'specie_id', 'specie','genus_id','genus','family_id','family', 'lat', 'lon', 'database'
union
(select f.nub_concept_id, f.nub_name, f.genus_concept_id, f.genus_name,
 f.family_concept_id, f.family_name, f.latitude, f.longitude, f.database
into outfile "/data/IABIN/Resources/LA/LA_filtered_Aves.csv"
FIELDS TERMINATED BY ','
from LAC_occurrence_record f
where f.class_name='Aves'
and f.rank=7000
and f.iso_country_code in (select iso_country_code from LA_countries)
group by f.nub_concept_id,f.latitude, f.longitude);


-- Kingdom:Animalia Phylum:Chordata Class:Reptilia
select 'specie_id', 'specie','genus_id','genus','family_id','family', 'lat', 'lon', 'database'
union
(select f.nub_concept_id, f.nub_name, f.genus_concept_id, f.genus_name,
 f.family_concept_id, f.family_name, f.latitude, f.longitude, f.database
into outfile "/data/IABIN/Resources/LA/LA_filtered_Reptilia.csv"
FIELDS TERMINATED BY ','
from LAC_occurrence_record f
where f.class_name='Reptilia'
and f.rank=7000
and f.iso_country_code in (select iso_country_code from LA_countries)
group by f.nub_concept_id,f.latitude, f.longitude);

-- Kingdom:Animalia Phylum:Chordata Class:Mammalia
select 'specie_id', 'specie','genus_id','genus','family_id','family', 'lat', 'lon', 'database'
union
(select f.nub_concept_id, f.nub_name, f.genus_concept_id, f.genus_name,
 f.family_concept_id, f.family_name, f.latitude, f.longitude, f.database
into outfile "/data/IABIN/Resources/LA/LA_filtered_Mammalia.csv"
FIELDS TERMINATED BY ','
from LAC_occurrence_record f
where f.class_name='Mammalia'
and f.rank=7000
and f.iso_country_code in (select iso_country_code from LA_countries)
group by f.nub_concept_id,f.latitude, f.longitude);


-- Kingdom:Animalia Phylum:Chordata Class:Amphibia
select 'specie_id', 'specie','genus_id','genus','family_id','family', 'lat', 'lon', 'database'
union
(select f.nub_concept_id, f.nub_name, f.genus_concept_id, f.genus_name,
 f.family_concept_id, f.family_name, f.latitude, f.longitude, f.database
into outfile "/data/IABIN/Resources/LA/LA_filtered_Amphibia.csv"
FIELDS TERMINATED BY ','
from LAC_occurrence_record f
where f.class_name='Amphibia'
and f.rank=7000
and f.iso_country_code in (select iso_country_code from LA_countries)
group by f.nub_concept_id,f.latitude, f.longitude);



-- Kingdom:Animalia Phylum:Arthropoda Class:Insecta
select 'specie_id', 'specie','genus_id','genus','family_id','family', 'lat', 'lon', 'database'
union
(select f.nub_concept_id, f.nub_name, f.genus_concept_id, f.genus_name,
 f.family_concept_id, f.family_name, f.latitude, f.longitude, f.database
into outfile "/data/IABIN/Resources/LA/LA_filtered_Insecta.csv"
FIELDS TERMINATED BY ','
from LAC_occurrence_record f
where f.class_name='Insecta'
and f.rank=7000
and f.iso_country_code in (select iso_country_code from LA_countries)
group by f.nub_concept_id,f.latitude, f.longitude);