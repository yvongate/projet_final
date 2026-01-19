package com.uici.classmanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uici.classmanager.R;
import com.uici.classmanager.models.POI;

import java.util.List;

public class POIAdapter extends RecyclerView.Adapter<POIAdapter.POIViewHolder> {

    private List<POI> pois;
    private OnPOIClickListener listener;

    public interface OnPOIClickListener {
        void onPOIClick(POI poi);
    }

    public POIAdapter(List<POI> pois, OnPOIClickListener listener) {
        this.pois = pois;
        this.listener = listener;
    }

    @NonNull
    @Override
    public POIViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poi, parent, false);
        return new POIViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull POIViewHolder holder, int position) {
        POI poi = pois.get(position);
        holder.textViewNom.setText(poi.getNom());
        holder.textViewType.setText(poi.getType());
        holder.textViewCoordinates.setText("Lat: " + poi.getLatitude() + ", Lon: " + poi.getLongitude());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPOIClick(poi);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pois.size();
    }

    static class POIViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNom;
        TextView textViewType;
        TextView textViewCoordinates;

        public POIViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNom = itemView.findViewById(R.id.textViewPOINom);
            textViewType = itemView.findViewById(R.id.textViewPOIType);
            textViewCoordinates = itemView.findViewById(R.id.textViewPOICoordinates);
        }
    }
}
