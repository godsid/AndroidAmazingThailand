package info.srihawong.amazingthailand;

import android.content.Context;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by godsid on 5/3/14.
 */
public class PlusAPI {
    //https://developers.google.com/apis-explorer/#p/plus/v1/plus.activities.search

    final static String API_KEY = "AIzaSyB3Mde-V5jR7RgKTg5EIw-2nRQHZ-vhAcs";
    final static String FIELDS = "items(actor/image,geocode,location,object(attachments(image,objectType)),title),nextLink,nextPageToken,updated";
    final static String MAX_RESULTS = "20";
    final static String ORDER_BY = "recent";
    final static String API_URL = "https://www.googleapis.com/plus/v1/people/100913591829936546940/activities/public?maxResults="+MAX_RESULTS+"&orderBy="+ORDER_BY+"&fields="+FIELDS;
    final static long CACHE_TIME = 15*60*1000;//within 10 minutes
    private String pageToken;
    private String query;
    JSONObject object;
    JSONArray items;
    public PlusAPI() {

    }

    public String getUrl(String query){
        this.query = query;
        String url = API_URL+"&key="+API_KEY;
        return url;
    }
    public String getUrlNextPage(String query){
        this.query = query;
        if(pageToken==null){
            return getUrl(query);
        }else{
            return getUrl(query)+"&pageToken="+pageToken;
        }
    }

    public void setObject(JSONObject object) throws JSONException {
        this.object = object;
        this.pageToken = object.getString("nextPageToken");
        this.items = object.getJSONArray("items");
    }

    public void setItems(JSONArray items){
        this.items = items;
    }
    public String getTitle(int position) throws JSONException {

       return items.getJSONObject(position)
               .getString("title")
               .replaceAll("#.*","")
               .replaceAll("\n+$","")
               .replaceAll("^\n+","")
               .trim();

    }
    public String getUseImage(int position) throws JSONException {

        return items.getJSONObject(position).getJSONObject("actor").getJSONObject("image").getString("url");

    }
    public Boolean isImage(int position) {
        try {
            return items.getJSONObject(position).has("object");

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

    }
    public ArrayList<String> getItemImage(int position) throws JSONException {
        ArrayList<String> images = new ArrayList<String>();

        JSONArray attachments = items.getJSONObject(position).getJSONObject("object").getJSONArray("attachments");
        if(attachments.length()>0){
            for(int i=0,j=attachments.length();i<j;i++){
                images.add(attachments.getJSONObject(i).getJSONObject("image").getString("url"));
            }
            return images;
        }else {
            return null;
        }

    }

}
