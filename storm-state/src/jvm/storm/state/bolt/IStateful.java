package storm.state.bolt;

import storm.state.Serializations;
import storm.state.State;
import storm.state.StateFactory;

public interface IStateful<T extends State> {
    StateFactory<T> getStateBuilder();
    Serializations getSerializations();    
}
