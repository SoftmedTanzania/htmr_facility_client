package com.softmed.htmr_facility.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.softmed.htmr_facility.R;
import com.softmed.htmr_facility.adapters.TbClientListAdapter;
import com.softmed.htmr_facility.base.BaseActivity;
import com.softmed.htmr_facility.dom.objects.Patient;
import com.softmed.htmr_facility.dom.objects.TbPatient;
import com.softmed.htmr_facility.utils.PatientsSearchFilterUtil;
import com.softmed.htmr_facility.viewmodels.PatientsListViewModel;

import static com.softmed.htmr_facility.utils.constants.TB_SERVICE_ID;

/**
 * Created by issy on 12/28/17.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class TbClientListActivity extends BaseActivity {

    //VIEWS
    Toolbar toolbar;
    RecyclerView patientsRecycler;
    TbClientListAdapter adapter;
    EditText patientNamesSearch, patientVillageSearch;
    Button filterButton;
    TextView activityTitle;

    private String clientFirstNameValue, clientLastNameValue, clientCTCNumberValue, clientVillageValue;
    private PatientsListViewModel patientsListViewModel;
    private List<Patient> patientList = new ArrayList<>();

    private PatientsSearchFilterUtil patientsSearchFilterUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tb_clients_list);
        setupview();

        patientsSearchFilterUtil  = new PatientsSearchFilterUtil(patientList);

        if (toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        activityTitle.setText(getResources().getString(R.string.clients_list)+" | "+getResources().getString(R.string.tb));

        adapter = new TbClientListAdapter(this, TB_SERVICE_ID);
        patientsListViewModel = ViewModelProviders.of(this).get(PatientsListViewModel.class);
        patientsListViewModel.getTbPatientsOnly().observe(TbClientListActivity.this, new Observer<List<Patient>>() {
            @Override
            public void onChanged(@Nullable List<Patient> patients) {
                patientList = patients;
                patientsSearchFilterUtil.setPatientList(patients);
                adapter.setPatient(patients);
            }
        });

        patientNamesSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.setPatient(patientsSearchFilterUtil.searchByNames(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        patientVillageSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.setPatient(patientsSearchFilterUtil.searchByVillage(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        patientsRecycler.setAdapter(adapter);

    }

    private void setupview(){

        activityTitle = findViewById(R.id.activity_title);

        filterButton =  findViewById(R.id.filter_button);

        patientNamesSearch =  findViewById(R.id.client_name_et);
        patientVillageSearch =  findViewById(R.id.client_village_et);

        toolbar =  findViewById(R.id.toolbar);

        patientsRecycler =  findViewById(R.id.patients_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        patientsRecycler.setLayoutManager(layoutManager);
        patientsRecycler.setHasFixedSize(true);
    }

}
