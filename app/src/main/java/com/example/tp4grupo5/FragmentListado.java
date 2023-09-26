package com.example.tp4grupo5;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tp4grupo5.adicionales.ProductoAdapter;
import com.example.tp4grupo5.entidades.Producto;
import com.example.tp4grupo5.helper.GestorProductos;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FragmentListado extends Fragment {
    View view;
    ListView lvList;
    public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            ActualizarProductos();
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_listado, container, false);

        // Crear una instancia de GestorProductos
        GestorProductos gestorProductos = new GestorProductos();
        lvList = view.findViewById(R.id.lvlistar);

        // Obtener la lista de todos los productos
        CompletableFuture<List<Producto>> futureProductos = gestorProductos.obtenerTodosLosProductos();

        futureProductos.thenAccept(productos -> {
            // Crear un adaptador personalizado
            ProductoAdapter adapter = new ProductoAdapter(requireContext(), productos);

            // Configurar el ListView para usar el adaptador personalizado
            lvList.setAdapter(adapter);

            // Agregar un clic a los elementos de la lista
            lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mostrarDialogoConfirmacion(productos.get(position));
                }
            });
        });

        return view;
    }

    // Método para mostrar un cuadro de diálogo de confirmación
    private void mostrarDialogoConfirmacion(final Producto producto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Cambiar Estado");
        builder.setMessage("¿Estás seguro de que deseas cambiar el estado de este producto?");
        builder.setCancelable(false);

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                producto.setEstado(!producto.isEstado());
                GestorProductos gp = new GestorProductos();
                gp.actualizarProducto(producto);

                ActualizarProductos();

                // Actualizar la lista de productos en el adaptador
                ProductoAdapter adapter = (ProductoAdapter) lvList.getAdapter();
                adapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // No hacer nada si el usuario elige no eliminar
            }
        });

        builder.show();
    }

    public void ActualizarProductos(){
        // Crear una instancia de GestorProductos
        GestorProductos gestorProductos = new GestorProductos();
        // Obtener la lista de todos los productos
        CompletableFuture<List<Producto>> futureProductos = gestorProductos.obtenerTodosLosProductos();

        futureProductos.thenAccept(productos -> {
            // Crear un adaptador personalizado
            ProductoAdapter adapter = new ProductoAdapter(requireContext(), productos);

            // Configurar el ListView para usar el adaptador personalizado
            lvList.setAdapter(adapter);

            // Agregar un clic a los elementos de la lista
            lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mostrarDialogoConfirmacion(productos.get(position));
                }
            });
        });
    }

}
