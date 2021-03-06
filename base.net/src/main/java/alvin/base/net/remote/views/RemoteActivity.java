package alvin.base.net.remote.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.base.Strings;

import alvin.base.net.R;
import alvin.base.net.remote.RemoteContracts;
import alvin.lib.mvp.contracts.adapters.ActivityAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RemoteActivity extends ActivityAdapter<RemoteContracts.Presenter>
        implements RemoteContracts.View {
    private static final String KEY_IMAGE_SRC = "key_image_src";

    @BindView(R.id.container)
    ViewPager container;

    @BindView(R.id.fab_start)
    FloatingActionButton fabStart;

    @BindView(R.id.fab_end)
    FloatingActionButton fabEnd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);

        ButterKnife.bind(this);

        initialize();
    }

    private void initialize() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        container.setAdapter(adapter);

        container.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                onImagePageSelected(position);
            }
        });
    }

    protected void onImagePageSelected(int position) {
        if (position > 0) {
            fabStart.show();
        } else {
            fabStart.hide();
        }

        if (position < presenter.getImageCount() - 1) {
            fabEnd.show();
        } else {
            fabEnd.hide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadImageUrls();
    }

    @Override
    public void imageSrcLoaded() {
        PagerAdapter adapter = container.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();

            onImagePageSelected(container.getCurrentItem());
        }
    }

    @Override
    public void imageLoadFailed(String imageSrc) {
        final String message = getString(R.string.error_load_image_file, imageSrc);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @OnClick({R.id.fab_start, R.id.fab_end})
    public void onFloatingButtonsClicked(FloatingActionButton b) {
        switch (b.getId()) {
        case R.id.fab_start:
            container.arrowScroll(ViewPager.FOCUS_LEFT);
            break;
        case R.id.fab_end:
            container.arrowScroll(ViewPager.FOCUS_RIGHT);
            break;
        default:
            break;
        }
    }

    class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(presenter.getImageSrcAt(position));
        }

        @Override
        public int getCount() {
            return presenter.getImageCount();
        }
    }

    public static class PlaceholderFragment extends Fragment {

        @BindView(R.id.iv_section)
        ImageView ivSection;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            android.view.View view = inflater.inflate(R.layout.fragment_image_slide, container, false);
            ButterKnife.bind(this, view);

            final Bundle bundle = getArguments();
            if (bundle != null) {
                final String imageSrc = bundle.getString(KEY_IMAGE_SRC);

                if (!Strings.isNullOrEmpty(imageSrc)) {
                    RemoteActivity activity = (RemoteActivity) getActivity();
                    assert activity != null;

                    activity.presenter.loadImageAsDrawable(imageSrc, drawable -> {
                        if (drawable != null) {
                            ivSection.setImageDrawable(drawable);
                        }
                    });
                }
            }
            return view;
        }

        @NonNull
        public static PlaceholderFragment newInstance(String imageSrc) {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_IMAGE_SRC, imageSrc);

            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setArguments(bundle);
            return fragment;
        }
    }
}
