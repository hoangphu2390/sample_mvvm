package com.sample_mvvm.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is the base adapter include general properties
 * <p>
 * Created by LoiHo on 12/10/2015
 */
public class BaseAdapter<V, VH extends BaseHolder> extends RecyclerView.Adapter<VH> {

    protected LayoutInflater m_inflater;
    protected List<V> m_dataSource = Collections.emptyList();

    public BaseAdapter(LayoutInflater m_inflater) {
        this.m_inflater = m_inflater;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bindData(m_dataSource.get(position));
    }

    @Override
    public int getItemCount() {
        return m_dataSource.size();
    }

    public void setDataSource(List<V> m_dataSource) {
      try {
        this.m_dataSource = new ArrayList<>(m_dataSource);
        notifyDataSetChanged();
      }catch (IllegalStateException e){      }
    }

    public List<V> getDataSource() {
        return m_dataSource;
    }

    public void appendItem(V item) {
        if (this.m_dataSource.isEmpty()) {
            this.m_dataSource = new ArrayList<>();
        }
        this.m_dataSource.add(item);
        notifyItemInserted(getItemCount());
    }

    public void removeAtPosition(int position){
        if(this.m_dataSource.size()> position){
            m_dataSource.remove(position);
            notifyItemRangeRemoved(position, 1);
        }
    }

    public void appendItems(@NonNull List<V> items) {
        if (m_dataSource.isEmpty()) {
            setDataSource(items);
        } else {
            int positionStart = getItemCount() - 1;
            m_dataSource.addAll(items);
            notifyItemRangeInserted(positionStart, items.size());
        }
    }

    public void addItemAtFirst(V item) {
        if (this.m_dataSource.isEmpty()) {
            this.m_dataSource = new ArrayList<>();
        }
        this.m_dataSource.add(0, item);
        notifyItemInserted(0);
    }

    public void addAtFirstAndRemoveEnd(V item) {
        if (this.m_dataSource.isEmpty()) {
            this.m_dataSource = new ArrayList<>();
        }
        this.m_dataSource.add(0, item);
        this.m_dataSource.remove(getItemCount() - 1);
        notifyItemRemoved(getItemCount() - 1);
        notifyItemInserted(0);
    }
}
