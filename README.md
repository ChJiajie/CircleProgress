# 简介 #
这是一个简单好用且带动画的进度条，如下图：

![](https://github.com/ChJiajie/CircleProgress/blob/Mr.Chen/pic/eg.png?raw=true)

其中背景环、进度条、小球的大小及颜色都可以改变

例如：

    <com.cjj.library.CircleProgressBar
	    android:id="@+id/cpb_main"
	    android:layout_width="80dp"
	    android:layout_height="80dp"
	    rp:rp_circleRadius="3dp"
	    rp:rp_circleColor="#987654"
	    rp:rp_progressBarColor="#123456"
	    rp:rp_progressSize="2dp"
	    rp:rp_smallCircle="1dp"
	    rp:rp_smallCircleColor="#fff"
    />

对应的属性：

	<declare-styleable name="RoundProgressBar">
        <attr name="rp_smallCircle" format="dimension"/><!--内环宽-->
        <attr name="rp_progressSize" format="dimension"/><!--进度条大小-->
        <attr name="rp_smallCircleColor" format="color"/><!--内环颜色-->
        <attr name="rp_progressBarColor" format="color"/><!--进度条颜色-->
        <attr name="rp_circleColor" format="color"/><!--小球颜色-->
        <attr name="rp_circleRadius" format="dimension"/><!--小球半径-->
    </declare-styleable>

设置当前进度：

	mProgressBar.setProgress(progress);

其中还有一个动画监听的回调

	public interface OnProgressBarListener{
        
        void onStart();//动画开始

        void onProgressing(float percent);//动画百分比

        void onFinish();//动画结束
    }

	...
	//你可以通过如下代码来监听动画回调，可以配合一些其它的动画
	mProgressBar.setListener(new CircleProgressBar.OnProgressBarListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onProgressing(float percent) {
                mTextView.setText(String.valueOf((int)(1000*percent)));
            }

            @Override
            public void onFinish() {

            }
        });
# 怎么将这个库直接导入你的项目中？ #
Step 1. 在你的project的build.gradle中添加如下代码：

    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. 在module中的build.gradle中添加依赖

	compile 'com.github.ChJiajie:CircleProgress:0.0.1'