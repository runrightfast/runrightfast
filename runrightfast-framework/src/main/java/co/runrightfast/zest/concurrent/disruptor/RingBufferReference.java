/* Copyright (C) RunRightFast.co - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Alfio Zappala azappala@azaptree.com, March 2014
 */
package co.runrightfast.zest.concurrent.disruptor;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author alfio
 * @param <DATA> reference to RingBuffer
 */
public final class RingBufferReference<DATA> {

    public DATA data;

    @Override
    public String toString() {
        return data != null ? data.toString() : StringUtils.EMPTY;
    }

}
