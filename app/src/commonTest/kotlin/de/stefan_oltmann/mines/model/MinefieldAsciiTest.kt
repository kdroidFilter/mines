/*
 * 💣 Mines 💣
 * Copyright (C) 2025 Stefan Oltmann
 * https://github.com/StefanOltmann/mines
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.stefan_oltmann.mines.model

import kotlin.test.Test
import kotlin.test.assertEquals

class MinefieldAsciiTest {

    @Test
    fun testSmallMinefieldToAscii() {

        assertEquals(
            expected = """
                111**2O1*1
                *223*31222
                3*1112*11*
                *21OO12332
                1211OO1**2
                23*1OO13*2
                **21O11211
                221O12*1OO
                111O1*3211
                1*1O12*11*
            """.trimIndent(),
            actual = MinefieldAscii.toAscii(smallTestMinefield)
        )
    }

    @Test
    fun testMediumMinefieldToAscii() {

        assertEquals(
            expected = """
                O2*2O1**213**3*2112*11***
                O2*2O24*21**6*6*21*321343
                O11212*32223****212*1O1*1
                1212*213*322333211221O122
                *3*32213**2*211112*321O2*
                2*4*11*233312*22*22**213*
                23*211233*2122*2222333*21
                *322O13**33*22211*11*322O
                12*113**6*212*1O111112*1O
                O1111****21O111OOOOOO111O
                122113**31OOOOOOOO111O122
                2**1O1221OOOOOO1112*2O1**
                *44321O111OOOOO1*12*2O122
                2*3**1O2*2OOOOO111112221O
                223*31O2*2OOOOOOOOOO1**1O
                *211211111OOOO111OOO2442O
                *3111*211OOOOO1*1OOO1**1O
                12*1123*1OOOOO111O1122321
                1322O1*21OOOOOO1111*212*1
                *4*31112221O1222*2222*211
                2***1O12**223**212*111211
                1232112*432**4443321OO1*1
                111OO1*4*22322****31O1332
                2*11233*22*1O13*5**213**1
                *211**211111OO112222*3*31
            """.trimIndent(),
            actual = MinefieldAscii.toAscii(mediumTestMinefield)
        )
    }

    @Test
    fun testLargeMinefieldToAscii() {

        assertEquals(
            expected = """
                O2*****3*212**2OO1*1OOOOO1*211*211*21213*3*2*1OOOO
                24*54334*32*5*4111221111O13*323*2222*2*3*312111121
                **3*211*22*24*5*312*11*1OO2*3*333*1224231211OO1*2*
                *432*11123323*6*5*532111112122*2*211*2*1O1*21O1121
                3*2112222**22*5*5***2OOO1*1OO11211122222123*21211O
                2*2OO1**34*212*3*34*211111211112122*1O1*11*22*2*21
                111OO1222*21O112111111*1OO1*22*2*3*2212111112233*2
                O1222221111OO1221OO1132212323*322*222*1O111O2*23*3
                12**2**1OO1222**1112*3*21**23*2O1111*21O1*1O2*23*4
                2*322332OO1**22212*423*2234*321OOOO222OO12212222**
                *43222*222222211O2**11234*43*1OOO112*1OOO1*11*124*
                *4**2*44**2OO1*112321O1****211OOO2*31211O2221122*3
                13*322**4*3112211*1122334321OOOO13*2O1*1O1*1OO1*5*
                O111O23322*23*311222**3*21211OOO1*2113431111O123**
                OOOOO1*1O112***222*34*324*4*3221111O2***1OOOO1*333
                OOOOO1111112343*2*3*3333**4*3**1OOOO2*4222321112*2
                11O112111*11*1112122*2**32212221O11223223***11233*
                *1O1*3*3233321OOOOO112221OOOOOOOO1*2*2*3***311**42
                22O236*5*3**21OOOOOOOOOOOOOOOOOOO112122*5542113**2
                *1O1****4*43*1OOOOOOOOOOOOOOOOOOO1222122**2*2234*2
                11O123334*3221OOOOOOOOOOOOOOOOOOO1**3*1234323**222
                11OO1222*33*211111OOOOOOOOOOOOOOO13*4211*2*12*433*
                *1OO1**212*22*12*2OOOOOOOOOOOOOOO123*212342112*3**
                12222332O2332223*2OOOOOOOOOOOOOOO1*22*22**11233*32
                O1**11*112**11*322OOOOOOOOOOOOOO1221224*4222**211O
                O12222223*421223*1OOOOOOOOOOOOOO1*211*3*323*321111
                OO112*11**2OO1*322OOOOOOOOOOOOOO12*332212**21OO2*2
                112*222222222212*1OOOOOOOOOOOOOOO12**1OO12211112*3
                1*2111*2222**11221OOOOOOOOOOOOOOOO122111211O2*322*
                22311112**33211*1OOOOOOOOOOOOOOO1111111*4*2O3**321
                2*3*212233*112321OOOOOOOOOOOOOOO1*34*212**2O2***2O
                *4*33*2*11112**31OOOOOOOOOOOOOOO12***2O12221224*2O
                *412*34332212***2OOOOO111OO1111111232111113*2O111O
                *42323**3**2346*3111111*1O13*33*21221O1*12**311OOO
                2**2*23*323*2**33*11*111112***4*21**1O1113*42*1OOO
                234322221O113443*22332OOO1*46*643344212212*3222211
                1*3*21*211OO2**5322**211O234*****2**12**224*22*3*1
                124*2123*1O14****23*54*1O2**45*5343312*43*3*23*632
                O1*32O1*2113**543*22**2113*33*32*3*21112*22112***1
                122*21322O2**4*1122222212*212*223*4*312332O1134*31
                *1113*3*1O2*322211*3322*32324443*34*5*3**1O1*2*21O
                11OO2*3121211O2*212***212*2*****3*33**4321O1121211
                OOOO222O1*1OOO2*3112322121334344423*55*31OO11112*1
                O1112*3232112222*211OO1*1O1*1O1**23*4***2213*43*21
                O1*23*3**211**1223*1OO111O111O1222*34*43*3*3***221
                O23*22344*222222*211O111112221OOO12*33322*234*311*
                O1*211*3*3*2222*3311O1*23*3**31OOO112**11111*21O11
                1211O12*2323**22*2*2122*3*44**21OOOO1221O11211OO11
                *311112111*333222323*321323*33*21O1221OOO1*21OOO2*
                *3*11*1OO12*11*11*12**1O1*21112*1O1**1OOO12*1OOO2*
            """.trimIndent(),
            actual = MinefieldAscii.toAscii(largeTestMinefield)
        )
    }
}
