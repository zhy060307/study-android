package alvin.lib.common.rx;

import java.util.concurrent.Executors;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public final class RxSchedulers {

    private static final Scheduler DATABASE_SCHEDULER = Schedulers.from(Executors.newSingleThreadExecutor());

    private RxSchedulers() {
    }

    public static Scheduler database() {
        return DATABASE_SCHEDULER;
    }

    public static Scheduler singlePool() {
        return Schedulers.from(Executors.newSingleThreadExecutor());
    }
}
