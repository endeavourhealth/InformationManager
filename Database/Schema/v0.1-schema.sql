CREATE DATABASE  IF NOT EXISTS `information_model`;

USE `information_model`;

DROP TABLE IF EXISTS `view`;
DROP TABLE IF EXISTS `concept_attribute`;
DROP TABLE IF EXISTS `concept_relationship`;
DROP TABLE IF EXISTS `concept`;
DROP TABLE IF EXISTS `table_identity`;


CREATE TABLE `table_identity` (
  `table_name` VARCHAR(50) NOT NULL COMMENT 'Name of the table',
  `next_id` INT NOT NULL DEFAULT 0 COMMENT 'Next available identifier',
  PRIMARY KEY (`table_name`)
);

CREATE TABLE `concept` (
  `id` int(4) NOT NULL COMMENT 'Concept Id',
  `name` varchar(250) DEFAULT NULL COMMENT 'Name for the concept in common parlance',
  `context_name` VARCHAR(250) DEFAULT NULL COMMENT 'Unique context-based name',
  `short_name` varchar(125) DEFAULT NULL COMMENT 'Familiar name for lists or when context is known',
  `class` int(4) DEFAULT NULL COMMENT 'Class to which the concept belongs',
  `description` varchar(10000) DEFAULT NULL COMMENT 'Concept narrative',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '0 - Draft (default), 1 - Active, 2 - Deprecated',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `concept_relationship` (
  `source_concept` int(4) NOT NULL COMMENT 'The Source concept in concept table',
  `target_concept` int(4) NOT NULL COMMENT 'The Target concept in concept table',
  `relationship_type` int(4) DEFAULT NULL COMMENT 'Concept identifier of the relationship e.g “has subtype”',
  `display_order` int(4) DEFAULT NULL COMMENT 'In the context of the expression, the order in which the relationships are presented',
  `cardinality` int(4) NOT NULL DEFAULT 1 COMMENT '1 - Single (default), 0 - Unlimited, n - Maximum',
  KEY `source_concept_idx` (`source_concept`),
  KEY `target_concept_idx` (`target_concept`),
  KEY `source_relationship_type_idx` (`source_concept`, `relationship_type`),
  CONSTRAINT `concept_relationship_relationship_fk` FOREIGN KEY (`relationship_type`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `concept_relationship_source_concept_fk` FOREIGN KEY (`source_concept`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `concept_relationship_target_concept_fk` FOREIGN KEY (`target_concept`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `concept_attribute` (
  `source_concept` int(4) NOT NULL COMMENT 'The Source concept in the concept table',
  `attribute_concept` int(4) NOT NULL COMMENT 'The Attribute concept in the concept table',
  `value_concept` int(4) NOT NULL COMMENT 'The Attributes value concept in the concept table',
  KEY `source_concept_idx` (`source_concept`),
  KEY `source_attribute_concept_idx` (`source_concept`, `attribute_concept`),
  CONSTRAINT `concept_attribute_source_concept_fk` FOREIGN KEY (`source_concept`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `concept_attribute_attribute_concept_fk` FOREIGN KEY (`attribute_concept`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `concept_attribute_value_concept_fk` FOREIGN KEY (`value_concept`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `view` (
  `id` int(4) NOT NULL COMMENT 'The id of the View concept',
  `parent_concept` int(4) NOT NULL COMMENT 'The parent concept in the concept table',
  `relationship_concept` int(4) NOT NULL COMMENT 'The relationship between the parent and child',
  `child_concept` int(4) NOT NULL COMMENT 'The child concept in the concept table',
  `display_order` int(4) NOT NULL COMMENT 'The order in which to display',
  KEY `id` (`id`),
  KEY `parent_concept_idx` (`parent_concept`),
  KEY `parent_relationship_idx` (`parent_concept`, `relationship_concept`),
  KEY `child_concept_idx` (`child_concept`),
  CONSTRAINT `view_parent_concept_fk` FOREIGN KEY (`parent_concept`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `view_relationship_concept_fk` FOREIGN KEY (`relationship_concept`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `view_child_concept_fk` FOREIGN KEY (`child_concept`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
