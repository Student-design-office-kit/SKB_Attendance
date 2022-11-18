package ru.bratusevd.skb_attendance.services.avatarPicker;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import ru.bratusevd.skb_attendance.R;

public class CustomAvatarPicker extends FrameLayout {

    private Context context;
    private int count;

    public CustomAvatarPicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        count = 0;
        loadView();
    }

    public CustomAvatarPicker(@NonNull Context context) {
        super(context);
        this.context = context;
        count = 0;
        loadView();
    }

    private ImageView leftImg;
    private ImageView rightImg;
    private ImageView centerImg;

    private int position = 1;
    private String link;
    private String[] avatarURLs = new String[]{
            "https://drive.google.com/uc?export=download&id=1K0jiBEixCLCYsYM3eDKOg-YyzmnzNwSI",
            "https://drive.google.com/uc?export=download&id=1ZrSF9q76vg5fkRt08dJRh5XYSD7S3056",
            "https://drive.google.com/uc?export=download&id=1u0JqPdtgBAYjeR7XLrbqeHE87kM9OxU3",
            "https://drive.google.com/uc?export=download&id=14ibrIOx_qmQPP1dmMo_Jhk3465WVy-Fq",
            "https://drive.google.com/uc?export=download&id=1Bv5KW28_7AFh-7Ra7_h5pj_q2KM2Ngv3",
            "https://drive.google.com/uc?export=download&id=1A5gfPCRNwPJNd9tkjLMZ6qwyk_BjtIRp",
            "https://drive.google.com/uc?export=download&id=1sovv7OYZqzWaaihyfgi8Kh4NiBgoJSt-",
            "https://drive.google.com/uc?export=download&id=1ScRplrYsxSEqIWwdz3ATFWjWsC5VXfG_"
    };

    private void loadView() {
        View view = LayoutInflater.from(context).inflate(R.layout.image_picker, this);
        initView(view);
    }

    private void initView(View view) {
        leftImg = view.findViewById(R.id.leftImage);
        centerImg = view.findViewById(R.id.centerImage);
        rightImg = view.findViewById(R.id.rightImage);

        Glide.with(this).load(avatarURLs[position]).into(centerImg);
        Glide.with(this).load(avatarURLs[position - 1]).into(leftImg);
        Glide.with(this).load(avatarURLs[position + 1]).into(rightImg);
        setOnClick();
    }

    private void swapImage() {
        Glide.with(this).load(avatarURLs[position]).into(centerImg);
        if (position - 1 < 0)
            Glide.with(this).load(avatarURLs[avatarURLs.length - 1]).into(leftImg);
        else Glide.with(this).load(avatarURLs[position - 1]).into(leftImg);
        if (position + 1 > (avatarURLs.length - 1))
            Glide.with(this).load(avatarURLs[0]).into(rightImg);
        else Glide.with(this).load(avatarURLs[position + 1]).into(rightImg);
    }

    private void setOnClick() {
        leftImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                position--;
                if (position < 0) position = avatarURLs.length - 1;
                swapImage();
            }
        });
        rightImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                position++;
                if (position == avatarURLs.length) position = 0;
                swapImage();
            }
        });

        centerImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if (count > 5) {
                    View promptsView = View.inflate(context, R.layout.link_input_bottom_dialog, null);
                    AlertDialog alertDialog = new AlertDialog.Builder(context)
                            .setView(promptsView)
                            .create();

                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    promptsView.findViewById(R.id.linkSuccess_button).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            link = ((EditText) promptsView.findViewById(R.id.link)).getText().toString();
                            alertDialog.cancel();
                        }
                    });
                    alertDialog.show();
                }
            }
        });
    }

    public String getLink() {
        if (count < 6) return avatarURLs[position];
        else return link;
    }
}
