package com.intuit.superglue.dao.model

import com.google.common.base.Charsets
import com.google.common.hash.Hashing
import com.intuit.superglue.dao.model.PrimaryKeys._

/**
  * Represents a single database table whose lineage is tracked by Superglue.
  *
  * A Table is uniquely defined by its name, the schema it belongs to, and
  * the platform it's located on (e.g. MySQL, Vertica, etc.). A [[TableEntity]]
  * may be instantiated by supplying just these fields, and an ID will automatically
  * be generated by hashing these fields. An example usage is shown below:
  *
  * {{{
  *   val tableEntity = TableEntity("tableName", "production_schema", "VERTICA")
  * }}}
  *
  * @param name The name of the table.
  * @param schema The schema the table belongs to.
  * @param platform The data platform this table is located on.
  * @param id A unique key generated from the other fields of this [[TableEntity]]
  */
case class TableEntity (
  name: String,
  schema: String,
  platform: String,
  id: TablePK,
)

object TableEntity {
  def apply(name: String, schema: String, platform: String): TableEntity = {
    val normalizedName = name.toUpperCase
    val normalizedSchema = schema.toUpperCase
    val normalizedPlatform = platform.toUpperCase

    val id = Hashing.sipHash24().newHasher()
      .putString(normalizedName, Charsets.UTF_8)
      .putString(normalizedSchema, Charsets.UTF_8)
      .putString(normalizedPlatform, Charsets.UTF_8)
      .hash().asLong()
    TableEntity(normalizedName, normalizedSchema, normalizedPlatform, TablePK(id))
  }
}
