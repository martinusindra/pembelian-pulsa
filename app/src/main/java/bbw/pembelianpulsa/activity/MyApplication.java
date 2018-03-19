package bbw.pembelianpulsa.activity;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyApplication extends Application {

    private RequestQueue queue;

    @Override
    public void onCreate() {
        queue = Volley.newRequestQueue(this);
        super.onCreate();
    }

    public RequestQueue getQueue() {
        return queue;
    }
}
