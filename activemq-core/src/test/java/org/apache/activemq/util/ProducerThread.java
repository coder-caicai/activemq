/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

public class ProducerThread extends Thread {

    private static final Log LOG = LogFactory.getLog(ProducerThread.class);

    int messageCount = 1000;
    Destination dest;
    Session sess;
    int sleep = 0;
    int sentCount = 0;

    public ProducerThread(Session sess, Destination dest) {
        this.dest = dest;
        this.sess = sess;
    }

    public void run() {
        MessageProducer producer = null;
        try {
            producer = sess.createProducer(dest);
            for (sentCount = 0; sentCount < messageCount; sentCount++) {
                producer.send(sess.createTextMessage("test message: " + sentCount));
                LOG.info("Sent 'test message: " + sentCount + "'");
                if (sleep > 0) {
                    Thread.sleep(sleep);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (producer != null) {
                try {
                    producer.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public void setSleep(int sleep) {
        this.sleep = sleep;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public int getSentCount() {
        return sentCount;
    }
}
