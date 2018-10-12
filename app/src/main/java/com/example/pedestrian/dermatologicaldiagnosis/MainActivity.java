package com.example.pedestrian.dermatologicaldiagnosis;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.GridView;

import com.donkingliang.imageselector.utils.ImageSelectorUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Collections;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static ArrayList<Uri> list_uri = new ArrayList<Uri>();
    private List<Bitmap> list = new ArrayList<Bitmap>();
    private List<String> path = new ArrayList<String>();
    private GridView gallery;
    private TextView textView;
    private Button camera;
    private Button album;
    private Button diagnose;
    private Button about;
    private BitmpAdapter bitmpAdapter;
    private Uri uri;

    private Classifier classifier;
    private List<Classifier.Recognition> results;

    private static final String cls_0 = "normal";
    private static final String cls_1 = "acne";
    private static final String cls_2 = "brandy";
    private static final String cls_3 = "chloasma";
    private static final String cls_4 = "eczema";
    private static final String cls_5 = "flat_wart";
    private static final String cls_6 = "freckle";
    private static final String cls_7 = "leucoderma";
    private static final String cls_8 = "lupus";
    private static final String cls_9 = "melanosis";
    private static final String cls_10 = "psoriasis";

    private static final String chi_cls_0 = "正常人脸";
    private static final String chi_cls_1 = "痤疮";
    private static final String chi_cls_2 = "玫瑰痤疮";
    private static final String chi_cls_3 = "黄褐斑";
    private static final String chi_cls_4 = "湿疹";
    private static final String chi_cls_5 = "扁平疣";
    private static final String chi_cls_6 = "雀斑";
    private static final String chi_cls_7 = "白癜风";
    private static final String chi_cls_8 = "红斑狼疮";
    private static final String chi_cls_9 = "黑变病";
    private static final String chi_cls_10 = "银屑病";

    private float cfd_0 = 0;
    private float cfd_1 = 0;
    private float cfd_2 = 0;
    private float cfd_3 = 0;
    private float cfd_4 = 0;
    private float cfd_5 = 0;
    private float cfd_6 = 0;
    private float cfd_7 = 0;
    private float cfd_8 = 0;
    private float cfd_9 = 0;
    private float cfd_10 = 0;

    private static final int INPUT_SIZE = 299;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128;
    private static final String INPUT_NAME = "ExpandDims";
    private static final String OUTPUT_NAME = "InceptionResnetV2/Logits/Predictions";

    private static final String MODEL_FILE = "file:///android_asset/skin_graph.pb";
    private static final String LABEL_FILE = "file:///android_asset/labels.txt";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        gallery = findViewById(R.id.gallery);
        textView = findViewById(R.id.hint);
        camera = findViewById(R.id.camera);
        album = findViewById(R.id.album);
        diagnose = findViewById(R.id.diagnose);
        about = findViewById(R.id.about);
        bitmpAdapter = new BitmpAdapter(this, list);
        gallery.setAdapter(bitmpAdapter);

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.alert)
                        .setTitle("Delete")
                        .setMessage("确定删除该图片？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                list.remove(position);
                                list_uri.remove(position);
                                onResume();
                                bitmpAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setCancelable(true)
                        .show();

            }
        });

        camera.setOnClickListener(this);
        album.setOnClickListener(this);
        diagnose.setOnClickListener(this);
        about.setOnClickListener(this);

        classifier = TensorFlowImageClassifier.create(
                getAssets(),
                MODEL_FILE,
                LABEL_FILE,
                INPUT_SIZE,
                IMAGE_MEAN,
                IMAGE_STD,
                INPUT_NAME,
                OUTPUT_NAME);
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data){
        super.onActivityResult(reqCode, resCode, data);
        if(resCode == Activity.RESULT_OK){
            if(reqCode == 100){
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(uri, "image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.putExtra("crop", true);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 400);
                intent.putExtra("outputY", 400);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, 102);
            }else if(reqCode == 101){
                try {
                    path = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT);
                    for (String p : path) {
                        list.add(Bimp.revitionImageSize(p.toString()));
                        list_uri.add(Uri.fromFile(new File(p.toString())));
                    }
                    bitmpAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "成功添加照片", Toast.LENGTH_SHORT).show();
                }catch (IOException e) {
                }

            }else {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                list_uri.add(uri);
                list.add(bitmap);
                bitmpAdapter.notifyDataSetChanged();
                Toast.makeText(this, "成功添加照片", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "没有添加照片", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.camera:
                if(list.size() == 9) {
                    Toast.makeText(this, "已经达到最高选择数量", Toast.LENGTH_SHORT).show();
                    break;
                }
                uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) + ".jpg"));
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent1, 100);
                break;
            case R.id.album:
                if(list.size() == 9) {
                    Toast.makeText(this, "已经达到最高选择数量", Toast.LENGTH_SHORT).show();
                    break;
                }
                ImageSelectorUtils.openPhoto(MainActivity.this, 101, false, 9-list.size());
                break;
            case R.id.diagnose:
                if(list.isEmpty()){
                    Toast.makeText(this, "没有待诊断图片", Toast.LENGTH_SHORT).show();
                    break;
                }
                new Classification().execute(new Integer(list.size()));
                break;
            case R.id.about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(list.isEmpty()){
            textView.setVisibility(View.VISIBLE);

        }
        else {
            textView.setVisibility(View.GONE);
        }
    }

    class Classification extends AsyncTask<Integer, Integer, Void> {

        private ProgressDialog progressDialog =
                new ProgressDialog(MainActivity.this);

        @Override
        protected Void doInBackground(Integer... arg) {
            progressDialog.setProgress(0);
            progressDialog.setTitle("皮肤病诊断中……");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(arg[0].intValue());
            progressDialog.setCancelable(false);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.show();
                }
            });
            int i;
            for(i=0; i<list.size(); i++) {
                publishProgress(new Integer(i));
                results = classifier.recognizeImage(Bitmap.createScaledBitmap(list.get(i), INPUT_SIZE, INPUT_SIZE, true));
                Log.e("0", results.toString());
                int j;
                for(j=0; j<results.size(); j++){
                    Classifier.Recognition recognition = results.get(j);
                    switch (recognition.getTitle()){
                        case cls_0:
                            cfd_0 += recognition.getConfidence();
                            break;
                        case cls_1:
                            cfd_1 += recognition.getConfidence();
                            break;
                        case cls_2:
                            cfd_2 += recognition.getConfidence();
                            break;
                        case cls_3:
                            cfd_3 += recognition.getConfidence();
                            break;
                        case cls_4:
                            cfd_4 += recognition.getConfidence();
                            break;
                        case cls_5:
                            cfd_5 += recognition.getConfidence();
                            break;
                        case cls_6:
                            cfd_6 += recognition.getConfidence();
                            break;
                        case cls_7:
                            cfd_7 += recognition.getConfidence();
                            break;
                        case cls_8:
                            cfd_8 += recognition.getConfidence();
                            break;
                        case cls_9:
                            cfd_9 += recognition.getConfidence();
                            break;
                        case cls_10:
                            cfd_10 += recognition.getConfidence();
                            break;
                        default:
                            break;
                    }
                }
            }
            progressDialog.cancel();
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer...progress){
            progressDialog.setProgress(progress[0].intValue());

        }

        @Override
        protected void onPostExecute(Void result){
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            List<Illness> illnesses = new ArrayList<Illness>();
            if(cfd_0 >= 0.1 * list.size()) illnesses.add(new Illness(chi_cls_0, cfd_0 /list.size()));
            if(cfd_1 >= 0.1 * list.size()) illnesses.add(new Illness(chi_cls_1, cfd_1 /list.size()));
            if(cfd_2 >= 0.1 * list.size()) illnesses.add(new Illness(chi_cls_2, cfd_2 /list.size()));
            if(cfd_3 >= 0.1 * list.size()) illnesses.add(new Illness(chi_cls_3, cfd_3 /list.size()));
            if(cfd_4 >= 0.1 * list.size()) illnesses.add(new Illness(chi_cls_4, cfd_4 /list.size()));
            if(cfd_5 >= 0.1 * list.size()) illnesses.add(new Illness(chi_cls_5, cfd_5 /list.size()));
            if(cfd_6 >= 0.1 * list.size()) illnesses.add(new Illness(chi_cls_6, cfd_6 /list.size()));
            if(cfd_7 >= 0.1 * list.size()) illnesses.add(new Illness(chi_cls_7, cfd_7 /list.size()));
            if(cfd_8 >= 0.1 * list.size()) illnesses.add(new Illness(chi_cls_8, cfd_8 /list.size()));
            if(cfd_9 >= 0.1 * list.size()) illnesses.add(new Illness(chi_cls_9, cfd_9 /list.size()));
            if(cfd_10 >= 0.1 * list.size()) illnesses.add(new Illness(chi_cls_10, cfd_10 /list.size()));

            Collections.sort(illnesses);

            intent.putExtra("result", (Serializable) illnesses);

            cfd_0 = 0;
            cfd_1 = 0;
            cfd_2 = 0;
            cfd_3 = 0;
            cfd_4 = 0;
            cfd_5 = 0;
            cfd_6 = 0;
            cfd_7 = 0;
            cfd_8 = 0;
            cfd_9 = 0;
            cfd_10 = 0;
            startActivity(intent);
        }
    }
}