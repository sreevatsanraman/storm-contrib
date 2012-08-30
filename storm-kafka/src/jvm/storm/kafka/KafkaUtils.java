package storm.kafka;

import backtype.storm.coordination.BatchOutputCollector;
import backtype.storm.transactional.TransactionAttempt;
import backtype.storm.utils.Utils;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import kafka.api.FetchRequest;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.Message;
import kafka.message.MessageAndOffset;

public class KafkaUtils {
    
    
     public static BatchMeta emitPartitionBatchNew(
             KafkaConfig config,
             int partition,
             SimpleConsumer consumer,
             TransactionAttempt attempt,
             BatchOutputCollector collector,
             BatchMeta lastMeta,
             String topologyInstanceId
     ) {
         long offset;
         if (lastMeta == null) {
             offset = getOffsetBefore(consumer, config, partition, -1);
         } else if (config.forceFromStart && !topologyInstanceId.equals(lastMeta.instanceId)) {
             offset = getOffsetBefore(consumer, config, partition, config.startOffsetTime);
         } else {
             offset = lastMeta.nextOffset;
         }

         ByteBufferMessageSet msgs;
         try {
            msgs = consumer.fetch(new FetchRequest(config.topic, partition % config.partitionsPerHost, offset, config.fetchSizeBytes));
         } catch(Exception e) {
             if(e instanceof ConnectException) {
                 throw new FailedFetchException(e);
             } else {
                 throw new RuntimeException(e);
             }
         }
         long endoffset = offset;
         for(MessageAndOffset msg: msgs) {
             emit(config, attempt, collector, msg.message());
             endoffset = msg.offset();
         }
         BatchMeta newMeta = new BatchMeta();
         newMeta.offset = offset;
         newMeta.nextOffset = endoffset;
         newMeta.instanceId = topologyInstanceId;
         return newMeta;
     }
     
     public static void emit(KafkaConfig config, TransactionAttempt attempt, BatchOutputCollector collector, Message msg) {
         List<Object> values = config.scheme.deserialize(Utils.toByteArray(msg.payload()));
         List<Object> toEmit = new ArrayList<Object>();
         toEmit.add(attempt);
         toEmit.addAll(values);
         collector.emit(toEmit);           
     }

     private static long getOffsetBefore(SimpleConsumer consumer, KafkaConfig config, int partition, long time) {
         return consumer.getOffsetsBefore(config.topic, partition % config.partitionsPerHost, time, 1)[0];
     }
}
