package com.mohan.autocam;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission_group.CAMERA;

public class CaptureCameraImage extends AppCompatActivity  implements View.OnClickListener{

	public static int cameraID = 0;
	public static boolean isBlack = true;
	public static ImageView image;
	private static final int PERMISSION_REQUEST_CODE = 200;
	private View view;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitycapturecameraimage);
        image = (ImageView) findViewById(R.id.imgView);
		Button check_permission = (Button) findViewById(R.id.check_permission);
		Button request_permission = (Button) findViewById(R.id.request_permission);
		check_permission.setOnClickListener(this);
		request_permission.setOnClickListener(this);
    }

    public void onFrontClick(View v){
    	RadioButton rdbBlack = (RadioButton) findViewById(R.id.rdb_black);
    	if(rdbBlack.isChecked()){
    		isBlack = true;
    	}else{
    		isBlack = false;
    	}
		cameraID = 1;
		Intent i = new Intent(CaptureCameraImage.this,CameraView.class);
        startActivityForResult(i, 999);
	}
    
	public void onBackClick(View v){
		RadioButton rdbBlack = (RadioButton) findViewById(R.id.rdb_black);
    	if(rdbBlack.isChecked()){
    		isBlack = true;
    	}else{
    		isBlack = false;
    	}
    	cameraID = 0;
		Intent i = new Intent(CaptureCameraImage.this,CameraView.class);
        startActivityForResult(i, 999);
	}


	// Returns true if external storage for photos is available
	private boolean isExternalStorageAvailable() {
		String state = Environment.getExternalStorageState();
		return state.equals(Environment.MEDIA_MOUNTED);
	}

	@Override
	public void onClick(View v) {

		view = v;

		int id = v.getId();
		switch (id) {
			case R.id.check_permission:
				if (checkPermission()) {

					Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show();

				} else {

					Snackbar.make(view, "Please request permission.", Snackbar.LENGTH_LONG).show();
				}
				break;
			case R.id.request_permission:
				if (!checkPermission()) {

					requestPermission();

				} else {

					Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show();

				}
				break;

			case R.id.camera:
				askForPermission(Manifest.permission.CAMERA,PERMISSION_REQUEST_CODE);
				break;
		}

	}

	private boolean checkPermission() {
		int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
		int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

		return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
	}

	private void requestPermission() {

		ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA}, PERMISSION_REQUEST_CODE);

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case PERMISSION_REQUEST_CODE:

				Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
					startActivityForResult(takePictureIntent, 12);
				}

				/*if (grantResults.length > 0) {

					boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
					boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

					if (locationAccepted && cameraAccepted)
						Snackbar.make(view, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
					else {

						Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
							if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
								showMessageOKCancel("You need to allow access to both the permissions",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
													requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA},
															PERMISSION_REQUEST_CODE);
												}
											}
										});
								return;
							}
						}

					}
				}*/


				break;
		}
	}


	private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
		new AlertDialog.Builder(CaptureCameraImage.this)
				.setMessage(message)
				.setPositiveButton("OK", okListener)
				.setNegativeButton("Cancel", null)
				.create()
				.show();
	}

	private void askForPermission(String permission, Integer requestCode) {
		if (ContextCompat.checkSelfPermission(CaptureCameraImage.this, permission) != PackageManager.PERMISSION_GRANTED) {

			// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale(CaptureCameraImage.this, permission)) {

				//This is called if user has denied the permission before
				//In this case I am just asking the permission again
				ActivityCompat.requestPermissions(CaptureCameraImage.this, new String[]{permission}, requestCode);

			} else {

				ActivityCompat.requestPermissions(CaptureCameraImage.this, new String[]{permission}, requestCode);
			}
		} else {

			Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show();
		}
	}
}
