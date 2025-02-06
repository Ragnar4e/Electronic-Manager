-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: home_manager
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `apartment`
--

DROP TABLE IF EXISTS `apartment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `apartment` (
  `area` decimal(38,2) NOT NULL,
  `floor_number` int NOT NULL,
  `building_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apartment_number` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmjjff7q9e4qfyop2w1vafy619` (`building_id`),
  CONSTRAINT `FKmjjff7q9e4qfyop2w1vafy619` FOREIGN KEY (`building_id`) REFERENCES `building` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apartment`
--

LOCK TABLES `apartment` WRITE;
/*!40000 ALTER TABLE `apartment` DISABLE KEYS */;
INSERT INTO `apartment` VALUES (75.50,1,1,1,'101'),(85.00,1,1,2,'102'),(75.50,2,1,3,'201'),(65.00,1,2,4,'101'),(70.00,1,2,5,'102'),(80.00,1,3,6,'101'),(80.00,2,3,7,'201'),(90.00,1,4,8,'101'),(90.00,2,4,9,'201'),(85.00,1,5,10,'101');
/*!40000 ALTER TABLE `apartment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `apartment_owner`
--

DROP TABLE IF EXISTS `apartment_owner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `apartment_owner` (
  `apartment_id` bigint NOT NULL,
  `owner_id` bigint NOT NULL,
  PRIMARY KEY (`apartment_id`,`owner_id`),
  KEY `FKoench1je7a3yydvt9jjfx9ky` (`owner_id`),
  CONSTRAINT `FKbmjqvgwfq2tuxrxa2p53uyjd8` FOREIGN KEY (`apartment_id`) REFERENCES `apartment` (`id`),
  CONSTRAINT `FKoench1je7a3yydvt9jjfx9ky` FOREIGN KEY (`owner_id`) REFERENCES `owner` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apartment_owner`
--

LOCK TABLES `apartment_owner` WRITE;
/*!40000 ALTER TABLE `apartment_owner` DISABLE KEYS */;
INSERT INTO `apartment_owner` VALUES (1,1),(2,1),(3,1),(2,2),(3,3),(4,4),(5,5);
/*!40000 ALTER TABLE `apartment_owner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `building`
--

DROP TABLE IF EXISTS `building`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `building` (
  `built_up_area` decimal(38,2) DEFAULT NULL,
  `common_area` decimal(38,2) DEFAULT NULL,
  `totalApartments` int NOT NULL,
  `totalFloors` int NOT NULL,
  `employee_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8qc6qhy8jw1saus3g6qlsb6qs` (`employee_id`),
  CONSTRAINT `FK8qc6qhy8jw1saus3g6qlsb6qs` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `building`
--

LOCK TABLES `building` WRITE;
/*!40000 ALTER TABLE `building` DISABLE KEYS */;
INSERT INTO `building` VALUES (2400.00,400.00,32,8,1,1,'10 Vitosha Street'),(1800.00,300.00,24,6,1,2,'15 Hristo Botev Blvd'),(3000.00,500.00,40,10,2,3,'20 Ivan Vazov Street'),(2100.00,350.00,28,7,3,4,'25 Rakovski Street'),(2700.00,450.00,36,9,4,5,'30 Levski Blvd');
/*!40000 ALTER TABLE `building` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `contactNumber` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company`
--

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;
INSERT INTO `company` VALUES (1,'123 Main Street','0888111222','office@citymgmt.com','City Management Ltd'),(2,'456 Oak Avenue','0888333444','contact@urbancare.com','Urban Care Co'),(3,'789 Pine Road','0888555666','info@buildingplus.com','Building Plus');
/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `company_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5v50ed2bjh60n1gc7ifuxmgf4` (`company_id`),
  CONSTRAINT `FK5v50ed2bjh60n1gc7ifuxmgf4` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1,1,'john.smith@citymgmt.com','John','Smith','0877111222'),(1,2,'maria.j@citymgmt.com','Maria','Johnson','0877333444'),(2,3,'peter.b@urbancare.com','Peter','Brown','0877555666'),(2,4,'sarah.d@urbancare.com','Sarah','Davis','0877777888'),(3,5,'michael.w@buildingplus.com','Michael','Wilson','0877999000');
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fee`
--

DROP TABLE IF EXISTS `fee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fee` (
  `amount` decimal(38,2) NOT NULL,
  `due_date` date NOT NULL,
  `payment_date` date DEFAULT NULL,
  `apartment_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `status` enum('PAID','PENDING') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkas28q9q6no5j1h6iuqqo4sso` (`apartment_id`),
  CONSTRAINT `FKkas28q9q6no5j1h6iuqqo4sso` FOREIGN KEY (`apartment_id`) REFERENCES `apartment` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fee`
--

LOCK TABLES `fee` WRITE;
/*!40000 ALTER TABLE `fee` DISABLE KEYS */;
INSERT INTO `fee` VALUES (150.00,'2024-02-01','2024-01-15',1,1,'PAID'),(170.00,'2024-02-01',NULL,2,2,'PENDING'),(150.00,'2024-02-01','2024-01-20',3,3,'PAID'),(130.00,'2024-02-01',NULL,4,4,'PENDING'),(140.00,'2024-02-01','2024-01-25',5,5,'PAID');
/*!40000 ALTER TABLE `fee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `owner`
--

DROP TABLE IF EXISTS `owner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `owner` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `owner`
--

LOCK TABLES `owner` WRITE;
/*!40000 ALTER TABLE `owner` DISABLE KEYS */;
INSERT INTO `owner` VALUES (1,'alex.t@email.com','Alex','Thompson','0899111222'),(2,'emma.a@email.com','Emma','Anderson','0899333444'),(3,'david.m@email.com','David','Martinez','0899555666'),(4,'sofia.g@email.com','Sofia','Garcia','0899777888'),(5,'lucas.m@email.com','Lucas','Miller','0899999000');
/*!40000 ALTER TABLE `owner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resident`
--

DROP TABLE IF EXISTS `resident`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resident` (
  `age` int DEFAULT NULL,
  `has_pet` bit(1) DEFAULT NULL,
  `uses_elevator` bit(1) DEFAULT NULL,
  `apartment_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK26401pp7xa5ab4a4l62l5llnq` (`apartment_id`),
  CONSTRAINT `FK26401pp7xa5ab4a4l62l5llnq` FOREIGN KEY (`apartment_id`) REFERENCES `apartment` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resident`
--

LOCK TABLES `resident` WRITE;
/*!40000 ALTER TABLE `resident` DISABLE KEYS */;
INSERT INTO `resident` VALUES (35,_binary '',_binary '',1,1,'James','Wilson'),(32,_binary '',_binary '',1,2,'Linda','Wilson'),(45,_binary '\0',_binary '',2,3,'Robert','Taylor'),(28,_binary '',_binary '',3,4,'Amy','Brown'),(55,_binary '\0',_binary '',4,5,'Daniel','Lee'),(40,_binary '',_binary '',5,6,'Emily','Clark');
/*!40000 ALTER TABLE `resident` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-21 12:49:07
