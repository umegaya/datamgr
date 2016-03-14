-- MySQL dump 10.11
--
-- Host: localhost    Database: eqdev_manager
-- ------------------------------------------------------
-- Server version	5.0.96-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


--
-- Table structure for table `managed_column`
--

DROP TABLE IF EXISTS `managed_column`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `managed_column` (
  `database` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `table` varchar(64) character set utf8 collate utf8_bin NOT NULL,
  `name` varchar(64) character set utf8 collate utf8_bin NOT NULL,
  `alias` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `ordinal` int(11) NOT NULL,
  `notes` text character set utf8 collate utf8_bin NOT NULL,
  `insert_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY  (`database`,`table`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `managed_database`
--

DROP TABLE IF EXISTS `managed_database`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `managed_database` (
  `name` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `alias` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `resource` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `notes` text character set utf8 collate utf8_bin NOT NULL,
  `insert_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY  (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `managed_database` WRITE;
/*!40000 ALTER TABLE `managed_database` DISABLE KEYS */;
INSERT INTO `managed_database` VALUES ('game','ゲーム','jdbc/game-readwrite','','2013-03-27 11:53:07','2013-03-30 20:26:23'),('manager','管理ツール','jdbc/manager','','2013-03-27 11:52:21','2013-03-27 11:52:21');
/*!40000 ALTER TABLE `managed_database` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `managed_table`
--

DROP TABLE IF EXISTS `managed_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `managed_table` (
  `database` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `name` varchar(64) character set utf8 collate utf8_bin NOT NULL,
  `alias` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `display_column` varchar(64) character set utf8 collate utf8_bin NOT NULL,
  `auto_random_column` varchar(64) character set utf8 collate utf8_bin NOT NULL,
  `renumbering_column` varchar(64) character set utf8 collate utf8_bin NOT NULL,
  `insert_time_column` varchar(64) character set utf8 collate utf8_bin NOT NULL,
  `update_time_column` varchar(64) character set utf8 collate utf8_bin NOT NULL,
  `notes` text character set utf8 collate utf8_bin NOT NULL,
  `insert_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY  (`database`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu` (
  `name` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `alias` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `path` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `notes` text character set utf8 collate utf8_bin NOT NULL,
  `insert_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY  (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES ('database','データベース管理','/console','','2013-03-26 13:51:39','2013-03-30 18:13:18');
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operator`
--

DROP TABLE IF EXISTS `operator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operator` (
  `name` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `password` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `published` tinyint(4) NOT NULL,
  `notes` text character set utf8 collate utf8_bin NOT NULL,
  `insert_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY  (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `operator` WRITE;
/*!40000 ALTER TABLE `operator` DISABLE KEYS */;
INSERT INTO `operator` VALUES ('root','63a9f0ea7bb98050796b649e85481845',1,'','2013-03-26 12:51:45','2013-04-02 19:02:36');
/*!40000 ALTER TABLE `operator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operator_column`
--

DROP TABLE IF EXISTS `operator_column`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operator_column` (
  `operator` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `database` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `table` varchar(64) character set utf8 collate utf8_bin NOT NULL,
  `column` varchar(64) character set utf8 collate utf8_bin NOT NULL,
  `filter_enabled` tinyint(4) NOT NULL,
  `filter_condition` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `sort_enabled` tinyint(4) NOT NULL,
  `sort_priority` int(11) NOT NULL,
  `sort_order` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `width` int(11) NOT NULL,
  `height` int(11) NOT NULL,
  `visible` tinyint(4) NOT NULL,
  `notes` text character set utf8 collate utf8_bin NOT NULL,
  `insert_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY  (`operator`,`database`,`table`,`column`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `operator_database`
--

DROP TABLE IF EXISTS `operator_database`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operator_database` (
  `operator` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `database` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `readable` tinyint(4) NOT NULL,
  `writable` tinyint(4) NOT NULL,
  `downloadable` tinyint(4) NOT NULL,
  `uploadable` tinyint(4) NOT NULL,
  `selected` tinyint(4) NOT NULL,
  `notes` text character set utf8 collate utf8_bin NOT NULL,
  `insert_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY  (`operator`,`database`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `operator_menu`
--

DROP TABLE IF EXISTS `operator_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operator_menu` (
  `operator` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `menu` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `readable` tinyint(4) NOT NULL,
  `selected` tinyint(4) NOT NULL,
  `notes` text character set utf8 collate utf8_bin NOT NULL,
  `insert_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY  (`operator`,`menu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `operator_menu` WRITE;
/*!40000 ALTER TABLE `operator_menu` DISABLE KEYS */;
INSERT INTO `operator_menu` VALUES ('root','database',1,0,'','2013-03-30 18:21:51','2013-09-04 02:49:19');
/*!40000 ALTER TABLE `operator_menu` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `operator_table`
--

DROP TABLE IF EXISTS `operator_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operator_table` (
  `operator` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `database` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `table` varchar(64) character set utf8 collate utf8_bin NOT NULL,
  `readable` tinyint(4) NOT NULL,
  `writable` tinyint(4) NOT NULL,
  `downloadable` tinyint(4) NOT NULL,
  `uploadable` tinyint(4) NOT NULL,
  `selected` tinyint(4) NOT NULL,
  `offset` int(11) NOT NULL,
  `limit` int(11) NOT NULL,
  `notes` text character set utf8 collate utf8_bin NOT NULL,
  `insert_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY  (`operator`,`database`,`table`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operator_table`
--

LOCK TABLES `operator_table` WRITE;
/*!40000 ALTER TABLE `operator_table` DISABLE KEYS */;
INSERT INTO `operator_table` VALUES ('root','game','',1,1,1,1,0,0,100,'','2013-04-16 18:45:44','2013-06-20 15:01:23'), ('root','manager','',1,1,1,1,0,0,100,'','2013-04-16 18:45:44','2013-06-20 15:01:23');
/*!40000 ALTER TABLE `operator_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `relation`
--

DROP TABLE IF EXISTS `relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `relation` (
  `database` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `parent_table` varchar(64) character set utf8 collate utf8_bin NOT NULL,
  `parent_column` varchar(64) character set utf8 collate utf8_bin NOT NULL,
  `child_table` varchar(64) character set utf8 collate utf8_bin NOT NULL,
  `child_column` varchar(64) character set utf8 collate utf8_bin NOT NULL,
  `notes` text character set utf8 collate utf8_bin NOT NULL,
  `insert_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY  (`database`,`parent_table`,`parent_column`,`child_table`,`child_column`),
  UNIQUE KEY `child` (`child_table`,`child_column`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `relation_rule`
--

DROP TABLE IF EXISTS `relation_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `relation_rule` (
  `pattern` varchar(255) character set utf8 collate utf8_bin NOT NULL,
  `column_name` varchar(255) character set utf8 collate utf8_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*!40101 SET character_set_client = @saved_cs_client */;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-09-13  4:07:17
