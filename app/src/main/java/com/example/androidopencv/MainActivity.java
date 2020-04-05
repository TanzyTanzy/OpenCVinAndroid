package com.example.androidopencv;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    CameraBridgeViewBase cameraBridgeViewBase;
    BaseLoaderCallback baseLoaderCallback;
    int counter = 0;
    int area;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cameraBridgeViewBase = (JavaCameraView)findViewById(R.id.CameraView);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);


        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        baseLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                super.onManagerConnected(status);

                switch(status){

                    case BaseLoaderCallback.SUCCESS:
                        cameraBridgeViewBase.enableView();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }


            }

        };




    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        Mat frame = inputFrame.rgba();
        Mat target1 = new Mat();


            Mat hsv = new Mat();
            Mat mask1 = new Mat();
            Mat mask2 = new Mat();
            Mat mask = new Mat();
            Mat target = new Mat();
            Scalar lower = new Scalar(-1, 100, 100);
            Scalar upper = new Scalar(5, 255, 255);
            Scalar lower1 = new Scalar(130, 100, 100);
            Scalar upper1 = new Scalar(190, 255, 255);
            Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_BGR2HSV);
            Core.inRange(hsv, lower, upper, mask1);
            Core.inRange(hsv, lower1, upper1, mask2);
            Core.bitwise_or(mask1, mask2, mask);
            Core.bitwise_and(frame, frame, target, mask);
            Mat search=new Mat();
            Imgproc.dilate(target1,search,Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5,5));
            Imgproc.cvtColor(search,search,Imgproc.COLOR_BGR2GRAY);
            List<MatOfPoint> contours = new ArrayList<>();
            Mat hierarchy = new Mat();
            Imgproc.findContours(search,contours,hierarchy,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
            Iterator<MatOfPoint> each = contours.iterator();
            each = contours.iterator();
            while (each.hasNext()) {
                MatOfPoint contour = each.next();
                Rect rect = Imgproc.boundingRect(contour);
                area=rect.height*rect.width;
                if(area>4000)
                    continue;
                Rect roi = new Rect(rect.x, rect.y, rect.width+1, rect.x+rect.height+1);
                Mat cropimg =frame.submat(roi);
                Mat test_image=new Mat();
                test_image=cropimg;
                Imgproc.cvtColor(test_image,test_image,Imgproc.COLOR_BGR2GRAY);
                Imgproc.resize(test_image,test_image,new Size(128,128));

                test_image = np.array(test_image)
                test_image = test_image.astype('float32')

                test_image= np.expand_dims(test_image, axis=3)
                test_image= np.expand_dims(test_image, axis=0)





//            Bitmap bmp = null;

//            Imgproc.cvtColor(target, target1, Imgproc.COLOR_BGR2RGB);
//        bmp = Bitmap.createBitmap(target1.cols(), target1.rows(), Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(target1, bmp);
//
//
//
////            subimg.release();
//        Imgcodecs imageCodecs = new Imgcodecs();
//        String file1="/home/tanzy/Downloads";
//        if(counter==10)
//            imageCodecs.imwrite(file1, target1);
//            counter+=1;
            return frame;

    }


    @Override
    public void onCameraViewStarted(int width, int height) {

    }


    @Override
    public void onCameraViewStopped() {

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(),"There's a problem, yo!", Toast.LENGTH_SHORT).show();
        }

        else
        {
            baseLoaderCallback.onManagerConnected(baseLoaderCallback.SUCCESS);
        }



    }

    @Override
    protected void onPause() {
        super.onPause();
        if(cameraBridgeViewBase!=null){

            cameraBridgeViewBase.disableView();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraBridgeViewBase!=null){
            cameraBridgeViewBase.disableView();
        }
    }
}