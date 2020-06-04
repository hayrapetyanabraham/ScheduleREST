package com.smartclick.custompushnotifications.receivers;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.widget.Toast;

import com.smartclick.custompushnotifications.GetDataService;
import com.smartclick.custompushnotifications.MainActivity;
import com.smartclick.custompushnotifications.RetrofitClientInstance;
import com.smartclick.custompushnotifications.models.RetroPhoto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class MySimpleReceiver extends ResultReceiver {
    private Receiver receiver;

    // Constructor takes a handler
    public MySimpleReceiver(Handler handler) {
        super(handler);
    }

    // Setter for assigning the receiver
    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    // Defines our event interface for communication
    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    // Setup the callback for when data is received from the service
    public static MySimpleReceiver setupServiceReceiver(final Context context) {
        MySimpleReceiver receiverForSimple = new MySimpleReceiver(new Handler());
        // This is where we specify what happens when data is received from the
        // service
        receiverForSimple.setReceiver(new MySimpleReceiver.Receiver() {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {

                if (resultCode == RESULT_OK) {
                    GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                    Call<List<RetroPhoto>> call = service.getAllPhotos();
                    call.enqueue(new Callback<List<RetroPhoto>>() {
                        @Override
                        public void onResponse(Call<List<RetroPhoto>> call, Response<List<RetroPhoto>> response) {
                            String resultValue = resultData.getString("resultValue");
                            Toast.makeText(context, "onResponse"+ resultValue, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<List<RetroPhoto>> call, Throwable t) {
                            String resultValue = resultData.getString("resultValue");
                            Toast.makeText(context, "onFailure"+resultValue, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        return receiverForSimple;
    }

    // Delegate method which passes the result to the receiver if the receiver
    // has been assigned
    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (receiver != null) {
            receiver.onReceiveResult(resultCode, resultData);
        }
    }
}
