--------------------------------------------------------
--
-- Script file to create database file for XDoc plugin
-- 
-- Alesia
--
--------------------------------------------------------

DROP TABLE XD_CONFIG CASCADE CONSTRAINTS;
CREATE TABLE XD_CONFIG
(
   XD_COID           VARCHAR2(19 Byte)     NOT NULL,
   XD_CONAME         VARCHAR2(30 Byte),
   XD_COEXTENSION    VARCHAR2(6 Byte),
   XD_CODESCRIPTION  VARCHAR2(60 Byte),
   XD_CODATA         BLOB,
   XD_COQRCODE       VARCHAR2(3000 Byte),
   XD_COPROPERTIES   VARCHAR2(3000 Byte)
);
ALTER TABLE XD_CONFIG
   ADD CONSTRAINT PK_XD_CONFIG PRIMARY KEY (XD_COID);

DROP TABLE XD_MERGED_DOC CASCADE CONSTRAINTS;
CREATE TABLE XD_MERGED_DOC
(
   XD_MDID           VARCHAR2(20 Byte)   NOT NULL,
   XD_MDCERTIFICATE  VARCHAR2(10 Byte)   NOT NULL,
   XD_MDDATE         DATE                NOT NULL,
   XD_MDREMITTENT    VARCHAR2(60 Byte)   NOT NULL,
   XD_MDDOC_NAME     VARCHAR2(30 Byte)   NOT NULL,
   XD_MDMERGED_DOC   BLOB                NOT NULL
);
ALTER TABLE XD_MERGED_DOC
   ADD CONSTRAINT PK_XD_MERGED_DOC PRIMARY KEY (XD_MDID);
