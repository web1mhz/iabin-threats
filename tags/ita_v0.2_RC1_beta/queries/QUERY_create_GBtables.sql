DROP TABLE IF EXISTS temp_good_records;

CREATE TABLE `temp_good_records` (
  `id` int(10) NOT NULL,
  `nub_concept_id` int(10) default NULL,
  `longitude` float(12,3) default NULL,
  `latitude` float(12,3) default NULL,
  `iso_country_code` char(2) default NULL,
  `canonical` varchar(255) default NULL,
  `bio_1` smallint(6) default '-9999',
  `bio_2` smallint(6) default '-9999',
  `bio_3` smallint(6) default '-9999',
  `bio_4` smallint(6) default '-9999',
  `bio_5` smallint(6) default '-9999',
  `bio_6` smallint(6) default '-9999',
  `bio_7` smallint(6) default '-9999',
  `bio_8` smallint(6) default '-9999',
  `bio_9` smallint(6) default '-9999',
  `bio_10` smallint(6) default '-9999',
  `bio_11` smallint(6) default '-9999',
  `bio_12` smallint(6) default '-9999',
  `bio_13` smallint(6) default '-9999',
  `bio_14` smallint(6) default '-9999',
  `bio_15` smallint(6) default '-9999',
  `bio_16` smallint(6) default '-9999',
  `bio_17` smallint(6) default '-9999',
  `bio_18` smallint(6) default '-9999',
  `bio_19` smallint(6) default '-9999',
  `alt` smallint(6) default '-9999',
  `outlier` tinyint(4) default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `nub_canonical` (`nub_concept_id`,`canonical`),
  KEY `nub_concept_id` (`nub_concept_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS temp_bad_records;

CREATE TABLE `temp_bad_records` (
  `id` int(10) NOT NULL,
  `nub_concept_id` int(10) default NULL,
  `longitude` float(12,3) default NULL,
  `latitude` float(12,3) default NULL,
  `iso_country_code` char(2) default NULL,
  `canonical` varchar(255) default NULL,
  `error` char(2) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `nub_canonical` (`nub_concept_id`,`canonical`),
  KEY `nub_concept_id` (`nub_concept_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;