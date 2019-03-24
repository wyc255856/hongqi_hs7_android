package com.gh1.ghdownload;




import android.content.Context;
import android.content.Intent;

import com.gh1.ghdownload.core.DownloadService;
import com.gh1.ghdownload.entity.DownloadEntry;
import com.gh1.ghdownload.notify.DataChanger;
import com.gh1.ghdownload.notify.DataWatcher;
import com.gh1.ghdownload.utils.Constants;
import com.gh1.ghdownload.utils.Trace;

/**
 * 
 * @author shuwoom
 * @email 294299195@qq.com
 * @date 2015-9-2
 * @update 2015-9-2
 * @des DownloadManager
 */
public class DownloadManager {
	
	private static DownloadManager mInstance;
	private final Context context;
	private long lastOperateTime = 0;
	
	private DownloadManager(Context context){
		this.context = context;
		context.startService(new Intent(context,DownloadService.class));
	}
	
	public synchronized static DownloadManager getInstance(Context context){
		if(mInstance == null){
			mInstance = new DownloadManager(context);
		}
		return mInstance;
	}
	
	private Intent getIntent(DownloadEntry entry, int action){
		Intent intent = new Intent(context, DownloadService.class);
		intent.putExtra(Constants.KEY_DOWNLOAD_ENTRY, entry);
		intent.putExtra(Constants.KEY_DOWNLOAD_ACTION, action);
		return intent;
	}
	
	private boolean checkIfExecutable() {
		long temp = System.currentTimeMillis();
		if(temp - lastOperateTime > DownloadConfig.getInstance().getMin_operate_interval()){
			lastOperateTime = temp;
			return true;
		}
		return false;
	}
	
	public void add(DownloadEntry entry){
		if(!checkIfExecutable())return;
		Trace.d("DownloadManager==>add()");
		context.startService(getIntent(entry, Constants.KEY_DOWNLOAD_ACTION_ADD));
	}
	
	public void pause(DownloadEntry entry){
		if(!checkIfExecutable())return;
		Trace.d("DownloadManager==>pause()");
		context.startService(getIntent(entry, Constants.KEY_DOWNLOAD_ACTION_PAUSE));
	};

	public void resume(DownloadEntry entry){
		if(!checkIfExecutable())return;
		Trace.d("DownloadManager==>resume()");
		context.startService(getIntent(entry, Constants.KEY_DOWNLOAD_ACTION_RESUME));
	};
	
	public void cancel(DownloadEntry entry){
		if(!checkIfExecutable())return;
		Trace.d("DownloadManager==>cancel()");
		context.startService(getIntent(entry, Constants.KEY_DOWNLOAD_ACTION_CANCEL));
	};
	
	public void pauseAll(){
		if(!checkIfExecutable())return;
		Trace.d("DownloadManager==>pauseAll()");
		context.startService(getIntent(null, Constants.KEY_DOWNLOAD_ACTION_PAUSE_ALL));
	}
	
	public void recoverAll(){
		if(!checkIfExecutable())return;
		Trace.d("DownloadManager==>recoverAll()");
		context.startService(getIntent(null, Constants.KEY_DOWNLOAD_ACTION_RECOVER_ALL));
	}
	
	public void addObserver(DataWatcher dataWatcher){
		DataChanger.getInstance(context).addObserver(dataWatcher);
	}
	
	public void removeObserver(DataWatcher dataWatcher){
		DataChanger.getInstance(context).deleteObserver(dataWatcher);
	}
	
	public void removeObservers(){
		DataChanger.getInstance(context).deleteObservers();
	}
}
