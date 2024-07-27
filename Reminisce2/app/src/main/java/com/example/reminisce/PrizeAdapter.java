package com.example.reminisce;

import static com.google.common.reflect.Reflection.getPackageName;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class PrizeAdapter extends ArrayAdapter<Prize> {

    int resource;
    Collections myact;

    public PrizeAdapter(Context ctx, int res, List<Prize> prizeList) {
        super(ctx, res, prizeList);
        resource = res;
        myact = (Collections) ctx;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout itemView;
        Prize prize = getItem(position);

        if (convertView == null) {
            itemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, itemView, true);
        } else {
            itemView = (LinearLayout) convertView;
        }
        //TODO:
        if (prize.getTitle().equals("black box")) {
            ImageView prizeImage = itemView.findViewById(R.id.prizeImage);
            prizeImage.setImageResource(getContext().getResources().getIdentifier("black_box", "drawable", getContext().getPackageName()));
            prizeImage.setClickable(false);
            return itemView;
        } else {
            ImageView prizeImage = itemView.findViewById(R.id.prizeImage);
            prizeImage.setImageResource(getContext().getResources().getIdentifier(prize.getImage(), "drawable", getContext().getPackageName()));
            prizeImage.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  LayoutInflater inflater = LayoutInflater.from(view.getContext());
                  View dialogView = inflater.inflate(R.layout.memory_frag, null);

                  Log.d("c", "clicked image" );

                  ImageView imageView = dialogView.findViewById(R.id.memoryFrag2);
                  TextView titleText = dialogView.findViewById(R.id.textRare2);
                  TextView descriptionText = dialogView.findViewById(R.id.textCaption2);
                  ImageView rarityView = dialogView.findViewById(R.id.rarityImg2);
                  //modify these appropriately
                  imageView.setImageResource(getContext().getResources().getIdentifier(prize.getImage(), "drawable", getContext().getPackageName()));
                  titleText.setText(prize.getTitle());
                  descriptionText.setText(prize.getDescription());
                  if (prize.getRarity().equals("one star")) {
                      rarityView.setImageResource(getContext().getResources().getIdentifier("one_star", "drawable", getContext().getPackageName()));
                  } else if (prize.getRarity().equals("two star")) {
                      rarityView.setImageResource(getContext().getResources().getIdentifier("two_star", "drawable", getContext().getPackageName()));
                  } else if (prize.getRarity().equals("three star")) {
                      rarityView.setImageResource(getContext().getResources().getIdentifier("three_star", "drawable", getContext().getPackageName()));
                  } else {
                      rarityView.setImageResource(getContext().getResources().getIdentifier("four_star", "drawable", getContext().getPackageName()));
                  }

                  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                  alertDialogBuilder.setView(dialogView);

                  int audioNumber = getContext().getResources().getIdentifier(prize.getImage(), "raw", getContext().getPackageName());
                  MediaPlayer mediaPlayer = MediaPlayer.create(view.getContext(), audioNumber);
                  mediaPlayer.start();

                  AlertDialog alertDialog = alertDialogBuilder.create();
                  alertDialog.setOnDismissListener(dialog -> {
                      // Stop and release the MediaPlayer when the dialog is dismissed
                      if (mediaPlayer != null) {
                          mediaPlayer.stop();
                          mediaPlayer.release();
                      }
                  }); //this might be inaccurate
                  alertDialog.show();

                  View x_button = alertDialog.findViewById(R.id.x_button2);
                  if (x_button != null) {
                      x_button.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              alertDialog.dismiss();
                          }
                      });
                  }
              }
          });
        //If prize.getTitle() is "black box" -> return imageView with a black box

        //Else, return that thing with the popup
        return itemView;
    }
    }
}
