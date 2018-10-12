package com.example.pedestrian.dermatologicaldiagnosis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class DiagnosisActivity extends AppCompatActivity {

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
    private TextView illness;
    private TextView treatment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diagnosisactivity);

        illness = (TextView)findViewById(R.id.illness);
        treatment = (TextView)findViewById(R.id.treatment);

        treatment.setMovementMethod(LinkMovementMethod.getInstance());

        switch (getIntent().getStringExtra("illness")){
            case chi_cls_0:
                illness.setText(chi_cls_0);
                treatment.setText(Html.fromHtml("illness:" + "<a href='https://www.baidu.com/'>" + chi_cls_0 + "</a>"));
                break;
            case chi_cls_1:
                illness.setText(chi_cls_1);
                treatment.setText(Html.fromHtml("illness:" + "<a href='https://www.baidu.com/'>" + chi_cls_1 + "</a>"));
                break;
            case chi_cls_2:
                illness.setText(chi_cls_2);
                treatment.setText(Html.fromHtml("illness:" + "<a href='https://www.baidu.com/'>" + chi_cls_2 + "</a>"));
                break;
            case chi_cls_3:
                illness.setText(chi_cls_3);
                treatment.setText(Html.fromHtml("illness:" + "<a href='https://www.baidu.com/'>" + chi_cls_3 + "</a>"));
                break;
            case chi_cls_4:
                illness.setText(chi_cls_4);
                treatment.setText(Html.fromHtml("illness:" + "<a href='https://www.baidu.com/'>" + chi_cls_4 + "</a>"));
                break;
            case chi_cls_5:
                illness.setText(chi_cls_5);
                treatment.setText(Html.fromHtml("illness:" + "<a href='https://www.baidu.com/'>" + chi_cls_5 + "</a>"));
                break;
            case chi_cls_6:
                illness.setText(chi_cls_6);
                treatment.setText(Html.fromHtml("illness:" + "<a href='https://www.baidu.com/'>" + chi_cls_6 + "</a>"));
                break;
            case chi_cls_7:
                illness.setText(chi_cls_7);
                treatment.setText(Html.fromHtml("illness:" + "<a href='https://www.baidu.com/'>" + chi_cls_7 + "</a>"));
                break;
            case chi_cls_8:
                illness.setText(chi_cls_8);
                treatment.setText(Html.fromHtml("illness:" + "<a href='https://www.baidu.com/'>" + chi_cls_8 + "</a>"));
                break;
            case chi_cls_9:
                illness.setText(chi_cls_9);
                treatment.setText(Html.fromHtml("illness:" + "<a href='https://www.baidu.com/'>" + chi_cls_9 + "</a>"));
                break;
            case chi_cls_10:
                illness.setText(chi_cls_10);
                treatment.setText(Html.fromHtml("illness:" + "<a href='https://www.baidu.com/'>" + chi_cls_10 + "</a>"));
                break;
        }
    }
}
