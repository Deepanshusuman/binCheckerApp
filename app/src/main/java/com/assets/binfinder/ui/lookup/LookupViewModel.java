package com.assets.binfinder.ui.lookup;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LookupViewModel extends AndroidViewModel {
    @NonNull
    public ArrayList<String> tools;
    @NonNull
    public ArrayList<String> toolsSelected;
    SharedPreferences sharedPreferences;


    String toolsAvail = "[\"Bulk\",\"Binlist.net\",\"By Network\",\"By Country\",\"By Product\",\"By Issuer\",\"By Type\",\"Ip Details\",\"IFSC\",\"Seon.io\",\"Validate\",\"Random User\",\"Generate\"]";

    @Inject
    public LookupViewModel(@NonNull Application application, @NonNull SharedPreferences sharedPreferences) {
        super(application);
        this.sharedPreferences = sharedPreferences;
        tools = getTools();
        toolsSelected = getToolsSelected();
    }


    public void addChip(@NonNull String chip) {
        setSelectedTool(chip);
        toolsSelected.add(chip);
        tools.remove(chip);
        sharedPreferences.edit().putString("toolsSelected", new JSONArray(toolsSelected).toString()).apply();
        sharedPreferences.edit().putString("tools", new JSONArray(tools).toString()).apply();
    }

    @NonNull
    public String getSelectedTool() {
        // Mar. 15th, 2023, 23:59 CET. to millis  1678921200000L
//        if (sharedPreferences.getString("selectedTool", "Binchekr").equals("Binlist.net")) {
//            sharedPreferences.edit().putString("selectedTool", "Binchekr").apply();
//            return "Binchekr";
//        }
        return sharedPreferences.getString("selectedTool", "Binchekr");
    }

    public void setSelectedTool(@NonNull String selectedTool) {
        sharedPreferences.edit().putString("selectedTool", selectedTool).apply();
    }

    public void removeChip(@NonNull String chip) {
        toolsSelected.remove(chip);
        tools.add(chip);

        sharedPreferences.edit().putString("toolsSelected", new JSONArray(toolsSelected).toString()).apply();
        sharedPreferences.edit().putString("tools", new JSONArray(tools).toString()).apply();
    }

    @NonNull
    public ArrayList<String> getTools() {
        try {
            JSONArray array = new JSONArray(sharedPreferences.getString("tools", toolsAvail));
            ArrayList<String> tools = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                // can be removed after 15 Mar 2023
//                if (Objects.equals(array.getString(i), "Binlist.net")) {
//                    continue;
//                }
                tools.add(array.getString(i));
            }
            return tools;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @NonNull
    public ArrayList<String> getToolsSelected() {
        try {
            JSONArray array = new JSONArray(sharedPreferences.getString("toolsSelected", "[]"));
            ArrayList<String> toolsSelected = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                // can be removed after 1 feb 2023
//                if (Objects.equals(array.getString(i), "Binlist.net")) {
//                    continue;
//                }
                toolsSelected.add(array.getString(i));
            }
            return toolsSelected;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}