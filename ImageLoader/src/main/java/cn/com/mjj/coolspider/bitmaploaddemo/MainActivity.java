package cn.com.mjj.coolspider.bitmaploaddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {
    private ImageView imageviewBtn;

    //1.绑定一个View （View不能为private 或者static）
    @BindView(R.id.textview)
    TextView mTxt;

    //1.绑定一个View （View不能为private 或者static）
    @BindView(R.id.mlistview)
    ListView mlistview;

    //2.给一个View添加点击事件
    @OnClick(R.id.textview)
    public void onImageClick() {
        Toast.makeText(this, "onImageClick", Toast.LENGTH_SHORT).show();
    }
    //3.给多个View添加点击事件：
    @OnClick({R.id.mlistview, R.id.mtextview})
    public void onclick() {
        Toast.makeText(this, "onImageClick", Toast.LENGTH_SHORT).show();
    }

    //4.给ListView setItemClickListener
    @OnItemClick(R.id.mlistview)
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        Toast.makeText(this, "onItemClick" + position, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Picasso.with(this).load("").into(imageviewBtn);



        Glide.with(MainActivity.this)
                .load("http://www.baidu.com/img/bdlogo.png")
                .into(imageviewBtn);
    }
}
