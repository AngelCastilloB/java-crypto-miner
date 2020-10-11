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
        /*
        # This will produce nonce 063c5e01 -> debug by using a bogus URL
block = "0000000120c8222d0497a7ab44a1a2c7bf39de941c9970b1dc7cdc400000079700000000e88aabe1f353238c668d8a4df9318e614c10c474f8cdf8bc5f6397b946c33d7c4e7242c31a098ea500000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000"
midstate = "33c5bf5751ec7f7e056443b5aee3800331432c83f404d9de38b94ecbf907b92d"
         */

        //byte[] midstate = Convert.hexStringToByteArray("DC5CCE02AC97D4C690130350751573B0F0C05B9C438C240AFA63AD11CD5CA18E");
        //byte[] data     = Convert.hexStringToByteArray("47BE4F1B5AE5B41EF8FF071D00000000800000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000280");
        byte[] midstate = Convert.hexStringToByteArray("33c5bf5751ec7f7e056443b5aee3800331432c83f404d9de38b94ecbf907b92d");
       // byte[] data     = Convert.hexStringToByteArray("46c33d7c4e7242c31a098ea500000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000");
        byte[] data     = Convert.hexStringToByteArray("46c33d7c4e7242c31a098ea500000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000");



//2020-10-11 23:29:49 DEBUG Main                : Nonce: 0726DC0D (119987213)

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

        Sha256Digester digester = new Sha256Digester();

        //7891EA91
        //91EA9178
        digester.hash(reverseEndian(Convert.hexStringToByteArray("0000000100000000000000000000000000000000000000000000000000000000000000005AF68BCC531AA80DA2F5A3AC2931F5D2CBF9CFEB0ADD431848F1F67247BE4F1B5AE5B41E1D07FFF800000000")));
        s_logger.debug(Convert.toHexString(reverseEndian(digester.getMidstate(0))));
        s_logger.debug(Convert.toHexString(reverseEndian(digester.getBlock(1))));

        s_logger.debug(
                Convert.toHexString(
                        reverse(Sha256Digester.digest(
                        Sha256Digester.digest(
                                reverseEndian(Convert.hexStringToByteArray("0000000120c8222d0497a7ab44a1a2c7bf39de941c9970b1dc7cdc400000079700000000e88aabe1f353238c668d8a4df9318e614c10c474f8cdf8bc5f6397b946c33d7c4e7242c31a098ea5015e3c06"))
                        ).getData()).getData())));

        s_logger.debug(
                Convert.toHexString(
                        reverse(Sha256Digester.digest(
                                Sha256Digester.digest(
                                        reverseEndian(Convert.hexStringToByteArray("0000000100000000000000000000000000000000000000000000000000000000000000005AF68BCC531AA80DA2F5A3AC2931F5D2CBF9CFEB0ADD431848F1F67247BE4F1B5AE5B41E1D00FFFF91EA9178"))
                                ).getData()).getData())));

        s_logger.debug(
                Convert.toHexString(
                        reverse(Sha256Digester.digest(
                                Sha256Digester.digest(
                                        Convert.hexStringToByteArray("0100000000000000000000000000000000000000000000000000000000000000000000003BA3EDFD7A7B12B27AC72C3E67768F617FC81BC3888A51323A9FB8AA4B1E5E4A29AB5F49FFFF001D1DAC2B7C")
                                ).getData()).getData())));
                            //000000000019d6689c085ae165831e934ff763ae46a2a6c172b3f1b60a8ce26f
                            //6FE28C0AB6F1B372C1A6A246AE63F74F931E8365E15A089C68D6190000000000*/



/*
        Job job  = new Job (midstate, data,1);
        job.setNonce(104619519);
        miner.start();
        miner.queueJob(job);
     Job job2  = new Job (midstate, data,2);
        job.setNonce(1073741820);
        miner.start();
        miner.queueJob(job2);

        Job job3  = new Job (midstate, data,3);
        job.setNonce(1073741820 * 2);
        miner.start();
        miner.queueJob(job3);

        Job job4  = new Job (midstate, data,4);
        job.setNonce(1073741820 * 3);
        miner.start();
        miner.queueJob(job4);*/

        while (true)
        {
            Thread.sleep(1000);
        }
    }
    private static byte[] reverse(byte[] data)
    {
        byte[] result = new byte[data.length];

        for (int i = 0; i < data.length; ++i)
        {
            result[i] = data[data.length - 1 - i];
        }

        return result;
    }
    static byte[] reverseEndian(byte[] data)
    {
        int blocks = data.length / 4;
        byte[] reversed = new byte[data.length];

        for (int i = 0; i < blocks; ++i)
        {
            for (int j = 0; j < 4; ++j)
            {
                reversed[i * 4 + j] = data[i * 4 + (3 - j)];
            }
        }

        return reversed;
    }
}