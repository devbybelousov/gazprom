-- MySQL dump 10.13  Distrib 5.5.62, for Win64 (AMD64)
--
-- Host: localhost    Database: gazprom_data
-- ------------------------------------------------------
-- Server version	5.7.29-log

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
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `department` (
  `department_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `unit_id` int(11) NOT NULL,
  PRIMARY KEY (`department_id`),
  KEY `department_unit_unit_id_fk` (`unit_id`),
  CONSTRAINT `department_unit_unit_id_fk` FOREIGN KEY (`unit_id`) REFERENCES `unit` (`unit_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (1,'Подразделение 1',1),(2,'Подразделение 2',2),(3,'Подразделение 3',3),(4,'Подразделение 4',4),(5,'Подразделение 5',5);
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (1);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `history`
--

DROP TABLE IF EXISTS `history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `reason` varchar(200) DEFAULT NULL,
  `request_id` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `status` varchar(15) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `history_request_request_id_fk` (`request_id`),
  KEY `FKq4kh99ws9lhtls5i3o73gw30t` (`user_id`),
  CONSTRAINT `FKq4kh99ws9lhtls5i3o73gw30t` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `history_request_request_id_fk` FOREIGN KEY (`request_id`) REFERENCES `request` (`request_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `history`
--

LOCK TABLES `history` WRITE;
/*!40000 ALTER TABLE `history` DISABLE KEYS */;
/*!40000 ALTER TABLE `history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `information_system`
--

DROP TABLE IF EXISTS `information_system`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `information_system` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) DEFAULT NULL,
  `owner_id` int(11) NOT NULL,
  `primary_admin_id` int(11) NOT NULL,
  `backup_admin_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `information_system_users_user_id_fk` (`owner_id`),
  KEY `information_system_users_user_id_fk_2` (`primary_admin_id`),
  KEY `information_system_users_user_id_fk_3` (`backup_admin_id`),
  CONSTRAINT `information_system_users_user_id_fk` FOREIGN KEY (`owner_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `information_system_users_user_id_fk_2` FOREIGN KEY (`primary_admin_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `information_system_users_user_id_fk_3` FOREIGN KEY (`backup_admin_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `information_system`
--

LOCK TABLES `information_system` WRITE;
/*!40000 ALTER TABLE `information_system` DISABLE KEYS */;
INSERT INTO `information_system` VALUES (1,'Система 1',1,1,1),(2,'Система 2',1,1,1),(3,'Система 3',1,1,1),(4,'Система 4',1,1,1);
/*!40000 ALTER TABLE `information_system` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `privilege`
--

DROP TABLE IF EXISTS `privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `privilege` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `description` varchar(250) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `privilege`
--

LOCK TABLES `privilege` WRITE;
/*!40000 ALTER TABLE `privilege` DISABLE KEYS */;
INSERT INTO `privilege` VALUES (1,'Роль 1','аавыа'),(2,'Роль 2','пава'),(3,'Роль 3','пвапав');
/*!40000 ALTER TABLE `privilege` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `request`
--

DROP TABLE IF EXISTS `request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `request` (
  `request_id` int(11) NOT NULL AUTO_INCREMENT,
  `system_id` int(11) NOT NULL,
  `filing_date` datetime NOT NULL,
  `expiry_date` datetime DEFAULT NULL,
  `status` varchar(25) NOT NULL,
  PRIMARY KEY (`request_id`),
  KEY `FK7vrq809dxla5762q0jw6qxlmx` (`system_id`),
  CONSTRAINT `FK7vrq809dxla5762q0jw6qxlmx` FOREIGN KEY (`system_id`) REFERENCES `information_system` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `request`
--

LOCK TABLES `request` WRITE;
/*!40000 ALTER TABLE `request` DISABLE KEYS */;
INSERT INTO `request` VALUES (1,1,'2020-12-14 15:36:56','2022-12-14 15:37:18','STATUS_ENABLE'),(2,2,'2020-12-14 15:37:10','2021-12-14 15:37:28','STATUS_ENABLE'),(3,3,'2020-12-14 15:37:14','2020-12-17 15:37:31','STATUS_ENABLE'),(4,4,'2020-12-14 15:37:16','2020-12-28 15:37:39','STATUS_ENABLE');
/*!40000 ALTER TABLE `request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `request_privilege`
--

DROP TABLE IF EXISTS `request_privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `request_privilege` (
  `request_id` int(11) NOT NULL,
  `privilege_id` int(11) NOT NULL,
  KEY `FKk7y26umia8fr8jseepfrxmfdh` (`privilege_id`),
  KEY `FKkyot1atrxqrt9mshnf3s2rxhn` (`request_id`),
  CONSTRAINT `FKk7y26umia8fr8jseepfrxmfdh` FOREIGN KEY (`privilege_id`) REFERENCES `privilege` (`id`),
  CONSTRAINT `FKkyot1atrxqrt9mshnf3s2rxhn` FOREIGN KEY (`request_id`) REFERENCES `request` (`request_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `request_privilege`
--

LOCK TABLES `request_privilege` WRITE;
/*!40000 ALTER TABLE `request_privilege` DISABLE KEYS */;
/*!40000 ALTER TABLE `request_privilege` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `request_user`
--

DROP TABLE IF EXISTS `request_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `request_user` (
  `request_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  KEY `request_user_request_request_id_fk` (`request_id`),
  KEY `request_user_users_user_id_fk` (`user_id`),
  CONSTRAINT `request_user_request_request_id_fk` FOREIGN KEY (`request_id`) REFERENCES `request` (`request_id`),
  CONSTRAINT `request_user_users_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `request_user`
--

LOCK TABLES `request_user` WRITE;
/*!40000 ALTER TABLE `request_user` DISABLE KEYS */;
INSERT INTO `request_user` VALUES (1,1),(2,1),(3,1),(4,1);
/*!40000 ALTER TABLE `request_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ROLE_USER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_privilege`
--

DROP TABLE IF EXISTS `system_privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `system_privilege` (
  `system_id` int(11) NOT NULL,
  `privilege_id` int(11) DEFAULT NULL,
  KEY `system_privilege_information_system_id_fk` (`system_id`),
  KEY `system_privilege_privilege_id_fk` (`privilege_id`),
  CONSTRAINT `system_privilege_information_system_id_fk` FOREIGN KEY (`system_id`) REFERENCES `information_system` (`id`),
  CONSTRAINT `system_privilege_privilege_id_fk` FOREIGN KEY (`privilege_id`) REFERENCES `privilege` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_privilege`
--

LOCK TABLES `system_privilege` WRITE;
/*!40000 ALTER TABLE `system_privilege` DISABLE KEYS */;
INSERT INTO `system_privilege` VALUES (1,3),(2,2),(3,1),(4,3),(1,1),(1,2),(2,1),(3,2),(3,3);
/*!40000 ALTER TABLE `system_privilege` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `unit`
--

DROP TABLE IF EXISTS `unit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `unit` (
  `unit_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  PRIMARY KEY (`unit_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `unit`
--

LOCK TABLES `unit` WRITE;
/*!40000 ALTER TABLE `unit` DISABLE KEYS */;
INSERT INTO `unit` VALUES (1,'Отдел 1'),(2,'Отдел 2'),(3,'Отдел 3'),(4,'Отдел 4'),(5,'Отдел 5'),(6,'Отдел 6'),(7,'Отдел 7'),(8,'Отдел 8'),(9,'Отдел 9'),(10,'Отдел 10');
/*!40000 ALTER TABLE `unit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `user_role_roles_role_id_fk` (`role_id`),
  CONSTRAINT `user_role_roles_role_id_fk` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`),
  CONSTRAINT `user_role_users_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,1);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `middle_name` varchar(75) DEFAULT NULL,
  `email` varchar(50) NOT NULL,
  `department_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `users_department_department_id_fk` (`department_id`),
  CONSTRAINT `users_department_department_id_fk` FOREIGN KEY (`department_id`) REFERENCES `department` (`department_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'User','$2a$10$D4Y9wp/YA23Uv/6P/i41JedOw7ZnWBzMJMq/K0aqXPo4PcM/e8nW6','Иван','Иванов','Иванович','ivanov@example.com',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-12-16  0:24:14
