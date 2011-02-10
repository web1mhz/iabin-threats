
# BRAZIL
mysql -p -u daniel iabin_i3n_bra_ene2011 -e 'DROP TABLE IF EXISTS I3N_BR_occurrence_record'
mysql -p -u daniel iabin_i3n_bra_ene2011 -e 'create table I3N_BR_occurrence_record as (select d.city_id as id,d.species_id as nub_concept_id,d.coord_utm_y as latitude,d.coord_utm_x as longitude,"BR" as iso_country_code, "Brazil" as country, d.state as state_province, d.municipio as county, d.location as locality  from data_places d);'
mysqldump -p -u daniel iabin_i3n_bra_ene2011 I3N_BR_occurrence_record > I3N_BR_occurrence_record.sql
mysql -p -u daniel gbif_sept2010 < I3N_BR_occurrence_record.sql
# COLOMBIA
mysql -p -u daniel iabin_i3n_col_ene2011 -e 'DROP TABLE IF EXISTS I3N_CO_occurrence_record'
mysql -p -u daniel iabin_i3n_col_ene2011 -e 'create table I3N_CO_occurrence_record as (select d.city_id as id,d.species_id as nub_concept_id,d.coord_utm_y as latitude,d.coord_utm_x as longitude,"CO" as iso_country_code, "Colombia" as country, d.state as state_province, d.municipio as county, d.location as locality from data_places d);'
mysqldump -p -u daniel iabin_i3n_col_ene2011 I3N_CO_occurrence_record > I3N_CO_occurrence_record.sql
mysql -p -u daniel gbif_sept2010 < I3N_CO_occurrence_record.sql
# CHILE
mysql -p -u daniel iabin_i3n_chl_ene2011 -e 'DROP TABLE IF EXISTS I3N_CL_occurrence_record'
mysql -p -u daniel iabin_i3n_chl_ene2011 -e 'create table I3N_CL_occurrence_record as (select d.city_id as id,d.species_id as nub_concept_id,d.coord_utm_y as latitude,d.coord_utm_x as longitude,"CL" as iso_country_code, "Chile" as country, d.state as state_province, d.municipio as county, d.location as locality  from data_places d);'
mysqldump -p -u daniel iabin_i3n_chl_ene2011 I3N_CL_occurrence_record > I3N_CL_occurrence_record.sql
mysql -p -u daniel gbif_sept2010 < I3N_CL_occurrence_record.sql
# EQUATOR
mysql -p -u daniel iabin_i3n_ecu_ene2011 -e 'DROP TABLE IF EXISTS I3N_EC_occurrence_record'
mysql -p -u daniel iabin_i3n_ecu_ene2011 -e 'create table I3N_EC_occurrence_record as (select d.city_id as id,d.species_id as nub_concept_id,d.coord_utm_y as latitude,d.coord_utm_x as longitude,"EC" as iso_country_code, "Equator" as country, d.state as state_province, d.municipio as county, d.location as locality  from data_places d);'
mysqldump -p -u daniel iabin_i3n_ecu_ene2011 I3N_EC_occurrence_record > I3N_EC_occurrence_record.sql
mysql -p -u daniel gbif_sept2010 < I3N_EC_occurrence_record.sql
# COSTA RICA
mysql -p -u daniel iabin_i3n_cri_ene2011 -e 'DROP TABLE IF EXISTS I3N_CR_occurrence_record'
mysql -p -u daniel iabin_i3n_cri_ene2011 -e 'create table I3N_CR_occurrence_record as (select d.city_id as id,d.species_id as nub_concept_id,d.coord_utm_y as latitude,d.coord_utm_x as longitude,"CR" as iso_country_code, "Costa Rica" as country, d.state as state_province, d.municipio as county, d.location as locality  from data_places d);'
mysqldump -p -u daniel iabin_i3n_cri_ene2011 I3N_CR_occurrence_record > I3N_CR_occurrence_record.sql
mysql -p -u daniel gbif_sept2010 < I3N_CR_occurrence_record.sql
# DOMINICAN REPUBLIC
mysql -p -u daniel iabin_i3n_dom_ene2011 -e 'DROP TABLE IF EXISTS I3N_DO_occurrence_record'
mysql -p -u daniel iabin_i3n_dom_ene2011 -e 'create table I3N_DO_occurrence_record as (select d.city_id as id,d.species_id as nub_concept_id,d.coord_utm_y as latitude,d.coord_utm_x as longitude,"DO" as iso_country_code, "Dominican Republic" as country, d.state as state_province, d.municipio as county, d.location as locality  from data_places d);'
mysqldump -p -u daniel iabin_i3n_dom_ene2011 I3N_DO_occurrence_record > I3N_DO_occurrence_record.sql
mysql -p -u daniel gbif_sept2010 < I3N_DO_occurrence_record.sql
# ARGENTINA
mysql -p -u daniel iabin_i3n_arg_ene2011 -e 'DROP TABLE IF EXISTS I3N_AR_occurrence_record'
mysql -p -u daniel iabin_i3n_arg_ene2011 -e 'create table I3N_AR_occurrence_record as (select d.city_id as id,d.species_id as nub_concept_id,d.coord_utm_y as latitude,d.coord_utm_x as longitude,"AR" as iso_country_code, "Argentina" as country, d.state as state_province, d.municipio as county, d.location as locality  from data_places d);'
mysqldump -p -u daniel iabin_i3n_arg_ene2011 I3N_AR_occurrence_record > I3N_AR_occurrence_record.sql
mysql -p -u daniel gbif_sept2010 < I3N_AR_occurrence_record.sql
# GUATEMALA
mysql -p -u daniel iabin_i3n_gtm_ene2011 -e 'DROP TABLE IF EXISTS I3N_GT_occurrence_record'
mysql -p -u daniel iabin_i3n_gtm_ene2011 -e 'create table I3N_GT_occurrence_record as (select d.city_id as id,d.species_id as nub_concept_id,d.coord_utm_y as latitude,d.coord_utm_x as longitude,"GT" as iso_country_code, "Guatemala" as country, d.state as state_province, d.municipio as county, d.location as locality  from data_places d);'
mysqldump -p -u daniel iabin_i3n_gtm_ene2011 I3N_GT_occurrence_record > I3N_GT_occurrence_record.sql
mysql -p -u daniel gbif_sept2010 < I3N_GT_occurrence_record.sql
