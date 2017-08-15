CREATE DATABASE  IF NOT EXISTS `information_model`
USE `information_model`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: information_model
-- ------------------------------------------------------
-- Server version	5.7.18-log

--
-- Table structure for table `concept`
--

DROP TABLE IF EXISTS `concept`;
CREATE TABLE `concept` (
  `id` int(4) NOT NULL,
  `name` varchar(250) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `class` int(4) DEFAULT NULL,
  `short_name` varchar(125) DEFAULT NULL,
  `description` varchar(10000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `concept_actual_range`
--

DROP TABLE IF EXISTS `concept_actual_range`;
CREATE TABLE `concept_actual_range` (
  `id` int(4) NOT NULL,
  `field` int(4) NOT NULL,
  `range qualifier` int(4) DEFAULT NULL,
  `lower_limit` float DEFAULT NULL,
  `range operator` varchar(2) DEFAULT NULL,
  `upper_limit` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `field_actual_range_field_idx` (`field`),
  KEY `field_actual_range_qualifier_idx` (`range qualifier`),
  CONSTRAINT `field_actual_range_field` FOREIGN KEY (`field`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `field_actual_range_qualifier` FOREIGN KEY (`range qualifier`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `concept_code_scheme`
--

DROP TABLE IF EXISTS `concept_code_scheme`;
CREATE TABLE `concept_code_scheme` (
  `id` int(11) NOT NULL,
  `concept` int(4) DEFAULT NULL,
  `code` varchar(20) DEFAULT NULL,
  `scheme` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `code_scheme_concept_idx` (`concept`),
  CONSTRAINT `code_scheme_concept` FOREIGN KEY (`concept`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `concept_expression`
--

DROP TABLE IF EXISTS `concept_expression`;
CREATE TABLE `concept_expression` (
  `id` int(4) NOT NULL,
  `concept` int(4) DEFAULT NULL,
  `attribute` int(4) DEFAULT NULL,
  `value_concept` int(4) DEFAULT NULL,
  `order` int(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `concept_idx` (`concept`),
  KEY `attribute_idx` (`attribute`),
  KEY `value_concept_idx` (`value_concept`),
  CONSTRAINT `attribute` FOREIGN KEY (`attribute`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `concept` FOREIGN KEY (`concept`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `value_concept` FOREIGN KEY (`value_concept`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `concept_range`
--

DROP TABLE IF EXISTS `concept_range`;
CREATE TABLE `concept_range` (
  `id` int(4) NOT NULL,
  `concept` int(4) DEFAULT NULL,
  `range_qualifier` int(4) DEFAULT NULL,
  `operator` varchar(2) DEFAULT NULL,
  `lower_limit` float DEFAULT NULL,
  `upper_limit` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `attribute_range_concept_idx` (`concept`),
  KEY `attribute_range_qualifier_idx` (`range_qualifier`),
  CONSTRAINT `attribute_range_concept` FOREIGN KEY (`concept`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `attribute_range_qualifier` FOREIGN KEY (`range_qualifier`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `concept_relationship`
--

DROP TABLE IF EXISTS `concept_relationship`;
CREATE TABLE `concept_relationship` (
  `id` int(4) NOT NULL COMMENT 'Relationship Id',
  `source_concept` int(4) NOT NULL COMMENT 'The Source concept in concept table',
  `relationship_type` int(4) DEFAULT NULL COMMENT 'Concept identifier of the relationship e.g “has subtype”',
  `target_concept` int(4) NOT NULL COMMENT 'The Target concept in concept table',
  `target_label` varchar(125) DEFAULT NULL COMMENT 'An abbreviated version of the target concept name (may be the same as the short name or even shorter)',
  `relationship_order` int(4) DEFAULT NULL COMMENT 'In the context of the expression, the order in which the relationships are presented',
  PRIMARY KEY (`id`),
  KEY `concept_fk_idx` (`source_concept`,`target_concept`),
  KEY `target_concept_fk` (`target_concept`),
  KEY `relationship_fk_idx` (`relationship_type`),
  CONSTRAINT `relationship_fk` FOREIGN KEY (`relationship_type`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `source_concept_fk` FOREIGN KEY (`source_concept`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `target_concept_fk` FOREIGN KEY (`target_concept`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `field_actual_value`
--

DROP TABLE IF EXISTS `field_actual_value`;
CREATE TABLE `field_actual_value` (
  `id` int(4) NOT NULL,
  `field` int(4) DEFAULT NULL,
  `value` int(4) DEFAULT NULL,
  `weighting` int(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `field_actual_value_field_idx` (`field`),
  KEY `field_actual_value_concept_idx` (`value`),
  CONSTRAINT `field_actual_value_concept` FOREIGN KEY (`value`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `field_actual_value_field` FOREIGN KEY (`field`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `information_model`.`table_identity` (
  `table_name` VARCHAR(50) NOT NULL,
  `next_id` INT NOT NULL,
  PRIMARY KEY (`table_name`)
);

CREATE TABLE IF NOT EXISTS `information_model`.`snomed_concept_map` (
  `snomed_id` BIGINT NOT NULL,
  `concept_id` INT NOT NULL,
  PRIMARY KEY (`snomed_id`),
  index(`snomed_id`)
);