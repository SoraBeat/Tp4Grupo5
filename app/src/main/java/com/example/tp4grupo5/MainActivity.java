package com.example.tp4grupo5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import com.example.tp4grupo5.entidades.Categoria;
import com.example.tp4grupo5.entidades.Producto;
import com.example.tp4grupo5.helper.GestorCategoria;
import com.example.tp4grupo5.helper.GestorProductos;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        getSupportActionBar().setElevation(0);
        loadViewPager();
    }
    private void loadViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        // Agrega tus fragmentos al adaptador viewPagerAdapter aquí
        viewPagerAdapter.addFragment(newInstanceAlta(), "ALTA");
        viewPagerAdapter.addFragment(newInstanceModifiacion(), "MODIFICACIÓN");
        viewPagerAdapter.addFragment(newInstanceListado(), "LISTADO");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private FragmentAlta newInstanceAlta() {
        Bundle bundle = new Bundle();
        FragmentAlta fragmentAlta = new FragmentAlta();
        fragmentAlta.setArguments(bundle);

        return fragmentAlta;
    }

    private FragmentModificacion newInstanceModifiacion() {
        Bundle bundle = new Bundle();
        FragmentModificacion fragmentModificacion = new FragmentModificacion();
        fragmentModificacion.setArguments(bundle);

        return fragmentModificacion;
    }

    private FragmentListado newInstanceListado() {
        Bundle bundle = new Bundle();
        FragmentListado fragmentListado = new FragmentListado();
        fragmentListado.setArguments(bundle);

        return fragmentListado;
    }

}