package teli.com.kpcc.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import teli.com.kpcc.R;
import teli.com.kpcc.activities.AccountActivity;
import teli.com.kpcc.activities.EventsActivity;
import teli.com.kpcc.activities.HomeActivity;
import teli.com.kpcc.activities.ImageActivity;
import teli.com.kpcc.activities.MessageActivity;
import teli.com.kpcc.activities.OurLeadersActivity;
import teli.com.kpcc.activities.PoolActivity;
import teli.com.kpcc.activities.VideoActivity;
import teli.com.kpcc.adapters.SwipeAdapter;

/**
 * Created by naveen on 14/1/15.
 */
public class MenuFragment extends Fragment implements AdapterView.OnItemClickListener {

    @InjectView(R.id.lv_swipe)
    ListView listSwipe;

    String[] nameOfAppsToShareWith;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.menu_listview,container,false);
        ButterKnife.inject(this,view);

        SwipeAdapter adapter=new SwipeAdapter(getActivity());
        listSwipe.setAdapter(adapter);

        return view;
    }

   public MenuFragment(){

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listSwipe.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
Intent intent = null;
        switch(i){

            case 0:
                if(!(getActivity() instanceof HomeActivity)) {
                    intent = new Intent(getActivity(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);
                    getActivity().finish();
                }
                break;
            case 1:
                if(!(getActivity() instanceof OurLeadersActivity))
                startActivity(new Intent(getActivity(), OurLeadersActivity.class));
                break;
            case 2:
                if(!(getActivity() instanceof EventsActivity))
                startActivity(new Intent(getActivity(), EventsActivity.class));
                break;
            case 3:
                if(!(getActivity() instanceof ImageActivity))
                startActivity(new Intent(getActivity(), ImageActivity.class));
                break;
            case 4:
                if(!(getActivity() instanceof VideoActivity))
                startActivity(new Intent(getActivity(), VideoActivity.class));
                break;
            case 5:
                if(!(getActivity() instanceof PoolActivity))
                startActivity(new Intent(getActivity(), PoolActivity.class));
                break;
            case 6:
                share ("KPCC");

                break;
            case 7:
                if(!(getActivity() instanceof MessageActivity))
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            case 8:
                if(!(getActivity() instanceof AccountActivity))
               startActivity(new Intent(getActivity(), AccountActivity.class));
                break;
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        onDestroy();
    }

    private void share(String messages) {

        SharedPreferences prefss = getActivity().getSharedPreferences("SHARE",getActivity().MODE_PRIVATE);
        String message= prefss.getString("SHAREMESSAGE","KPCC");


         nameOfAppsToShareWith = new String[] { "google+", "facebook", "twitter", "gmail","hangouts","whatsapp","messages","messaging", "email"};
        String[] blacklist = new String[]{"com.any.package", "net.other.package"};

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, message);

        startActivityForResult(generateCustomChooserIntent(intent, blacklist), 0);

    }

    private Intent generateCustomChooserIntent(Intent prototype, String[] forbiddenChoices) {

        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        List<HashMap<String, String>> intentMetaInfo = new ArrayList<HashMap<String, String>>();
        Intent chooserIntent;

        Intent dummy = new Intent(prototype.getAction());
        dummy.setType(prototype.getType());
        List<ResolveInfo> resInfo = getActivity().getPackageManager().queryIntentActivities(dummy, 0);

        if (!resInfo.isEmpty())
        {
            for (ResolveInfo resolveInfo : resInfo)
            {
                if (resolveInfo.activityInfo == null
                        || Arrays.asList(forbiddenChoices).contains(
                        resolveInfo.activityInfo.packageName))
                    continue;

                HashMap<String, String> info = new HashMap<String, String>();
                info.put("packageName", resolveInfo.activityInfo.packageName);
                info.put("className", resolveInfo.activityInfo.name);
                String appName = String.valueOf(resolveInfo.activityInfo
                        .loadLabel(getActivity().getPackageManager()));
                info.put("simpleName", appName);

                if (Arrays.asList(nameOfAppsToShareWith).contains(
                        appName.toLowerCase()))
                {
                    intentMetaInfo.add(info);
                }
            }

            for (HashMap<String, String> metaInfo : intentMetaInfo)
            {
                Intent targetedShareIntent = (Intent) prototype.clone();
                targetedShareIntent.setPackage(metaInfo.get("packageName"));
                targetedShareIntent.setClassName(
                        metaInfo.get("packageName"),
                        metaInfo.get("className"));
                targetedShareIntents.add(targetedShareIntent);
            }
            String shareVia = "SHARE VIA";
            String shareTitle = shareVia.substring(0, 1).toUpperCase()
                    + shareVia.substring(1);
            chooserIntent = Intent.createChooser(targetedShareIntents
                    .remove(targetedShareIntents.size() - 1), shareTitle);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    targetedShareIntents.toArray(new Parcelable[] {}));
            return chooserIntent;
        }


        return Intent.createChooser(prototype,"Share via");

    }
}
