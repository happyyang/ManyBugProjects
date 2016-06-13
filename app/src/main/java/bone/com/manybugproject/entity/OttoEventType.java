package bone.com.manybugproject.entity;

/**
 * 功能：Otto 传递事件的时候 消息的类型
 * ＊创建者：赵然 on 16/1/26 14:29
 * ＊
 */
public enum  OttoEventType {
    /**
     * 修改用户信息后 - 返回我的界面
     */
    USERIFNO_MODIFY,
    /**
     * 修改用户信息--地址
     */
    USERIFNO_MODIFY_ADDRESS,
    /**
     * 修改用户信息--添加地址
     */
    USERIFNO_MODIFY_ADDADDRESS,
    /**
     * 购买流程选择收货地址
     */
    SHOPPING_GET_GOODSADRESS,
    /**
     * 取消收藏  用于 收藏列表 帖子详情
     */
    CANCLE_COLLECTION,
    /**
     * 取消关注
     */
    CANCLE_MARKATTENTION,
    /**
     * 标记关注
     */
    MARK_ATTENTION;

}
