/*
 * FlightIntel for Pilots
 *
 * Copyright 2012 Nadeem Hasan <nhasan@nadmm.com>
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 */

package com.nadmm.airports.wx;

import android.content.Intent;

public class AreaForecastFragment extends WxTextFragmentBase {

    private final static String[] mAreaCodes = {
            "alaska_n1_fa",
            "alaska_n2_fa",
            "alaska_sc1_fa",
            "alaska_sc2_fa",
            "alaska_se1_fa",
            "alaska_se2_fa",
            "alaska_bswa_fa",
            "boston_fa",
            "carib_fa",
            "chicago_fa",
            "ftworth_fa",
            "gulf_fa",
            "hawaii_fa",
            "miami_fa",
            "saltlakecity_fa",
            "sanfrancisco_fa"
    };

    private final static String[] mAreaNames = {
            "Alaska North Part 1",
            "Alaska North Part 2",
            "Alaska Southcentral Part 1",
            "Alaska Southcentral Part 2",
            "Alaska Southeast Part 1",
            "Alaska Southeast Part 2",
            "Alaska Southwest",
            "Boston",
            "Carribean",
            "Chicago",
            "Fort Worth",
            "Gulf of Mexico",
            "Hawaii",
            "Miami",
            "Salt Lake City",
            "San Francisco"
    };


    public AreaForecastFragment() {
        super( NoaaService.ACTION_GET_FA, mAreaCodes, mAreaNames );
    }

    @Override
    protected String getTitle() {
        return "Area Forecast";
    }

    @Override
    protected Intent getServiceIntent() {
        return new Intent( getActivity(), AreaForecastService.class );
    }

}
