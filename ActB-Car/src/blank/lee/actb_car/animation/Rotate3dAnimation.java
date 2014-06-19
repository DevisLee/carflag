package blank.lee.actb_car.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/******
* @Project: SYMG_AI_DIAGNOSE
* @File: Rotate3dAnimation.java
* @Package: com.vsc.android.symg.aidiagnose.animation
* @Description: 3D翻转动画效果的实现类
* @Author: Libin
* @CreateDate: 2013-12-26
******/
public class Rotate3dAnimation extends Animation {
	private final float mFromDegrees;
	private final float mToDegrees;
	private final float mCenterX;
	private final float mCenterY;
	private final float mDepthZ;
	private final boolean mReverse;
	private Camera mCamera;

	public Rotate3dAnimation(float fromDegrees, float toDegrees, float centerX, float centerY, float depthZ,
			boolean reverse) {
		this.mFromDegrees = fromDegrees;
		this.mToDegrees = toDegrees;
		this.mCenterX = centerX;
		this.mCenterY = centerY;
		this.mDepthZ = depthZ;
		this.mReverse = reverse;
	}

	public void initialize(int width, int height, int parentWidth, int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		this.mCamera = new Camera();
	}

	protected void applyTransformation(float interpolatedTime, Transformation t) {
		float fromDegrees = this.mFromDegrees;
		float degrees = fromDegrees + (this.mToDegrees - fromDegrees) * interpolatedTime;
		float centerX = this.mCenterX;
		float centerY = this.mCenterY;
		Camera camera = this.mCamera;
		Matrix matrix = t.getMatrix();
		camera.save();
		if (this.mReverse) {
			camera.translate(0.0F, 0.0F, this.mDepthZ * interpolatedTime);
		} else {
			camera.translate(0.0F, 0.0F, this.mDepthZ * (1.0F - interpolatedTime));
			camera.rotateY(degrees);
			camera.getMatrix(matrix);
			camera.restore();

			matrix.preTranslate(-centerX, -centerY);
			matrix.postTranslate(centerX, centerY);
		}
	}
}
