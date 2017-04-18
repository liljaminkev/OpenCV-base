package com.example.kevinchan.mytest01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity implements CvCameraViewListener2 {

    CameraBridgeViewBase mOpenCvCameraView;
    //this code will tell us when Camera connected it will enable view

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        //grab a "handle" to the OpenCV class responsible for viewing Image
        // look at the XML the id of our CamerBridgeViewBase is HelloOpenCVView
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.HelloOpenCvView);
        mOpenCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);//the activity will listen to events on Camera
    }


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

        return imageMat;

    }
}
