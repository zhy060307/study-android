package alvin.base.net.http;

import android.support.annotation.NonNull;

import alvin.base.net.http.common.domain.models.LiveWeather;
import alvin.lib.mvp.IPresenter;
import alvin.lib.mvp.IView;

public interface WeatherContract {

    interface View extends IView {
        void showLiveWeather(@NonNull LiveWeather weather);

        void showError(@NonNull Throwable error);
    }

    interface Presenter extends IPresenter {
        void getLiveWeather();
    }
}
