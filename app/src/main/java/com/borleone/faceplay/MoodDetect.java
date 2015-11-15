package com.borleone.faceplay;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.faceplay.emotion.EmotionUtil;
import com.faceplay.emotion.FacePlaylist;
import com.faceplay.emotion.ProjectOxfordEmotionRecognizer;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Pushkar Borle on 11/14/2015.
 */
public class MoodDetect extends Activity {

    private static final String TAG = "CallCamera";
    private static final int CAMERA_REQUEST = 1;
    Uri fileUri = null;
    private ImageView iv = null;
    private static final int REQUEST_WRITE_STORAGE = 112;
    List<FacePlaylist> playList = new ArrayList<FacePlaylist>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
        setContentView(R.layout.mood_detect);
        iv = (ImageView) findViewById(R.id.imageView1);
        Button camButton = (Button) this.findViewById(R.id.button);
//        Button musicButton = (Button) this.findViewById(R.id.button1);
        Button playListButton = (Button) this.findViewById(R.id.button2);


        camButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getOutputPhotoFile();
                fileUri = Uri.fromFile(getOutputPhotoFile());
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(i, CAMERA_REQUEST);
            }
        });

/*
        musicButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent k = new Intent(MoodDetect.this, MusicList.class);
                startActivity(k);
            }
        });
*/

        playListButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MoodDetect.this, SpotifyList.class);
                //intent.putExtra("List", playList);

                Bundle informacion = new Bundle();
                informacion.putSerializable("List", (Serializable) playList);
                intent.putExtras(informacion);
//                intent.putStringArrayListExtra("list", playList);
                startActivity(intent);
            }
        });

    }


    private File getOutputPhotoFile() {
        File directory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), getPackageName());
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Log.e(TAG, "Failed to create storage directory.");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.US).format(new Date());
        return new File(directory.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri photoUri = null;
                if (data == null) {
                    // A known bug here! The image should have saved in fileUri
                    Toast.makeText(this, "Image saved successfully",
                            Toast.LENGTH_LONG).show();
                    photoUri = fileUri;
                } else {
                    photoUri = data.getData();
                    Toast.makeText(this, "Image saved successfully in: " + data.getData(),
                            Toast.LENGTH_LONG).show();
                }
                System.out.println(photoUri.toString());
                System.out.println(photoUri.getEncodedPath().toString());
                showPhoto(photoUri);
                ProjectOxfordEmotionRecognizer recog = new ProjectOxfordEmotionRecognizer();
                try {
                    recog.submitImage(new URL(photoUri.toString()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                String emo = recog.getEmotion().getEmotion();
                Double scr = recog.getEmotion().getScore();
                if (emo.equals("happiness")) {
                    if (scr * 100 % 10 >= 8)
                        emo = "excite";
                    else if (scr * 100 % 10 >= 5)
                        emo = "energy";
                    else if (scr * 100 % 10 < 3)
                        emo = "joy";
                }
                if (emo.equals("neutral")) {
                    if (scr * 100 % 10 >= 7)
                        emo = "classical";
                    else if (scr * 100 % 10 >= 4)
                        emo = "peace";
                    else if (scr * 100 % 10 < 3)
                        emo = "calm";
                }
                if (emo.equals("anger")) {
                    if (scr * 100 % 10 >= 7)
                        emo = "metal";
                    else if (scr * 100 % 10 >= 4)
                        emo = "mad";
                }
                if (emo.equals("sadness")) {
                    if (scr * 100 % 10 >= 7)
                        emo = "blues";
                    else if (scr * 100 % 10 >= 5)
                        emo = "soul";
                    else if (scr * 100 % 10 < 3)
                        emo = "heartbreak";
                }
                if (emo.equals("disgust")) {
                    if (scr * 100 % 10 >= 6)
                        emo = "horror";
                    else if (scr * 100 % 10 >= 3)
                        emo = "sick";
                }
                if (emo.equals("surprise")) {
                    if (scr * 100 % 10 >= 6)
                        emo = "wonder";
                }
                if (emo.equals("fear")) {
                    if (scr * 100 % 10 >= 6)
                        emo = "scare";
                }

                List<FacePlaylist> playListForMood = EmotionUtil.getPlayListForMood(emo);
                Log.e("playList", "" + playListForMood.size());
                playList = playListForMood;
                //playListForMood.get(0).getImageURL();
                Toast.makeText(this, recog.getEmotion().getEmotion() + "", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Callout for image capture failed!",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showPhoto(Uri photoUri) {
        String filePath = photoUri.getEncodedPath();
        File imageFile = new File(filePath);
        if (imageFile.exists()) {
            Drawable oldDrawable = iv.getDrawable();
            if (oldDrawable != null) {
                ((BitmapDrawable) oldDrawable).getBitmap().recycle();
            }
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            BitmapDrawable drawable = new BitmapDrawable(this.getResources(), bitmap);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            iv.setImageDrawable(drawable);  //setImageBitmap(Bitmap)
            iv.setRotation(-90);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //reload my activity with permission granted
                } else {
                    Toast.makeText(this.getParent(), "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}
