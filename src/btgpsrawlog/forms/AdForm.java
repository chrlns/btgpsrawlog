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

import InneractiveSDK.IADView;
import InneractiveSDK.IADView.IaOptionalParams;
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
    protected Worker    worker;

    public AdForm(String title) {
        super(title);

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        worker = new Worker(this);
        worker.start();
        worker.doTask(GET_BANNER_AD);
    }

    class Worker extends Thread {

        boolean terminated = false;
        int     mTask;
        AdForm  mOwner;

        public Worker(AdForm owner) {
            mOwner = owner;
        }

        synchronized public void run() {

            while (!terminated) {
                mTask = IDLE;
                try {
                    wait();
                } catch (InterruptedException ex) {
                }

                try {
                    switch (mTask) {
                    case CLICK_THE_BANNER:
                        if (null != ad) {
                            String clickURL = (String) ad.elementAt(1);
                            if (null != clickURL && !clickURL.equals("")) {
                                boolean forceExit = BTGPSRawLogMidlet.instance
                                        .platformRequest(clickURL);

                                System.out.println("platformRequest - the midlet should be close? "
                                        + forceExit);

                                if (forceExit) {
                                    BTGPSRawLogMidlet.quitApp();
                                }
                            }
                        }
                        break;

                    case DISPLAY_INTERSTITIAL_AD:
                        System.out.println("IADView.displayInterstitialAd()");
                        Hashtable interstitialData = new Hashtable();
                        interstitialData.put(IaOptionalParams.Key_Age, "30");
                        interstitialData.put(IaOptionalParams.Key_Gender, "F");
                        interstitialData.put(IaOptionalParams.Key_Gps_Location,
                                "53.542132,-2.239856");
                        interstitialData.put(IaOptionalParams.Key_Keywords, "Games");
                        interstitialData.put(IaOptionalParams.Key_Location, "US");
                        interstitialData.put(IaOptionalParams.Key_interstitial_GO_btn, "GO");
                        interstitialData.put(IaOptionalParams.Key_interstitial_SKIP_btn, "SKIP");
                        IADView.displayInterstitialAd(BTGPSRawLogMidlet.instance,
                                "MyCompany_MyApp", interstitialData, BTGPSRawLogMidlet.instance);
                        break;

                    case GET_BANNER_AD:
                        System.out.println("IADView.getBannerAd()");
                        Hashtable metaData = new Hashtable();

                        metaData.put(IaOptionalParams.Key_Age, "30");
                        metaData.put(IaOptionalParams.Key_Gender, "F");
                        metaData.put(IaOptionalParams.Key_Gps_Location, "53.542132,-2.239856");
                        metaData.put(IaOptionalParams.Key_Keywords, "Games");
                        metaData.put(IaOptionalParams.Key_Location, "US");

                        ad = IADView.getBannerAdData(BTGPSRawLogMidlet.instance, "MyCompany_MyApp",
                                metaData);
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

                        break;

                    case EXIT:
                        BTGPSRawLogMidlet.quitApp();
                        break;
                    }
                } catch (Exception e) {
                    mOwner.append(e.getMessage());
                } catch (Throwable e) {
                    mOwner.append(e.getMessage());
                }
            }
        }

        synchronized public boolean doTask(int task) {
            if (mTask != IDLE)
                return false;
            mTask = task;
            notify();
            return true;
        }
    }

    private void jbInit() throws Exception {
        append(imageItem);
    }
}
