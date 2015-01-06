package cn.edu.nuist.baidumap3;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

public class UserListFragment extends Fragment {

	private ListView lv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//��fragment��Ȼ���Խ��ܲ˵�Ч��
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.mainlist, container,false);
		view.setBackgroundResource(R.drawable.backgroundss);
		lv = (ListView) view.findViewById(R.id.lv);
		MyAdapter mAdapter = new MyAdapter(getActivity());// �õ�һ��MyAdapter����
		lv.setAdapter(mAdapter);// ΪListView��Adapter
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.user, menu);
	}



	private class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;// �õ�һ��LayoutInfalter�����������벼��
		/* ���캯�� */
		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			return UserManagerActivity.listItem.size();// ��������ĳ���
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			// �۲�convertView��ListView�������
			Log.v("MyListViewBase", "getView " + position + " " + convertView);
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item, null);
				holder = new ViewHolder();
				/* �õ������ؼ��Ķ��� */
				holder.order = (TextView) convertView.findViewById(R.id.userOrder_text);
				holder.id = (TextView) convertView.findViewById(R.id.userId_text);
				holder.username = (TextView) convertView.findViewById(R.id.username_text);
				holder.cb = (CheckBox) convertView.findViewById(R.id.select_check);
				convertView.setTag(holder);// ��ViewHolder����
			} else {
				holder = (ViewHolder) convertView.getTag();// ȡ��ViewHolder����
			}
		
			holder.order.setText(UserManagerActivity.listItem.get(position).get("order").toString());
			holder.id.setText(UserManagerActivity.listItem.get(position).get("id").toString());
			holder.username.setText(UserManagerActivity.listItem.get(position).get("username").toString());
			holder.cb.setChecked(Boolean.parseBoolean(UserManagerActivity.listItem.get(position).get("checked").toString()));

			/* ΪCheckBox��ӵ���¼� */
			holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Log.v("MyListViewBase", "��ѡ����checkBox" + position+isChecked); // ��ӡCheckBox�ĵ����Ϣ
					//ɾ����һ�У��޸���һ�У�
					if(isChecked)
						UserManagerActivity.listItem.get(position).put("checked", true);
					else
						UserManagerActivity.listItem.get(position).put("checked", false);
					for(int i=0;i<UserManagerActivity.listItem.size();i++)
					{
						Log.v("list", UserManagerActivity.listItem.get(i).get("id")+""+UserManagerActivity.listItem.get(i).get("checked").toString());
					}
				}
			});
			return convertView;
		}
	}

	/* ��ſؼ� */
	public final class ViewHolder {
		public TextView order;
		public TextView id;
		public TextView username;
		public CheckBox cb;
	}

}
