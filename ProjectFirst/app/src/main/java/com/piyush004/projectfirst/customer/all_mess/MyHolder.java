package com.piyush004.projectfirst.customer.all_mess;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.piyush004.projectfirst.R;
import com.squareup.picasso.Picasso;

public class MyHolder extends RecyclerView.ViewHolder {

    TextView textViewTitle, textViewAddress, textViewMobile, textViewCity;
    ImageView imageView;

    public MyHolder(@NonNull View itemView) {
        super(itemView);

        this.textViewTitle = itemView.findViewById(R.id.title_card_c);
        this.textViewAddress = itemView.findViewById(R.id.address_card_c);
        this.textViewMobile = itemView.findViewById(R.id.mobile_card_c);
        this.textViewCity = itemView.findViewById(R.id.city_card_c);
        this.imageView = itemView.findViewById(R.id.imageViewMess_c);
    }

    public void setTxtTitle(String string) {
        textViewTitle.setText(string);
    }


    public void setTxtAddress(String string) {
        textViewAddress.setText(string);
    }

    public void setTxtMobile(String string) {
        textViewMobile.setText(string);
    }

    public void setTxtCity(String string) {
        textViewCity.setText(string);
    }

    public void setTxtImg(String string) {
        Picasso.get().load(string).into(imageView);
    }
}
