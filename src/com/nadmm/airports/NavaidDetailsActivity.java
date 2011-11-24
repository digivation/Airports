/*
 * FlightIntel for Pilots
 *
 * Copyright 2011 Nadeem Hasan <nhasan@nadmm.com>
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

package com.nadmm.airports;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.nadmm.airports.DatabaseManager.Nav1;
import com.nadmm.airports.DatabaseManager.Nav2;
import com.nadmm.airports.DatabaseManager.States;
import com.nadmm.airports.utils.DataUtils;
import com.nadmm.airports.utils.GuiUtils;

public class NavaidDetailsActivity extends ActivityBase {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        Intent intent = getIntent();
        String navaidId = intent.getStringExtra( Nav1.NAVAID_ID );
        String navaidType = intent.getStringExtra( Nav1.NAVAID_TYPE );
        NavaidDetailsTask task = new NavaidDetailsTask();
        task.execute( navaidId, navaidType );
    }

    private final class NavaidDetailsTask extends CursorAsyncTask {

        @Override
        protected Cursor[] doInBackground( String... params ) {
            String navaidId = params[ 0 ];
            String navaidType = params[ 1 ];

            SQLiteDatabase db = mDbManager.getDatabase( DatabaseManager.DB_FADDS );
            Cursor[] cursors = new Cursor[ 2 ];

            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables( Nav1.TABLE_NAME+" a LEFT OUTER JOIN "+States.TABLE_NAME+" s"
                    +" ON a."+Nav1.ASSOC_STATE+"=s."+States.STATE_CODE );
            Cursor c = builder.query( db, new String[] { "*" },
                    Nav1.NAVAID_ID+"=? AND "+Nav1.NAVAID_TYPE+"=?",
                    new String[] { navaidId, navaidType }, null, null, null, null );
            if ( !c.moveToFirst() ) {
                return null;
            }

            cursors[ 0 ] = c;

            builder = new SQLiteQueryBuilder();
            builder.setTables( Nav2.TABLE_NAME );
            c = builder.query( db, new String[] { "*" },
                    Nav1.NAVAID_ID+"=? AND "+Nav1.NAVAID_TYPE+"=?",
                    new String[] { navaidId, navaidType }, null, null, null, null );
            cursors[ 1 ] = c;

            return cursors;
        }

        @Override
        protected void onResult( Cursor[] result ) {
            setContentView( R.layout.navaid_detail_view );

            if ( result == null ) {
                GuiUtils.showToast( getApplicationContext(), "Navaid not found" );
                finish();
                return;
            }

            Cursor nav1 = result[ 0 ];
            showNavaidTitle( nav1 );
            showNavaidDetails( result );
            showNavaidRemarks( result );
        }

    }

    protected void showNavaidDetails( Cursor[] result ) {
        Cursor nav1 = result[ 0 ];
        TableLayout layout = (TableLayout) findViewById( R.id.navaid_details );
        String navaidClass = nav1.getString( nav1.getColumnIndex( Nav1.NAVAID_CLASS ) );
        String navaidType = nav1.getString( nav1.getColumnIndex( Nav1.NAVAID_TYPE ) );
        addRow( layout, "Class", navaidClass );
        double freq = nav1.getDouble( nav1.getColumnIndex( Nav1.NAVAID_FREQUENCY ) );
        String tacan = nav1.getString( nav1.getColumnIndex( Nav1.TACAN_CHANNEL ) );
        if ( freq == 0 ) {
            freq = DataUtils.getTacanChannelFrequency( tacan );
        }
        if ( freq > 0 ) {
            addSeparator( layout );
            if ( !DataUtils.isDirectionalNavaid( navaidType ) && ( freq%1.0 ) == 0 ) {
                addRow( layout, "Frequency", String.format( "%.0f", freq ) );
            } else {
                addRow( layout, "Frequency", String.format( "%.2f", freq ) );
            }
        }
        String power = nav1.getString( nav1.getColumnIndex( Nav1.POWER_OUTPUT ) );
        if ( power.length() > 0 ) {
            addSeparator( layout );
            addRow( layout, "Power output", power+" Watts" );
        }
        if ( tacan.length() > 0 ) {
            addSeparator( layout );
            addRow( layout, "Tacan channel", tacan );
        }
        String magVar = nav1.getString( nav1.getColumnIndex( Nav1.MAGNETIC_VARIATION_DEGREES ) );
        if ( magVar.length() > 0 ) {
            String magDir = nav1.getString( nav1.getColumnIndex( 
                    Nav1.MAGNETIC_VARIATION_DIRECTION ) );
            String magYear = nav1.getString( nav1.getColumnIndex( 
                    Nav1.MAGNETIC_VARIATION_YEAR ) );
            addSeparator( layout );
            addRow( layout, "Magnetic variation", String.format( "%d\u00B0%s (%s)",
                    Integer.valueOf( magVar ), magDir, magYear ) );
        }
        String alt = nav1.getString( nav1.getColumnIndex( Nav1.PROTECTED_FREQUENCY_ALTITUDE ) );
        if ( alt.length() > 0 ) {
            addSeparator( layout );
            addRow( layout, "Service volume", DataUtils.decodeNavProtectedAltitude( alt ) );
        }
        String hours = nav1.getString( nav1.getColumnIndex( Nav1.OPERATING_HOURS ) );
        addSeparator( layout );
        addRow( layout, "Operating hours", hours );
        String voiceFeature = nav1.getString( nav1.getColumnIndex( Nav1.VOICE_FEATURE ) );
        addSeparator( layout );
        addRow( layout, "Voice feature", voiceFeature.equals( "Y" )? "Yes" : "No" );
        String voiceIdent = nav1.getString( nav1.getColumnIndex( Nav1.AUTOMATIC_VOICE_IDENT ) );
        addSeparator( layout );
        addRow( layout, "Voice ident", voiceIdent.equals( "Y" )? "Yes" : "No" );
        addSeparator( layout );
        String navaidId = nav1.getString( nav1.getColumnIndex( Nav1.NAVAID_ID ) );
        Intent intent = new Intent( this, NavaidNotamActivity.class );
        intent.putExtra( Nav1.NAVAID_ID, navaidId );
        intent.putExtra( Nav1.NAVAID_TYPE, navaidType );
        addClickableRow( layout, "NOTAMs", intent, R.drawable.row_selector_bottom );
    }

    protected void showNavaidRemarks( Cursor[] result ) {
        Cursor nav2 = result[ 1 ];
        LinearLayout layout = (LinearLayout) findViewById( R.id.navaid_remarks );

        if ( nav2.moveToFirst() ) {
            do {
                String remark = nav2.getString( nav2.getColumnIndex( Nav2.REMARK_TEXT ) );
                addBulletedRow( layout, remark );
            } while ( nav2.moveToNext() );
        } else {
            layout.setVisibility( View.GONE );
        }
    }

}
