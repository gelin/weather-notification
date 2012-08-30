/*
 *  Weather API.
 *  Copyright (C) 2011  Vladimir Kubyshev, Denis Nelubin
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package ru.gelin.android.weather;

public enum WindDirection {
    N, NNE, NE, ENE, E, ESE, SE, SSE, S, SSW, SW, WSW, W, WNW, NW, NNW;

    /**
     *  Creates the direction from the azimuth degrees.
     */
    public static WindDirection valueOf(int deg) {
        int degPositive = deg;
        if (deg < 0) {
            degPositive += (-deg / 360 + 1) * 360;
        }
        int degNormalized = degPositive % 360;
        int degRotated = degNormalized + (360 / 16 / 2);
        int sector = degRotated / (360 / 16);
        switch (sector) {
            case 0: return WindDirection.N;
            case 1: return WindDirection.NNE;
            case 2: return WindDirection.NE;
            case 3: return WindDirection.ENE;
            case 4: return WindDirection.E;
            case 5: return WindDirection.ESE;
            case 6: return WindDirection.SE;
            case 7: return WindDirection.SSE;
            case 8: return WindDirection.S;
            case 9: return WindDirection.SSW;
            case 10: return WindDirection.SW;
            case 11: return WindDirection.WSW;
            case 12: return WindDirection.W;
            case 13: return WindDirection.WNW;
            case 14: return WindDirection.NW;
            case 15: return WindDirection.NNW;
            case 16: return WindDirection.N;
        }
        return WindDirection.N;
    }

}
