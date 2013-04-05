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

import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.Spacer;

import InneractiveSDK.IADView;
import btgpsrawlog.BTGPSRawLogMidlet;

public class AdForm extends Form {

    final static int    IDLE                    = 0;
    final static int    GET_BANNER_AD           = 1;
    final static int    DISPLAY_INTERSTITIAL_AD = 3;
    final static int    EXIT                    = 4;
    final static int    CLICK_THE_BANNER        = 5;

    protected Vector    ad;
    protected ImageItem imageItem               = new ImageItem("", null, ImageItem.LAYOUT_DEFAULT,
                                                        "");

    class Loader extends Thread {
        public void run() {
            try {
                loadBannerAd();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public AdForm(String title) {
        super(title);

        append(imageItem);
        append(new Spacer(getWidth(), 10));

        Loader loader = new Loader();
        loader.setPriority(Thread.MIN_PRIORITY);
        loader.start();
    }

    private void loadBannerAd() {
        System.out.println("IADView.getBannerAd()");
        Hashtable metaData = new Hashtable();

        /*
         * metaData.put(IaOptionalParams.Key_Age, "30");
         * metaData.put(IaOptionalParams.Key_Gender, "F");
         * metaData.put(IaOptionalParams.Key_Gps_Location,
         * "53.542132,-2.239856"); metaData.put(IaOptionalParams.Key_Keywords,
         * "Games"); metaData.put(IaOptionalParams.Key_Location, "US");
         */

        ad = IADView
                .getBannerAdData(BTGPSRawLogMidlet.instance, "Lins_btgpsrawlog_Nokia", metaData);
        Image retImg = null;
        if (null != ad) {
            retImg = (Image) ad.elementAt(0);
        }
        if (retImg != null) {
            Image.createImage(retImg);
            imageItem.setImage(retImg);
        } else {
            System.out.println("retImg is null");
        }
    }
}
