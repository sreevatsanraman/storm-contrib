package storm.state;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.Executor;

public interface IBackingStore<T extends State> {
    void init(Map conf, Serializations sers);
    Object appendAndApply(Transaction<T> transaction, T state);
    void commit(T state);
    void commit(BigInteger txid, T state);
    void compact(T state);
    void compactAsync(T state);
    void setExecutor(Executor executor);
    void resetToLatest(T state);
    void rollback(T state);
    void close();
    BigInteger getVersion();
}
