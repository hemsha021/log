package vion.logtracks.network;


import com.google.gson.JsonElement;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiInterface {

    @POST
    @FormUrlEncoded
    Observable<JsonElement> makeCall(@Url String url, @FieldMap Map<String, String> map);

}
