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
import android.os.Bundle;

import com.google.analytics.tracking.android.EasyTracker;
import com.nadmm.airports.FragmentActivityBase;

public class TafMapActivity extends FragmentActivityBase {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        Bundle args = getIntent().getExtras();
        addFragment( TafMapFragment.class, args );
    }

    @Override
    public void onStart() {
      super.onStart();
      EasyTracker.getInstance( this ).activityStart( this );
    }

    @Override
    public void onStop() {
      super.onStop();
      EasyTracker.getInstance( this ).activityStop( this );
    }

    public static class TafMapFragment extends WxMapFragmentBase {

        public TafMapFragment() {
            super( NoaaService.ACTION_GET_TAF, WxRegions.sWxRegionCodes,
                    WxRegions.sWxRegionNames );
            setLabel( "Select Region" );
        }

        @Override
        protected Intent getServiceIntent() {
            return new Intent( getActivity(), TafService.class );
        }

    }

}
