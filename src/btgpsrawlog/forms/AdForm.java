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

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.Spacer;

import InneractiveSDK.IADView;
import InneractiveSDK.IADView.IaOptionalParams;
import btgpsrawlog.BTGPSRawLogMidlet;

public class AdForm extends Form {

    protected static Vector children = new Vector();

    static {
        Loader loader = new Loader();
        loader.setPriority(Thread.MIN_PRIORITY);
        loader.start();
    }

    static class Loader extends Thread {
        public void run() {
            try {
                for (;;) {
                    loadBannerAd();
                    Thread.sleep(120000); // 120 seconds
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected Command   bannerClick = new Command("Click!", "Click the banner!", Command.OK, 0);
    protected Image     bannerImage;
    protected String    bannerURL;
    protected ImageItem imageItem   = new ImageItem("", null, ImageItem.LAYOUT_DEFAULT, "");

    public AdForm(String title) {
        super(title);

        if (!BTGPSRawLogMidlet.isPro) {
            imageItem.setDefaultCommand(bannerClick);
            imageItem.setItemCommandListener(new ItemCommandListener() {

                public void commandAction(Command cmd, Item item) {
                    try {
                        BTGPSRawLogMidlet.instance.platformRequest(bannerURL);
                    } catch (ConnectionNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            append(imageItem);
            append(new Spacer(getWidth(), 10));

            synchronized (children) {
                children.addElement(this);
            }
        }
    }

    private static void loadBannerAd() {
        Hashtable metaData = new Hashtable();
        metaData.put(IaOptionalParams.Key_Keywords,
                "tools,gps,bluetooth,location,maps,openstreetmap");
        metaData.put(IaOptionalParams.Key_Location, System.getProperty("microedition.locale"));

        Vector ad = IADView.getBannerAdData(BTGPSRawLogMidlet.instance, "Lins_btgpsrawlog_Nokia",
                metaData);

        synchronized (children) {
            for (int n = 0; n < children.size(); n++) {
                ((AdForm) children.elementAt(n)).updateBannerAd(ad);
            }
        }
    }

    private void updateBannerAd(Vector ad) {
        if (null != ad) {
            bannerImage = (Image) ad.elementAt(0);
            bannerURL = (String) ad.elementAt(1);
        }
        if (bannerImage != null) {
            Image.createImage(bannerImage);
            imageItem.setImage(bannerImage);
        } else {
            System.out.println("img is null");
        }
    }
}
