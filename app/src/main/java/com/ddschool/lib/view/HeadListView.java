package com.ddschool.lib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;

/**
 * 重写的ListView,让每条新闻的时间显示
 */
public class HeadListView extends CustomListView {

    //	private final static int RELEASE_To_REFRESH = 0;// 下拉过程的状态值
//	private final static int PULL_To_REFRESH = 1; // 从下拉返回到不刷新的状态值
//	private final static int REFRESHING = 2;// 正在刷新的状态值
//	private final static int DONE = 3;
//	private final static int LOADING = 4;
//
//	// 实际的padding的距离与界面上偏移距离的比例
//	private final static int RATIO = 3;
//	private LayoutInflater inflater;
//
//	// ListView头部下拉刷新的布局
//	private LinearLayout headerView;
//	private TextView lvHeaderTipsTv;
//	private TextView lvHeaderLastUpdatedTv;
//	private ImageView lvHeaderArrowIv;
//	private ProgressBar lvHeaderProgressBar;
//
//	// 定义头部下拉刷新的布局的高度
//	private int headerContentHeight;
//
//	private RotateAnimation animation;
//	private RotateAnimation reverseAnimation;
//
//	private int startY;
//	private int state;
//	private boolean isBack;
//
//	// 用于保证startY的值在一个完整的touch事件中只被记录一次
//	private boolean isRecored;
//
//	private SwipeRefreshLayout.OnRefreshListener refreshListener;
//
//	private boolean isRefreshable;
//
//	private void init(Context context) {
//		setCacheColorHint(context.getResources().getColor(R.color.transparent));
//		inflater = LayoutInflater.from(context);
//		headerView = (LinearLayout) inflater.inflate(R.layout.list_header, null);
//		lvHeaderTipsTv = (TextView) headerView
//				.findViewById(R.id.lvHeaderTipsTv);
//		lvHeaderLastUpdatedTv = (TextView) headerView
//				.findViewById(R.id.lvHeaderLastUpdatedTv);
//
//		lvHeaderArrowIv = (ImageView) headerView
//				.findViewById(R.id.lvHeaderArrowIv);
//		// 设置下拉刷新图标的最小高度和宽度
//		lvHeaderArrowIv.setMinimumWidth(70);
//		lvHeaderArrowIv.setMinimumHeight(50);
//
//		lvHeaderProgressBar = (ProgressBar) headerView
//				.findViewById(R.id.lvHeaderProgressBar);
//		measureView(headerView);
//		headerContentHeight = headerView.getMeasuredHeight();
//		// 设置内边距，正好距离顶部为一个负的整个布局的高度，正好把头部隐藏
//		headerView.setPadding(0, -1 * headerContentHeight, 0, 0);
//		// 重绘一下
//		headerView.invalidate();
//		// 将下拉刷新的布局加入ListView的顶部
//		addHeaderView(headerView, null, false);
//		// 设置滚动监听事件
//		setOnScrollListener(this);
//
//		// 设置旋转动画事件
//		animation = new RotateAnimation(0, -180,
//				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
//				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//		animation.setInterpolator(new LinearInterpolator());
//		animation.setDuration(250);
//		animation.setFillAfter(true);
//
//		reverseAnimation = new RotateAnimation(-180, 0,
//				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
//				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//		reverseAnimation.setInterpolator(new LinearInterpolator());
//		reverseAnimation.setDuration(200);
//		reverseAnimation.setFillAfter(true);
//
//		// 一开始的状态就是下拉刷新完的状态，所以为DONE
//		state = DONE;
//		// 是否正在刷新
//		isRefreshable = false;
//	}
//
//	@Override
//	public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//	}
//
//	@Override
//	public void onScroll(AbsListView view, int firstVisibleItem,
//						 int visibleItemCount, int totalItemCount) {
//		if (firstVisibleItem == 0) {
//			isRefreshable = true;
//		} else {
//			isRefreshable = false;
//		}
//	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent ev) {
//		if (isRefreshable) {
//			switch (ev.getAction()) {
//				case MotionEvent.ACTION_DOWN:
//					if (!isRecored) {
//						isRecored = true;
//						startY = (int) ev.getY();// 手指按下时记录当前位置
//					}
//					break;
//				case MotionEvent.ACTION_UP:
//					if (state != REFRESHING && state != LOADING) {
//						if (state == PULL_To_REFRESH) {
//							state = DONE;
//							changeHeaderViewByState();
//						}
//						if (state == RELEASE_To_REFRESH) {
//							state = REFRESHING;
//							changeHeaderViewByState();
//							onLvRefresh();
//						}
//					}
//					isRecored = false;
//					isBack = false;
//
//					break;
//
//				case MotionEvent.ACTION_MOVE:
//					int tempY = (int) ev.getY();
//					if (!isRecored) {
//						isRecored = true;
//						startY = tempY;
//					}
//					if (state != REFRESHING && isRecored && state != LOADING) {
//						// 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
//						// 可以松手去刷新了
//						if (state == RELEASE_To_REFRESH) {
//							setSelection(0);
//							// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
//							if (((tempY - startY) / RATIO < headerContentHeight)// 由松开刷新状态转变到下拉刷新状态
//									&& (tempY - startY) > 0) {
//								state = PULL_To_REFRESH;
//								changeHeaderViewByState();
//							}
//							// 一下子推到顶了
//							else if (tempY - startY <= 0) {// 由松开刷新状态转变到done状态
//								state = DONE;
//								changeHeaderViewByState();
//							}
//						}
//						// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
//						if (state == PULL_To_REFRESH) {
//							setSelection(0);
//							// 下拉到可以进入RELEASE_TO_REFRESH的状态
//							if ((tempY - startY) / RATIO >= headerContentHeight) {// 由done或者下拉刷新状态转变到松开刷新
//								state = RELEASE_To_REFRESH;
//								isBack = true;
//								changeHeaderViewByState();
//							}
//							// 上推到顶了
//							else if (tempY - startY <= 0) {// 由DOne或者下拉刷新状态转变到done状态
//								state = DONE;
//								changeHeaderViewByState();
//							}
//						}
//						// done状态下
//						if (state == DONE) {
//							if (tempY - startY > 0) {
//								state = PULL_To_REFRESH;
//								changeHeaderViewByState();
//							}
//						}
//						// 更新headView的size
//						if (state == PULL_To_REFRESH) {
//							headerView.setPadding(0, -1 * headerContentHeight
//									+ (tempY - startY) / RATIO, 0, 0);
//
//						}
//						// 更新headView的paddingTop
//						if (state == RELEASE_To_REFRESH) {
//							headerView.setPadding(0, (tempY - startY) / RATIO
//									- headerContentHeight, 0, 0);
//						}
//
//					}
//					break;
//
//				default:
//					break;
//			}
//		}
//		return super.onTouchEvent(ev);
//	}
//
//	// 当状态改变时候，调用该方法，以更新界面
//	private void changeHeaderViewByState() {
//		switch (state) {
//			case RELEASE_To_REFRESH:
//				lvHeaderArrowIv.setVisibility(View.VISIBLE);
//				lvHeaderProgressBar.setVisibility(View.GONE);
//				lvHeaderTipsTv.setVisibility(View.VISIBLE);
//				lvHeaderLastUpdatedTv.setVisibility(View.VISIBLE);
//
//				lvHeaderArrowIv.clearAnimation();// 清除动画
//				lvHeaderArrowIv.startAnimation(animation);// 开始动画效果
//
//				lvHeaderTipsTv.setText("松开刷新");
//				break;
//			case PULL_To_REFRESH:
//				lvHeaderProgressBar.setVisibility(View.GONE);
//				lvHeaderTipsTv.setVisibility(View.VISIBLE);
//				lvHeaderLastUpdatedTv.setVisibility(View.VISIBLE);
//				lvHeaderArrowIv.clearAnimation();
//				lvHeaderArrowIv.setVisibility(View.VISIBLE);
//				// 是由RELEASE_To_REFRESH状态转变来的
//				if (isBack) {
//					isBack = false;
//					lvHeaderArrowIv.clearAnimation();
//					lvHeaderArrowIv.startAnimation(reverseAnimation);
//
//					lvHeaderTipsTv.setText("下拉刷新");
//				} else {
//					lvHeaderTipsTv.setText("下拉刷新");
//				}
//				break;
//
//			case REFRESHING:
//
//				headerView.setPadding(0, 0, 0, 0);
//
//				lvHeaderProgressBar.setVisibility(View.VISIBLE);
//				lvHeaderArrowIv.clearAnimation();
//				lvHeaderArrowIv.setVisibility(View.GONE);
//				lvHeaderTipsTv.setText("正在刷新...");
//				lvHeaderLastUpdatedTv.setVisibility(View.VISIBLE);
//				break;
//			case DONE:
//				headerView.setPadding(0, -1 * headerContentHeight, 0, 0);
//
//				lvHeaderProgressBar.setVisibility(View.GONE);
//				lvHeaderArrowIv.clearAnimation();
//				lvHeaderArrowIv.setImageResource(R.mipmap.refresh_arrow);
//				lvHeaderTipsTv.setText("下拉刷新");
//				lvHeaderLastUpdatedTv.setVisibility(View.VISIBLE);
//				break;
//		}
//	}
//
//	// 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
//	private void measureView(View child) {
//		ViewGroup.LayoutParams params = child.getLayoutParams();
//		if (params == null) {
//			params = new ViewGroup.LayoutParams(
//					ViewGroup.LayoutParams.FILL_PARENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//		}
//		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0,
//				params.width);
//		int lpHeight = params.height;
//		int childHeightSpec;
//		if (lpHeight > 0) {
//			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
//					MeasureSpec.EXACTLY);
//		} else {
//			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
//					MeasureSpec.UNSPECIFIED);
//		}
//		child.measure(childWidthSpec, childHeightSpec);
//	}
//
//	public void setonRefreshListener(SwipeRefreshLayout.OnRefreshListener refreshListener) {
//		this.refreshListener = refreshListener;
//		isRefreshable = true;
//	}
//
//	public interface OnRefreshListener {
//		public void onRefresh();
//	}
//
//	public void onRefreshComplete() {
//		state = DONE;
//		lvHeaderLastUpdatedTv.setText("最近更新:" + new Date().toLocaleString());
//		changeHeaderViewByState();
//	}
//
//	private void onLvRefresh() {
//		if (refreshListener != null) {
//			refreshListener.onRefresh();
//		}
//	}

    public interface HeaderAdapter {
        public static final int HEADER_GONE = 0;
        public static final int HEADER_VISIBLE = 1;
        public static final int HEADER_PUSHED_UP = 2;

        int getHeaderState(int position);

        void configureHeader(View header, int position, int alpha);
    }

    private static final int MAX_ALPHA = 255;

    private HeaderAdapter mAdapter;
    private View mHeaderView;
    private boolean mHeaderViewVisible;
    private int mHeaderViewWidth;
    private int mHeaderViewHeight;

    public HeadListView(Context context) {
        super(context);
        //init(context);
    }

    public HeadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //init(context);
    }

    public HeadListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //init(context);
    }

    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mHeaderView != null) {
            mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
            configureHeaderView(getFirstVisiblePosition());
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            mHeaderViewWidth = mHeaderView.getMeasuredWidth();
            mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        }
    }

    public void setPinnedHeaderView(View view) {
        mHeaderView = view;
        if (mHeaderView != null) {
            //listview的上边和下边有黑色的阴影。xml中： android:fadingEdge="none"
            setFadingEdgeLength(0);
        }
        requestLayout();
    }

    public void setAdapter(ListAdapter adapter) {
        //lvHeaderLastUpdatedTv.setText("最近更新:" + new Date().toLocaleString());
        super.setAdapter(adapter);
        mAdapter = (HeaderAdapter) adapter;
    }

    public void configureHeaderView(int position) {
        if (mHeaderView == null) {
            return;
        }
        int state = mAdapter.getHeaderState(position);
        switch (state) {
            case HeaderAdapter.HEADER_GONE: {
                mHeaderViewVisible = false;
                break;
            }

            case HeaderAdapter.HEADER_VISIBLE: {
                mAdapter.configureHeader(mHeaderView, position, MAX_ALPHA);
                if (mHeaderView.getTop() != 0) {
                    mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
                }
                mHeaderViewVisible = true;
                break;
            }

            case HeaderAdapter.HEADER_PUSHED_UP: {
                View firstView = getChildAt(0);
                int bottom = firstView.getBottom();
                int headerHeight = mHeaderView.getHeight();
                int y;
                int alpha;
                if (bottom < headerHeight) {
                    y = (bottom - headerHeight);
                    alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
                } else {
                    y = 0;
                    alpha = MAX_ALPHA;
                }
                mAdapter.configureHeader(mHeaderView, position, alpha);
                if (mHeaderView.getTop() != y) {
                    mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight + y);
                }
                mHeaderViewVisible = true;
                break;
            }
        }
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mHeaderViewVisible) {
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }
}
