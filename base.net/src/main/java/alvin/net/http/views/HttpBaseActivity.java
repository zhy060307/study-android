package alvin.net.http.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

import alvin.net.R;
import alvin.net.http.WeatherContract;
import alvin.net.http.models.LiveWeather;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class HttpBaseActivity extends AppCompatActivity implements WeatherContract.View {
    private WeatherContract.Presenter presenter;

    @BindView(R.id.text_weather)
    TextView textWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);

        ButterKnife.bind(this);

        presenter = getPresenter();
        presenter.doCreate();
    }

    protected abstract WeatherContract.Presenter getPresenter();

    @Override
    protected void onStart() {
        super.onStart();
        presenter.showLiveWeather();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.doDestroy();
    }

    @OnClick(R.id.btn_refresh)
    public void onRefreshButtonClick(Button button) {
        presenter.showLiveWeather();
    }

    @Override
    public void showLiveWeather(LiveWeather weather) {
        textWeather.setText(formatLiveWeather(weather));
    }

    private String formatLiveWeather(LiveWeather weather) {
        List<String> lines = new ArrayList<>();

        lines.add(String.format("%s: %s (%s)",
                getString(R.string.label_city_name),
                weather.getLocation().getName(),
                weather.getLocation().getCityId()));

        lines.add(String.format("%s: %s",
                getString(R.string.label_timezone),
                weather.getLocation().getTimezone()));

        lines.add(String.format("%s: %s",
                getString(R.string.label_longitude),
                weather.getLocation().getLongitude()));

        lines.add(String.format("%s: %s",
                getString(R.string.label_latitude),
                weather.getLocation().getLatitude()));

        lines.add("");

        lines.add(String.format("%s: %s (%s)",
                getString(R.string.label_condition),
                weather.getWeather().getConditionText(),
                weather.getWeather().getConditionCode()));

        lines.add(String.format("%s: %s%s",
                getString(R.string.label_feels_like_temperature),
                weather.getWeather().getFeelsLikeTemperature(),
                getString(R.string.unit_temperature)));

        lines.add(String.format("%s: %s%s",
                getString(R.string.label_temperature),
                weather.getWeather().getTemperature(),
                getString(R.string.unit_temperature)));

        lines.add(String.format("%s: %s%s",
                getString(R.string.label_precipitation),
                weather.getWeather().getPrecipitation(),
                getString(R.string.unit_mm)));

        lines.add(String.format("%s: %s%s",
                getString(R.string.label_pressure),
                weather.getWeather().getPressure(),
                getString(R.string.unit_pa)));

        lines.add(String.format("%s: %s",
                getString(R.string.label_humidity),
                weather.getWeather().getHumidity()));

        lines.add(String.format("%s: %s",
                getString(R.string.label_wind_direction),
                weather.getWeather().getWindDirection()));

        lines.add(String.format("%s: %s",
                getString(R.string.label_wind_power),
                weather.getWeather().getWindPower()));

        lines.add(String.format("%s: %s%s",
                getString(R.string.label_wind_speed),
                weather.getWeather().getWindSpeed(),
                getString(R.string.unit_speed)));

        lines.add(String.format("%s: %s%s",
                getString(R.string.label_visibility),
                weather.getWeather().getVisibility(),
                getString(R.string.unit_km)));

        lines.add(String.format("%s: %s",
                getString(R.string.label_visibility),
                weather.getWeather().getCloud()));

        lines.add("");

        lines.add(String.format("%s: %s",
                getString(R.string.label_update_time),
                weather.getTimestamp().getUtcTime()));

        return Joiner.on("\n").join(lines);
    }

    @Override
    public void showWeatherError() {
        Toast.makeText(this, R.string.error_get_weather, Toast.LENGTH_LONG).show();
    }
}