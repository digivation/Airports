/*
 * FlightIntel for Pilots
 *
 * Copyright 2011-2013 Nadeem Hasan <nhasan@nadmm.com>
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

package com.nadmm.airports.e6b;

import java.util.HashMap;

import android.database.Cursor;

import com.nadmm.airports.ListMenuFragment;
import com.nadmm.airports.R;

public class E6bMenuFragment extends ListMenuFragment {

    private static final HashMap<Long, Class<?>> mDispatchMap;
    static {
        mDispatchMap = new HashMap<Long, Class<?>>();
        mDispatchMap.put( (long)R.id.E6B_UNIT_CONVERSIONS, UnitConvertFrament.class );
        mDispatchMap.put( (long)R.id.E6B_WIND_CALCS, E6bMenuFragment.class );
        mDispatchMap.put( (long)R.id.E6B_CROSS_WIND, CrossWindFragment.class );
        mDispatchMap.put( (long)R.id.E6B_WIND_TRIANGLE_WIND, WindTriangleFragment.class );
        mDispatchMap.put( (long)R.id.E6B_WIND_TRIANGLE_HDG_GS, WindTriangleFragment.class );
        mDispatchMap.put( (long)R.id.E6B_WIND_TRIANGLE_CRS_GS, WindTriangleFragment.class );
        mDispatchMap.put( (long)R.id.E6B_ALTIMETRY, E6bMenuFragment.class );
        mDispatchMap.put( (long)R.id.E6B_ALTIMETRY_ISA, IsaFragment.class );
        mDispatchMap.put( (long)R.id.E6B_ALTIMETRY_ALTITUDES, AltitudesFragment.class );
        mDispatchMap.put( (long)R.id.E6B_ALTIMETRY_TAS, TrueAirspeedFragment.class );
        mDispatchMap.put( (long)R.id.E6B_ALTIMETRY_OAT, OutsideAirTemperatureFragment.class );
        mDispatchMap.put( (long)R.id.E6B_ALTIMETRY_MACH, MachNumberFragment.class );
        mDispatchMap.put( (long)R.id.E6B_ALTIMETRY_TA, TrueAltitudeFragment.class );
        mDispatchMap.put( (long)R.id.E6B_TIME_SPEED_DISTANCE, E6bMenuFragment.class );
        mDispatchMap.put( (long)R.id.E6B_TSD_TIME, TimeSpeedDistanceFragment.class );
        mDispatchMap.put( (long)R.id.E6B_TSD_SPEED, TimeSpeedDistanceFragment.class );
        mDispatchMap.put( (long)R.id.E6B_TSD_DISTANCE, TimeSpeedDistanceFragment.class );
        mDispatchMap.put( (long)R.id.E6B_FUEL_CALCS, E6bMenuFragment.class );
        mDispatchMap.put( (long)R.id.E6B_FUEL_ENDURANCE_GALLONS, FuelCalcsFragment.class );
        mDispatchMap.put( (long)R.id.E6B_FUEL_ENDURANCE_LBS, FuelCalcsFragment.class );
        mDispatchMap.put( (long)R.id.E6B_FUEL_BURN_RATE_GALLONS, FuelCalcsFragment.class );
        mDispatchMap.put( (long)R.id.E6B_FUEL_BURN_RATE_LBS, FuelCalcsFragment.class );
        mDispatchMap.put( (long)R.id.E6B_FUEL_TOTAL_BURNED_GALLONS, FuelCalcsFragment.class );
        mDispatchMap.put( (long)R.id.E6B_FUEL_TOTAL_BURNED_LBS, FuelCalcsFragment.class );
    }

    @Override
    protected Class<?> getItemFragmentClass( long id ) {
        return mDispatchMap.get( id );
    }

    @Override
    protected Cursor getMenuCursor( long id ) {
        return new E6bMenuCursor( id );
    }

    public class E6bMenuCursor extends ListMenuCursor {

        public E6bMenuCursor( long id ) {
            super( id );
        }

        @Override
        protected void populateMenuItems( long id ) {
            if ( id == R.id.CATEGORY_MAIN ) {
                addRow( R.id.E6B_WIND_CALCS,
                        "Wind Calculations",
                        "Cross wind, head wind and wind triangle" );
                addRow( R.id.E6B_ALTIMETRY,
                        "Altimetry",
                        "Altimeter, altitude and the standard atmosphere" );
                addRow( R.id.E6B_TIME_SPEED_DISTANCE,
                        "Time, Speed and Distance",
                        "Solve for time, speed and distance" );
                addRow( R.id.E6B_FUEL_CALCS,
                        "Fuel Calculations",
                        "Find endurance, burn rate and fuel required" );
                addRow( R.id.E6B_UNIT_CONVERSIONS,
                        "Unit Conversions",
                        "Convert between units of measurement" );
            } else if ( id == R.id.E6B_WIND_CALCS ) {
                addRow( R.id.E6B_CROSS_WIND,
                        "Crosswind and Headwind",
                        "Cross wind and head wind for a runway" );
                addRow( R.id.E6B_WIND_TRIANGLE_WIND,
                        "Find Wind Speed and Direction",
                        "WS and WDIR using Wind Triangle" );
                addRow( R.id.E6B_WIND_TRIANGLE_HDG_GS,
                        "Find Heading and Ground Speed",
                        "HDG and GS using Wind Triangle" );
                addRow( R.id.E6B_WIND_TRIANGLE_CRS_GS,
                        "Find Course and Ground Speed",
                        "CRS and GS using Wind Triangle" );
            } else if ( id == R.id.E6B_ALTIMETRY ) {
                addRow( R.id.E6B_ALTIMETRY_ISA,
                        "Standard Atmosphere",
                        "International Standard Atmosphere (ISA 1976 model)" );
                addRow( R.id.E6B_ALTIMETRY_ALTITUDES,
                        "Pressure & Density Altitude",
                        "Altimeter and temperature" );
                addRow( R.id.E6B_ALTIMETRY_TAS,
                        "True Airspeed",
                        "True airspeed at a given altitude and temperature" );
                addRow( R.id.E6B_ALTIMETRY_OAT,
                        "Outside Air Temperature",
                        "Also known as True Air Temperature" );
                addRow( R.id.E6B_ALTIMETRY_MACH,
                        "Mach Number",
                        "Supersonic or subsonic" );
                addRow( R.id.E6B_ALTIMETRY_TA,
                        "True Altitude",
                        "True altitude at a given altitude and temperature" );
            } else if ( id == R.id.E6B_TIME_SPEED_DISTANCE ) {
                addRow( R.id.E6B_TSD_TIME,
                        "Find Flight Time",
                        "Find flight time based on speed and distance" );
                addRow( R.id.E6B_TSD_SPEED,
                        "Find Ground Speed",
                        "Find ground speed based on time and distance" );
                addRow( R.id.E6B_TSD_DISTANCE,
                        "Find Distance Flown",
                        "Find distance flown based on time and speed" );
            } else if ( id == R.id.E6B_FUEL_CALCS ) {
                addRow( R.id.E6B_FUEL_ENDURANCE_GALLONS,
                        "Endurance",
                        "Find endurance in flight" );
                addRow( R.id.E6B_FUEL_TOTAL_BURNED_GALLONS,
                        "Total Fuel Burn",
                        "Find total fuel burned during the flight" );
                addRow( R.id.E6B_FUEL_BURN_RATE_GALLONS,
                        "Fuel Burn Rate",
                        "Find rate of fuel burn during the flight" );
                addRow( R.id.E6B_FUEL_ENDURANCE_LBS,
                        "Endurance (lbs)",
                        "Find endurance in flight" );
                addRow( R.id.E6B_FUEL_TOTAL_BURNED_LBS,
                        "Total Fuel Burn (lbs)",
                        "Find total fuel burned during the flight" );
                addRow( R.id.E6B_FUEL_BURN_RATE_LBS,
                        "Fuel Burn Rate (lbs)",
                        "Find rate of fuel burn during the flight" );
            }
        }

        private void addRow( long id, String title, String subtitle ) {
            newRow().add( id )
                .add( 0 )
                .add( title )
                .add( subtitle );
        }
    }

}
