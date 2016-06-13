package bone.com.manybugproject.base;

import android.annotation.SuppressLint;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import bone.com.manybugproject.R;
import bone.com.manybugproject.constant.StringConstant;
import bone.com.manybugproject.interfaces.JavaScriptinterface;
import bone.com.manybugproject.utils.LogUtil;
import bone.com.manybugproject.view.BaseWebView;
import butterknife.Bind;

/**
 * 通用的基本WebView
 * 创建者:赵然
 */
public class CommonWebViewActivity extends BaseActivity {

    @Bind(R.id.bwv_commowebviewacvivity)
    BaseWebView mWebView;

    private  String loadUrl;

    @Override
    public void loadLayout() {
        setContentView(R.layout.activity_common_web_view);
    }

    @Override
    public void logic() {
        hiddenTitlebar();
        if (getIntent() != null){
           loadUrl = getIntent().getStringExtra(StringConstant.PARAMS_URL);
            initWebView();
        }

    }

    @Override
    public void loadListener() {

    }

    @Override
    public void request() {

    }

    @Override
    public void clickRequest() {

    }
    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack(); // 后退
            return; // 已处理
        } else {
            finish();
        }
    }
    /**
     * 初始化WebView
     */

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setIsBackEnable(true);
        mWebView.setShowLoading(true);
        setH5LocalStorageEnable();
        mWebView.loadUrl(loadUrl);

        mWebView.addJavascriptInterface(new JavaScriptinterface() {

            @Override
            @JavascriptInterface
            public void notifyOnAndroid(String str) {
                LogUtil.zLog().e("JS:"+str);
                //TODO 拿到js传回的json  json格式自己定义，，，解析处理---web页传值给安卓
            }

            @Override
            @JavascriptInterface
            public String getSQNum() {
                //TODO js调用安卓的方法 安卓传值给web页面

                return null;
            }

        }, StringConstant.WEBVIEW_JS_JSNAME);


        mWebView.setWebViewClient(mWebView.new BaseWebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(mWebView.new BaseWebChromeClient() {
        });
    }
    /**
     * 添加H5中使用本地缓存 的支持
     */
    private void setH5LocalStorageEnable(){
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getCacheDir().getAbsolutePath();
        mWebView.getSettings().setAppCachePath(appCachePath);
    }
}
