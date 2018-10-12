package com.example.pedestrian.dermatologicaldiagnosis;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private List<Illness> list;
    private TextView resultView;
    private TextView textView;
    private Button button;
    private TextView tipsView;
    private File file;

    private static final String chi_cls_0 = "正常人脸";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultactivity);

        resultView = (TextView)findViewById(R.id.resultView);
        textView = (TextView)findViewById(R.id.textView);
        tipsView = (TextView)findViewById(R.id.tipsView);
        button = (Button)findViewById(R.id.send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBitmapToSD(shotActivity(ResultActivity.this));
                ArrayList<Uri> list_uri = new ArrayList<Uri>();
                list_uri.addAll(MainActivity.list_uri);
                list_uri.add(Uri.fromFile(file));
                Intent mulIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                String[] tos = { "zhoubaokuan2007@163.com" };
                String[] ccs = { "yunhyan@me.neu.edu.cn" };
                mulIntent.putExtra(Intent.EXTRA_EMAIL, tos);
                mulIntent.putExtra(Intent.EXTRA_CC, ccs);
                mulIntent.putExtra(Intent.EXTRA_TEXT, "周宝宽大夫您好！这是我的面部皮肤病诊断结果，请您帮忙看一下！");
                mulIntent.putExtra(Intent.EXTRA_SUBJECT, "面部皮肤病诊断");
                mulIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, list_uri);
                mulIntent.setType("image/*");
                mulIntent.setType("message/rfc882");
                Intent.createChooser(mulIntent, "Choose Email Client");
                startActivity(mulIntent);
            }
        });
        final Intent intent = getIntent();
        list = (List<Illness>)intent.getSerializableExtra("result");
        Illness illness = list.get(0);
        if(illness.getProbability() <= 0.70 && list.size() <6)
            tipsView.setText("提示：多加几张图片，识别率会更高哦～");
        if(illness.getProbability() <= 0.50) {
            resultView.setText("未发现任何病情");
        }
        else if(illness.getProbability() > 0.50 & illness.getIllness() == chi_cls_0) {
            resultView.setText("面部正常(" + String.format("%.2f", illness.getProbability() * 100.0) + "%)");
        }
        else {
            resultView.setText(illness.getIllness() + "(" + String.format("%.2f", illness.getProbability() * 100.0) + "%)");
            textView.setText("处理意见：");
        }
    }

    public Bitmap shotActivity(Activity ctx) {

        View view = ctx.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        Bitmap bp = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getMeasuredWidth(),
                view.getMeasuredHeight());

        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();

        return bp;
    }

    protected void saveBitmapToSD(Bitmap bt) {
        try {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/data/" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) + ".jpg");
            FileOutputStream  out = new FileOutputStream(file);
            bt.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
