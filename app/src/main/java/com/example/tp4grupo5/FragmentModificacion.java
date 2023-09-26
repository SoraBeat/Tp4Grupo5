package com.example.tp4grupo5;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.tp4grupo5.adicionales.ItemSpinner;
import com.example.tp4grupo5.entidades.Categoria;
import com.example.tp4grupo5.entidades.Producto;
import com.example.tp4grupo5.helper.GestorCategoria;
import com.example.tp4grupo5.helper.GestorProductos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class FragmentModificacion extends Fragment {
    View view;
    Spinner spinnerCategoria;
    EditText editTextId;
    EditText editTextNombre;
    EditText editTextStock;
    CompletableFuture<Producto> Producto;
    private MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainActivity = (MainActivity)getActivity();
        view = inflater.inflate(R.layout.fragment_modificacion,container,false);
        inicializarSpinner();


        Button btnBuscar = view.findViewById(R.id.btnBuscar);
        Button btnModificar = view.findViewById(R.id.btnModificar);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextId = view.findViewById(R.id.txtId);
                String idProducto = editTextId.getText().toString();
                btnBuscar_Click(idProducto);
            }
        });

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextId = view.findViewById(R.id.txtId);
                btnModificar_Click();
            }
        });

        return view;
    }

    public void inicializarSpinner(){
        //Obtengo el spinner
        spinnerCategoria = view.findViewById(R.id.spinnerCategoria);

        //Inicializo un par de cositas
        GestorCategoria gestorCategoria = new GestorCategoria();
        List<ItemSpinner> items = new ArrayList<>();
        CompletableFuture<List<Categoria>> futureCategoria = gestorCategoria.obtenerTodasLasCategorias();

        //Cuando se termina la llamada asincrona comienza
        futureCategoria.thenAccept(categorias -> {
            // Aquí puedes trabajar con la lista de categorias
            for (Categoria categoria : categorias) {
                items.add(new ItemSpinner(categoria.getDescripcion(),categoria.getId()));
            }

            //Seteo los datos en el spinner
            ArrayAdapter<ItemSpinner> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategoria.setAdapter(adapter);
        });
    }
    public void btnBuscar_Click(String idProducto)
    {
        GestorProductos gestorProductos = new GestorProductos();
        Producto = gestorProductos.obtenerProductoPorId(idProducto);

        editTextNombre = view.findViewById(R.id.txtNomProducto);
        editTextStock = view.findViewById(R.id.txtStock);
        Producto.thenAccept(p -> {
            String nombre = p.getNombre();
            int stock = p.getStock();
            String cat = p.getCategoria_id();

            ArrayList<ItemSpinner> listItems = new ArrayList<ItemSpinner>();
            ArrayAdapter myAdapter = (ArrayAdapter) spinnerCategoria.getAdapter();
            int positionToSelect = -1;

            for(int i=0;i<myAdapter.getCount();i++){
                ItemSpinner item = (ItemSpinner)myAdapter.getItem(i);
                if (item.getValor().equalsIgnoreCase(cat)) {
                    positionToSelect = i;
                    break;
                }
            }

            if(positionToSelect != -1){
                spinnerCategoria.setSelection(positionToSelect);
            }

            getActivity().runOnUiThread(() -> editTextNombre.setText(nombre));
            getActivity().runOnUiThread(() -> editTextStock.setText(Integer.toString(stock)));
        });
    }

    public void btnModificar_Click(){
        GestorProductos gestorProductos = new GestorProductos();

        editTextNombre = view.findViewById(R.id.txtNomProducto);
        editTextStock = view.findViewById(R.id.txtStock);
        spinnerCategoria = view.findViewById(R.id.spinnerCategoria);

        String nombre = editTextNombre.getText().toString();
        int stock = (editTextStock.getText().toString().equals("")) ? 0 : Integer.parseInt(editTextStock.getText().toString());
        ItemSpinner selectedCategoria = (ItemSpinner) spinnerCategoria.getSelectedItem();
        String categoriaId = selectedCategoria.getValor();

        Pattern patronSinNumeros = Pattern.compile(".*\\d.*");
        if(nombre.equals("")){
            Toast.makeText(requireContext(),"Nombre requerido",Toast.LENGTH_LONG).show();
            return;
        }
        else if(patronSinNumeros.matcher(nombre).matches()){
            Toast.makeText(requireContext(),"Nombre no debe contener numeros",Toast.LENGTH_LONG).show();
            return;
        }
        else if(stock<=0){
            Toast.makeText(requireContext(),"Stock requerido",Toast.LENGTH_LONG).show();
            return;
        }
        Producto.thenAccept(p -> {
            p.setNombre(nombre);
            p.setStock(stock);
            p.setCategoria_id(categoriaId);
            p.setEstado(true);
            gestorProductos.actualizarProducto(p);
            Toast.makeText(requireContext(),"Producto Modificado",Toast.LENGTH_LONG).show();
        });

        editTextId.setText("");
        editTextNombre.setText("");
        editTextStock.setText("");    }
}
