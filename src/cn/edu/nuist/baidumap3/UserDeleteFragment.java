package cn.edu.nuist.baidumap3;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class UserDeleteFragment extends Fragment{
	private ContentResolver contentResolver;
	private static String CONTENT_URI_USER="content://cn.edu.nuist.baidumap3.MySqliteContentProvider/user";
	private String ids = "";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			//inflater.inflate(R.layout., root)
			ImageView view=new ImageView(getActivity());
			view.setImageResource(R.drawable.backgroundss);
			setHasOptionsMenu(true);
			
			contentResolver=getActivity().getContentResolver();
			ids="";
			for(int i=0;i<UserManagerActivity.listItem.size();i++)
			{
				Log.v("list", UserManagerActivity.listItem.get(i).get("id")+""+UserManagerActivity.listItem.get(i).get("checked").toString());
				if(Boolean.parseBoolean(UserManagerActivity.listItem.get(i).get("checked").toString()))
				{
					ids+=UserManagerActivity.listItem.get(i).get("id").toString()+",";
				}
			}
			if(ids.length()>0)
			{
				ids = ids.substring(0,ids.length()-1);
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("ɾ��ȷ��");
				builder.setMessage("ȷ��Ҫɾ����?");
				builder.setPositiveButton("ȷ��", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Uri uri=Uri.parse(CONTENT_URI_USER);
						int rows = contentResolver.delete(uri, "_id in ("+ids+")", null);
						if(rows>0)
						{
							UserManagerActivity.getDate();
							Toast toast = Toast.makeText(getActivity(), "ɾ���ɹ�", Toast.LENGTH_LONG);
							toast.show();
						}
						else
						{
								Toast toast = Toast.makeText(getActivity(), "ɾ��ʧ��", Toast.LENGTH_LONG);
								toast.show();
						}
					}
				});
				builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() { 
				    public void onClick(DialogInterface dialog, int whichButton) { 
				    	dialog.dismiss();
				    }
				});
				builder.create().show();
			}
			else
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("ɾ����Ϣ");
				builder.setMessage("û�п���ɾ��������");
				builder.setPositiveButton("ȷ��", null);
				builder.create().show();
			}
			return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.user, menu);
	}

}
