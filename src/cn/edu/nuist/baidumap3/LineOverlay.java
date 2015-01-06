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
	
	//��㾭γ�ȶ���
	private GeoPoint beginGeoPoint;
	//�յ㾭γ�ȶ���
	private GeoPoint endGeoPoint;

	//���������Ĺ�����,Ϊ�˷��㴫�ݲ���
	public LineOverlay(GeoPoint beginGeoPoint,GeoPoint endGeoPoint) {
		super();
		this.beginGeoPoint = beginGeoPoint;
		this.endGeoPoint = endGeoPoint;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		//����xy���ʾ������
		Point beginPoint = new Point();
		Point endPoint = new Point();
		//����γ�ȶ����ֵת����xy�������Ӧ��ֵ
		Projection p = mapView.getProjection();
		p.toPixels(beginGeoPoint, beginPoint);
		p.toPixels(endGeoPoint, endPoint);
		//����һ�����ʶ���
		Paint paint = new Paint();
		//���û��ʵ�һЩ����
		//��ɫ
		paint.setColor(Color.RED);
		//��ʽ
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		//��ϸ����
		paint.setStrokeWidth(2);
		//ʹ�û����ڻ����ϻ�ֱ��
		canvas.drawLine(beginPoint.x, beginPoint.y, endPoint.x, endPoint.y, paint);
	}
}

