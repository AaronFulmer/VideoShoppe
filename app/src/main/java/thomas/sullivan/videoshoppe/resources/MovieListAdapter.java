package thomas.sullivan.videoshoppe.resources;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import thomas.sullivan.videoshoppe.activity.R;

public class MovieListAdapter extends BaseAdapter {

    private ArrayList<MovieItem> movieItems;
    private LayoutInflater layoutInflater;

    public MovieListAdapter(Context aContext, ArrayList<MovieItem> aMovieItems)
    {
        this.movieItems = aMovieItems;
        layoutInflater = LayoutInflater.from(aContext);
    }


    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int position) {
        return movieItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_movie_layout, null);
            holder = new ViewHolder();
            holder.titleView = (TextView) convertView.findViewById(R.id.title);
            holder.directorView = (TextView) convertView.findViewById(R.id.director);
            holder.actorsView = (TextView) convertView.findViewById(R.id.actor);
            holder.genreView = (TextView) convertView.findViewById(R.id.genre);
            holder.releaseDateView = (TextView) convertView.findViewById(R.id.releaseDate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titleView.setText(movieItems.get(position).getTitle());
        holder.directorView.setText("Directed by: "+movieItems.get(position).getDirector());
        holder.actorsView.setText("Actors: "+movieItems.get(position).getActor());
        holder.genreView.setText("Genre: "+movieItems.get(position).getGenre());
        holder.releaseDateView.setText("Released: "+movieItems.get(position).getReleaseDate());

        return convertView;
    }

    static class ViewHolder {
        TextView titleView;
        TextView directorView;
        TextView actorsView;
        TextView genreView;
        TextView releaseDateView;
    }
}
