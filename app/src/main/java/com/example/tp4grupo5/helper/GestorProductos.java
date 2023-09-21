package com.example.tp4grupo5.helper;

import com.example.tp4grupo5.entidades.Producto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GestorProductos {
    private DatabaseReference mDatabase;

    public GestorProductos() {
        mDatabase = FirebaseDatabase.getInstance().getReference("productos"); // Cambia "productos" por el nombre de tu nodo en Firebase.
    }

    public void agregarProducto(Producto producto) {
        // Generar una clave única para el nuevo producto.
        //String key = mDatabase.push().getKey();
        //producto.setId(key);

        // Guardar el producto en Firebase.
        mDatabase.child(producto.getId()).setValue(producto);
    }

    public void actualizarProducto(Producto producto) {
        // Actualizar el producto en Firebase usando su ID.
        mDatabase.child(producto.getId()).setValue(producto);
    }

    public void eliminarProducto(String productoId) {
        // Eliminar el producto de Firebase usando su ID.
        mDatabase.child(productoId).removeValue();
    }

    public CompletableFuture<List<Producto>> obtenerTodosLosProductos() {
        final CompletableFuture<List<Producto>> future = new CompletableFuture<>();
        final List<Producto> productos = new ArrayList<>();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Producto producto = snapshot.getValue(Producto.class);
                    productos.add(producto);
                }
                future.complete(productos); // Completa el futuro con la lista de productos cuando se hayan cargado todos los datos.
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar el error si es necesario
                future.completeExceptionally(databaseError.toException()); // Completa el futuro con una excepción en caso de error.
            }
        });

        return future;
    }
}

