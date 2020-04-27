package com.example.photoalarm.data.repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.photoalarm.data.models.Day
import com.example.photoalarm.data.models.Alarm
import com.example.photoalarm.data.Database
import com.example.photoalarm.data.TableAlarm
import com.example.photoalarm.data.TableDay
import com.example.photoalarm.data.TableDayXAlarm

class GenericRepository {

    private lateinit var dbHelper: Database
    private lateinit var db: SQLiteDatabase

    companion object {
        //Utilizo el patrón singleton
        private var instance: GenericRepository? = null

        fun getInstance(context: Context): GenericRepository {
            if (instance == null) {
                instance = GenericRepository()
                instance!!.dbHelper = Database(context)
                instance!!.db = instance!!.dbHelper.writableDatabase
            }
            return instance!!
        }
    }

    //Insertamos una alarma
    fun insert(item: Alarm): Long {
        return try {
            val values = ContentValues().apply {
                put(TableAlarm.Columns.COLUMN_NAME_LABEL, item.label)
                put(TableAlarm.Columns.COLUMN_NAME_TIME, item.time)
                put(TableAlarm.Columns.COLUMN_NAME_SONG, item.song)
                put(TableAlarm.Columns.COLUMN_NAME_IS_ACTIVE, item.isActive)
                put(TableAlarm.Columns.COLUMN_NAME_REQUIRE_VIBRATE, item.requireVibrate)
            }
            db.insert(TableAlarm.Columns.TABLE_NAME, null, values)
        } catch (e: Exception) {
            Log.e("INSERT ALERT", e.message)
            -1
        }
    }

    //Insertamos un dia
    fun insert(item: Day): Long {
        return try {
            val values = ContentValues().apply {
                put(TableDay.Columns.COLUMN_NAME_NAME, item.name)
            }
            db.insert(TableDay.Columns.TABLE_NAME, null, values)
        } catch (e: Exception) {
            Log.e("INSERT DAY", e.message)
            -1
        }
    }

    //Insertamos un dia x alarma
    fun insert(idAlarma: Long, idDay: Int): Long {
        return try {
            val values = ContentValues().apply {
                put(TableDayXAlarm.Columns.COLUMN_NAME_ID_ALARM, idAlarma)
                put(TableDayXAlarm.Columns.COLUMN_NAME_ID_DAY, idDay)
            }
            db.insert(TableDayXAlarm.Columns.TABLE_NAME, null, values)
        } catch (e: Exception) {
            Log.e("INSERT DAY X ALARM", e.message)
            -1
        }
    }

    fun update(item: Alarm): Int {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(TableAlarm.Columns.COLUMN_NAME_ID, item.id)
            put(TableAlarm.Columns.COLUMN_NAME_LABEL, item.label)
            put(TableAlarm.Columns.COLUMN_NAME_TIME, item.time)
            put(TableAlarm.Columns.COLUMN_NAME_SONG, item.song)
            put(TableAlarm.Columns.COLUMN_NAME_IS_ACTIVE, item.isActive)
            put(TableAlarm.Columns.COLUMN_NAME_REQUIRE_VIBRATE, item.requireVibrate)
        }

        // Which row to update, based on the title
        val selection = "${TableAlarm.Columns.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(item.id.toString())

        return db.update(TableAlarm.Columns.TABLE_NAME, values, selection, selectionArgs)
    }

    fun getAlarms(
        whereColumns: Array<String>?,
        whereArgs: Array<String>?,
        orderByColumn: String?
    ): MutableList<Alarm> {
        val db = dbHelper.readableDatabase

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        val projection = arrayOf(
            TableAlarm.Columns.COLUMN_NAME_ID,
            TableAlarm.Columns.COLUMN_NAME_LABEL,
            TableAlarm.Columns.COLUMN_NAME_TIME,
            TableAlarm.Columns.COLUMN_NAME_SONG,
            TableAlarm.Columns.COLUMN_NAME_IS_ACTIVE,
            TableAlarm.Columns.COLUMN_NAME_REQUIRE_VIBRATE
        )

        // Filter results WHERE "title" = 'My Title'
        val selection = setWhere(whereColumns)

        // How you want the results sorted in the resulting Cursor
        val sortOrder: String? = if (orderByColumn?.count() != 0) "$orderByColumn DESC" else null

        var cursor = db.query(
            TableAlarm.Columns.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            whereArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )

        var idsAlarms = ""

        val items = mutableListOf<Alarm>()
        while (cursor.moveToNext()) {
            idsAlarms += "${cursor.getLong(cursor.getColumnIndex(TableAlarm.Columns.COLUMN_NAME_ID))},"
            items.add(
                Alarm(
                    cursor.getLong(cursor.getColumnIndex(TableAlarm.Columns.COLUMN_NAME_ID)),
                    cursor.getString(cursor.getColumnIndex(TableAlarm.Columns.COLUMN_NAME_LABEL)),
                    cursor.getString(cursor.getColumnIndex(TableAlarm.Columns.COLUMN_NAME_TIME)),
                    cursor.getString(cursor.getColumnIndex(TableAlarm.Columns.COLUMN_NAME_SONG)),
                    mutableListOf(),
                    cursor.getInt(cursor.getColumnIndex(TableAlarm.Columns.COLUMN_NAME_IS_ACTIVE)) != 0,
                    cursor.getInt(cursor.getColumnIndex(TableAlarm.Columns.COLUMN_NAME_REQUIRE_VIBRATE)) != 0
                )
            )
        }

        if(idsAlarms.isNotEmpty()) idsAlarms = idsAlarms.substring(0, idsAlarms.lastIndex)

        var idAlarm: Long
        var day: String
        var alarm: Alarm

        cursor = db.rawQuery(
            "select a.id, d.name from alarm a inner join day_x_alarm dx on a.id = dx.id_alarm inner join day d on d.id = dx.id_day where a.id in ($idsAlarms)",
            null
        )

        while (cursor.moveToNext()) {
            idAlarm = cursor.getLong(cursor.getColumnIndex(TableAlarm.Columns.COLUMN_NAME_ID))
            day = cursor.getString(cursor.getColumnIndex(TableDay.Columns.COLUMN_NAME_NAME))

            alarm = items.first { a -> a.id == idAlarm }
            alarm.days.add(day)
        }

        return items
    }

    fun getDays(
        whereColumns: Array<String>?,
        whereArgs: Array<String>?,
        orderByColumn: String?
    ): List<Day> {
        val db = dbHelper.readableDatabase

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        val projection = arrayOf(
            TableDay.Columns.COLUMN_NAME_ID,
            TableDay.Columns.COLUMN_NAME_NAME
        )

        // Filter results WHERE "title" = 'My Title'
        val selection = setWhere(whereColumns)

        // How you want the results sorted in the resulting Cursor
        val sortOrder: String? = if (orderByColumn?.count() != 0) "$orderByColumn DESC" else null

        val cursor = db.query(
            TableDay.Columns.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            whereArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )

        val items = mutableListOf<Day>()
        while (cursor.moveToNext()) {
            items.add(
                Day(
                    cursor.getLong(cursor.getColumnIndex(TableDay.Columns.COLUMN_NAME_ID)),
                    cursor.getString(cursor.getColumnIndex(TableDay.Columns.COLUMN_NAME_NAME))
                )
            )
        }
        return items
    }

    fun deleteAlarm(id: Long): Int {
        val db = dbHelper.writableDatabase
        // Define 'where' part of query.
        val selection = "${TableAlarm.Columns.COLUMN_NAME_ID} = ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(id.toString())
        // Issue SQL statement.
        return db.delete(TableAlarm.Columns.TABLE_NAME, selection, selectionArgs)
    }

    fun deleteDayXAlarm(idAlarm: Long): Int{
        val db = dbHelper.writableDatabase
        // Define 'where' part of query.
        val selection = "${TableDayXAlarm.Columns.COLUMN_NAME_ID_ALARM} = ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(idAlarm.toString())
        // Issue SQL statement.
        return db.delete(TableDayXAlarm.Columns.TABLE_NAME, selection, selectionArgs)
    }

    private fun setWhere(whereColumns: Array<String>?): String? {
        if (whereColumns != null) {
            val result = StringBuilder()
            var first = true
            for (w in whereColumns) {
                if (first)
                    first = false
                else
                    result.append(" AND ")

                result.append(w)
                result.append("=?")
            }
            return result.toString()
        } else {
            return null
        }
    }
}