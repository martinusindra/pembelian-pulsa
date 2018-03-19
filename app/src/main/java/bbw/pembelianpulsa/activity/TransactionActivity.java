package bbw.pembelianpulsa.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bbw.pembelianpulsa.R;
import bbw.pembelianpulsa.activity.fragment.OperatorFragment;
import bbw.pembelianpulsa.activity.fragment.TransactionFragment;
import bbw.pembelianpulsa.activity.fragment.VoucherFragment;

public class TransactionActivity extends AppCompatActivity implements OperatorFragment.TaskCallbacks, VoucherFragment.TaskCallbacks,
        TransactionFragment.TaskCallbacks, View.OnClickListener, AdapterView.OnItemSelectedListener{

    private EditText ePhoneNumber;
    private Spinner sOperator;
    private Spinner sPulsa;
    private TextView tHarga;
    private Button bSave;

    private MyApplication app;
    private ProgressDialog pd;
    private OperatorFragment operatorFragment;
    private static final String OPERATOR_FRAGMENT = "OperatorFragment";
    private VoucherFragment voucherFragment;
    private static final String VOUCHER_FRAGMENT = "VoucherFragment";
    private TransactionFragment transactionFragment;
    private static final String TRANSACTION_FRAGMENT = "TransactionFragment";
    private List<String> listPulsa = new ArrayList<>();
    private Map<Integer, String> listHarga;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        app = (MyApplication) getApplication();

        ePhoneNumber = (EditText) findViewById(R.id.ePhoneNumber);
        sOperator = (Spinner) findViewById(R.id.sOperator);
        sPulsa = (Spinner) findViewById(R.id.sPulsa);
        tHarga = (TextView) findViewById(R.id.tHarga);
        bSave = (Button) findViewById(R.id.bSave);

        operatorFragment = (OperatorFragment) getFragmentManager().findFragmentByTag(OPERATOR_FRAGMENT);
        if (operatorFragment == null) {
            operatorFragment = new OperatorFragment();
            getFragmentManager().beginTransaction().add(operatorFragment, OPERATOR_FRAGMENT).commit();
        }

        voucherFragment = (VoucherFragment) getFragmentManager().findFragmentByTag(VOUCHER_FRAGMENT);
        if (voucherFragment == null) {
            voucherFragment = new VoucherFragment();
            getFragmentManager().beginTransaction().add(voucherFragment, VOUCHER_FRAGMENT).commit();
        }

        transactionFragment = (TransactionFragment) getFragmentManager().findFragmentByTag(TRANSACTION_FRAGMENT);
        if (transactionFragment == null) {
            transactionFragment = new TransactionFragment();
            getFragmentManager().beginTransaction().add(transactionFragment, TRANSACTION_FRAGMENT).commit();
        }

        bSave.setOnClickListener(this);
        sOperator.setOnItemSelectedListener(this);
        sPulsa.setOnItemSelectedListener(this);

        onPreExecute();
        operatorFragment.GetOperators(this);
    }

    @Override
    public void onPreExecute() {
        initDialog();
        pd.show();
    }

    @Override
    public void onPostExecuteTransaction() {
        if (pd.isShowing()) {
            pd.dismiss();
        }

        ePhoneNumber.setText("");
    }

    @Override
    public void onPostExecuteVoucher(Map<Integer, String> listHarga, List<String> listPulsa) {
        if (pd.isShowing()) {
            pd.dismiss();
        }
        this.listHarga = listHarga;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(TransactionActivity.this, R.layout.support_simple_spinner_dropdown_item ,listPulsa);
        sPulsa.setAdapter(adapter);
    }

    @Override
    public void onPostExecute(List<String> listOperator) {
        if (pd.isShowing()) {
            pd.dismiss();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(TransactionActivity.this, R.layout.support_simple_spinner_dropdown_item ,listOperator);
        sOperator.setAdapter(adapter);
    }

    @Override
    public void onError() {
        if (pd.isShowing()) {
            pd.dismiss();
        }
    }

    public void initDialog() {
        pd = new ProgressDialog(this);
        pd.setTitle(R.string.app_name);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Login ...");
        pd.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bSave:
                if(ePhoneNumber.getText().toString().equals("")){
                    Toast.makeText(TransactionActivity.this, "Please input the phone number first", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Patterns.PHONE.matcher(ePhoneNumber.getText().toString()).matches() || ePhoneNumber.getText().toString().length() < 10){
                    Toast.makeText(TransactionActivity.this, "Wrong phone number format", Toast.LENGTH_SHORT).show();
                    return;
                }
                onPreExecute();
                transactionFragment.SaveTransaction(this, sOperator.getSelectedItem().toString(), tHarga.getText().toString());
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        Spinner spinner = (Spinner) parent;
        switch (spinner.getId()){
            case R.id.sOperator:
                String selectedOperator = parent.getItemAtPosition(position).toString();
                onPreExecute();
                voucherFragment.GetVouchers(this, selectedOperator);
                break;
            case R.id.sPulsa:
                tHarga.setText(listHarga.get(position));
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
