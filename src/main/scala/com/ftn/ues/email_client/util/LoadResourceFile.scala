package com.ftn.ues.email_client.client.util

import play.api.libs.json.{JsValue, Json}

import scala.util.{Try, Using}

object LoadResourceFile {

  def loadElasticTemplate(templateFileName: String): Try[JsValue] =
    loadResourceJsonFile(s"elasticsearch/templates/$templateFileName.json")

  def loadResourceJsonFile(filePath: String): Try[JsValue] = {
    val classLoader = LoadResourceFile.getClass.getClassLoader;
    Using(classLoader.getResourceAsStream(filePath)){ stream =>
      Json.parse(stream)
    }
  }
}
