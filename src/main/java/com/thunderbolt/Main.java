/*
 * Copyright (c) 2020 Angel Castillo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thunderbolt;

/* IMPORTS *******************************************************************/

import com.thunderbolt.common.Convert;
import com.thunderbolt.common.NumberSerializer;
import com.thunderbolt.mining.Job;
import com.thunderbolt.mining.miners.CpuMiner;
import com.thunderbolt.mining.miners.GekkoScienceNewpacMiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/* IMPLEMENTATION ************************************************************/


/**
 * Application main class.
 */
public class Main
{
    // Static variables
    private static final Logger s_logger = LoggerFactory.getLogger(Main.class);
    private static final Object s_lock  = new Object();
    /**
     * Application entry point.
     *
     * @param args Arguments.
     */
    public static void main(String[] args) throws InterruptedException
    {
        byte[] midstate = Convert.hexStringToByteArray("05D387352B75D4529F235910CCDDAEB836B7C9629AB6DFAF4249F9CAB90B4481");
        byte[] data     = Convert.hexStringToByteArray("1B4FBE471EB4E55AFFFF001D7891EA91800000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000280");

        GekkoScienceNewpacMiner miner = new GekkoScienceNewpacMiner();

        miner.addJobFinishListener(ended ->
        {
            s_logger.debug("Job {} ended.", ended.getId());
            s_logger.debug("Block {}.", ended.isSolved() ? "Solved" : "Not Solved");

            // Nonce must be provided to the application as big endian. Since it will be reverted before hashing
            // so we need to make sure that when it is reverted the hash matches.
            s_logger.debug("Nonce: {} ({})",
                    Convert.toHexString(NumberSerializer.serialize(Integer.reverseBytes((int)ended.getNonce()))),
                    Integer.reverseBytes((int)ended.getNonce()));
            s_logger.debug("Hash:  {}",
                    Convert.toHexString(ended.getHash().getData()));

            if (ended.isSolved())
                miner.cancelAllJobs();
        });

        miner.start();

        Job job  = new Job(midstate, data,(short)0x75);
        miner.queueJob(job);

        while(true)
        {
            Thread.sleep(100);
        }
    }
}