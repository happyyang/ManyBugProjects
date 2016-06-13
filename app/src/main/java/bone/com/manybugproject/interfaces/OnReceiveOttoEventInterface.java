package bone.com.manybugproject.interfaces;

import com.squareup.otto.Subscribe;

import bone.com.manybugproject.entity.OttoEventEntity;

/**
 * 功能： otto  接收发送的消息
 * ＊创建者：赵然 on 16/1/26 15:37
 * ＊
 */
public interface OnReceiveOttoEventInterface {
    /**
     * 接收 Otto消息 请在实现此类的方法上添加 @Subscribe注解 否则无效
     * @param eventEntity
     */
    @Subscribe
    void OnReceiveEvent(OttoEventEntity eventEntity);
}
