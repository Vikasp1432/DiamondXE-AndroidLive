package com.diamondxe.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamondxe.Adapter.CrownFromDataListAdapter;
import com.diamondxe.Adapter.CrownToDataListAdapter;
import com.diamondxe.Adapter.CutTypeListAdapter;
import com.diamondxe.Adapter.DepthFromDataListAdapter;
import com.diamondxe.Adapter.DepthToDataListAdapter;
import com.diamondxe.Adapter.EyeCleanDataListAdapter;
import com.diamondxe.Adapter.FancyColorIntensityListAdapter;
import com.diamondxe.Adapter.FancyColorIntensityTypeListAdapter;
import com.diamondxe.Adapter.FancyColorOverToneListAdapter;
import com.diamondxe.Adapter.LusterDataListAdapter;
import com.diamondxe.Adapter.PavillionFromDataListAdapter;
import com.diamondxe.Adapter.PavillionToDataListAdapter;
import com.diamondxe.Adapter.PolishTypeListAdapter;
import com.diamondxe.Adapter.ShadeDataListAdapter;
import com.diamondxe.Adapter.SymmetryTypeListAdapter;
import com.diamondxe.Adapter.TableFromDataListAdapter;
import com.diamondxe.Adapter.TableToDataListAdapter;
import com.diamondxe.Adapter.TechnologyTypeListAdapter;
import com.diamondxe.ApiCalling.VollyApiActivity;
import com.diamondxe.Beans.AttributeDetailsModel;
import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.CommonUtility;
import com.diamondxe.Utils.Constant;
import com.diamondxe.Utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class AdvanceFiltersActivity extends SuperActivity implements RecyclerInterface {

    private ImageView back_img;
    private EditText mm_length_from_et, mm_length_to_et, mm_width_from_et, mm_width_to_et,mm_depth_from_et,mm_depth_to_et, lot_id_et,location_et;
    private CardView cardView1;
    private TextView fancy_color_tv, clear_filter_tv, apply_filter_tv;
    private RecyclerView recycler_cut_view, recycler_polish_view, recycler_symmetry_view, recycler_technology_view,
            recycler_fancy_color_intensity_view, recycler_eye_clean_view,recycler_shade_view,recycler_luster_view;
    CutTypeListAdapter cutTypeListAdapter;
    PolishTypeListAdapter polishTypeListAdapter;
    SymmetryTypeListAdapter symmetryTypeListAdapter;
    TechnologyTypeListAdapter technologyTypeListAdapter;
    FancyColorIntensityListAdapter fancyColorIntensityListAdapter;
    FancyColorOverToneListAdapter fancyColorOverToneListAdapter;
    TableFromDataListAdapter tableFromDataListAdapter;
    TableToDataListAdapter tableToDataListAdapter;
    DepthFromDataListAdapter depthFromDataListAdapter;
    DepthToDataListAdapter depthToDataListAdapter;
    CrownFromDataListAdapter crownFromDataListAdapter;
    CrownToDataListAdapter crownToDataListAdapter;
    PavillionFromDataListAdapter pavillionFromDataListAdapter;
    PavillionToDataListAdapter pavillionToDataListAdapter;
    EyeCleanDataListAdapter eyeCleanDataListAdapter;
    ShadeDataListAdapter shadeDataListAdapter;
    LusterDataListAdapter lusterDataListAdapter;
    FancyColorIntensityTypeListAdapter fancyColorIntensityTypeListAdapter;

    Spinner spinner_fancy_intensity_view,spinner_fancy_over_tone_view, table_from_spinner, table_to_spinner,depth_from_spinner,depth_to_spinner,
            crown_from_spinner, crown_to_spinner,pavillion_from_spinner,pavillion_to_spinner;


    private Activity activity;
    private Context context;
    //For Api Calling
    private VollyApiActivity vollyApiActivity;
    private HashMap<String, String> urlParameter;
    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_filters);

        context = activity = this;

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

        // This Key Check Any Diamond Quality Previous Screen (Search Screen) Selected or Not.

        recycler_cut_view = findViewById(R.id.recycler_cut_view);
        recycler_polish_view = findViewById(R.id.recycler_polish_view);
        recycler_symmetry_view = findViewById(R.id.recycler_symmetry_view);
        recycler_technology_view = findViewById(R.id.recycler_technology_view);
        recycler_eye_clean_view = findViewById(R.id.recycler_eye_clean_view);
        recycler_shade_view = findViewById(R.id.recycler_shade_view);
        recycler_luster_view = findViewById(R.id.recycler_luster_view);

        recycler_cut_view.setHasFixedSize(true);
        recycler_polish_view.setHasFixedSize(true);
        recycler_symmetry_view.setHasFixedSize(true);
        recycler_technology_view.setHasFixedSize(true);
        recycler_eye_clean_view.setHasFixedSize(true);
        recycler_shade_view.setHasFixedSize(true);
        recycler_luster_view.setHasFixedSize(true);

        final GridLayoutManager layoutManagerCut= new GridLayoutManager(context, 3);
        recycler_cut_view.setLayoutManager(layoutManagerCut);
        recycler_cut_view.setItemAnimator(new DefaultItemAnimator());
        recycler_cut_view.setNestedScrollingEnabled(false);

        final GridLayoutManager layoutManagerPolish = new GridLayoutManager(context, 3);
        recycler_polish_view.setLayoutManager(layoutManagerPolish);
        recycler_polish_view.setItemAnimator(new DefaultItemAnimator());
        recycler_polish_view.setNestedScrollingEnabled(false);

        final GridLayoutManager layoutManagerSymmetry = new GridLayoutManager(context, 3);
        recycler_symmetry_view.setLayoutManager(layoutManagerSymmetry);
        recycler_symmetry_view.setItemAnimator(new DefaultItemAnimator());
        recycler_symmetry_view.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManagerTechnology = new LinearLayoutManager(activity);
        layoutManagerTechnology.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_technology_view.setLayoutManager(layoutManagerTechnology);
        recycler_technology_view.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManagerEyeClean = new LinearLayoutManager(activity);
        layoutManagerEyeClean.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_eye_clean_view.setLayoutManager(layoutManagerEyeClean);
        recycler_eye_clean_view.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManagerShade = new LinearLayoutManager(activity);
        layoutManagerShade.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_shade_view.setLayoutManager(layoutManagerShade);
        recycler_shade_view.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManagerLuster = new LinearLayoutManager(activity);
        layoutManagerLuster.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_luster_view.setLayoutManager(layoutManagerLuster);
        recycler_luster_view.setNestedScrollingEnabled(false);

        cutTypeListAdapter = new CutTypeListAdapter(Constant.cutTypeArrayList,context,this);
        recycler_cut_view.setAdapter(cutTypeListAdapter);

        polishTypeListAdapter = new PolishTypeListAdapter(Constant.polishTypeArrayList,context,this);
        recycler_polish_view.setAdapter(polishTypeListAdapter);

        symmetryTypeListAdapter = new SymmetryTypeListAdapter(Constant.symmertyTypeArrayList,context,this);
        recycler_symmetry_view.setAdapter(symmetryTypeListAdapter);

        technologyTypeListAdapter = new TechnologyTypeListAdapter(Constant.technologyTypeArrayList,context,this);
        recycler_technology_view.setAdapter(technologyTypeListAdapter);

        eyeCleanDataListAdapter = new EyeCleanDataListAdapter(Constant.eyeCleanArrayList,context,this);
        recycler_eye_clean_view.setAdapter(eyeCleanDataListAdapter);

        shadeDataListAdapter = new ShadeDataListAdapter(Constant.shadeArrayList,context,this);
        recycler_shade_view.setAdapter(shadeDataListAdapter);

        lusterDataListAdapter = new LusterDataListAdapter(Constant.lusterArrayList,context,this);
        recycler_luster_view.setAdapter(lusterDataListAdapter);

        spinner_fancy_intensity_view = findViewById(R.id.spinner_fancy_intensity_view);
        spinner_fancy_over_tone_view = findViewById(R.id.spinner_fancy_over_tone_view);


        table_from_spinner = findViewById(R.id.table_from_spinner);
        table_to_spinner = findViewById(R.id.table_to_spinner);

        depth_from_spinner = findViewById(R.id.depth_from_spinner);
        depth_to_spinner = findViewById(R.id.depth_to_spinner);

        mm_length_from_et = findViewById(R.id.mm_length_from_et);
        mm_length_to_et = findViewById(R.id.mm_length_to_et);

        mm_width_from_et = findViewById(R.id.mm_width_from_et);
        mm_width_to_et = findViewById(R.id.mm_width_to_et);

        mm_depth_from_et = findViewById(R.id.mm_depth_from_et);
        mm_depth_to_et = findViewById(R.id.mm_depth_to_et);

        crown_from_spinner = findViewById(R.id.crown_from_spinner);
        crown_to_spinner = findViewById(R.id.crown_to_spinner);

        pavillion_from_spinner = findViewById(R.id.pavillion_from_spinner);
        pavillion_to_spinner = findViewById(R.id.pavillion_to_spinner);

        lot_id_et = findViewById(R.id.lot_id_et);
        location_et = findViewById(R.id.location_et);

        apply_filter_tv = findViewById(R.id.apply_filter_tv);
        apply_filter_tv.setOnClickListener(this);

        clear_filter_tv = findViewById(R.id.clear_filter_tv);
        clear_filter_tv.setOnClickListener(this);

        String hint = getResources().getString(R.string.select_intensity);
        String hintOvertone = getResources().getString(R.string.select_overtone);
        String hintFromTable = getResources().getString(R.string.from_string);
        String hintToTable = getResources().getString(R.string.to_string);
        String hintFromCrown = getResources().getString(R.string.from_string1);
        String hintToCrown = getResources().getString(R.string.to_string1);


        spinner_fancy_intensity_view.setDropDownVerticalOffset(80);
        fancyColorIntensityListAdapter = new FancyColorIntensityListAdapter(context, Constant.fancyColorIntensityArrayList, hint);
        spinner_fancy_intensity_view.setAdapter(fancyColorIntensityListAdapter);

        spinner_fancy_over_tone_view.setDropDownVerticalOffset(80);
        fancyColorOverToneListAdapter = new FancyColorOverToneListAdapter(context, Constant.fancyColorOverToneArrayList, hintOvertone);
        spinner_fancy_over_tone_view.setAdapter(fancyColorOverToneListAdapter);
        try {
            Field popupField = Spinner.class.getDeclaredField("mPopup");
            popupField.setAccessible(true);
            ListPopupWindow popupWindow = (ListPopupWindow) popupField.get(spinner_fancy_over_tone_view);
            popupWindow.setHeight(500); // Set the dropdown height in pixels
        } catch (Exception e) {
            e.printStackTrace();
        }

        tableFromDataListAdapter = new TableFromDataListAdapter(context, Constant.tableDataPercantageArrayList, hintFromTable);
        table_from_spinner.setAdapter(tableFromDataListAdapter);

        tableToDataListAdapter = new TableToDataListAdapter(context, Constant.tableDataPercantageArrayList, hintToTable);
        table_to_spinner.setAdapter(tableToDataListAdapter);

        depthFromDataListAdapter = new DepthFromDataListAdapter(context, Constant.depthDataPercantageArrayList, hintFromTable);
        depth_from_spinner.setAdapter(depthFromDataListAdapter);

        depthToDataListAdapter = new DepthToDataListAdapter(context, Constant.depthDataPercantageArrayList, hintToTable);
        depth_to_spinner.setAdapter(depthToDataListAdapter);

        crownFromDataListAdapter = new CrownFromDataListAdapter(context, Constant.crowmArrayList, hintFromCrown);
        crown_from_spinner.setAdapter(crownFromDataListAdapter);

        crownToDataListAdapter = new CrownToDataListAdapter(context, Constant.crowmArrayList, hintToCrown);
        crown_to_spinner.setAdapter(crownToDataListAdapter);

        pavillionFromDataListAdapter = new PavillionFromDataListAdapter(context, Constant.pavillionArrayList, hintFromCrown);
        pavillion_from_spinner.setAdapter(pavillionFromDataListAdapter);

        pavillionToDataListAdapter = new PavillionToDataListAdapter(context, Constant.pavillionArrayList, hintToCrown);
        pavillion_to_spinner.setAdapter(pavillionToDataListAdapter);

        spinner_fancy_intensity_view.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        if (position > 0) {
                            // It returns the clicked item.
                            AttributeDetailsModel clickedItem = (AttributeDetailsModel) parent.getItemAtPosition(position);
                            String name = clickedItem.getDisplayAttr();
                            Constant.fancyColorIntensity = clickedItem.getAttribCode();
                            //Toast.makeText(context, name + " selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                    }

                });


        spinner_fancy_intensity_view.setPrompt(getResources().getString(R.string.select_intensity));


        spinner_fancy_over_tone_view.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        if (position > 0) {
                            // It returns the clicked item.
                            AttributeDetailsModel clickedItem = (AttributeDetailsModel) parent.getItemAtPosition(position);
                            String name = clickedItem.getDisplayAttr();
                            Constant.fancyColorOvertone = clickedItem.getAttribCode();
                            //Toast.makeText(context, name + " selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                    }

                });


        spinner_fancy_over_tone_view.setPrompt(getResources().getString(R.string.select_overtone));

        table_from_spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        if (position > 0) {
                            // It returns the clicked item.
                            AttributeDetailsModel clickedItem = (AttributeDetailsModel) parent.getItemAtPosition(position);
                            String name = clickedItem.getDisplayAttr();
                            Constant.tableFrm = clickedItem.getAttribCode();
                            //Toast.makeText(context, name + " selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                    }

                });

        table_to_spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        if (position > 0) {
                            // It returns the clicked item.
                            AttributeDetailsModel clickedItem = (AttributeDetailsModel) parent.getItemAtPosition(position);
                            String name = clickedItem.getDisplayAttr();
                            Constant.tableTo = clickedItem.getAttribCode();
                            //Toast.makeText(context, name + " selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                    }

                });

        depth_from_spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        if (position > 0) {
                            // It returns the clicked item.
                            AttributeDetailsModel clickedItem = (AttributeDetailsModel) parent.getItemAtPosition(position);
                            String name = clickedItem.getDisplayAttr();
                            Constant.depthFrmSpinner = clickedItem.getAttribCode();
                            //Toast.makeText(context, name + " selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                    }

                });

        depth_to_spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        if (position > 0) {
                            // It returns the clicked item.
                            AttributeDetailsModel clickedItem = (AttributeDetailsModel) parent.getItemAtPosition(position);
                            String name = clickedItem.getDisplayAttr();
                            Constant.depthToSpinner = clickedItem.getAttribCode();
                            //Toast.makeText(context, name + " selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                    }

                });

        crown_from_spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        if (position > 0) {
                            // It returns the clicked item.
                            AttributeDetailsModel clickedItem = (AttributeDetailsModel) parent.getItemAtPosition(position);
                            String name = clickedItem.getDisplayAttr();
                            Constant.crownFrm = clickedItem.getAttribCode();
                            //Toast.makeText(context, name + " selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                    }

                });

        crown_to_spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        if (position > 0) {
                            // It returns the clicked item.
                            AttributeDetailsModel clickedItem = (AttributeDetailsModel) parent.getItemAtPosition(position);
                            String name = clickedItem.getDisplayAttr();
                            Constant.crownTo = clickedItem.getAttribCode();
                            //Toast.makeText(context, name + " selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                    }

                });

        pavillion_from_spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        if (position > 0) {
                            // It returns the clicked item.
                            AttributeDetailsModel clickedItem = (AttributeDetailsModel) parent.getItemAtPosition(position);
                            String name = clickedItem.getDisplayAttr();
                            Constant.pavillionFrm = clickedItem.getAttribCode();
                            //Toast.makeText(context, name + " selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                    }

                });

        pavillion_to_spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        if (position > 0) {
                            // It returns the clicked item.
                            AttributeDetailsModel clickedItem = (AttributeDetailsModel) parent.getItemAtPosition(position);
                            String name = clickedItem.getDisplayAttr();
                            Constant.pavillionTo= clickedItem.getAttribCode();
                            //Toast.makeText(context, name + " selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                    }

                });

        //  Log.e("------diamondQualitySelected----- : ", diamondQualitySelected.toString() +" ------> "+ makeSelectedValue.toString());

        // Check Condition If Diamond Quality Selected or Make Tab Selected Then Check I or Else if Condition and Show Selected Tab According Condition
        if(Constant.makeSelectedValue.equalsIgnoreCase("3EX") ||
                (Constant.diamondQualitySelected.equalsIgnoreCase("best") && Constant.makeSelectedValue.equalsIgnoreCase("3EX")))
        {
            // Set all items to not selected
            resetAllSelections();

            // Set selected items For Cut Array
            for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Ideal") || displayAttr.equalsIgnoreCase("Excellent"))
                {
                    Constant.cutTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Excellent"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Polish Array
            for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent"))
                {
                    Constant.polishTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Excellent"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Symmetry Array
            for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent"))
                {
                    Constant.symmertyTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Excellent"))
                    {
                        break;
                    }
                }
            }

            adapterNotifyData();
        }
        else if(Constant.makeSelectedValue.equalsIgnoreCase("EX CUT") ||
                (Constant.diamondQualitySelected.equalsIgnoreCase("best") && Constant.makeSelectedValue.equalsIgnoreCase("EX CUT")))
        {
            // Set all items to not selected
            resetAllSelections();

            // Set selected items For Cut Array
            for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent"))
                {
                    Constant.cutTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Excellent"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Polish Array
            for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    Constant.polishTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Symmetry Array
            for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    Constant.symmertyTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        break;
                    }
                }
            }

            adapterNotifyData();
        }
        else if(Constant.makeSelectedValue.equalsIgnoreCase("3 VG+") ||
                (Constant.diamondQualitySelected.equalsIgnoreCase("best") && Constant.makeSelectedValue.equalsIgnoreCase("3 VG+")))
        {
            // Set all items to not selected
            resetAllSelections();

            // Set selected items For Cut Array
            for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    Constant.cutTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Polish Array
            for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    Constant.polishTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Symmetry Array
            for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    Constant.symmertyTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        break;
                    }
                }
            }

            adapterNotifyData();
        }
        else if(Constant.diamondQualitySelected.equalsIgnoreCase("medium") && Constant.makeSelectedValue.equalsIgnoreCase("3EX"))
        {
            // Set all items to not selected
            resetAllSelections();

            // Set selected items For Cut Array
            for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Ideal") || displayAttr.equalsIgnoreCase("Excellent"))
                {
                    Constant.cutTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Excellent"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Polish Array
            for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent"))
                {
                    Constant.polishTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Excellent"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Symmetry Array
            for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent"))
                {
                    Constant.symmertyTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Excellent"))
                    {
                        break;
                    }
                }
            }

            adapterNotifyData();
        }
        else if(Constant.diamondQualitySelected.equalsIgnoreCase("medium") && Constant.makeSelectedValue.equalsIgnoreCase("EX CUT"))
        {
            // Set all items to not selected
            resetAllSelections();

            // Set selected items For Cut Array
            for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent"))
                {
                    Constant.cutTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Excellent"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Polish Array
            for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    Constant.polishTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Symmetry Array
            for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    Constant.symmertyTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        break;
                    }
                }
            }

            adapterNotifyData();
        }
        else if(Constant.diamondQualitySelected.equalsIgnoreCase("medium") && Constant.makeSelectedValue.equalsIgnoreCase("3 VG+"))
        {
            // Set all items to not selected
            resetAllSelections();

            // Set selected items For Cut Array
            for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    Constant.cutTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Polish Array
            for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    Constant.polishTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Symmetry Array
            for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    Constant.symmertyTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        break;
                    }
                }
            }

            adapterNotifyData();
        }
        else if(Constant.diamondQualitySelected.equalsIgnoreCase("low") && Constant.makeSelectedValue.equalsIgnoreCase("3EX"))
        {
            // Set all items to not selected
            resetAllSelections();

            // Set selected items For Cut Array
            for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Ideal") || displayAttr.equalsIgnoreCase("Excellent"))
                {
                    Constant.cutTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Excellent"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Polish Array
            for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent"))
                {
                    Constant.polishTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Excellent"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Symmetry Array
            for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent"))
                {
                    Constant.symmertyTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Excellent"))
                    {
                        break;
                    }
                }
            }

            adapterNotifyData();
        }
        else if(Constant.diamondQualitySelected.equalsIgnoreCase("low") && Constant.makeSelectedValue.equalsIgnoreCase("EX CUT"))
        {
            // Set all items to not selected
            resetAllSelections();

            // Set selected items For Cut Array
            for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent"))
                {
                    Constant.cutTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Excellent"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Polish Array
            for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    Constant.polishTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Symmetry Array
            for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    Constant.symmertyTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        break;
                    }
                }
            }

            adapterNotifyData();
        }
        else if(Constant.diamondQualitySelected.equalsIgnoreCase("low") && Constant.makeSelectedValue.equalsIgnoreCase("3 VG+"))
        {
            // Set all items to not selected
            resetAllSelections();

            // Set selected items For Cut Array
            for (int i = 0; i < Constant.cutTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.cutTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    Constant.cutTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Polish Array
            for (int i = 0; i < Constant.polishTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.polishTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    Constant.polishTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        break;
                    }
                }
            }

            // Set selected items For Symmetry Array
            for (int i = 0; i < Constant.symmertyTypeArrayList.size(); i++)
            {
                String displayAttr = Constant.symmertyTypeArrayList.get(i).getDisplayAttr();
                if (displayAttr.equalsIgnoreCase("Excellent") || displayAttr.equalsIgnoreCase("Very Good"))
                {
                    Constant.symmertyTypeArrayList.get(i).setSelected(true);
                    if (displayAttr.equalsIgnoreCase("Very Good"))
                    {
                        break;
                    }
                }
            }

            adapterNotifyData();
        }
        else{
            // Set all items to not selected
            resetAllSelections();
        }

        // Handle Device Back Button code.
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed()
            {
                getEditTextValue();
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void resetAllSelections() {
        setAllItemsToNotSelected(Constant.cutTypeArrayList);
        setAllItemsToNotSelected(Constant.polishTypeArrayList);
        setAllItemsToNotSelected(Constant.symmertyTypeArrayList);
    }

    // Clear All Selected Data.
    private void clearAllSelectionsArrayList() {
        setAllItemsToNotSelected(Constant.cutTypeArrayList);
        setAllItemsToNotSelected(Constant.polishTypeArrayList);
        setAllItemsToNotSelected(Constant.symmertyTypeArrayList);
        setAllItemsToNotSelected(Constant.technologyTypeArrayList);
        setAllItemsToNotSelected(Constant.eyeCleanArrayList);
        setAllItemsToNotSelected(Constant.shadeArrayList);
        setAllItemsToNotSelected(Constant.lusterArrayList);
        location_et.setText("");
        lot_id_et.setText("");
        mm_length_from_et.setText("");
        mm_length_to_et.setText("");
        mm_width_from_et.setText("");
        mm_width_to_et.setText("");
        mm_depth_from_et.setText("");
        mm_depth_to_et.setText("");

        cutTypeListAdapter.notifyDataSetChanged();
        polishTypeListAdapter.notifyDataSetChanged();
        symmetryTypeListAdapter.notifyDataSetChanged();
        technologyTypeListAdapter.notifyDataSetChanged();
        eyeCleanDataListAdapter.notifyDataSetChanged();
        shadeDataListAdapter.notifyDataSetChanged();
        lusterDataListAdapter.notifyDataSetChanged();

        String hint = getResources().getString(R.string.select_intensity);
        String hintOvertone = getResources().getString(R.string.select_overtone);
        String hintFromTable = getResources().getString(R.string.from_string);
        String hintToTable = getResources().getString(R.string.to_string);
        String hintFromCrown = getResources().getString(R.string.from_string1);
        String hintToCrown = getResources().getString(R.string.to_string1);

        fancyColorIntensityListAdapter = new FancyColorIntensityListAdapter(context, Constant.fancyColorIntensityArrayList, hint);
        spinner_fancy_intensity_view.setAdapter(fancyColorIntensityListAdapter);

        fancyColorOverToneListAdapter = new FancyColorOverToneListAdapter(context, Constant.fancyColorOverToneArrayList, hintOvertone);
        spinner_fancy_over_tone_view.setAdapter(fancyColorOverToneListAdapter);

        tableFromDataListAdapter = new TableFromDataListAdapter(context, Constant.tableDataPercantageArrayList, hintFromTable);
        table_from_spinner.setAdapter(tableFromDataListAdapter);

        tableToDataListAdapter = new TableToDataListAdapter(context, Constant.tableDataPercantageArrayList, hintToTable);
        table_to_spinner.setAdapter(tableToDataListAdapter);

        depthFromDataListAdapter = new DepthFromDataListAdapter(context, Constant.depthDataPercantageArrayList, hintFromTable);
        depth_from_spinner.setAdapter(depthFromDataListAdapter);

        depthToDataListAdapter = new DepthToDataListAdapter(context, Constant.depthDataPercantageArrayList, hintToTable);
        depth_to_spinner.setAdapter(depthToDataListAdapter);

        crownFromDataListAdapter = new CrownFromDataListAdapter(context, Constant.crowmArrayList, hintFromCrown);
        crown_from_spinner.setAdapter(crownFromDataListAdapter);

        crownToDataListAdapter = new CrownToDataListAdapter(context, Constant.crowmArrayList, hintToCrown);
        crown_to_spinner.setAdapter(crownToDataListAdapter);

        pavillionFromDataListAdapter = new PavillionFromDataListAdapter(context, Constant.pavillionArrayList, hintFromCrown);
        pavillion_from_spinner.setAdapter(pavillionFromDataListAdapter);

        pavillionToDataListAdapter = new PavillionToDataListAdapter(context, Constant.pavillionArrayList, hintToCrown);
        pavillion_to_spinner.setAdapter(pavillionToDataListAdapter);

    }


    private void setAllItemsToNotSelected(ArrayList<AttributeDetailsModel> list) {
        for (AttributeDetailsModel item : list) {
            item.setSelected(false);
        }
    }

    void adapterNotifyData()
    {
        cutTypeListAdapter.notifyDataSetChanged();
        polishTypeListAdapter.notifyDataSetChanged();
        symmetryTypeListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view)
    {
        Intent intent;
        int id = view.getId();

        if(id == R.id.back_img)
        {
            Utils.hideKeyboard(activity);
            getEditTextValue();
        }
        else if(id == R.id.apply_filter_tv)
        {
            Utils.hideKeyboard(activity);
            getEditTextValue();
        }
        else if(id == R.id.clear_filter_tv)
        {
            Utils.hideKeyboard(activity);
            clearAllFilter();
        }
    }

    void clearAllFilter()
    {
        Constant.filterClear = "yes"; // This is USe For Clear All Select Filter BG Color

        Constant.shapeDiamondValue="";
        Constant.priceFrm="";
        Constant.priceTo="";
        Constant.caratFrm="";
        Constant.caratTo="";
        //Constant.colorType="";
        Constant.colorValue="";
        Constant.fancyColorValue="";
        Constant.clarityValue="";
        Constant.certificateValue="";
        Constant.fluorescenceValue="";
        Constant.makeValue="";
        Constant.isReturnable="";
        Constant.searchKeyword="";
        Constant.cutValue="";
        Constant.polishValue="";
        Constant.symmetryValue="";
        Constant.technologyValue="";
        Constant.eyeCleanValue="";
        Constant.shadeValue="";
        Constant.lusterValue="";
        Constant.fancyColorIntensity="";
        Constant.fancyColorOvertone="";
        Constant.tableFrm="";
        Constant.tableTo="";
        Constant.depthFrmSpinner="";
        Constant.depthToSpinner="";
        Constant.lengthFrm="";
        Constant.lengthTo="";
        Constant.widthFrm="";
        Constant.widthTo="";
        Constant.depthFrmInput="";
        Constant.depthToInput="";
        Constant.crownFrm="";
        Constant.crownTo="";
        Constant.pavillionFrm="";
        Constant.pavillionTo="";
        Constant.lotID="";
        Constant.location="";
        Constant.sortingBy="";

        clearAllSelectionsArrayList();

      //  finish();
    }

    void getEditTextValue()
    {
        Constant.lengthFrm = mm_length_from_et.getText().toString().toString();
        Constant.lengthTo = mm_length_to_et.getText().toString().toString();

        Constant.widthFrm = mm_width_from_et.getText().toString().toString();
        Constant.widthTo = mm_width_to_et.getText().toString().toString();

        Constant.depthFrmInput = mm_depth_from_et.getText().toString().toString();
        Constant.depthToInput = mm_depth_to_et.getText().toString().toString();

        Constant.lotID = lot_id_et.getText().toString().toString();
        Constant.location = location_et.getText().toString().toString();

        finish();
    }


    private BottomSheetDialog dialog;

    private void showFancyColorIntensity()
    {
        dialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog.setContentView(R.layout.dialog_show_fancy_color);
        recycler_fancy_color_intensity_view = dialog.findViewById(R.id.recycler_fancy_color_intensity_view);

        TextView textView2 = dialog.findViewById(R.id.textView2);

        textView2.setText(getResources().getString(R.string.select_intensity));

        recycler_fancy_color_intensity_view.setHasFixedSize(true);
        recycler_fancy_color_intensity_view.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recycler_fancy_color_intensity_view.setLayoutManager(layoutManager);


        fancyColorIntensityTypeListAdapter = new FancyColorIntensityTypeListAdapter(Constant.fancyColorIntensityArrayList, context, this);
        recycler_fancy_color_intensity_view.setAdapter(fancyColorIntensityTypeListAdapter);
        fancyColorIntensityTypeListAdapter.notifyDataSetChanged();

        ImageView ib_cross = dialog.findViewById(R.id.ib_cross);

        ib_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

    }


    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {

    }

    @Override
    public void getErrorResponce(String error, int service_ID) {

    }

    @Override
    public void itemClick(int position, String action)
    {

        if(action.equalsIgnoreCase("cutType"))
        {
            Constant.cutTypeArrayList.get(position).setSelected(!Constant.cutTypeArrayList.get(position).isSelected());

            cutTypeListAdapter.notifyDataSetChanged();
        }
        else if(action.equalsIgnoreCase("polishType"))
        {
            Constant.polishTypeArrayList.get(position).setSelected(!Constant.polishTypeArrayList.get(position).isSelected());

            polishTypeListAdapter.notifyDataSetChanged();
        }
        else if(action.equalsIgnoreCase("symmetryType"))
        {
            Constant.symmertyTypeArrayList.get(position).setSelected(!Constant.symmertyTypeArrayList.get(position).isSelected());

            symmetryTypeListAdapter.notifyDataSetChanged();
        }
        else if(action.equalsIgnoreCase("technologyType"))
        {
            Constant.technologyTypeArrayList.get(position).setSelected(!Constant.technologyTypeArrayList.get(position).isSelected());

            technologyTypeListAdapter.notifyDataSetChanged();
        }
        else if(action.equalsIgnoreCase("eyeCleanType"))
        {
            Constant.eyeCleanArrayList.get(position).setSelected(!Constant.eyeCleanArrayList.get(position).isSelected());

            eyeCleanDataListAdapter.notifyDataSetChanged();
        }
        else if(action.equalsIgnoreCase("shadeType"))
        {
            Constant.shadeArrayList.get(position).setSelected(!Constant.shadeArrayList.get(position).isSelected());

            shadeDataListAdapter.notifyDataSetChanged();
        }
        else if(action.equalsIgnoreCase("lusterType"))
        {
            Constant.lusterArrayList.get(position).setSelected(!Constant.lusterArrayList.get(position).isSelected());

            lusterDataListAdapter.notifyDataSetChanged();
        }
        /*else if(action.equalsIgnoreCase("fancyColorPick"))
        {
            //Toast.makeText(activity, Constant.fancyColorIntensityArrayList.get(position).getDisplayAttr(), Toast.LENGTH_SHORT).show();
            fancy_color_tv.setText(Constant.fancyColorIntensityArrayList.get(position).getDisplayAttr());
            dialog.dismiss();
        }*/
    }
}