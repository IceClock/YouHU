package com.example.abood.youhu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS;

public class Lectures extends Fragment{
    private final String URL_TO_HIT = "http://ahmadkhwaja.com/abd/getvideos.php";
    ListView vidLV;
    private ProgressDialog dialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lectures, container, false);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));

        dialog = new ProgressDialog(getContext());
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setMessage("Loading. Please wait...");




        new JSONTask().execute(URL_TO_HIT);

        vidLV = (ListView) view.findViewById(R.id.lectlv);
        vidLV.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);

        return view;
    }
    public class JSONTask extends AsyncTask<String, String, List<LectureModel>> {


        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }


        protected List<LectureModel> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("menu");

                List<LectureModel> lectureModelList = new ArrayList<>();

                Gson gson = new Gson();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    /**
                     * below single line of code from Gson saves you from writing the json parsing yourself
                     * which is commented below
                     */
                    LectureModel lectureModel = gson.fromJson(finalObject.toString(), LectureModel.class); // a single line json parsing using Gson
//                    lectureModel.setMovie(finalObject.getString("movie"));
//                    lectureModel.setYear(finalObject.getInt("year"));
//                    lectureModel.setRating((float) finalObject.getDouble("rating"));
//                    lectureModel.setDirector(finalObject.getString("director"));
//
//                    lectureModel.setDuration(finalObject.getString("duration"));
//                    lectureModel.setTagline(finalObject.getString("tagline"));
//                    lectureModel.setImage(finalObject.getString("image"));
//                    lectureModel.setStory(finalObject.getString("story"));
//
//                    List<MovieModel.Cast> castList = new ArrayList<>();
//                    for(int j=0; j<finalObject.getJSONArray("cast").length(); j++){
//                        MovieModel.Cast cast = new MovieModel.Cast();
//                        cast.setName(finalObject.getJSONArray("cast").getJSONObject(j).getString("name"));
//                        castList.add(cast);
//                    }
//                    lectureModel.setCastList(castList);
                    // adding the final object in the list
                    lectureModelList.add(lectureModel);


                }
                return lectureModelList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        protected void onPostExecute(final List<LectureModel> result) {
            super.onPostExecute(result);

            dialog.dismiss();
            if (result != null) {
                dialog.dismiss();
                final LectureAdapter adapter = new LectureAdapter(getActivity(), R.layout.lectures_row, result);
                vidLV.setAdapter(adapter);
                vidLV.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);


                vidLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {  // list item click opens a new detailed activity

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        LectureModel lectureModel = result.get(position); // getting the model
                        Intent intent = new Intent(getActivity(), LectureDetailActivity.class);
                        intent.putExtra("lectureModel", new Gson().toJson(lectureModel)); // converting model json into string type and sending it via intent
                        startActivity(intent);
                    }
                });
            } else {
                Toast.makeText(getContext(), "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }

    }


}
class LectureAdapter extends ArrayAdapter {

    private List<LectureModel> lectureModelList;
    private int resource;
    private LayoutInflater inflater;

    public LectureAdapter(Context context, int resource, List<LectureModel> objects) {
        super(context, resource, objects);
        lectureModelList = objects;
        this.resource = resource;
        inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(resource, null);
            holder.vidthump = (VideoView) convertView.findViewById(R.id.lecthump);

            holder.vidname = (TextView) convertView.findViewById(R.id.lecttext);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
        holder.vidthump.setVideoPath(lectureModelList.get(position).getVideo());


        holder.vidthump.setOnPreparedListener(new
                                             MediaPlayer.OnPreparedListener()  {
                                                 @Override
                                                 public void onPrepared(MediaPlayer mp) {
                                                     progressBar.setVisibility(View.GONE);
                                                     mp.start();
                                                     mp.seekTo(mp.getDuration()/2);
                                                     mp.pause();
                                                 }
                                             });

        holder.vidname.setText(lectureModelList.get(position).getName());
        return convertView;
    }



    class ViewHolder{
        private VideoView vidthump;

        private TextView vidname;

    }




}