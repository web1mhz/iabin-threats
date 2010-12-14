-- /////////////filtered_records///////////////////
DROP TABLE IF EXISTS filtered_records;

create table filtered_records as
select * from occurrence_record o
where o.id in
	(select id
	from temp_good_records
	where  outlier<=16);

alter table filtered_records add index(nub_concept_id);
alter table filtered_records add index(id);

-- /////////////////////////////////////////////