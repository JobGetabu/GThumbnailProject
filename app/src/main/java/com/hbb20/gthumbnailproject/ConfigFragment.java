package com.hbb20.gthumbnailproject;


import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.hbb20.GThumb;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigFragment extends Fragment {

    List<GThumb> gThumbList;
    List<CaptainPlayer> captainPlayers;
    MainActivity mainActivity;
    GThumb.BACKGROUND_SHAPE selectedShape = GThumb.BACKGROUND_SHAPE.ROUND;
    boolean needToSetClickListener = true;
    boolean useBold = false;
    @BindView(R.id.demo1)
    GThumb gThumb1;
    @BindView(R.id.demo2)
    GThumb gThumb2;
    @BindView(R.id.demo3)
    GThumb gThumb3;
    @BindView(R.id.demo4)
    GThumb gThumb4;
    @BindView(R.id.checkbox_useBold)
    CheckBox checkBoxUseBold;

    @BindView(R.id.radioGroupShape)
    RadioGroup radioGroupShape;
    @BindView(R.id.radio_round)
    RadioButton radioRound;
    @BindView(R.id.radio_square)
    RadioButton radioSquare;

    @BindView(R.id.radioGroupClick)
    RadioGroup radioGroupClick;
    @BindView(R.id.radio_click_set)
    RadioButton radioClickSet;
    @BindView(R.id.radio_click_avoid)
    RadioButton radioClickAvoid;

    @BindView(R.id.radioGroupColor)
    RadioGroup radioGroupColor;

    @BindView(R.id.viewSampleColor)
    View viewSampleColor;
    @BindView(R.id.seekbar_red)
    SeekBar seekBarRed;
    @BindView(R.id.seekbar_green)
    SeekBar seekBarGreen;
    @BindView(R.id.seekbar_blue)
    SeekBar seekBarBlue;
    @BindView(R.id.overlay)
    View overlay;

    @BindView(R.id.buttonList)
    Button buttonList;
    int customMonoColor;
    boolean useMonoColor;

    public ConfigFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_config, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, getView());
        setInitial();
        setListeners();
    }

    private void setListeners() {

        //for bold
        checkBoxUseBold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                useBold = isChecked;
                applyChanges();
            }
        });

        //shape
        radioGroupShape.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_round:
                        selectedShape = GThumb.BACKGROUND_SHAPE.ROUND;
                        break;
                    case R.id.radio_square:
                        selectedShape = GThumb.BACKGROUND_SHAPE.SQUARE;
                        break;
                }
                applyChanges();
            }
        });

        //click listener
        radioGroupClick.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_click_set:
                        needToSetClickListener = true;
                        break;
                    case R.id.radio_click_avoid:
                        needToSetClickListener = false;
                        break;
                }
                applyChanges();
            }
        });

        //mono color selector
        radioGroupColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_color_mono:
                        useMonoColor = true;
                        enableSeekbars(true);
                        break;
                    case R.id.radio_color_multi:
                        useMonoColor = false;
                        enableSeekbars(false);
                        break;
                }
                applyChanges();
            }
        });

        //seek bar listeners
        SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setSampleColorFromSeekBar();
                applyChanges();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        seekBarRed.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarGreen.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarBlue.setOnSeekBarChangeListener(seekBarChangeListener);

        buttonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainActivity!=null){
                    mainActivity.scrollToList();
                }
            }
        });
    }

    private void enableSeekbars(boolean b) {
        seekBarRed.setEnabled(b);
        seekBarGreen.setEnabled(b);
        seekBarBlue.setEnabled(b);
        if (b) {
            overlay.setVisibility(View.GONE);
        } else {
            overlay.setVisibility(View.VISIBLE);
        }
    }

    private void setInitial() {
        //set demo thumbs
        gThumbList = new ArrayList<>();
        gThumbList.add(gThumb1);
        gThumbList.add(gThumb2);
        gThumbList.add(gThumb3);
        gThumbList.add(gThumb4);

        //set captainPlayer
        captainPlayers = new ArrayList<>();
        captainPlayers.add(new CaptainPlayer("Elton", "Chigumbura", "Zimbabwe", ""));
        captainPlayers.add(new CaptainPlayer("AB de", "Villiers", "South Africa", "https://s.yimg.com/qx/cricket/fufp/images/3675_large-20-6-2012-9549fcb8e83238bb4736dafd21cf2569.jpg"));
        captainPlayers.add(new CaptainPlayer("Steven", "Smith", "Australia", ""));
        captainPlayers.add(new CaptainPlayer("Mohammad", "Tauqir", "UAE", ""));

        loadAllThumbs();
        seekBarRed.setProgress(255);
        seekBarGreen.setProgress(86);
        seekBarBlue.setProgress(34);
        //seek bar disabled
        setSampleColorFromSeekBar();
        enableSeekbars(false);
    }

    private void setSampleColorFromSeekBar() {
        readCustomColor();
        viewSampleColor.setBackgroundColor(customMonoColor);
    }

    private void readCustomColor() {
        int r = seekBarRed.getProgress();
        int g = seekBarGreen.getProgress();
        int b = seekBarBlue.getProgress();
        customMonoColor = Color.rgb(r, g, b);
    }

    private void loadAllThumbs() {
        for (int i = 0; i < gThumbList.size() && i < captainPlayers.size(); i++) {
            GThumb gThumb = gThumbList.get(i);
            final CaptainPlayer captainPlayer = captainPlayers.get(i);
            gThumb.loadThumbForName(captainPlayer.getUrl(), captainPlayer.getFirstName(), captainPlayer.getLastName());
            if (needToSetClickListener()) {
                gThumb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Clicked on " + captainPlayer.getFirstName(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                gThumb.setOnClickListener(null);
            }
        }
    }

    public void applyChanges() {
        //changes to demo
        setChangesForAllDemo();

        //to changes to list
        if (mainActivity != null && mainActivity.getListFragment() != null) {
            mainActivity.getListFragment().updateAdapter(this);
        }
    }

    private void setShapeForAllDemo() {

    }

    private void setChangesForAllDemo() {
        for (GThumb gThumb : gThumbList) {
            gThumb.setUseBoldText(useBold());
            gThumb.setBackgroundShape(getSelectedShape());
            gThumb.setOnClickListener(null);
            if (useMonoColor) {
                gThumb.setMonoColor(customMonoColor);
            } else {
                gThumb.applyMultiColor();
            }
        }
        loadAllThumbs();
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public boolean useBold() {
        return useBold;
    }

    public GThumb.BACKGROUND_SHAPE getSelectedShape() {
        return selectedShape;
    }

    public boolean needToSetClickListener() {
        return needToSetClickListener;
    }

    public boolean useMonoColor() {
        return useMonoColor;
    }

    public int getCustomMonoColor() {
        return customMonoColor;
    }
}
