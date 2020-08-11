package com.example.photoalarm.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class Database(context: Context): SQLiteOpenHelper(context.applicationContext, DATABASE_NAME, null, DATABASE_VERSION){

    companion object {
        // If you change the database schema, you must increment the database version.
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "alarmDB.db3"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //Create Table
        db!!.execSQL(SQL_CREATE_ALARM)
        db.execSQL(SQL_CREATE_DAY)
        db.execSQL(SQL_CREATE_DAY_X_ALARM)
        db.execSQL(SQL_CREATE_WEATHER)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //delete table
        db!!.execSQL(SQL_DELETE_ALARM)
        db.execSQL(SQL_DELETE_DAY)
        db.execSQL(SQL_DELETE_DAY_X_ALARM)
        db.execSQL(SQL_DELETE_WEATHER)
        //create table
        onCreate(db)
    }
}

//CREATE
private const val  SQL_CREATE_ALARM =
    "CREATE TABLE ${TableAlarm.Columns.TABLE_NAME} (" +
            "${TableAlarm.Columns.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
            "${TableAlarm.Columns.COLUMN_NAME_LABEL} TEXT," +
            "${TableAlarm.Columns.COLUMN_NAME_TIME} TEXT," +
            "${TableAlarm.Columns.COLUMN_NAME_SONG} TEXT," +
            "${TableAlarm.Columns.COLUMN_NAME_IS_ACTIVE} BOOL," +
            "${TableAlarm.Columns.COLUMN_NAME_REQUIRE_VIBRATE} BOOL)"

private const val  SQL_CREATE_DAY =
    "CREATE TABLE ${TableDay.Columns.TABLE_NAME} (" +
            "${TableDay.Columns.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
            "${TableDay.Columns.COLUMN_NAME_NAME} TEXT)"

private const val  SQL_CREATE_DAY_X_ALARM =
    "CREATE TABLE ${TableDayXAlarm.Columns.TABLE_NAME} (" +
            "${TableDayXAlarm.Columns.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
            "${TableDayXAlarm.Columns.COLUMN_NAME_ID_ALARM} LONG," +
            "${TableDayXAlarm.Columns.COLUMN_NAME_ID_DAY} LONG)"

private const val  SQL_CREATE_WEATHER =
    "CREATE TABLE ${TableWeather.Columns.TABLE_NAME} (" +
            "${TableWeather.Columns.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
            "${TableWeather.Columns.COLUMN_NAME_TEMP} INTEGER," +
            "${TableWeather.Columns.COLUMN_NAME_TIME_LAST_REQUEST} DATE)"

//DELETE
private const val SQL_DELETE_ALARM          = "DROP TABLE IF EXISTS ${TableAlarm.Columns.TABLE_NAME}"
private const val SQL_DELETE_DAY            = "DROP TABLE IF EXISTS ${TableDay.Columns.TABLE_NAME}"
private const val SQL_DELETE_DAY_X_ALARM    = "DROP TABLE IF EXISTS ${TableDayXAlarm.Columns.TABLE_NAME}"
private const val SQL_DELETE_WEATHER        = "DROP TABLE IF EXISTS ${TableWeather.Columns.TABLE_NAME}"

//TABLE
object TableAlarm{
    // Table contents are grouped together in an anonymous object.
    object Columns : BaseColumns {
        const val TABLE_NAME                       =    "alarm"
        const val COLUMN_NAME_ID                   =    "id"
        const val COLUMN_NAME_LABEL                =    "label"
        const val COLUMN_NAME_TIME                 =    "time"
        const val COLUMN_NAME_SONG                 =    "song"
        const val COLUMN_NAME_IS_ACTIVE            =    "is_active"
        const val COLUMN_NAME_REQUIRE_VIBRATE      =    "require_vibrate"
    }
}

object TableDay{
    // Table contents are grouped together in an anonymous object.
    object Columns : BaseColumns {
        const val TABLE_NAME          =    "day"
        const val COLUMN_NAME_ID      =    "id"
        const val COLUMN_NAME_NAME    =    "name"
    }
}

object TableDayXAlarm{
    // Table contents are grouped together in an anonymous object.
    object Columns : BaseColumns {
        const val TABLE_NAME              =    "day_x_alarm"
        const val COLUMN_NAME_ID          =    "id"
        const val COLUMN_NAME_ID_ALARM    =    "id_alarm"
        const val COLUMN_NAME_ID_DAY      =    "id_day"
    }
}

object TableWeather{
    object Columns : BaseColumns {
        const val TABLE_NAME                       =    "weather"
        const val COLUMN_NAME_ID                   =    "id"
        const val COLUMN_NAME_TEMP                 =    "temp"
        const val COLUMN_NAME_TIME_LAST_REQUEST    =    "time_last_request"
    }
}