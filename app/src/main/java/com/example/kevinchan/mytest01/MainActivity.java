package com.example.kevinchan.mytest01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Mat;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.imgproc.Imgproc;

import android.widget.AdapterView.OnItemSelectedListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements CvCameraViewListener2, OnItemSelectedListener {

    Spinner spinner_menu;

    CameraBridgeViewBase mOpenCvCameraView; //tells when camera connected it will enable view

    String[] menu_items;

    String menu_item_selected;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.menu_items = getResources().getStringArray(R.array.spinner_menu);
        this.menu_item_selected = menu_items[0]; //init 1st item

        Log.i("SPINNER", "menu item is " + this.menu_item_selected); //log slected item

        //spinner handle
        spinner_menu = (Spinner)findViewById(R.id.spinner_menu);


        //ArrayAdapter using string arr and spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_menu, android.R.layout.simple_spinner_item);


        //apply adapter to spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //init to index 0
        spinner_menu.setAdapter(adapter);
        spinner_menu.setSelection(0);

        spinner_menu.setOnItemSelectedListener(this);




        //grab a "handle" to the OpenCV class responsible for viewing Image
        // look at the XML the id of our CamerBridgeViewBase is HelloOpenCVView
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.HelloOpenCvView);
        mOpenCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);//the activity will listen to events on Camera
    }


    //check to see if camrea is connected and enables mOpenCVCamreaView
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OPENCV", "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    //  if we need to resume this Activity will sync up with OpenCV version software
    @Override  public void onResume()  {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
    }

    //pause camera
    @Override
    public void onPause()   {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }


    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();   }

    public void onCameraViewStarted(int width, int height) {   }

    public void onCameraViewStopped() {   }

    // THIS IS THE main method that is called each time you get a new Frame/Image
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat imageMat = inputFrame.rgba();
        Mat gray = inputFrame.gray();

        // now you use the Mat class to represent the Image and you can use method calls
        // like imageMat
        // make calls like to get a pixel at i,j   imageMat.get
        // double pixel[] = new double[3];
        // pixel = imageMat.get(20,10);  this wil retrieve pixel and column = 20, row =10
        //similarly can set a pixel in Mat  via imageMat.set(i,j,pixel);
        // read API on Mat class for OPenCV

        // A VERY USEFUL class to call image processing routines is ImagProc
        // This code in comments shows how to do the Sobel Edge Detection on our image in imageMat
       /*
            Mat gray = inputFrame.gray();
            Mat mIntermediateMat = new Mat();
            Imgproc.Sobel(gray, mIntermediateMat, CvType.CV_8U, 1, 1);
            Core.convertScaleAbs(mIntermediateMat, mIntermediateMat, 10, 0);
            Imgproc.cvtColor(mIntermediateMat, imageMat, Imgproc.COLOR_GRAY2BGRA, 4);

         */


        //random selected
        //get random number
        //if < .5 return color
        //else return greyscale image
        if(this.menu_item_selected.equals("Random")){
            Random rand = new Random(System.currentTimeMillis());


            if (rand.nextDouble() < 0.5){
                Log.d("SPINNER", "return color");
                return imageMat;
            }
            else{
                Log.d("SPINNER", "return greyscale");
                return gray;
            }
        }
        //greyscale selected return grey
        else if (this.menu_item_selected.equals("Greyscale")){
            Log.d("SPINNER", "return greyscale");
            return gray;
        }
        //thresh
        else if (this.menu_item_selected.equals("Threshold")){
            Log.i("SPINNER", "performing thresholding");
            Imgproc.threshold(gray,gray,50.0,255.0,Imgproc.THRESH_BINARY);
            return gray;
        }
        //else return gray
        else{
            return gray;
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        this.menu_item_selected = parent.getItemAtPosition(pos).toString();
        Log.i("SPINNER", "choice is" + this.menu_item_selected);
    }

    public void onNothingSelected(AdapterView<?> parent){
        this.menu_item_selected = this.menu_items[0];
    }
}

