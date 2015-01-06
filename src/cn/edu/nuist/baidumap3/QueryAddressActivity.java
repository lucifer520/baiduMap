package cn.edu.nuist.baidumap3;



import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKPoiInfo;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;

/**
 * ���ݾ�γ�Ȳ�ѯ��ַ��Ϣ
 * 
 * @author liufeng
 * @date 2011-05-03
 */
public class QueryAddressActivity extends MapActivity {
	// �����ͼ���������
	private BMapManager mapManager;
	// ��������������
	private MKSearch mMKSearch;

	private EditText longitudeEditText;
	private EditText latitudeEditText;
	private TextView addressTextView;
	private Button queryButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_address);

		// ��ʼ��MapActivity
		mapManager = new BMapManager(getApplication());
		// init�����ĵ�һ�����������������API Key
		mapManager.init("285B415EBAB2A92293E85502150ADA7F03C777C4", null);
		super.initMapActivity(mapManager);

		// ��ʼ��MKSearch
		mMKSearch = new MKSearch();
		mMKSearch.init(mapManager, new MySearchListener());

		// ͨ��id��ѯ�ڲ����ļ��ж���Ŀؼ�
		longitudeEditText = (EditText) findViewById(R.id.longitude_input);
		latitudeEditText = (EditText) findViewById(R.id.latitude_input);
		addressTextView = (TextView) findViewById(R.id.address_text);
		queryButton = (Button) findViewById(R.id.query_button);

		// ����ַ��ѯ��ť���õ����¼�������
		queryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// �û�����ľ���ֵ
				String longitudeStr = longitudeEditText.getText().toString();
				// �û������γ��ֵ
				String latitudeStr = latitudeEditText.getText().toString();

				try {
					// ���û�����ľ�γ��ֵת����int����
					int longitude = (int) (1000000 * Double.parseDouble(longitudeStr));
					int latitude = (int) (1000000 * Double.parseDouble(latitudeStr));

					// ��ѯ�þ�γ��ֵ����Ӧ�ĵ�ַλ����Ϣ
					mMKSearch.reverseGeocode(new GeoPoint(latitude, longitude));
				} catch (Exception e) {
					addressTextView.setText("��ѯ��������������ľ�γ��ֵ��");
				}
			}
		});
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected void onDestroy() {
		if (mapManager != null) {
			// �����˳�ǰ����ô˷���
			mapManager.destroy();
			mapManager = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		if (mapManager != null) {
			// ��ֹ�ٶȵ�ͼAPI
			mapManager.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (mapManager != null) {
			// �����ٶȵ�ͼAPI
			mapManager.start();
		}
		super.onResume();
	}

	/**
	 * �ڲ���ʵ��MKSearchListener�ӿ�,����ʵ���첽��������
	 * 
	 * @author liufeng
	 */
	public class MySearchListener implements MKSearchListener {
		/**
		 * ���ݾ�γ��������ַ��Ϣ���
		 * 
		 * @param result �������
		 * @param iError ����ţ�0��ʾ��ȷ���أ�
		 */
		@Override
		public void onGetAddrResult(MKAddrInfo result, int iError) {
			if (result == null) {
				return;
			}
			StringBuffer sb = new StringBuffer();
			// ��γ������Ӧ��λ��
			sb.append(result.strAddr).append("/n");

			// �жϸõ�ַ�����Ƿ���POI��Point of Interest,����Ȥ�㣩
			if (null != result.poiList) {
				// �������е���Ȥ����Ϣ
				for (MKPoiInfo poiInfo : result.poiList) {
					sb.append("----------------------------------------").append("<p>");
					sb.append("���ƣ�").append(poiInfo.name).append("</p><p>");
					sb.append("��ַ��").append(poiInfo.address).append("</p><p>");
					sb.append("���ȣ�").append(poiInfo.pt.getLongitudeE6() / 1000000.0f).append("</p><p>");
					sb.append("γ�ȣ�").append(poiInfo.pt.getLatitudeE6() / 1000000.0f).append("</p><p>");
					sb.append("�绰��").append(poiInfo.phoneNum).append("</p><p>");
					sb.append("�ʱࣺ").append(poiInfo.postCode).append("</p><p>");
					// poi���ͣ�0����ͨ�㣬1������վ��2��������·��3������վ��4��������·
					sb.append("���ͣ�").append(poiInfo.ePoiType).append("</p><p>");
				}
			}
			// ����ַ��Ϣ����Ȥ����Ϣ��ʾ��TextView��
			addressTextView.setText(Html.fromHtml(sb.toString()));
		}

		/**
		 * �ݳ�·���������
		 * 
		 * @param result �������
		 * @param iError ����ţ�0��ʾ��ȷ���أ�
		 */
		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {
		}

		/**
		 * POI�����������Χ����������POI�������ܱ߼�����
		 * 
		 * @param result �������
		 * @param type ���ؽ�����ͣ�11,12,21:poi�б� 7:�����б�
		 * @param iError ����ţ�0��ʾ��ȷ���أ�
		 */
		@Override
		public void onGetPoiResult(MKPoiResult result, int type, int iError) {
		}

		/**
		 * ��������·���������
		 * 
		 * @param result �������
		 * @param iError ����ţ�0��ʾ��ȷ���أ�
		 */
		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {
		}

		/**
		 * ����·���������
		 * 
		 * @param result �������
		 * @param iError ����ţ�0��ʾ��ȷ���أ�
		 */
		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetRGCShareUrlResult(String arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
	}
}