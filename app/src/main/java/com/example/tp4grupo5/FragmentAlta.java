package com.example.tp4grupo5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tp4grupo5.adicionales.ItemSpinner;
import com.example.tp4grupo5.entidades.Categoria;
import com.example.tp4grupo5.entidades.Producto;
import com.example.tp4grupo5.helper.GestorCategoria;
import com.example.tp4grupo5.helper.GestorProductos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FragmentAlta extends Fragment {
    View view;
    Spinner spinnerCrear;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_alta, container, false);
        inicializarSpinner();

        Button btnAgregar = view.findViewById(R.id.btn_createAceptar);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se ejecutará cuando se haga clic en el botón "AGREGAR"
                agregarProducto();
            }
        });
        /*
        //Prueba firebase
        //FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);
        // Crear una instancia de GestorProductos
        GestorProductos gestorProductos = new GestorProductos();

        // Obtener la lista de todos los productos
        CompletableFuture<List<Producto>> futureProductos = gestorProductos.obtenerTodosLosProductos();

        futureProductos.thenAccept(productos -> {
            // Aquí puedes trabajar con la lista de productos
            for (Producto producto : productos) {
                System.out.println(producto);
            }
        });*/
        return view;
    }
    public void inicializarSpinner(){
        //Obtengo el spinner
        spinnerCrear = view.findViewById(R.id.s_createCategoria);

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
            spinnerCrear.setAdapter(adapter);
        });
    }

    private void agregarProducto() {
        //Obtengo los controles
        EditText etId = view.findViewById(R.id.et_createId);
        EditText etNombre = view.findViewById(R.id.et_createNombre);
        EditText etStock = view.findViewById(R.id.et_createStock);
        Spinner spinnerCategoria = view.findViewById(R.id.s_createCategoria);

        //Obtengolos valores de los controles
        String id = etId.getText().toString();
        String nombre = etNombre.getText().toString();
        int stock = (etStock.getText().toString().equals("")) ? 0 : Integer.parseInt(etStock.getText().toString());
        ItemSpinner selectedCategoria = (ItemSpinner) spinnerCategoria.getSelectedItem();
        String categoriaId = selectedCategoria.getValor();
        //Validaciones
        Pattern patronSinNumeros = Pattern.compile(".*\\d.*");

        if(id.equals("")){
            Toast.makeText(requireContext(),"ID requerido",Toast.LENGTH_LONG).show();
            return;
        }else if(nombre.equals("")){
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

        // Guardar datos
        GestorProductos gestorProductos = new GestorProductos();
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setStock(stock);
        producto.setCategoria_id(categoriaId);
        producto.setEstado(true);
        gestorProductos.agregarProducto(producto);
        Toast.makeText(requireContext(),"Producto creado",Toast.LENGTH_LONG).show();

        //Reiniciar campos
        etId.setText("");
        etNombre.setText("");
        etStock.setText("");
    }
}
