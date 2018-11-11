package com.vk.challenge.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.vk.challenge.GlideApp;
import com.vk.challenge.R;
import com.vk.challenge.data.entity.Attachment;
import com.vk.challenge.data.entity.Photo;
import com.vk.challenge.data.entity.Post;
import com.vk.challenge.data.entity.PostItem;
import com.vk.challenge.data.entity.PostOwner;
import com.vk.challenge.utils.ListUtils;
import com.vk.challenge.utils.TimeUtils;
import com.vk.challenge.utils.ViewUtils;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedItemFragment extends Fragment {

    private static final String EXTRA_ITEM = "extra_item";

    @BindView(R.id.scroll_view)
    ScrollView mScrollView;
    @BindView(R.id.avatar)
    ImageView mAvatar;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.subtitle)
    TextView mSubtitle;
    @BindView(R.id.text)
    TextView mText;
    @BindView(R.id.attachments_layout)
    FrameLayout mAttachmentsLayout;
    @BindView(R.id.top_layout)
    View mTopLayout;
    @BindView(R.id.image_view)
    ImageView mImageView;
    @BindView(R.id.card_layout)
    View mCardLayout;
    @BindView(R.id.btn_more)
    View mMoreButton;

    public static FeedItemFragment create(PostItem post) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_ITEM, Parcels.wrap(post));
        FeedItemFragment feedItemFragment = new FeedItemFragment();
        feedItemFragment.setArguments(args);
        return feedItemFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed_item, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        PostItem post = getArguments() != null ? Parcels.unwrap(getArguments().getParcelable(EXTRA_ITEM)) : null;
        if (post != null) updateView(post);
    }

    private void updateView(PostItem postItem) {
        Post post = postItem.getPost();
        PostOwner postOwner = postItem.getPostOwner();
        mSubtitle.setText(TimeUtils.formatRelativeSeconds(getContext(), post.getDate()));
        mText.setText(post.getText());

        List<Attachment> attachments = ListUtils.filter(post.getAttachments(), it ->
                "photo".equals(it.getType()) ||
                "video".equals(it.getType()));

        mTitle.setText(postOwner.getDisplayName());

        GlideApp.with(mAvatar)
                .load(postOwner.getPhoto())
                .apply(new RequestOptions()
                        .transform(new CircleCrop()))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mAvatar);

        if (attachments != null && attachments.size() > 0) {
            mAttachmentsLayout.setVisibility(View.VISIBLE);
            Attachment attachment = attachments.get(0);
            if (attachment.getPhoto() != null) {
                Photo photo = attachment.getPhoto();
                Photo.Size size = ListUtils.find(photo.getSizes(), s -> "x".equals(s.getType()));
                size = size == null ? photo.getSizes().get(0) : size;
                setImage(size.getUrl());
            } else if (attachment.getVideo() != null) {
                setImage(attachment.getVideo().getPhoto());
            }
            ViewUtils.onPreDraw(mText, () -> mMoreButton.setVisibility(isEllipsized(mText) ? View.VISIBLE : View.GONE));
        } else {
            mAttachmentsLayout.setVisibility(View.GONE);
            mText.setMaxLines(Integer.MAX_VALUE);

            ViewUtils.onPreDraw(mText, () -> {
                int textVerticalPadding = mText.getPaddingTop() + mText.getPaddingBottom();
                int cardVerticalPadding = mCardLayout.getPaddingTop() + mCardLayout.getPaddingBottom();
                int availableHeight = mScrollView.getHeight() - cardVerticalPadding
                        - mTopLayout.getHeight() - textVerticalPadding;
                int maxLines = availableHeight / mText.getLineHeight();
                int lines = mText.getLineCount();
                mText.setMaxLines(maxLines);
                mMoreButton.setVisibility(lines <= maxLines ? View.GONE : View.VISIBLE);
            });
        }
    }

    private void setImage(String url) {
        GlideApp.with(mImageView)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mImageView);
    }

    @OnClick(R.id.btn_more)
    public void onMoreClick(View v) {
        mAttachmentsLayout.setMinimumHeight(mAttachmentsLayout.getHeight());
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mAttachmentsLayout.getLayoutParams();
        lp.weight = 0;
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        mText.setMaxLines(Integer.MAX_VALUE);
        mMoreButton.setVisibility(View.GONE);
    }

    private boolean isEllipsized(TextView textView) {
        Layout l = textView.getLayout();
        if (l == null) return false;
        int lines = l.getLineCount();
        return (lines > 0 && l.getEllipsisCount(lines - 1) > 0);
    }


}
