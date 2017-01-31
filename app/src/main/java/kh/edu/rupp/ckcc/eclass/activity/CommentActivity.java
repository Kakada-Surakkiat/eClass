package kh.edu.rupp.ckcc.eclass.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import kh.edu.rupp.ckcc.eclass.R;
import kh.edu.rupp.ckcc.eclass.app.AppConstant;
import kh.edu.rupp.ckcc.eclass.utility.Utils;
import kh.edu.rupp.ckcc.eclass.vo.Comment;

import static android.speech.SpeechRecognizer.RESULTS_RECOGNITION;

/**
 * eClass
 * Created by leapkh on 24/1/17.
 */

public class CommentActivity extends ToolbarActivity implements View.OnClickListener {

    private final int IMAGE_CAPTURE_REQUEST_CODE = 1;

    private EditText etxtComment;
    private ImageView imgCamera;
    private ImageButton btnMic;

    private String courseId;
    private MediaRecorder recorder;
    private MediaPlayer player;
    private String audioFileName;

    private SpeechRecognizer recognizer;
    private Intent recognizerIntent;
    private Uri imageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comment);
        etxtComment = (EditText) findViewById(R.id.etxt_comment);
        imgCamera = (ImageView) findViewById(R.id.img_camera);
        imgCamera.setOnClickListener(this);
        btnMic = (ImageButton) findViewById(R.id.btn_mic);
        findViewById(R.id.btn_add).setOnClickListener(this);

        courseId = getIntent().getStringExtra(CategoryActivity.EXTRA_COURSE_KEY);

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        audioFileName = Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".mp3";
        recorder.setOutputFile(audioFileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        btnMic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d("ckcc", "ACTION_DOWN");
                    try {
                        recorder.prepare();
                        recorder.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.d("ckcc", "ACTION_UP");
                    recorder.stop();
                }
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("ckcc", "Data: " + data);
        if (requestCode == IMAGE_CAPTURE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data == null) {
                Bitmap thumbnail = Utils.decodeUri(this, imageUri, 220);
                imgCamera.setImageBitmap(thumbnail);
            } else {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                imgCamera.setImageBitmap(thumbnail);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_camera) {
            onCameraClick();
        } else if (v.getId() == R.id.btn_mic) {
            onMicClick();
        } else if (v.getId() == R.id.btn_add) {
            addComment();
        }
    }

    private void addComment() {
        String fileName = System.currentTimeMillis() + ".jpg";
        String directoryName = "feedback-images/";
        StorageReference imageRef = FirebaseStorage.getInstance().getReference(directoryName + fileName);
        imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("ckcc", "Upload image success");
            }
        });

        Comment comment = new Comment(courseId, etxtComment.getText().toString(), fileName);
        DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference(AppConstant.REF_COMMENTS).child(courseId).push();
        commentsRef.setValue(comment);
        commentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("ckcc", "Add comment success");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ckcc", "Add comment fail: " + databaseError.getMessage());
            }
        });
    }

    private void onCameraClick() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String imageFileName = System.currentTimeMillis() + ".jpg";
        File imageFile = null;
        try {
            imageFile = new File(directory, imageFileName);
            imageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageUri = FileProvider.getUriForFile(this, "com.example.android.fileprovider", imageFile);
        //imageUri = Uri.fromFile(new File(directory + imageFileName)); // This method does not require FileProvider declaration in Manifest
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, IMAGE_CAPTURE_REQUEST_CODE);
    }

    private void onMicClick() {

    }

    private void onPlayClick() {
        player = new MediaPlayer();
        try {
            player.setDataSource(audioFileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onSpeechRecognitionClick() {
        if (recognizer == null) {
            recognizer = SpeechRecognizer.createSpeechRecognizer(this);
            recognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    Log.d("ckcc", "onResults");
                    for (String result : results.getStringArrayList(RESULTS_RECOGNITION)) {
                        Log.d("ckcc", "Result: " + result);
                    }
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
            recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        }
        recognizer.startListening(recognizerIntent);
    }

}
