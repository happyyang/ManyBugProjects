package bone.com.manybugproject.entity;

/**
 * 功能： otto 事件传递的实体
 * ＊创建者：赵然 on 16/1/26 14:26
 * ＊
 */
public class OttoEventEntity {
    /**
     *事件类型
     */
    private OttoEventType Type;
    /**
     * 传递的数据
     */
    private Object datas;
    /**
     * 标记
     */
    private String tag;


    public OttoEventType getType() {
        return Type;
    }

    /**
     *
     * @param type  事件类型
     */
    public void setType(OttoEventType type) {
        Type = type;
    }

    public Object getDatas() {
        return datas;
    }

    public void setDatas(Object datas) {
        this.datas = datas;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
