package com.vk.challenge.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.vk.challenge.data.entity.Post;
import com.vk.challenge.data.entity.PostItem;
import com.vk.challenge.data.entity.PostOwner;
import com.vk.challenge.utils.ListUtils;
import com.vk.challenge.utils.TimeUtils;
import com.vk.challenge.utils.ViewUtils;
import com.vk.challenge.widget.OnStackScrollListener;
import com.vk.challenge.widget.StackViewPager;
import com.vk.challenge.widget.ViewPagerIndicator;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedItemFragment extends Fragment implements OnStackScrollListener {

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
    ViewGroup mAttachmentsLayout;
    @BindView(R.id.attachments_pager)
    ViewPager mAttachmentsPager;
    @BindView(R.id.view_pager_indicator)
    ViewPagerIndicator mViewPagerIndicator;
    @BindView(R.id.top_layout)
    View mTopLayout;
    @BindView(R.id.card_layout)
    View mCardLayout;
    @BindView(R.id.btn_more)
    View mMoreButton;

    @BindView(R.id.badge_like)
    View mLikeBadge;
    @BindView(R.id.badge_skip)
    View mSkipBadge;

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

    @Override
    public void onStackScrolled(float offset, int direction) {
        float alpha = offset > 1/3f ? (1 - offset) * 3f : offset * 3f;
        mSkipBadge.setAlpha(direction == StackViewPager.DIRECTION_LEFT ? alpha : 0);
        mLikeBadge.setAlpha(direction == StackViewPager.DIRECTION_RIGHT ? alpha : 0);
    }

    private void updateView(PostItem postItem) {
        Post post = postItem.getPost();
        PostOwner postOwner = postItem.getPostOwner();
        mSubtitle.setText(TimeUtils.formatRelativeSeconds(getContext(), post.getDate()));
        mText.setText(post.getText());

        List<Attachment> attachments = ListUtils.filter(post.getAttachments(), it ->
                "photo".equals(it.getType()) || "video".equals(it.getType()));
        List<String> photos = ListUtils.map(attachments, Attachment::getDisplayPhoto);

        mTitle.setText(postOwner.getDisplayName());


        GlideApp.with(mAvatar.getContext())
                .load(postOwner.getPhoto())
                .apply(new RequestOptions()
                        .transform(new CircleCrop()))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mAvatar);

        mAttachmentsPager.setOffscreenPageLimit(1);

        if (photos != null && photos.size() > 0) {
            mAttachmentsPager.setAdapter(new AttachmentAdapter(photos));
            mAttachmentsLayout.setVisibility(View.VISIBLE);
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

        mViewPagerIndicator.setViewPager(mAttachmentsPager);
    }

    @OnClick(R.id.next_handle)
    public void onNextClick(View v) {
        setCurrentAttachment(mAttachmentsPager.getCurrentItem() + 1);
    }

    @OnClick(R.id.prev_handle)
    public void onPrevClick(View v) {
        setCurrentAttachment(mAttachmentsPager.getCurrentItem() - 1);
    }

    private void setCurrentAttachment(int position) {
        PagerAdapter adapter = mAttachmentsPager.getAdapter();
        if (adapter == null) return;
        if (position >= 0 && position < adapter.getCount()) {
            mAttachmentsPager.setCurrentItem(position);
        }
    }

    @OnClick(R.id.btn_more)
    public void onMoreClick(View v) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mAttachmentsLayout.getLayoutParams();
        lp.weight = 0;
        lp.height = mAttachmentsLayout.getHeight();
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
