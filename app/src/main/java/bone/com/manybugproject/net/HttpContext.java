package bone.com.manybugproject.net;

/**
 * @author LiuYuHang Date: 2015年5月29日
 *         <p/>
 *         Modifier： Modified Date： Modify：
 *         <p/>
 *         Copyright @ 2015 Corpration CHSY
 * @Package com.ichsy.libs.core.net
 * @File HttpContext.java
 */
public class HttpContext {
    private Object lock1 = new Object();
    private Object lock2 = new Object();
    private long requestTime;
//    private Context context;
    /**
     * 请求的数据string
     */
    private String request;
    private Object requestObject;
    private Class<?> responseClass;
    /**
     * 返回的数据string
     */
    private String response;
    private Object responseObject;
    /**
     * 请求标记 可用于同一接口 同时请求多次 拿到回调时区别使用哪个请求结果
     */
    private Object requestTag;


    /**
     * 每次网络请求的唯一标示 用于缓存key
     */
    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        synchronized (lock2) {
            return response;
        }
    }

    public void setResponse(String response) {
        synchronized (lock2) {
            this.response = response;
        }
    }

    public Object getRequestObject() {
        return requestObject;
    }

    public void setRequestObject(Object requestObject) {
        this.requestObject = requestObject;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public Class<?> getResponseClass() {
        return responseClass;
    }

    public void setResponseClass(Class<?> responseClass) {
        this.responseClass = responseClass;
    }


    @SuppressWarnings("unchecked")
    public <T> T getResponseObject() {
        synchronized (lock1) {
            return (T) responseObject;
        }
    }

    public void setResponseObject(Object responseObject) {
        synchronized (lock1) {
            this.responseObject = responseObject;
        }
    }

    public Object getRequestTag() {
        return requestTag;
    }

    public void setRequestTag(Object requestTag) {
        this.requestTag = requestTag;
    }
}
