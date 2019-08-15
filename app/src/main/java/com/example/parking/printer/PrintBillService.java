package com.example.parking.printer;


import android.app.IntentService;
import android.content.Intent;
import android.device.PrinterManager;
import android.util.Log;
import com.example.parking.bean.PrintBillBean;


public class PrintBillService extends IntentService {


    private static final String TAG = "PrintBillService<打印机服务>";

    private PrinterManager printer;

    public PrintBillService() { super("bill"); }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        printer = new PrinterManager();
        //初始化
        printer.setupPage(384, -1);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {

            int type = intent.getIntExtra("type", 0);

            PrintBillBean printBillBean = (PrintBillBean) intent.getSerializableExtra("SPRT");

            Log.i(TAG, printBillBean.toString());

            if (type == 1) {

                gaozi(printBillBean);
            } else if (type == 2) {

                soufei(printBillBean);
            } else if (type == 3) {

                taodan(printBillBean);
            } else if (type == 4) {

                bujiao(printBillBean);
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    private void gaozi( PrintBillBean printBillBean ){

        try {

            //打印Title
            printer.drawTextEx("告知", 130, 0, 380, -1, "simsun", 50, 0, 0, 0);
            printer.drawTextEx(printBillBean.getData(), 0, 0, 385, -1, "simsun", 28, 0, 0, 0);
            sendBroadcast(new Intent("android.prnt.message").putExtra("ret", printer.printPage(0)));

            //收费二维码
            super.onCreate();
            printer = new PrinterManager();
            printer.setupPage(384, -1);
            printer.prn_drawBarcode(printBillBean.getQRcode(), 75, 10, 58, 6, 6, 0);
            sendBroadcast(new Intent("android.prnt.message").putExtra("ret", printer.printPage(0)));

            //最下面的空格
            super.onCreate();
            printer = new PrinterManager();
            printer.setupPage(384, -1);
            printer.drawTextEx(("扫码支付\r\n\r\n"), 80, 0, 380, -1, "simsun", 50, 0, 0, 0);
            // printer.drawTextEx("\r\n\r\n\r\n", 0, 0, 385, -1, "simsun", 28, 0, 0, 0);
            sendBroadcast(new Intent("android.prnt.message").putExtra("ret", printer.printPage(0)));


        } catch (Exception e) {

            Log.w(TAG, e);
        }

    }

    private void soufei( PrintBillBean printBillBean ){

        try {

            //打印Title
            printer.drawTextEx("收费", 130, 0, 380, -1, "simsun", 50, 0, 0, 0);
            printer.drawTextEx(printBillBean.getData(), 0, 0, 385, -1, "simsun", 28, 0, 0, 0);
            sendBroadcast(new Intent("android.prnt.message").putExtra("ret", printer.printPage(0)));

//            //打印文字"扫码支付"
//            super.onCreate();
//            printer = new PrinterManager();
//            printer.setupPage(384, -1);
//            printer.drawTextEx(("扫码查看"), 80, 0, 380, -1, "simsun", 50, 0, 0, 0);
//            sendBroadcast(new Intent("android.prnt.message").putExtra("ret", printer.printPage(0)));

//            //收费二维码
//            super.onCreate();
//            printer = new PrinterManager();
//            printer.setupPage(384, -1);
//            printer.prn_drawBarcode(printBillBean.getQRcode(), 75, 10, 58, 6, 6, 0);
//            sendBroadcast(new Intent("android.prnt.message").putExtra("ret", printer.printPage(0)));

            //最下面的空格
            super.onCreate();
            printer = new PrinterManager();
            printer.setupPage(384, -1);
            printer.drawTextEx(("\r\n\r\n"), 80, 0, 380, -1, "simsun", 50, 0, 0, 0);
           // printer.drawTextEx("\r\n\r\n\r\n", 0, 0, 385, -1, "simsun", 28, 0, 0, 0);
            sendBroadcast(new Intent("android.prnt.message").putExtra("ret", printer.printPage(0)));


        } catch (Exception e) {

            Log.w(TAG, e);
        }
    }

    private void taodan( PrintBillBean printBillBean ){

        try {

            //打印Title
            printer.drawTextEx("补缴", 130, 0, 380, -1, "simsun", 50, 0, 0, 0);
            printer.drawTextEx(printBillBean.getData(), 0, 0, 385, -1, "simsun", 28, 0, 0, 0);
            sendBroadcast(new Intent("android.prnt.message").putExtra("ret", printer.printPage(0)));

            //收费二维码
            super.onCreate();
            printer = new PrinterManager();
            printer.setupPage(384, -1);
            printer.prn_drawBarcode(printBillBean.getQRcode(), 75, 10, 58, 6, 6, 0);
            sendBroadcast(new Intent("android.prnt.message").putExtra("ret", printer.printPage(0)));

            //最下面的空格
            super.onCreate();
            printer = new PrinterManager();
            printer.setupPage(384, -1);
            printer.drawTextEx(("扫码补缴\r\n\r\n"), 80, 0, 380, -1, "simsun", 50, 0, 0, 0);
            // printer.drawTextEx("\r\n\r\n\r\n", 0, 0, 385, -1, "simsun", 28, 0, 0, 0);
            sendBroadcast(new Intent("android.prnt.message").putExtra("ret", printer.printPage(0)));


        } catch (Exception e) {

            Log.w(TAG, e);
        }
    }

    private void bujiao( PrintBillBean printBillBean ){

        try {

            //打印Title
            printer.drawTextEx("补缴", 130, 0, 380, -1, "simsun", 50, 0, 0, 0);
            printer.drawTextEx(printBillBean.getData(), 0, 0, 385, -1, "simsun", 28, 0, 0, 0);
            sendBroadcast(new Intent("android.prnt.message").putExtra("ret", printer.printPage(0)));

            //最下面的空格
            super.onCreate();
            printer = new PrinterManager();
            printer.setupPage(384, -1);
            printer.drawTextEx("\r\n\r\n\r\n", 0, 0, 385, -1, "simsun", 28, 0, 0, 0);
            sendBroadcast(new Intent("android.prnt.message").putExtra("ret", printer.printPage(0)));
        } catch (Exception e) {

            Log.w(TAG, e);
        }
    }


}