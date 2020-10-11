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
import com.thunderbolt.mining.CpuMiner;
import com.thunderbolt.mining.Job;
import com.thunderbolt.mining.contracts.IJobFinishListener;
import com.thunderbolt.security.Sha256Digester;
import com.thunderbolt.security.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* IMPLEMENTATION ************************************************************/


/**
 * Application main class.
 */
public class Main
{
    // Static variables
    private static final Logger s_logger = LoggerFactory.getLogger(Main.class);

    /**
     * Application entry point.
     *
     * @param args Arguments.
     */
    public static void main(String[] args) throws InterruptedException
    {
        byte[] midstate = Convert.hexStringToByteArray("DC5CCE02AC97D4C690130350751573B0F0C05B9C438C240AFA63AD11CD5CA18E");
        byte[] data     = Convert.hexStringToByteArray("47BE4F1B5AE5B41E1D07FFF800000000800000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000280");

        CpuMiner miner = new CpuMiner();

        miner.addJobFinishListener(ended ->
        {
            s_logger.debug("Job {} ended.", ended.getId());
            s_logger.debug("Block {}.", ended.isSolved() ? "Solved" : "Not Solved");
            s_logger.debug("Nonce: {} ({})",
                    Convert.toHexString(NumberSerializer.serialize(ended.getNonce())),
                    ended.getNonce());

            if (ended.isSolved())
                miner.cancelAllJobs();
        });

        Job job  = new Job (midstate, data,1);
        miner.start();
        miner.queueJob(job);

        while (true)
        {
            Thread.sleep(1000);
        }
    }
}