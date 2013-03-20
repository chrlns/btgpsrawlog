/*
 *  BTGPSRawLog
 *  Copyright (C) 2010-2013 Christian Lins <christian@lins.me>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package btgpsrawlog.forms;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.List;

import btgpsrawlog.BTGPSRawLogMidlet;

public class BluetoothDeviceList extends List {

    public static Command       BACK   = new Command("Back", Command.BACK, 1);
    public static Command       SEARCH = new Command("Search", "Start device search", Command.OK, 2);

    protected BTGPSRawLogMidlet midlet;

    public BluetoothDeviceList(BTGPSRawLogMidlet midlet) {
        super("Bluetooth GPS devices", List.IMPLICIT);
        this.midlet = midlet;

        addCommand(BACK);
        addCommand(SEARCH);
    }
}
