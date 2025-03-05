package com.example.programandroid.ui.program;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.programandroid.R;

import java.util.ArrayList;

public class ProgramFragment extends Fragment {

    private ListView listViewProgram;
    private ProgramViewModel programViewModel;

    public ProgramFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Инфлейтваме layout-а за фрагмента
        View view = inflater.inflate(R.layout.fragment_program, container, false);

        // Получаваме инстанция на ViewModel
        programViewModel = new ViewModelProvider(requireActivity()).get(ProgramViewModel.class);

        listViewProgram = view.findViewById(R.id.ListViewProgram);

        ArrayAdapter<String> adapterListView = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, new ArrayList<>());
        listViewProgram.setAdapter(adapterListView);

        // Намираме Spinner в XML файла
        Spinner spinner = view.findViewById(R.id.spinnerDaysOfWeek);

        // Създаваме ArrayAdapter за Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.spinner_options_days_of_week, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDay = parent.getItemAtPosition(position).toString();
                if (programViewModel.getSchedule() != null) {
                    programViewModel.updateSchedule(selectedDay);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Подписваме се за наблюдение на данните от ViewModel
        programViewModel.getScheduleLiveData().observe(getViewLifecycleOwner(), activities -> {
            // Обновяваме ListView с новите данни
            ArrayAdapter<String> adapterListView_ = (ArrayAdapter<String>) listViewProgram.getAdapter();
            adapterListView_.clear();
            adapterListView_.addAll(activities);
            adapterListView_.notifyDataSetChanged();
        });

        return view;
    }
}
