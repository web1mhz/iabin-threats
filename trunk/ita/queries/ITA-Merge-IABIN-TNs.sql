

drop table if EXISTS LAC_GBIF_occurrence_record;
create table LAC_GBIF_occurrence_record 
as (SELECT * from filtered_records t
where t.iso_country_code in (select l.iso_country_code from LAC_countries l));

drop table if EXISTS LAC_SSTN_occurrence_record;
create table LAC_SSTN_occurrence_record
as (SELECT * from SSTN_filtered_records t
where t.iso_country_code in (select l.iso_country_code from LAC_countries l));

drop table if EXISTS LAC_PTN_occurrence_record;
create table LAC_PTN_occurrence_record
as (SELECT * from PTN_filtered_records t
where t.iso_country_code in (select l.iso_country_code from LAC_countries l));

--unir los datos
drop table if exists LAC_occurrence_record;
create table LAC_occurrence_record
as (select id,
data_provider_id,
data_resource_id,
institution_code_id,
collection_code_id,
catalogue_number_id,
taxon_concept_id,
taxon_name_id,
kingdom_concept_id,
phylum_concept_id,
class_concept_id,
order_concept_id,
family_concept_id,
genus_concept_id,
species_concept_id,
nub_concept_id,
iso_country_code,
latitude,
longitude,
cell_id	centi_cell_id,
mod360_cell_id,
year,
month,
occurrence_date,
basis_of_record,
taxonomic_issue,
geospatial_issue,
other_issue,
deleted,
altitude_metres,
depth_centimetres,
modified,
'GBIF' as 'database'
 from LAC_GBIF_occurrence_record l);
 
insert into LAC_occurrence_record
SELECT  id,
data_provider_id,
data_resource_id,
institution_code_id,
collection_code_id,
catalogue_number_id,
taxon_concept_id,
taxon_name_id,
kingdom_concept_id,
phylum_concept_id,
class_concept_id,
order_concept_id,
family_concept_id,
genus_concept_id,
species_concept_id,
nub_concept_id,
iso_country_code,
latitude,
longitude,
cell_id	centi_cell_id,
mod360_cell_id,
year,
month,
occurrence_date,
basis_of_record,
taxonomic_issue,
geospatial_issue,
other_issue,
deleted,
altitude_metres,
depth_centimetres,
modified, 'SSTN' as 'database' 
from  LAC_SSTN_occurrence_record;

insert into LAC_occurrence_record
SELECT  id,
data_provider_id,
data_resource_id,
institution_code_id,
collection_code_id,
catalogue_number_id,
taxon_concept_id,
taxon_name_id,
kingdom_concept_id,
phylum_concept_id,
class_concept_id,
order_concept_id,
family_concept_id,
genus_concept_id,
species_concept_id,
nub_concept_id,
iso_country_code,
latitude,
longitude,
cell_id	centi_cell_id,
mod360_cell_id,
year,
month,
occurrence_date,
basis_of_record,
taxonomic_issue,
geospatial_issue,
other_issue,
deleted,
altitude_metres,
depth_centimetres,
modified, 'PTN' as 'database' 
from  LAC_PTN_occurrence_record;

-- adding taxa names
Alter table LAC_occurrence_record add kingdom_name varchar(255) default null;
Alter table LAC_occurrence_record add phylum_name varchar(255) default null;
Alter table LAC_occurrence_record add class_name varchar(255) default null;
Alter table LAC_occurrence_record add order_name varchar(255) default null;
Alter table LAC_occurrence_record add family_name varchar(255) default null;
Alter table LAC_occurrence_record add genus_name varchar(255) default null;
Alter table LAC_occurrence_record add species_name varchar(255) default null;
Alter table LAC_occurrence_record add nub_name varchar(255) default null;
Alter table LAC_occurrence_record add rank smallint(5) default null;
Alter table LAC_occurrence_record add collection_code varchar(255);
Alter table LAC_occurrence_record add institution_code varchar(255);
Alter table LAC_occurrence_record add country_name varchar(255);
Alter table LAC_occurrence_record add data_provider_name varchar(255);
Alter table LAC_occurrence_record add data_resource_name varchar(255);

-- gbif taxa names

update LAC_occurrence_record l set kingdom_name=(select tnk.canonical from taxon_name tnk, taxon_concept tck, taxon_concept tc
	where  l.taxon_concept_id=tc.id
    and tc.kingdom_concept_id=tck.id 
    and tnk.id=tck.taxon_name_id
    limit 1),
phylum_name=(select tnk.canonical from taxon_name tnk, taxon_concept tck, taxon_concept tc
	where  l.taxon_concept_id=tc.id
    and tc.phylum_concept_id=tck.id 
    and tnk.id=tck.taxon_name_id
    limit 1),
class_name=(select tnk.canonical from taxon_name tnk, taxon_concept tck, taxon_concept tc
	where  l.taxon_concept_id=tc.id
    and tc.class_concept_id=tck.id 
    and tnk.id=tck.taxon_name_id
    limit 1),
order_name=(select tnk.canonical from taxon_name tnk, taxon_concept tck, taxon_concept tc
	where  l.taxon_concept_id=tc.id
    and tc.order_concept_id=tck.id 
    and tnk.id=tck.taxon_name_id
    limit 1),
family_name=(select tnk.canonical from taxon_name tnk, taxon_concept tck, taxon_concept tc
	where  l.taxon_concept_id=tc.id
    and tc.family_concept_id=tck.id 
    and tnk.id=tck.taxon_name_id
    limit 1),
genus_name=(select tnk.canonical from taxon_name tnk, taxon_concept tck, taxon_concept tc
	where  l.taxon_concept_id=tc.id
    and tc.genus_concept_id=tck.id 
    and tnk.id=tck.taxon_name_id
    limit 1),
species_name=(select tnk.canonical from taxon_name tnk, taxon_concept tck, taxon_concept tc
	where  l.taxon_concept_id=tc.id
    and tc.species_concept_id=tck.id 
    and tnk.id=tck.taxon_name_id
    limit 1),
nub_name=(select tnk.canonical from taxon_name tnk, taxon_concept tck
	where  l.taxon_concept_id=tck.id
    and tnk.id=tck.taxon_name_id
    limit 1),
rank=(select rank from taxon_concept tck
	where  l.taxon_concept_id=tck.id
    limit 1),
collection_code=(select code from collection_code tck
	where  l.collection_code_id=tck.id
    limit 1),
institution_code=(select code from institution_code tck
	where  l.institution_code_id=tck.id
    limit 1),
country_name=(select name from country_name tck
	where  l.iso_country_code=tck.iso_country_code
    and tck.locale = 'en'
    limit 1),
data_provider_name=(select name from data_provider tck
	where  l.data_provider_id=tck.id
    limit 1),
data_resource_name=(select name from data_provider tck
	where  l.data_resource_id=tck.id
    limit 1)
where l.database='GBIF';




-- sstn taxa name

update LAC_occurrence_record l set kingdom_name=(select tnk.canonical from SSTN_taxon_name tnk, SSTN_taxon_concept tck, SSTN_taxon_concept tc
	where  l.taxon_concept_id=tc.id
    and tc.kingdom_concept_id=tck.id 
    and tnk.id=tck.taxon_name_id
    limit 1),
phylum_name=(select tnk.canonical from SSTN_taxon_name tnk, SSTN_taxon_concept tck, SSTN_taxon_concept tc
	where  l.taxon_concept_id=tc.id
    and tc.phylum_concept_id=tck.id 
    and tnk.id=tck.taxon_name_id
    limit 1),
class_name=(select tnk.canonical from SSTN_taxon_name tnk, SSTN_taxon_concept tck, SSTN_taxon_concept tc
	where  l.taxon_concept_id=tc.id
    and tc.class_concept_id=tck.id 
    and tnk.id=tck.taxon_name_id
    limit 1),
order_name=(select tnk.canonical from SSTN_taxon_name tnk, SSTN_taxon_concept tck, SSTN_taxon_concept tc
	where  l.taxon_concept_id=tc.id
    and tc.order_concept_id=tck.id 
    and tnk.id=tck.taxon_name_id
    limit 1),
family_name=(select tnk.canonical from SSTN_taxon_name tnk, SSTN_taxon_concept tck, SSTN_taxon_concept tc
	where  l.taxon_concept_id=tc.id
    and tc.family_concept_id=tck.id 
    and tnk.id=tck.taxon_name_id
    limit 1),
genus_name=(select tnk.canonical from SSTN_taxon_name tnk, SSTN_taxon_concept tck, SSTN_taxon_concept tc
	where  l.taxon_concept_id=tc.id
    and tc.genus_concept_id=tck.id 
    and tnk.id=tck.taxon_name_id
    limit 1),
species_name=(select tnk.canonical from SSTN_taxon_name tnk, SSTN_taxon_concept tck, SSTN_taxon_concept tc
	where  l.taxon_concept_id=tc.id
    and tc.species_concept_id=tck.id 
    and tnk.id=tck.taxon_name_id
    limit 1),
nub_name=(select tnk.canonical from SSTN_taxon_name tnk, SSTN_taxon_concept tck
	where  l.taxon_concept_id=tck.id
    and tnk.id=tck.taxon_name_id
    limit 1),
rank=(select rank from SSTN_taxon_concept tck
	where  l.taxon_concept_id=tck.id
    limit 1),
collection_code=(select code from SSTN_collection_code tck
	where  l.collection_code_id=tck.id
    limit 1),
institution_code=(select code from SSTN_institution_code tck
	where  l.institution_code_id=tck.id
    limit 1),
country_name=(select name from SSTN_country_name tck
	where  l.iso_country_code=tck.iso_country_code
    and tck.locale = 'en'
    limit 1),
data_provider_name=(select name from SSTN_data_provider tck
	where  l.data_provider_id=tck.id
    limit 1),
data_resource_name=(select name from SSTN_data_provider tck
	where  l.data_resource_id=tck.id
    limit 1)
where l.database='SSTN';

update LAC_occurrence_record l set kingdom_name=(select tnk.canonical from PTN_taxon_name tnk, PTN_taxon_concept tck, PTN_taxon_concept tc
	where  l.taxon_concept_id=tc.id
    and tc.kingdom_concept_id=tck.id 
    and tnk.id=tck.taxon_name_id
    limit 1),
phylum_name=(select tnk.canonical from PTN_taxon_name tnk, PTN_taxon_concept tck, PTN_taxon_concept tc
	where  l.taxon_concept_id=tc.id
    and tc.phylum_concept_id=tck.id 
    and tnk.id=tck.taxon_name_id
    limit 1),
class_name=(select tnk.canonical from PTN_taxon_name tnk, PTN_taxon_concept tck, PTN_taxon_concept tc
	where  l.taxon_concept_id=tc.id
    and tc.class_concept_id=tck.id 
    and tnk.id=tck.taxon_name_id
    limit 1),
order_name=(select tnk.canonical from PTN_taxon_name tnk, PTN_taxon_concept tck, PTN_taxon_concept tc
	where  l.taxon_concept_id=tc.id
    and tc.order_concept_id=tck.id 
    and tnk.id=tck.taxon_name_id
    limit 1),
family_name=(select tnk.canonical from PTN_taxon_name tnk, PTN_taxon_concept tck, PTN_taxon_concept tc
	where  l.taxon_concept_id=tc.id
    and tc.family_concept_id=tck.id 
    and tnk.id=tck.taxon_name_id
    limit 1),
genus_name=(select tnk.canonical from PTN_taxon_name tnk, PTN_taxon_concept tck, PTN_taxon_concept tc
	where  l.taxon_concept_id=tc.id
    and tc.genus_concept_id=tck.id 
    and tnk.id=tck.taxon_name_id
    limit 1),
species_name=(select tnk.canonical from PTN_taxon_name tnk, PTN_taxon_concept tck, PTN_taxon_concept tc
	where  l.taxon_concept_id=tc.id
    and tc.species_concept_id=tck.id 
    and tnk.id=tck.taxon_name_id
    limit 1),
nub_name=(select tnk.canonical from PTN_taxon_name tnk, PTN_taxon_concept tck
	where  l.taxon_concept_id=tck.id
    and tnk.id=tck.taxon_name_id
    limit 1),
rank=(select rank from PTN_taxon_concept tck
	where  l.taxon_concept_id=tck.id
    limit 1),
collection_code=(select code from PTN_collection_code tck
	where  l.collection_code_id=tck.id
    limit 1),
institution_code=(select code from PTN_institution_code tck
	where  l.institution_code_id=tck.id
    limit 1),
country_name=(select name from PTN_country_name tck
	where  l.iso_country_code=tck.iso_country_code
    and tck.locale = 'en'
    limit 1),
data_provider_name=(select name from PTN_data_provider tck
	where  l.data_provider_id=tck.id
    limit 1),
data_resource_name=(select name from PTN_data_provider tck
	where  l.data_resource_id=tck.id
    limit 1)
where l.database='PTN';



select count(*) from LAC_SSTN_occurrence_record;
-- 3352057
select count(*) from LAC_GBIF_occurrence_record;
-- 9586755
select count(*) from LAC_PTN_occurrence_record;
-- 234831

select count(*) from LAC_occurrence_record;
-- 12938812 GBIF SSTN
-- 13173643 GBIF SSTN PTN

select count(*) from LAC_occurrence_record l
where l.database='GBIF';
-- 9586755

select count(*) from LAC_occurrence_record l
where l.database='SSTN';
-- 3352057

select count(*) from LAC_occurrence_record l
where l.database='PTN';
-- 234831


drop table if exists LA_occurrence_record;
create table LA_occurrence_record as select * from
LAC_occurrence_record f
where f.iso_country_code in (select iso_country_code from LA_countries);


drop table if exists LAC_unique_occurrence_record;

create table LAC_unique_occurrence_record
as 
select * from LAC_occurrence_record l
group by l.nub_name,  l.latitude, l.longitude


create table LAC_GBIF_SSTN_occurrence_record
as 
select * from LAC_occurrence_record l
where (l.data_provider_id!=90 and l.database='GBIF') or l.database='SSTN';


