package com.olegych.scastie
package api

import play.api.libs.json._

object User {
  // low tech solution
  val admins = Set(
    "dimart",
    "Duhemm",
    "heathermiller",
    "julienrf",
    "jvican",
    "MasseGuillaume",
    "olafurpg",
    "rorygraves",
    "travissarles"
  )
  implicit val formatUser = Json.format[User]
}
case class User(login: String, name: Option[String], avatar_url: String) {
  def isAdmin: Boolean = User.admins.contains(login)
}

object SnippetUserPart {
  implicit val formatSnippetUserPart = Json.format[SnippetUserPart]
}

case class SnippetUserPart(login: String, update: Int = 0)

object SnippetId {
  implicit val formatSnippetId = Json.format[SnippetId]
}

case class SnippetId(base64UUID: String, user: Option[SnippetUserPart]) {
  def isOwnedBy(user2: Option[User]): Boolean = {
    (user, user2) match {
      case (Some(SnippetUserPart(snippetLogin, _)),
            Some(User(userLogin, _, _))) =>
        snippetLogin == userLogin
      case _ => false
    }
  }

  override def toString: String = url

  def url: String = {
    this match {
      case SnippetId(uuid, None) => uuid
      case SnippetId(uuid, Some(SnippetUserPart(login, update))) =>
        s"$login/$uuid/$update"
    }
  }

  def scalaJsUrl(end: String): String = {
    val middle = url
    s"/${Shared.scalaJsHttpPathPrefix}/$middle/$end"
  }
}
