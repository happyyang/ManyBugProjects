package bone.com.manybugproject.net;


/**
 * 请求网络的回调
 *
 * @author 赵然
 * @date 2016年05月18日00:40:48
 */
public interface RequestListener {

    /**
     * 请求开始的时候
     *
     * @param url
     */
     void onHttpRequestBegin(String url) ;

    /**
     * 请求之后会回调的方法
     *
     * @param url
     * @param httpContext
     */
     void onHttpRequestSuccess(String url, HttpContext httpContext);

    /**
     * 请求失败回调的接口
     *
     * @param url
     */
     void onHttpRequestFailed(String url, HttpContext httpContext, Throwable e);

    /**
     * 请求结束之后（不管成功或者失败，都会执行本方法）
     *
     * @param url
     * @param httpContext 请求回来的对象
     */
    void onHttpRequestComplete(String url, HttpContext httpContext);

    /**
     * @param url
     * @param httpContext
     */
     void onHttpRequestCancel(String url, HttpContext httpContext);

    /**
     * 返回码异常的统一处理
     */

    void  onHttpResponseCodeException(String url, HttpContext httpContext, int status);
}
