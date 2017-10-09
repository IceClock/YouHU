package com.example.abood.youhu;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.UUID;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class UploadActivity extends Fragment{





    private Button upload;
    private EditText editText;
    public Uri filePath; // file url to store image/video
    private Button btnCapturePicture, btnRecordVideo;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private TextView textViewResponse;
    private TextView locationtext;
    private String selectedPath;
public int imgorvid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_upload, container, false);

        requestStoragePermission();

        btnCapturePicture = (Button) view.findViewById(R.id.btmCaptureImage);
        btnRecordVideo = (Button) view.findViewById(R.id.btnRecordVideo);
        upload = (Button) view.findViewById(R.id.buttonUpload);
        editText = (EditText) view.findViewById(R.id.editTextName);
        textViewResponse = (TextView) view.findViewById(R.id.textViewResponse);
        locationtext = (TextView) view.findViewById(R.id.location);

        upload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

    if (imgorvid == MEDIA_TYPE_IMAGE){uploadImage();}

    else if(imgorvid == MEDIA_TYPE_VIDEO) {uploadVideo();
}

            }
        });


        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                pickimg();


            }
        });

        btnRecordVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // record video
                pickvideo();

            }
        });

        return view;
    }




    private void pickimg() {
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Select Picture"), MEDIA_TYPE_IMAGE);
// Always show the chooser (if there are multiple options available
    }
    private void pickvideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent, "Select Video"), MEDIA_TYPE_VIDEO);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VideoView videoView = (VideoView) getActivity().findViewById(R.id.videoP);
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.imgP);

      imgorvid = requestCode;
        if (requestCode == MEDIA_TYPE_IMAGE  && data != null && data.getData() != null) {
            filePath = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);

                   imageView.setImageBitmap(bitmap);


                    locationtext.setVisibility(View.INVISIBLE);
                    textViewResponse.setVisibility(View.INVISIBLE);
                    videoView.setVisibility(View.INVISIBLE);

                    imageView.setVisibility(View.VISIBLE);
                    editText.setVisibility(View.VISIBLE);
                    upload.setVisibility(View.VISIBLE);


                } catch (IOException e) {
                    e.printStackTrace();
                }
        }


        else if (requestCode == MEDIA_TYPE_VIDEO  && data != null && data.getData() != null) {
            filePath = data.getData();

            videoView.setVideoURI(filePath);


            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                            VideoView videoView = (VideoView) getActivity().findViewById(R.id.videoP);

                            MediaController mc = new MediaController(getContext());
                            mc.setAnchorView(videoView);
                            mc.setMediaPlayer(videoView);
                            videoView.setMediaController(mc);

                            mc = new MediaController(getContext());
                            videoView.setMediaController(mc);

                            mc.setAnchorView(videoView);

                        }
                    });
                }
            });

               videoView.start();
            selectedPath = getPath2(filePath);
            locationtext.setText(selectedPath);


            imageView.setVisibility(View.INVISIBLE);
            editText.setVisibility(View.INVISIBLE);

            locationtext.setVisibility(View.VISIBLE);
            textViewResponse.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.VISIBLE);
            upload.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", filePath);
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContext().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();

        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    public String getPath2(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }

    // start video upload


    private void uploadVideo() {
        class UploadVideo extends AsyncTask<Void, Void, String> {

            ProgressDialog uploading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                uploading = ProgressDialog.show(getActivity(), "Uploading File", "Please wait...", false, false);
                uploading.setCancelable(true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                uploading.dismiss();
                textViewResponse.setText(Html.fromHtml("<b>Uploaded at: <a href='" + s + "'>" + s + "</a></b>"));
                textViewResponse.setMovementMethod(LinkMovementMethod.getInstance());
            }

            @Override
            protected String doInBackground(Void... params) {
                Upload u = new Upload();
                String msg = u.upLoad2Server(selectedPath);
                return msg;
            }
        }
        UploadVideo uv = new UploadVideo();
        uv.execute();
    }



    // end video upload


    public void uploadImage() {

        //getting name for the image
        String name = editText.getText().toString().trim();

        //getting the actual path of the image

        String path = getPath(filePath);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(getActivity(), uploadId, Constants.UPLOAD_URL)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", name) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getActivity(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getActivity(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


}