package dev.alfianh.scanapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private String TAG = "Scan Result";
    TextView tvResult;
    Button btnScan;
    MarshMallowPermission permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permission = new MarshMallowPermission(this);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
        if (permission.checkPermissionForCamera()) {
            mScannerView.startCamera();
        } else {
            permission.requestPermissionForCamera();
            mScannerView.startCamera();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        if (permission.checkPermissionForCamera()) {
            mScannerView.startCamera();
        } else {
            permission.requestPermissionForCamera();
            mScannerView.startCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();  // Stop camera on pause
    }

    @Override
    public void handleResult(Result result) {
// Do something with the result here
        Log.v(TAG, result.getText()); // Prints scan results
        Log.v(TAG, result.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        // If you would like to resume scanning, call this method below:
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Result");
        builder.setMessage(result.getText());
        builder.setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mScannerView.resumeCameraPreview(MainActivity.this);
            }
        });
        builder.show();
    }
}
