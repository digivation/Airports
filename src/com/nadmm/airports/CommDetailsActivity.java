/*
 * Airports for Android
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableLayout.LayoutParams;

import com.nadmm.airports.DatabaseManager.Airports;
import com.nadmm.airports.DatabaseManager.Tower1;
import com.nadmm.airports.DatabaseManager.Tower3;
import com.nadmm.airports.DatabaseManager.Tower6;
import com.nadmm.airports.DatabaseManager.Tower7;

public class CommDetailsActivity extends Activity {

    private LinearLayout mMainLayout;
    private LayoutInflater mInflater;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        mInflater = getLayoutInflater();
        setContentView( R.layout.wait_msg );

        Intent intent = getIntent();
        String siteNumber = intent.getStringExtra( Airports.SITE_NUMBER );
        CommDetailsTask task = new CommDetailsTask();
        task.execute( siteNumber );
    }

    private final class CommDetailsTask extends AsyncTask<String, Void, Cursor[]> {

        @Override
        protected Cursor[] doInBackground( String... params ) {
            String siteNumber = params[ 0 ];
            Cursor[] cursors = new Cursor[ 5 ];
            
            DatabaseManager dbManager = DatabaseManager.instance( getApplicationContext() );
            Cursor apt = dbManager.getAirportDetails( siteNumber );
            cursors[ 0 ] = apt;

            SQLiteDatabase db = dbManager.getDatabase( DatabaseManager.DB_FADDS );
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder = new SQLiteQueryBuilder();
            builder.setTables( Tower1.TABLE_NAME );
            Cursor c = builder.query( db, new String[] { "*" },
                    Tower1.SITE_NUMBER+"=? ",
                    new String[] { siteNumber }, null, null, null, null );
            cursors[ 1 ] = c;

            String faaCode = apt.getString( apt.getColumnIndex( Airports.FAA_CODE ) );
            builder = new SQLiteQueryBuilder();
            builder.setTables( Tower3.TABLE_NAME );
            c = builder.query( db, new String[] { "*" },
                    Tower3.FACILITY_ID+"=? ",
                    new String[] { faaCode }, null, null, Tower3.MASTER_AIRPORT_FREQ_USE, null );
            cursors[ 2 ] = c;

            builder = new SQLiteQueryBuilder();
            builder.setTables( Tower6.TABLE_NAME );
            c = builder.query( db, new String[] { "*" },
                    Tower3.FACILITY_ID+"=? ",
                    new String[] { faaCode }, null, null, null, null );
            cursors[ 3 ] = c;

            builder = new SQLiteQueryBuilder();
            builder.setTables( Tower7.TABLE_NAME );
            c = builder.query( db, new String[] { "*" },
                    Tower7.SATELLITE_AIRPORT_SITE_NUMBER+"=? ",
                    new String[] { siteNumber }, null, null, null, null );
            cursors[ 4 ] = c;

            return cursors;
        }

        @Override
        protected void onPostExecute( Cursor[] result ) {
            if ( result == null ) {
                // TODO: Show an error here
                return;
            }

            View view = mInflater.inflate( R.layout.comm_detail_view, null );
            setContentView( view );
            mMainLayout = (LinearLayout) view.findViewById( R.id.comm_top_layout );

            // Title
            Cursor apt = result[ 0 ];
            GuiUtils.showAirportTitle( mMainLayout, apt );

            showAirportFrequencies( result );
        }
    }

    protected void showAirportFrequencies( Cursor[] result ) {
        Cursor twr3 = result[ 2 ];
        if ( twr3.moveToFirst() ) {
            TextView label = (TextView) mMainLayout.findViewById( R.id.airport_comm_label );
            TableLayout layout = (TableLayout) mMainLayout.findViewById(
                    R.id.airport_comm_details );
            HashMap<String, String> map = new HashMap<String, String>();
            do {
                String freq = twr3.getString( twr3.getColumnIndex( Tower3.MASTER_AIRPORT_FREQ ) );
                String freqUse = twr3.getString( twr3.getColumnIndex(
                        Tower3.MASTER_AIRPORT_FREQ_USE ) );
                // Remove any text past the frequency
                int i = 0;
                while ( i < freq.length() ) {
                    char c = freq.charAt( i );
                    if ( ( c >= '0' && c <= '9' ) || c == '.' ) {
                        ++i;
                        continue;
                    }
                    freq = freq.substring( 0, i );
                    break;
                }
                if ( freqUse.contains( "LCL" ) ) {
                    addFrequencyToMap( map, "Tower", freq );
                }
                if ( freqUse.contains( "GND" ) ) {
                    addFrequencyToMap( map, "Ground", freq );
                }
                if ( freqUse.contains( "CD" ) || freqUse.contains( "CLNC DEL" ) ) {
                    addFrequencyToMap( map, "Clearance Delivery", freq );
                }
                if ( freqUse.contains( "CLASS B" ) ) {
                    addFrequencyToMap( map, "Class B", freq );
                }
                if ( freqUse.contains( "CLASS C" ) ) {
                    addFrequencyToMap( map, "Class C", freq );
                }
                if ( freqUse.contains( "ATIS" ) ) {
                    if ( freqUse.contains( "D-ATIS" ) ) {
                        addFrequencyToMap( map, "D-ATIS", freq );
                    } else {
                        addFrequencyToMap( map, "ATIS", freq );
                    }
                }
                if ( freqUse.contains( "BASIC RADAR" ) ) {
                    addFrequencyToMap( map, "Basic Radar", freq );
                }
                if ( freqUse.contains( "RADAR" ) || freqUse.contains( "RDR" ) ) {
                    addFrequencyToMap( map, "Radar", freq );
                }
                if ( freqUse.contains( "TRSA" ) ) {
                    addFrequencyToMap( map, "TRSA", freq );
                }
                if ( freqUse.contains( "TAXI CLNC" ) ) {
                    addFrequencyToMap( map, "Pre-Taxi Clearance", freq );
                }
            } while ( twr3.moveToNext() );

            int row = 0;
            for ( Map.Entry<String, String> pair : map.entrySet() ) {
                if ( row > 0 ) {
                    addSeparator( layout );
                }
                addRow( layout, pair.getKey(), pair.getValue() );
                ++row;
            }

            label.setVisibility( View.VISIBLE );
            layout.setVisibility( View.VISIBLE );
        } else {
            TextView label = (TextView) mMainLayout.findViewById( R.id.airport_comm_label );
            label.setText( "No Tower3 records" );
        }
    }

    protected void addFrequencyToMap( HashMap<String, String> map, String key, String value ) {
        String curValue = map.get( key );
        if ( curValue == null ) {
            curValue = "";
        }
        if ( curValue.length() > 0 ) {
            curValue += ", ";
        }
        curValue += value;
        map.put( key, curValue );
    }

    protected void addRow( TableLayout table, String label, String text ) {
        TableRow row = (TableRow) mInflater.inflate( R.layout.airport_detail_item, null );
        TextView tvLabel = new TextView( this );
        tvLabel.setText( label );
        tvLabel.setSingleLine();
        tvLabel.setGravity( Gravity.LEFT );
        tvLabel.setPadding( 4, 4, 2, 4 );
        row.addView( tvLabel, new TableRow.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1f ) );
        TextView tvValue = new TextView( this );
        tvValue.setText( text );
        tvValue.setGravity( Gravity.RIGHT );
        tvValue.setPadding( 4, 4, 2, 4 );
        row.addView( tvValue, new TableRow.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 0f ) );
        table.addView( row, new TableLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT ) );
    }

    protected void addSeparator( TableLayout layout ) {
        View separator = new View( this );
        separator.setBackgroundColor( Color.LTGRAY );
        layout.addView( separator, 
                new TableLayout.LayoutParams( TableLayout.LayoutParams.FILL_PARENT, 1 ) );
    }

}