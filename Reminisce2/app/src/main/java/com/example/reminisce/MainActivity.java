package com.example.reminisce;
import static java.util.Arrays.asList;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Uri imageURI;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference userRef;

    private HashMap<String, List<String>> gacha;
    private ArrayList<String> allNames;
    private HashMap<String, String[]> captions;

    int userBonus;
    private final double one = 50;
    private final double two = 80;
    private final double three = 95;
    private final double four = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //instantiate gacha
        ArrayList<String> oneStarList = new ArrayList<>(asList("pancakes", "women_pancake", "quandale", "goku", "sad_wojak", "freddy", "ls_chica", "pepe", "wife_gone", "gambling", "tokyo", "sam"));
        ArrayList<String> twoStarList = new ArrayList<>(asList("sweat", "gambling_stat", "dedication", "hood_irony", "husbant", "mew_faz", "sigma_wolf", "low_tiergod", "giga", "megamind", "sam_pose"));
        ArrayList<String> threeStarList = new ArrayList<>(asList("cat", "goku_drip", "shadow_wizard", "green", "test_torwiz", "ash_baby", "honeybun", "niko"));
        ArrayList<String> fourStarList = new ArrayList<>(asList("astolfo", "obunga", "ricardo_milos", "lebron", "swmg2"));
        gacha = new HashMap<>();
        gacha.put("one star", oneStarList);
        gacha.put("two star", twoStarList);
        gacha.put("three star", threeStarList);
        gacha.put("four star", fourStarList);
        allNames = new ArrayList<>();
        allNames.addAll(oneStarList);
        allNames.addAll(twoStarList);
        allNames.addAll(threeStarList);
        allNames.addAll(fourStarList);
        captions = new HashMap<>();
        //NOTE: DO NOT CHANGE ORDER, THESE MUST ABSOLUTELY BE IN THIS ORDER!!!
        ArrayList<String> titles = new ArrayList<>(asList("Breakfast", "Perfect Wife", "Debt", "Stranger", "Regret", "Ominous Bear", "\"Bear?\"", "Punishment", "No Wife", "Gambling", "Contemplation", "Made in Heaven",
                "Pressure Makes Diamonds", "Never Give Up", "Mamba Mentality", "Strange Night", "Poverty", "Questionable Bear", "Duality of Man", "Mewatar", "Mentorship", "Reminisce", "Transformation",
                "A Dance With The Devil", "Warning", "Twist In Fate", "Green FN", "Tragedy", "OH NOOOOO", "Cursed Technique: Honeybun Defense", "Bulked Too Hard",
                "Perfect \"Wife\"", "???", "Dancing Guy",  "You Are My Sunshine", "Redemption"));

        //NOTE: DO NOT CHANGE ORDER, THESE MUST ABSOLUTELY BE IN THIS ORDER!!!
        ArrayList<String> descriptions = new ArrayList<>(asList("You sneak out of bed slowly to prepare your wife pancakes and strawberries.", "Your pancakes are ready. Your wife comes in, and has a look of delight on her face.", "You blew it! You just increased your debt. You return home, a mix of anxiety, adrenaline, and sadness in your heart. You're trying to ignore everything, and you sneak back in bed…",
                "A mysterious stranger stops you on your way back. \"Yo bro. If you keep gambling, the shadow wizard money gang will punish you. Trust me bro.\"", "When regret overtakes repentance, and emptiness overtakes emotion….one cannot love even those people who were most dear to them…you drag yourself to the casino yet again…",
                "Since your wife has turned into freddy fazbear, she quickly darts out the room into the woods.", "What do these bears mean?? Why do they scare you so much? This one isn't even a bear???", "You think, \"I punish myself for my whole life, my whole life I punish. Every day, I wish myself off the earth. There is nothing wrong with me, except myself…", "Day 199 homeless. And when you wake up with nobody next to you, and return with nobody waiting for you, do you call it freedom? Or loneliness?",
                "Because ever since that day, all you've tried to do is forget. All you've done is go numb. And life has been so grey and so meaningless, that gambling has taken over your life, because it's the only spark of light that you get. But the thing about gambling is that there's no bottom to it, except rock bottom.",
                "And for most of your life, you've been going around with this first layer of numbness as your first layer of defense, but underneath, there hasn't been anything else, except your identity as a failure.",
                "You shed bittersweet tears…, as the light reflects off your chiseled body. It is as if you were sculpted by the angels. You are a leader, as you hypnotize with your destiny…you are magical, lyrical, beautiful…like a dream come alive, incredible…",
                "But as soon as she finishes eating, you get major gas. You calmly wait for them to leave so that you can fart….",
                "You fart so hard that the gust of wind blows off the top of a box…Oh no….that's your forbidden box! It's got dice in it. It's your lucky dice…Grrrrgh! The urges! The need to win! Because life is a gamble! ",
                "You're locked in. The grind never stops…Not even for your wife…call that foreshadowing….",
                "While commuting, you inadvertently lose touch with the present moment. As you proceed, your reflection mimics your actions with a slight delay, prompting you to move cautiously…fearing the consequences of synchronization. For you are in too much grief to forget, but you cannot bring yourself to face reality…",
                "Your wife is waiting for you when you return. There are tears in her eyes. Oh husbant…you gamble too much…now we are homeress…",
                "Every time you see a bear, you feel so uneasy",
                "Whether you define yourself by your actions, or by your past, or by how you think about things, it's all the same. Your actions show that you are a failure. Your past shows that you are a failure. And the inside of your mind is naught but a distorted mirror of your past actions. You are a failure, because you are you, and because you have lived as you.",
                "In order to get your memories back, you decide to lock into the Mewatar state to talk to the past Mewers for advice.",
                "Gigachad responds to the call. \"Let's go lift, bro\", he says telepathically. He doesn't say anything, because he's mewing.",
                "\"Wake up. It's time to hit the gym! Let's get gains, brother! You are not your gambling addiction! The thing about addiction is that when it's your only escape, it makes you forget everything else. Even though it isn't everything you are, it makes your actions, your past, and your mind distorted to think that it's your everything! But you must reminisce!!! You must remember!!!!\"",
                "After a year of lifting, you decide to check your progress. For the first time, you look at yourself, as you are. You're shocked. A tear rolls down your face, as you realized it takes a great man to even look at oneself honestly…but moreso, you are beautiful.",
                "It's evening. The sultry night mood heightens your senses. You shake the dice, and roll! The night goes on, and you don't even notice. You watch the casino table with anticipation, betting more and more money…",
                "\"Why should i trust you?\" \"Because I also once lost everything to gambling. I sacrificed everything for this drip. But the shadow wizard money gang punished me first. And that punishment…man it's different for everyone, but it sucked man.\" The mysterious stranger fades into the background. How is he doing that…",
                "Goku: \"dude if you keep gambling, the Rizzard of Oz will come and turn someone precious to you into freddy fazbear!\" But you ignore him, because you don't think his drip is hard, and you think Gojo could beat Goku.",
                "Then a wizard appears from around the alleyway!!! \"I am the Rizzler of Oz! I cast the spell, Green FN: Har har har har version!!!!\"",
                "A beam blasts out of the wizard's wand and zaps your wife. Green fn. Shawty turned into the freddy fazbear oh NOOOOOOOOOOOO.",
                "OH NOOOOOOOOOOO you scream in terror. The experience traumatized you, causing you to lose your memories from the sheer stress.",
                "Day 365 homeless and depressed. It is getting dismal. Such a pretty house. Such a pretty garden. But wait! Who just broke into the building you were bumming out in??? Your years of honeybun kung fu training kick in.",
                "\"Aight bro chill I'm coming to the gym\"",
                "You wake up, yawwwwnnnnn :3 . You see your beautiful wife(?) next to you xD, and there's sunlight streaming in through the window…",
                "???",
                "A buff guy breaks into your room and starts dancing. It's kind of confusing, but moderately uplifting at the same time. It's Ricardo Milos! You can't tell if he's taunting you. Before you throw a honeybun at him, he dances away.",
                "And recently there's been a difference between being numb, and being in control. You used to be faced with a problem, and you'd look like it didn't faze you at all, because you used to just forget about it. Just letting it wash over you. No matter how many freddy fazbears you saw, you just pretended to forget that your gambling addiction cost you your wife. But now you're different. You still seem unfazed by any problems in your life, but it's not because you're being numb. It's because you have self confidence. You're confident in your ability to make it better. You hold yourself accountable, and you're strong enough to not run away. You've become the true gigachad….",
                "The shadow wizard money gang descends from the skies…Your muscles tense up in defense. But then they begin to serenade you! You're confused. They sing, \"O noble warrior, stand proud, for you are strong….\" And then they give your wife back to you."));



        Log.d("Size", "" + allNames.size() + " " + titles.size() + " " + descriptions.size());
        for (int i = 0; i < allNames.size(); i++) {
            captions.put(allNames.get(i), new String[]{titles.get(i), descriptions.get(i)});
        }

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        TextView money = findViewById(R.id.money_text);
        SharedPreferences prefs = getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        String username_text = prefs.getString("username", "User");
        userRef = FirebaseDatabase.getInstance().getReference("users").child(username_text);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Object data = dataSnapshot.child("money").getValue();
                    if (data != null) {
                        money.setText(data.toString());
                    }
                }
                DataSnapshot bonusRef = dataSnapshot.child("bonus");
                if (bonusRef.exists()) {
                    userBonus = bonusRef.getValue(Integer.class);
                    Log.d("idk", "set to " + userBonus);
                } else {
                    userBonus = 0;
                    Log.d("idk", "0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Error handling
            }
        });

        Button reminisceBtn = findViewById(R.id.reminisce_btn);
        reminisceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(view.getContext());
                View dialogView = inflater.inflate(R.layout.prize, null);
                String money_text = money.getText().toString();
                int money_number = Integer.parseInt(money_text);
                if (money_number >= 50) {
                    money_number -= 50;
                    money.setText(String.format(Integer.toString(money_number)));
                    userRef.child("money").setValue(money_number);


                    // Get ImageView from the dialogView with the correct ID
                    ImageView imageView = dialogView.findViewById(R.id.memoryFrag);
                    TextView titleText = dialogView.findViewById(R.id.textRare);
                    TextView descriptionText = dialogView.findViewById(R.id.textCaption);
                    ImageView rarityView = dialogView.findViewById(R.id.rarityImg);

                    // Load a random image from Firebase Storage into the ImageView
                    String s = getRandomImageUriFromFirebaseStorage(imageView, titleText, descriptionText, rarityView);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                    alertDialogBuilder.setView(dialogView);

                    int audioNumber = getResources().getIdentifier(s, "raw", getPackageName());
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

                    View x_button = alertDialog.findViewById(R.id.x_button);
                    if (x_button != null) {
                        x_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                    }

                } else {
                    Toast myToast = Toast.makeText(dialogView.getContext(), "ERROR: NOT ENOUGH MONEY", Toast.LENGTH_SHORT);
                    myToast.show();
                }
            }
        });

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.hide(); // Hide the ActionBar
        }


        drawerLayout = findViewById(R.id.drawer_layout);
        View hamburgerIcon = findViewById(R.id.hamburgerIcon);
        hamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle drawer open/close
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.Navigation_drawer, new Navigation())
                            .commit();

                }
            }
        });
    }

        private String getRandomImageUriFromFirebaseStorage(ImageView imageView, TextView titleView, TextView captionView, ImageView rarityView) {
            //code to get gacha
            //get rarity

            int num = new Random().nextInt(101);
            num = Math.max(num + userBonus, 0);

            if (userBonus > 0) {
                userBonus--;
            } else if (userBonus < 0) {
                userBonus++;
            }
            userRef.child("bonus").setValue(userBonus);

            String rarity = "";
            if (num <= one) {
                rarity = "one star";
            } else if (num <= two) {
                rarity = "two star";
            } else if (num <= three) {
                rarity = "three star";
            } else {
                rarity = "four star";
            }
            //gets collection of that rarity
            List<String> col = gacha.get(rarity);
            //chooses random image from that rarity
            String imageName = col.get(new Random().nextInt(col.size()));
            Log.d("images", imageName);
            imageView.setImageResource(getResources().getIdentifier(imageName, "drawable", getPackageName()));

            String titleStr = "";
            if (rarity.equals("one star")) {
                titleStr = "Common";
                rarityView.setImageResource(getResources().getIdentifier("one_star", "drawable", getPackageName()));
            } else if (rarity.equals("two star")) {
                titleStr = "Rare";
                rarityView.setImageResource(getResources().getIdentifier("two_star", "drawable", getPackageName()));
            } else if (rarity.equals("three star")) {
                titleStr = "Very Rare";
                rarityView.setImageResource(getResources().getIdentifier("three_star", "drawable", getPackageName()));
            } else {
                titleStr = "Mythic";
                rarityView.setImageResource(getResources().getIdentifier("four_star", "drawable", getPackageName()));
            }
            //TODO
            String[] values = captions.get(imageName);
            titleStr = titleStr + ": " + values[0];
            String descriptionStr = values[1];
            titleView.setText(titleStr);
            captionView.setText(descriptionStr);

            userRef.child("collected").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        String collection = task.getResult().getValue(String.class);
                        Log.d("collected", collection);
                        //create hashmap of already won prizes
                        HashMap<String, Integer> alreadyHave = new HashMap<>();
                        String[] have = collection.split(" ");
                        for (String s : have) {
                            alreadyHave.put(s, 1);
                        }
                        //check to see if prize is already won
                        //prize hasnt been won yet
                        if (alreadyHave.get(imageName) == null) {
                            //update value
                            if (collection.length() == 0) {
                                userRef.child("collected").setValue(imageName);
                            } else {
                                userRef.child("collected").setValue(collection + " " + imageName);
                            }

                        }
                    }
                }
            });

            return imageName;
        }
}
