package com.example.abood.youhu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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


public class Pictures extends Fragment {

    private final String URL_TO_HIT = "http://ahmadkhwaja.com/abd/getImages.php";
    ListView picLV;
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_pictures, container, false);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));

        dialog = new ProgressDialog(getContext());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");


        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        new JSONTask().execute(URL_TO_HIT);


        // ListAdapter buckysAdapter = new CustomAdapterPictures(getActivity(), foods);
        picLV = (ListView) view.findViewById(R.id.piclv);



        return view;
    }


    public class JSONTask extends AsyncTask<String, String, List<PictureModel>> {


        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }


        protected List<PictureModel> doInBackground(String... params) {
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

                List<PictureModel> pictureModelList = new ArrayList<>();

                Gson gson = new Gson();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    /**
                     * below single line of code from Gson saves you from writing the json parsing yourself
                     * which is commented below
                     */
                    PictureModel pictureModel = gson.fromJson(finalObject.toString(), PictureModel.class); // a single line json parsing using Gson
//                    pictureModel.setMovie(finalObject.getString("movie"));
//                    pictureModel.setYear(finalObject.getInt("year"));
//                    pictureModel.setRating((float) finalObject.getDouble("rating"));
//                    pictureModel.setDirector(finalObject.getString("director"));
//
//                    pictureModel.setDuration(finalObject.getString("duration"));
//                    pictureModel.setTagline(finalObject.getString("tagline"));
//                    pictureModel.setImage(finalObject.getString("image"));
//                    pictureModel.setStory(finalObject.getString("story"));
//
//                    List<MovieModel.Cast> castList = new ArrayList<>();
//                    for(int j=0; j<finalObject.getJSONArray("cast").length(); j++){
//                        MovieModel.Cast cast = new MovieModel.Cast();
//                        cast.setName(finalObject.getJSONArray("cast").getJSONObject(j).getString("name"));
//                        castList.add(cast);
//                    }
//                    pictureModel.setCastList(castList);
                    // adding the final object in the list
                    pictureModelList.add(pictureModel);
                }
                return pictureModelList;

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


        protected void onPostExecute(final List<PictureModel> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result != null) {
                PictureAdapter adapter = new PictureAdapter(getContext(), R.layout.pictures_row, result);
                picLV.setAdapter(adapter);
                picLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {  // list item click opens a new detailed activity
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        PictureModel pictureModel = result.get(position); // getting the model
                        Intent intent = new Intent(getActivity(), PictureDetailActivity.class);
                        intent.putExtra("pictureModel", new Gson().toJson(pictureModel)); // converting model json into string type and sending it via intent
                       startActivity(intent);
                    }
                });
            } else {
                Toast.makeText(getContext(), "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }

    }
}


     class PictureAdapter extends ArrayAdapter {

        private List<PictureModel> pictureModelList;
        private int resource;
        private LayoutInflater inflater;

        public PictureAdapter(Context context, int resource, List<PictureModel> objects) {
            super(context, resource, objects);
            pictureModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(resource, null);
                holder.picthump = (ImageView) convertView.findViewById(R.id.picthumb);

                holder.picname = (TextView) convertView.findViewById(R.id.pictext);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);

            // Then later, when you want to display image
            final ViewHolder finalHolder = holder;
            ImageLoader.getInstance().displayImage(pictureModelList.get(position).getImage(), holder.picthump, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                    finalHolder.picthump.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                    finalHolder.picthump.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                    finalHolder.picthump.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    progressBar.setVisibility(View.GONE);
                    finalHolder.picthump.setVisibility(View.INVISIBLE);
                }
            });


            holder.picname.setText(pictureModelList.get(position).getName());

            return convertView;
        }



        class ViewHolder{
            private ImageView picthump;

            private TextView picname;

        }




    }
