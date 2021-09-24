package ${package}.functions.join;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class to define common method to join fact and dimension tables.
 *
 * @param <K>   key
 * @param <IN1> fact table
 * @param <IN2> dimension table
 * @param <OUT> joined table
 */
public abstract class AbstractFactDimTableJoin<K, IN1, IN2, OUT> extends
    KeyedCoProcessFunction<K, IN1, IN2, OUT> {
  private static final long serialVersionUID = 0L;

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());
  private final Class<IN2> dimensionClass;
  private final String stateName;
  private final ObjectMapper mapper;

  private transient ValueState<IN2> dimState;

  /**
   * Constructor for {@link AbstractFactDimTableJoin}
   *
   * @param stateName          name for Flink state for dimension table stream
   * @param debugStateContents whether to debug state contents or not
   * @param dimensionClass     class of the dimension table
   */
  public AbstractFactDimTableJoin(
      final String stateName,
      final boolean debugStateContents,
      final Class<IN2> dimensionClass) {
    this.dimensionClass = dimensionClass;
    this.stateName = stateName;
    this.mapper = debugStateContents ? new ObjectMapper() : null;
  }

  /**
   * Method to join fact and dimension tables with the same key.
   *
   * @param key  join key
   * @param fact fact table
   * @param dim  dimension table
   * @return joined table
   */
  public abstract OUT join(K key, IN1 fact, IN2 dim);

  @Override
  public void open(final Configuration parameters) throws Exception {
    super.open(parameters);

    final ValueStateDescriptor<IN2> dimensionStateDescriptor = new ValueStateDescriptor<>(
        stateName, dimensionClass);

    dimState = getRuntimeContext().getState(dimensionStateDescriptor);
  }

  @Override
  public void processElement1(
      final IN1 value,
      final Context ctx,
      final Collector<OUT> out) throws Exception {
    final K key = ctx.getCurrentKey();
    final IN2 dim = dimState.value();

    out.collect(join(key, value, dim));
  }

  @Override
  public void processElement2(
      final IN2 value,
      final Context ctx,
      final Collector<OUT> out) throws Exception {
    dimState.update(value);

    if (mapper != null) {
      final String valueString = mapper.writeValueAsString(value);

      LOGGER.info(valueString);
    }
  }
}
