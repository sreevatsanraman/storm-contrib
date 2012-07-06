package storm.state;

import java.io.Serializable;
import java.util.Map;


public interface StateFactory<T extends State> extends Serializable {
    // takes a "BackingStore" as a parameter (which implements log + commit + compact interface)?
    // MapState / ListState re-implemented in terms of this
    // the statefulboltexecutor takes a PartitionedBackingStore, which has methods for partition => BackingStore
    // and storing / retrieving metadata
    // builder.setBolt(
    T makeState(Map conf, IBackingStore store, Serializations sers);
}
