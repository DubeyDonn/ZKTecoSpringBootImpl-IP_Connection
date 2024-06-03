package com.zkt.zktspring.sdk.commands;

import com.zkt.zktspring.sdk.Enum.CommandReplyCodeEnum;
import com.zkt.zktspring.sdk.utils.HexUtils;

import java.text.ParseException;
import java.util.Date;

public class GetTimeReply extends ZKCommandReply {

    private final Date deviceDate;

    public GetTimeReply(CommandReplyCodeEnum code, int sessionId, int replyId, int[] payloads) throws ParseException {
        super(code, sessionId, replyId, payloads);

        String payloadsStr = HexUtils.bytesToHex(payloads);

        long encDate = Integer.valueOf(payloadsStr.substring(6, 8), 16) * 0x1000000L
                + (Integer.valueOf(payloadsStr.substring(4, 6), 16) * 0x10000L)
                + (Integer.valueOf(payloadsStr.substring(2, 4), 16) * 0x100L)
                + (Integer.valueOf(payloadsStr.substring(0, 2), 16));

        deviceDate = HexUtils.extractDate(encDate);
    }

    /**
     * @return the deviceDate
     */
    public Date getDeviceDate() {
        return deviceDate;
    }

}
