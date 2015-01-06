package cn.edu.nuist.baidumap3;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.Overlay;
import com.baidu.mapapi.Projection;

public class LineOverlay extends Overlay {
	
	//起点经纬度对象
	private GeoPoint beginGeoPoint;
	//终点经纬度对象
	private GeoPoint endGeoPoint;

	//两个参数的构造器,为了方便传递参数
	public LineOverlay(GeoPoint beginGeoPoint,GeoPoint endGeoPoint) {
		super();
		this.beginGeoPoint = beginGeoPoint;
		this.endGeoPoint = endGeoPoint;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		//定义xy轴表示的坐标
		Point beginPoint = new Point();
		Point endPoint = new Point();
		//将经纬度对象的值转换成xy轴坐标对应的值
		Projection p = mapView.getProjection();
		p.toPixels(beginGeoPoint, beginPoint);
		p.toPixels(endGeoPoint, endPoint);
		//定义一个画笔对象
		Paint paint = new Paint();
		//设置画笔的一些属性
		//颜色
		paint.setColor(Color.RED);
		//样式
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		//粗细级别
		paint.setStrokeWidth(2);
		//使用画笔在画布上画直线
		canvas.drawLine(beginPoint.x, beginPoint.y, endPoint.x, endPoint.y, paint);
	}
}

