package com.vk.challenge.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vk.challenge.R;
import com.vk.challenge.data.entity.State;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoadingFragment extends Fragment {

    public interface Callback {
        void onRepeatClick();
    }

    private static final String EXTRA_STATE = "extra_state";

    @BindView(R.id.progress_bar)
    View mProgressBar;
    @BindView(R.id.error_text)
    View mErrorText;
    @BindView(R.id.repeat_btn)
    View mRepeatButton;

    private Callback mCallback;

    public static LoadingFragment create(State state) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_STATE, state);
        LoadingFragment loadingFragment = new LoadingFragment();
        loadingFragment.setArguments(bundle);
        return loadingFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_loading_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        State state = getArguments() == null ? State.LOADING : (State) getArguments().getSerializable(EXTRA_STATE);
        mProgressBar.setVisibility(state == State.LOADING ? View.VISIBLE : View.GONE);
        mErrorText.setVisibility(state == State.LOADING ? View.GONE : View.VISIBLE);
        mRepeatButton.setVisibility(state == State.LOADING ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            mCallback = (Callback) context;
        }
    }

    @OnClick(R.id.repeat_btn)
    public void onRepeatClick(View v) {
        if (mCallback != null) mCallback.onRepeatClick();
    }
}
