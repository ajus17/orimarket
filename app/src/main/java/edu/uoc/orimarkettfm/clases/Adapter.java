package com.uoc.adolfoursa.pecfirebaserecyclerview.objetos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uoc.adolfoursa.pecfirebaserecyclerview.BookDetail;
import com.uoc.adolfoursa.pecfirebaserecyclerview.R;

import java.util.List;



public class Adapter extends RecyclerView.Adapter<Adapter.librosViewHolder> {

    List<libros> libro;
    public Adapter(List<libros> libro) {
        this.libro = libro;
    }
   static String Description;
    static String url;

    @NonNull
    @Override
    public librosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        //Permite inflar una vista con elementos definidos (TextView e ImageView)
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_recycler,viewGroup, false);
        librosViewHolder holder = new librosViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull librosViewHolder librosViewHolder, final int i) {

        //Este método permite establecer en cada elemento de la vista inflada, un dato capturado de la FirebaseDatabase

        libros librolist = libro.get(i);
        librosViewHolder.txAuthor.setText(librolist.getAuthor());
        librosViewHolder.txDate.setText(librolist.getPublication_date());
        librosViewHolder.txTitle.setText(librolist.getTitle());

        Picasso.get()
                .load(librolist.getUrl_image())
                .resize(100, 150)
                .centerCrop()
                .into(librosViewHolder.imgbook);

        Description = librolist.getDescription();
         url= librolist.getUrl_image();

         //Establecemos un listener a la vista inflada

        librosViewHolder.setOnclickListeners();

    }

    //Devuelve el total de elementos que contiene el objeto de la clase libro
    @Override
    public int getItemCount() {
        return libro.size();
    }


    public static class librosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txAuthor,txDate,txTitle;
        ImageView imgbook;
        CardView viewCard;

        Context context;


        //inicializa los componentes de la vista inflada
        public librosViewHolder(@NonNull View itemView) {
            super(itemView);


            context = itemView.getContext();

            txAuthor = itemView.findViewById(R.id.txAuthor);
            txDate = itemView.findViewById(R.id.txDate);
            txTitle = itemView.findViewById(R.id.txTitle);
            imgbook=itemView.findViewById(R.id.imglibro);

            viewCard = itemView.findViewById(R.id.cardview);


        }

        //Se establece un listener al viewCard de la vista inflada

        void setOnclickListeners(){
            viewCard.setOnClickListener(this);
        }

        //Mediante el método onclick se envian datos a la activity BookDetail
        @Override
        public void onClick(View v) {

            Intent it = new Intent(context,BookDetail.class);
            it.putExtra("datoautor",txAuthor.getText());
            it.putExtra("datofecha",txDate.getText());
            it.putExtra("datodescription",Description);
            it.putExtra("datourl",url);
            context.startActivity(it);

        }
    }
}
