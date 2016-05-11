package cn.jdywl.driver.ui.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.ResponseEntry;
import cn.jdywl.driver.network.GsonRequest;

public class InsuranceActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = LogHelper.makeLogTag(InsuranceActivity.class);

    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.image)
    ImageView image;
    @Bind(R.id.previous)
    Button previous;
    @Bind(R.id.next)
    Button next;
    @Bind(R.id.ll_btn)
    LinearLayout llBtn;
    @Bind(R.id.ll_pdf)
    RelativeLayout llPdf;
    @Bind(R.id.tv_insstatus)
    TextView tvInsstatus;

    private DownloadPDFTask mTask = null;
    private String filename;

    /**
     * File descriptor of the PDF.
     */
    private ParcelFileDescriptor mFileDescriptor;

    /**
     * {@link PdfRenderer} to render the PDF.
     */
    private PdfRenderer mPdfRenderer;

    /**
     * Page that is currently shown on the screen.
     */
    private PdfRenderer.Page mCurrentPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance);
        ButterKnife.bind(this);
        setupToolbar();

        //TODO:判断保单状态，如果保单未生成，显示网页
        filename = getIntent().getStringExtra("filename");

        String url = getIntent().getStringExtra("url");

        getInsStatus(url);
        showProgress(true);

        /*
        showProgress(true);

        mTask = new DownloadPDFTask(filename);
        mTask.execute((Void) null);

        */
    }

    /**
     * Sets up a {@link PdfRenderer} and related resources.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void openRenderer(Context context) throws IOException {
        // In this sample, we read a PDF from the assets directory.
        //mFileDescriptor = context.getAssets().openFd("sample.pdf").getParcelFileDescriptor();
        File cacheDir = this.getFilesDir();
        //create a new file, specifying the path, and the filename
        //which we want to save the file as.
        File file = new File(cacheDir, filename);
        mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        // This is the PdfRenderer we use to render the PDF.
        mPdfRenderer = new PdfRenderer(mFileDescriptor);
    }

    /**
     * Closes the {@link PdfRenderer} and related resources.
     *
     * @throws IOException When the PDF file cannot be closed.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void closeRenderer() throws IOException {
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }
        if (null != mPdfRenderer) {
            mPdfRenderer.close();
        }
        if (null != mFileDescriptor) {
            mFileDescriptor.close();
        }
    }

    /**
     * Shows the specified page of PDF to the screen.
     *
     * @param index The page index.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void showPage(int index) {
        if (mPdfRenderer == null) {
            return;
        }
        if (mPdfRenderer.getPageCount() <= index) {
            return;
        }
        // Make sure to close the current page before opening another one.
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }
        // Use `openPage` to open a specific page in PDF.
        mCurrentPage = mPdfRenderer.openPage(index);
        // Important: the destination bitmap must be ARGB (not RGB).
        Bitmap bitmap = Bitmap.createBitmap(mCurrentPage.getWidth(), mCurrentPage.getHeight(),
                Bitmap.Config.ARGB_8888);
        // Here, we render the page onto the Bitmap.
        // To render a portion of the page, use the second and third parameter. Pass nulls to get
        // the default result.
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        // We are ready to show the Bitmap to user.
        image.setImageBitmap(bitmap);
        updateUi();
    }

    /**
     * Updates the state of 2 control buttons in response to the current page index.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void updateUi() {
        int index = mCurrentPage.getIndex();
        int pageCount = mPdfRenderer.getPageCount();
        previous.setEnabled(0 != index);
        next.setEnabled(index + 1 < pageCount);
        setToolbarTitle(getString(R.string.activity_insurance_with_index, index + 1, pageCount));
    }

    /**
     * Gets the number of pages in the PDF. This method is marked as public for testing.
     *
     * @return The number of pages.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public int getPageCount() {
        return mPdfRenderer.getPageCount();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.previous: {
                // Move to the previous page
                showPage(mCurrentPage.getIndex() - 1);
                break;
            }
            case R.id.next: {
                // Move to the next page
                showPage(mCurrentPage.getIndex() + 1);
                break;
            }
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class DownloadPDFTask extends AsyncTask<Void, Void, Boolean> {

        private final String mFilename;

        DownloadPDFTask(String filename) {
            mFilename = filename;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            return downloadInsurancePDF(filename);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mTask = null;
            showProgress(false);

            if (success) {
                try {
                    openRenderer(InsuranceActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(InsuranceActivity.this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    InsuranceActivity.this.finish();
                }

                // Bind events.
                previous.setOnClickListener(InsuranceActivity.this);
                next.setOnClickListener(InsuranceActivity.this);
                llBtn.setVisibility(View.VISIBLE);
                llPdf.setVisibility(View.VISIBLE);

                showPage(0);
            } else {
                Toast.makeText(InsuranceActivity.this, "保险合同正在生成，请您稍等...", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mTask = null;
            showProgress(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            closeRenderer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getInsStatus(String url) {
        GsonRequest<ResponseEntry> myReq = new GsonRequest<ResponseEntry>(Request.Method.GET,
                url,
                ResponseEntry.class,
                null,
                new Response.Listener<ResponseEntry>() {
                    @Override
                    public void onResponse(ResponseEntry response) {

                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }

                        if (response.getStatusCode() == 100) {
                            //showProgress(true);
                            mTask = new DownloadPDFTask(filename);
                            mTask.execute((Void) null);
                        } else {
                            //Toast.makeText(InsuranceActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                            tvInsstatus.setVisibility(View.VISIBLE);
                            tvInsstatus.setText(response.getMessage());
                            showProgress(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showProgress(false);
                        Helper.processVolleyErrorMsg(InsuranceActivity.this, error);
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }

    boolean downloadInsurancePDF(String filename) {
        try {
            //set the download URL, a url that points to a file on the internet
            //this is the file to be downloaded

            URL url = new URL(ApiConfig.api_url + "assets/insurance/" + filename);

            //create the new connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //set up some things on the connection
            urlConnection.setRequestMethod("GET");
            //urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            //and connect!
            urlConnection.connect();

            int response = urlConnection.getResponseCode();

            //set the path where we want to save the file
            //in this case, going to save it on the root directory of the
            //sd card.
            File cacheDir = this.getFilesDir();
            //create a new file, specifying the path, and the filename
            //which we want to save the file as.
            File file = new File(cacheDir, filename);
            if (file.exists()) {
                LogHelper.i(TAG, "保单已经存在，直接打开。");
                return true;
            }

            //this will be used to write the downloaded data into the file we created
            FileOutputStream fileOutput = new FileOutputStream(file);

            //this will be used in reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file
            int totalSize = urlConnection.getContentLength();
            //variable to store total downloaded bytes
            int downloadedSize = 0;

            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0; //used to store a temporary size of the buffer

            //now, read through the input buffer and write the contents to the file
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                //add the data in the buffer to the file in the file output stream (the file on the sd card
                fileOutput.write(buffer, 0, bufferLength);
                //add up the size so we know how much is downloaded
                downloadedSize += bufferLength;
                //this is where you would do something to report the prgress, like this maybe
                //updateProgress(downloadedSize, totalSize);

            }
            //close the output stream when done
            fileOutput.close();

            return true;

            //catch some possible errors...
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        progressBar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_insurance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_insurance) {
            String url = ApiConfig.api_url + "assets/insurance/" + filename;
            Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(it);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setRefreshEnabled(boolean enabled) {

    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }
}
