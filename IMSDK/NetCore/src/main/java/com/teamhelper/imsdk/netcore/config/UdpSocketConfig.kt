package com.teamhelper.imsdk.netcore.config

import androidx.annotation.Keep

@Keep
object UdpSocketConfig {

    /**
     * 连接超时时间(ms)
     */
    var CONNECT_TIMEOUT = 10 * 1000
    var ENABLE_KCP = false

    /**
     *         conv = 0x11223344;
     *         // normal mode
     *         nodelay = 0;
     *         interval = 40;
     *         fastresend = 0;
     *         nocwnd = 0;
     *         // fast mode
     *         // nodelay = 1;
     *         // interval = 10;
     *         // fastresend = 2;
     *         // nocwnd = 1;
     *
     *         sndwnd = 0;
     *         rcvwnd = 0;
     *         mtu = 1400;
     *         update_interval = 10; // ms
     */
    var KCP_CONV = 0x11223344
    var KCP_NODELAY = 0
    var KCP_INTERVAL = 40
    var KCP_FAST_RESEND = 0
    var KCP_NOCWND = 0
    var KCP_SNDWND = 0
    var KCP_RCVWND = 0
    var KCP_MTU = 1400
    var KCP_UPDATE_INTERVAL = 10
}
