package bbw.pembelianpulsa.activity.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import bbw.pembelianpulsa.activity.MyApplication;


public class LoginFragment extends Fragment {

    private MyApplication app;
    private TaskCallbacks mCallbacks;
    private Activity parent;

    public interface TaskCallbacks {
        void onPreExecute();
        void onPostExecute();
        void onError();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (MyApplication) getActivity().getApplication();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (TaskCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public void Login(String username, String password, Activity parent){
        this.parent = parent;

        String url = "http://202.158.48.243:2018/soal/middle/pulsa.jsp?type=login&username=" + username +"&password=" + password;

        JsonObjectRequest jsonReq;
        jsonReq = new JsonObjectRequest(Request.Method.GET, url, null, response, error);

        app.getQueue().add(jsonReq);
    }

    Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject json) {
            if (json != null) {
                try {
                    if(json.get("status").toString().equals("1")){
                        Toast.makeText(parent, json.get("msg").toString(), Toast.LENGTH_SHORT).show();
                        mCallbacks.onPostExecute();
                    } else {
                        Toast.makeText(parent, json.get("msg").toString(), Toast.LENGTH_SHORT).show();
                        mCallbacks.onError();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    Response.ErrorListener error = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            int statusCode = 0;
            if (volleyError.networkResponse != null) {
                statusCode = volleyError.networkResponse.statusCode;
                Log.e(getClass().getName(), "status code: " + statusCode);
                String errJson = new String(volleyError.networkResponse.data);
                Log.e(getClass().getName(), "errJson: " + errJson);
                Toast.makeText(parent, "Error code: " + statusCode, Toast.LENGTH_SHORT).show();
                mCallbacks.onError();
            } else {
                Toast.makeText(parent, "No connection to Server.", Toast.LENGTH_SHORT).show();
                mCallbacks.onError();
            }
        }
    };
}
