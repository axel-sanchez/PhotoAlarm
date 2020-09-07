package com.example.photoalarm.helpers

import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AlarmHelperTest {

    //Estos tests no van a funcionar porque hay que personalizar el expected
    @Test fun getTimeRestHoraPasada() {
        assertEquals("Alarma en 22 horas y 45 minutos", AlarmHelper.getTimeRest(3,45))
    }

    @Test fun getTimeRest() {
        assertEquals("Alarma en 23 horas y 45 minutos", AlarmHelper.getTimeRest(4,15))
    }
}