package edu.gdmec.s07150815.myguard.m7processmanager;

import android.app.ActivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.gdmec.s07150815.myguard.R;
import edu.gdmec.s07150815.myguard.m7processmanager.adapter.ProcessManagerAdapter;
import edu.gdmec.s07150815.myguard.m7processmanager.entity.TaskInfo;

public class ProcessManagerActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mRunProcessNum;
    private TextView mMemmoryTV;
    private TextView mProcesNumTV;
    private ListView mListView;
    ProcessManagerAdapter adapter;
    private List<TaskInfo> runningTaskInfos;
    private List<TaskInfo> userTaskInfo = new ArrayList<TaskInfo>();
    private List<TaskInfo> sysTaskInfo = new ArrayList<TaskInfo>();
    private ActivityManager manager;
    private int runningPocessCount;
    private long totalMem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_process_manager);
        initView();
        //fillData();
    }

    @Override
    protected void onResume() {
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
        super.onResume();
    }
    private void initView(){
        //findViewById(R.id)
    }
    @Override
    public void onClick(View v) {

    }
}
