package $package;

import ${package}.streaming.StreamPipeline;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamingJob {
  private static final Logger LOGGER = LoggerFactory.getLogger(StreamingJob.class);

  public static void main(String[] args) throws Exception {
    final ParameterTool params = ParameterTool.fromArgs(args);

    new StreamPipeline(params) {
      @Override
      public <T> DataStreamSource<T> readStream(
          final StreamExecutionEnvironment env,
          final String topic,
          final KafkaDeserializationSchema<T> valueDeserializer) {
        return null;
      }

      @Override
      public StreamExecutionEnvironment createExecutionEnvironment() {
        return null;
      }

      @Override
      public <T> DataStreamSink<T> writeStream(DataStream<T> stream) {
        return null;
      }
    }.createPipeline().execute();
  }
}
