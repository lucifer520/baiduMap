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
						nearby_text.setText(Html.fromHtml("<p>信息工程大学地铁站</p>	<p>盘城龙山北路疏导市场</p>	<p>中国邮政储蓄银行</p>	<p>天然居饭馆</p>	<p>一磅倾情糕点店</p>	<p>轰炸大鱿鱼</p>	<p>荷叶包饭</p>		<p>世界零食店</p> "));
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
		
        //根据经纬度搜索地址信息结果          
        @Override  
        public void onGetAddrResult(MKAddrInfo result, int iError) {  
            if (result == null) {  
                return;  
            }  
            StringBuffer sb = new StringBuffer();  
            // 经纬度所对应的位置  
            sb.append(result.strAddr).append("/n");  
  
            // 判断该地址附近是否有POI（Point of Interest,即兴趣点）  
            if (null != result.poiList) {  
                // 遍历所有的兴趣点信息  
                for (MKPoiInfo poiInfo : result.poiList) {  
                    sb.append("----------------------------------------").append("/n");  
                    sb.append("名称：").append(poiInfo.name).append("/n");  
                    sb.append("地址：").append(poiInfo.address).append("/n");  
                    sb.append("经度：").append(poiInfo.pt.getLongitudeE6() / 1000000.0f).append("/n");  
                    sb.append("纬度：").append(poiInfo.pt.getLatitudeE6() / 1000000.0f).append("/n");  
                    sb.append("电话：").append(poiInfo.phoneNum).append("/n");  
                    sb.append("邮编：").append(poiInfo.postCode).append("/n");  
                    // poi类型，0：普通点，1：公交站，2：公交线路，3：地铁站，4：地铁线路  
                    sb.append("类型：").append(poiInfo.ePoiType).append("/n");  
                }  
            }  
            // 将地址信息、兴趣点信息显示在TextView上  
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
	        

	

