package bbw.pembelianpulsa.activity.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import bbw.pembelianpulsa.activity.MyApplication;

public class TransactionFragment extends Fragment {

    private MyApplication app;
    private TaskCallbacks mCallbacks;
    private Activity parent;

    public interface TaskCallbacks {
        void onPreExecute();
        void onPostExecuteTransaction();
        void onError();
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

    public void SaveTransaction(Activity parent, String operator, String harga){
        this.parent = parent;

        app = (MyApplication) parent.getApplication();

        String url = "http://202.158.48.243:2018/soal/middle/pulsa.jsp?type=transaction&userid=1&operator=" + operator + "&harga=" + harga;

        JsonObjectRequest jsonReq;
        jsonReq = new JsonObjectRequest(Request.Method.POST, url, null, response, error);

        app.getQueue().add(jsonReq);
    }

    Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject json) {
            if (json != null) {
                try {
                    if(json.get("status").toString().equals("1")){
                        Toast.makeText(parent, json.get("msg").toString(), Toast.LENGTH_SHORT).show();
                        mCallbacks.onPostExecuteTransaction();
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
