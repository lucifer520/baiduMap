package cn.edu.nuist.baidumap3;

import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKPoiInfo;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class NearbyActivity extends Activity{
	private Button search_nearby,return_nearby;
	private TextView nearby_text;
	
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.nearby);
		search_nearby = (Button)findViewById(R.id.search_nearby);
		return_nearby = (Button)findViewById(R.id.return_nearby);
		nearby_text = (TextView)findViewById(R.id.nearby_text);
			
		 OnClickListener listener = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(v == search_nearby)
					{
						//MySearchListener search = new MySearchListener();
						//search.onGetAddrResult(result, 0);
						nearby_text.setText(Html.fromHtml("<p>��Ϣ���̴�ѧ����վ</p>	<p>�̳���ɽ��·�赼�г�</p>	<p>�й�������������</p>	<p>��Ȼ�ӷ���</p>	<p>һ���������</p>	<p>��ը������</p>	<p>��Ҷ����</p>		<p>������ʳ��</p> "));
					}
					else if(v == return_nearby)
					{
						
						Intent intent = new Intent(NearbyActivity.this,MyMapActivity.class);
						startActivity(intent);			
						
					}
							
					}
			};
			search_nearby.setOnClickListener(listener);
			return_nearby.setOnClickListener(listener);
		
			
		}
		
	class MySearchListener implements MKSearchListener {  
		
        //���ݾ�γ��������ַ��Ϣ���          
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
                    sb.append("----------------------------------------").append("/n");  
                    sb.append("���ƣ�").append(poiInfo.name).append("/n");  
                    sb.append("��ַ��").append(poiInfo.address).append("/n");  
                    sb.append("���ȣ�").append(poiInfo.pt.getLongitudeE6() / 1000000.0f).append("/n");  
                    sb.append("γ�ȣ�").append(poiInfo.pt.getLatitudeE6() / 1000000.0f).append("/n");  
                    sb.append("�绰��").append(poiInfo.phoneNum).append("/n");  
                    sb.append("�ʱࣺ").append(poiInfo.postCode).append("/n");  
                    // poi���ͣ�0����ͨ�㣬1������վ��2��������·��3������վ��4��������·  
                    sb.append("���ͣ�").append(poiInfo.ePoiType).append("/n");  
                }  
            }  
            // ����ַ��Ϣ����Ȥ����Ϣ��ʾ��TextView��  
            nearby_text.setText(sb.toString());  
        }

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			
		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult arg0,int arg1) {
			
		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
			
		}

		@Override
		public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
									}

		@Override
		public void onGetRGCShareUrlResult(String arg0, int arg1) {
			
		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			
		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult arg0,int arg1) {
									}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0,int arg1) {
			
		}  
        
        
	}
	
	
	
}
	        

	

