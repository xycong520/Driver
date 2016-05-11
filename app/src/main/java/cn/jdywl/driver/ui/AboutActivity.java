package cn.jdywl.driver.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.VersionItem;

public class AboutActivity extends AppUpdateActivity {
    public static final String TAG = LogHelper.makeLogTag(AboutActivity.class);

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_version)
    TextView tvVersion;
    @Bind(R.id.tv_update)
    TextView tvUpdate;
    @Bind(R.id.tv_new)
    TextView tvNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        setupToolbar();

        tvVersion.setText(getString(R.string.versonName, getVersionName(), getVersionCode()));
    }

    /**
     * 检查更新
     *
     * @param view
     */
    public void checkUpdate(View view) {
        if(hasNewVersion())
        {
            VersionItem ver = getVertionItem();

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
            builder.setTitle("更新到新版本(v" + ver.getVersionName() + ")");
            builder.setMessage(ver.getDesc())
                    .setPositiveButton("立即下载", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                            downloadAPK();
                        }
                    })
                    .setNegativeButton("稍后再说", null);
            // Create the AlertDialog object and return it
            builder.create().show();
        }else {
            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("已是最新版本，无需更新！");

            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }

    /**
     * 访问主页
     *
     * @param view
     */
    public void visitHomepage(View view) {
        Uri uri = Uri.parse(ApiConfig.WEB_HOME);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        startActivity(intent);
    }


    public void updateNotify(boolean bNewVersion, VersionItem ver) {
        if (bNewVersion) {
            tvUpdate.setText("立即更新");
            tvNew.setTextColor(getResources().getColor(R.color.colorAccent));
            tvNew.setText("检测到新版本");

        } else {
            tvUpdate.setText("检查更新");
            tvNew.setTextColor(getResources().getColor(R.color.gray_content));
            tvNew.setText("已是最新版本");
        }
    }

}
