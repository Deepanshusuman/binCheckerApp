package com.assets.binfinder.ui.binchekr;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingSource;
import androidx.paging.PagingState;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.assets.binfinder.model.bin.Bin;
import com.assets.binfinder.model.bin.BinDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import kotlin.coroutines.Continuation;

public class BinchekrPagingSource extends PagingSource<Integer, Bin> {

    final BinDao binDao;
    final String queryString;
    private final ExecutorService executorService;

    public BinchekrPagingSource(@NonNull BinDao binDao, @NonNull String queryString) {
        this.binDao = binDao;
        this.queryString = queryString;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @NonNull
    @Override
    public PagingSource.LoadResult<Integer, Bin> load(@NonNull LoadParams<Integer> params, @NonNull Continuation<? super LoadResult<Integer, Bin>> continuation) {
        try {
            int pageNumber = params.getKey() != null ? params.getKey() : 0; // Adjusting to zero-based index
            int pageSize = params.getLoadSize();
            int offset = pageNumber * pageSize;
            Future<List<Bin>> futureBins = executorService.submit(() -> {
                String query = queryString + " LIMIT " + pageSize + " OFFSET " + offset;
                Log.d("TAG", "Query: " + query);
                SimpleSQLiteQuery qr = new SimpleSQLiteQuery(query);
                return binDao.find_bins(qr);
            });
            List<Bin> bins = futureBins.get();
            return new PagingSource.LoadResult.Page<>(bins, pageNumber > 0 ? pageNumber - 1 : null, bins.isEmpty() ? null : pageNumber + 1);
        } catch (Exception e) {
            e.printStackTrace();
            return new LoadResult.Error<>(e);
        }
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Bin> state) {
        Integer anchorPosition = state.getAnchorPosition();
        if (anchorPosition == null) {
            return null;
        }

        LoadResult.Page<Integer, Bin> anchorPage = state.closestPageToPosition(anchorPosition);
        if (anchorPage == null) {
            return null;
        }

        Integer prevKey = anchorPage.getPrevKey();
        if (prevKey != null) {
            return prevKey + 1;
        }

        Integer nextKey = anchorPage.getNextKey();
        if (nextKey != null) {
            return nextKey - 1;
        }

        return null;
    }
}
