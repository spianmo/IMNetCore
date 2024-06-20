package com.teamhelper.imsdk.protocol;


import com.teamhelper.imsdk.constant.DefaultFrom;
import com.teamhelper.imsdk.utils.JSONUtils;

import java.util.UUID;

import cn.teamhelper.signal.protocol.ProtocolProto;
import cn.teamhelper.signal.protocol.ProtocolProto.Platform;

/**
 * @Description:
 * @Author: ZhangLuo (Moss)
 * @Email: zhangluo.chn@gmail.com
 * @Date: 2023/10/24
 */
public class ProtocolFactory {

    public static ProtocolProto.Protocol convertBytesToProtobuf(byte[] bytes) {
        // 解析字节数组为 Protobuf 消息
        try {
            return ProtocolProto.Protocol.parseFrom(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 全属性的构造
     *
     * @param type
     * @param from
     * @param platform
     * @param to
     * @param qos
     * @return
     */
    public static ProtocolProto.Protocol create(int type, String from, Platform platform,
                                                String to, boolean qos) {
        return create(type, from, platform, to, qos, "", -1);
    }

    /**
     * 全属性的构造
     *
     * @param type
     * @param from
     * @param platform
     * @param to
     * @param qos
     * @param dataContent
     * @param <T>
     * @return
     */
    public static <T> ProtocolProto.Protocol create(int type, String from, Platform platform,
                                                    String to, boolean qos, T dataContent, String fp, int typeu) {
        ProtocolProto.Protocol protocol = ProtocolProto.Protocol.newBuilder().setType(type).setFrom(from).setPlatform(platform).setTo(to).setQos(qos)
                .setDataContent(dataContent instanceof String ? (String) dataContent : JSONUtils.toString(dataContent))
                .setFp(fp).setTypeu(typeu).build();
        return protocol;
    }

    /**
     * 创建系统通用消息
     *
     * @param to
     * @param qos
     * @param dataContent
     * @param typeu
     * @param <T>
     * @return
     */
    public static <T> ProtocolProto.Protocol createSystemCommonData(String to, boolean qos, T dataContent, int typeu) {
        String fp = UUID.randomUUID().toString();
        return create(ProtocolType.C.FROM_CLIENT_TYPE_OF_COMMON$DATA, DefaultFrom.SERVER, Platform.SERVER,
                to, qos, dataContent, fp, typeu);
    }

    /**
     * 创建用户通用消息
     *
     * @param from
     * @param platform
     * @param to
     * @param qos
     * @param dataContent
     * @param typeu
     * @param <T>
     * @return
     */
    public static <T> ProtocolProto.Protocol createUserCommonData(String from, Platform platform,
                                                                  String to, boolean qos, T dataContent, int typeu) {
        String fp = UUID.randomUUID().toString();
        return create(ProtocolType.C.FROM_CLIENT_TYPE_OF_COMMON$DATA, from, platform,
                to, qos, dataContent, fp, typeu);
    }

    /**
     * 全属性的构造
     *
     * @param type
     * @param from
     * @param platform
     * @param to
     * @param qos
     * @param dataContent
     * @param <T>
     * @return
     */
    public static <T> ProtocolProto.Protocol create(int type, String from, Platform platform,
                                                    String to, boolean qos, T dataContent, int typeu) {
        String fp = UUID.randomUUID().toString();
        return create(type, from, platform, to, qos, dataContent, fp, typeu);
    }

    /**
     * 默认QOS和重试次数的消息体
     *
     * @param type
     * @param from
     * @param platform
     * @param to
     * @param dataContent
     * @param <T>
     * @return
     */
    public static <T> ProtocolProto.Protocol create(int type, String from, Platform platform,
                                                    String to, T dataContent) {
        return create(type, from, platform, to, true, dataContent, -1);
    }


    /**
     * 通用消息的Protocal报文对象新建方法（typeu字段默认-1）。
     * <p>
     * <font color="#0000ff"><b>友情提示：</b></font>为了您能定义更优雅的IM协议，
     * 建议优先使用typeu定义您的协议类型，而不是使用默认的-1。
     *
     * @param dataContent  要发送的消息内容
     * @param from_user_id 发送人的user_id
     * @param to_user_id   接收人的user_id
     * @param QoS          是否需要QoS支持，true表示需要，否则不需要
     * @param fingerPrint  消息指纹特征码，为null则表示由系统自动生成指纹码，否则使用本参数指明的指纹码
     * @return 新建的{@link Protocal}报文对象
     */
    public static ProtocolProto.Protocol createCommonData(String dataContent, String from_user_id, Platform platform, String to_user_id, boolean QoS, String fingerPrint) {
        return createCommonData(dataContent, from_user_id, platform, to_user_id, QoS, fingerPrint, -1);
    }

    /**
     * 通用消息的Protocol报文对象新建方法。
     *
     * @param dataContent  要发送的消息内容
     * @param from_user_id 发送人的user_id
     * @param to_user_id   接收人的user_id
     * @param QoS          是否需要QoS支持，true表示需要，否则不需要
     * @param fingerPrint  消息指纹特征码，为null则表示由系统自动生成指纹码，否则使用本参数指明的指纹码
     * @param typeu        应用层专用字段——用于应用层存放聊天、推送等场景下的消息类型，不需要设置时请填-1即可
     * @return 新建的{@link Protocal}报文对象
     */
    public static ProtocolProto.Protocol createCommonData(String dataContent, String from_user_id, Platform platform,
                                                          String to_user_id, boolean QoS, String fingerPrint, int typeu) {
        ProtocolProto.Protocol protocol = ProtocolProto.Protocol.newBuilder().setType(ProtocolType.C.FROM_CLIENT_TYPE_OF_COMMON$DATA).setFp(from_user_id)
                .setPlatform(platform).setTo(to_user_id).setQos(QoS).setFp(fingerPrint).setTypeu(typeu).build();
        return protocol;
    }

    /**
     * 服务端发出
     * 默认QOS和重试次数的消息体
     *
     * @param type
     * @param to
     * @param dataContent
     * @param <T>
     * @return
     */
    public static <T> ProtocolProto.Protocol createOfflineMsg(int type, String to, T dataContent) {
        return create(type, DefaultFrom.SERVER, Platform.SERVER, to, false, dataContent, -1);
    }

    /**
     * 创建ACK消息体
     *
     * @param protocol
     * @return
     */
    public static ProtocolProto.Protocol createAck(ProtocolProto.Protocol protocol) {
        ProtocolProto.Protocol ack = ProtocolProto.Protocol.newBuilder().setType(ProtocolType.C.FROM_CLIENT_TYPE_OF_RECIVED).setFrom(DefaultFrom.SERVER)
                .setPlatform(Platform.SERVER).setTo(protocol.getFrom()).setToPlatform(protocol.getPlatform())
                .setQos(false).setFp(protocol.getFp()).setTypeu(-1).build();
        return ack;
    }

    /**
     * 克隆消息
     *
     * @param protocol
     * @return
     */
    public static ProtocolProto.Protocol clone(ProtocolProto.Protocol protocol) {
        String jsonString = JSONUtils.toString(protocol);
        return JSONUtils.parseObject(jsonString, ProtocolProto.Protocol.class);
    }
}
