package com.diamondxe.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diamondxe.Beans.DrawerMenuModel;
import com.diamondxe.Interface.TwoRecyclerInterface;
import com.diamondxe.R;

import java.util.ArrayList;

public class DrawerItemCustomAdapter extends BaseExpandableListAdapter {

    private LayoutInflater inflater;
    private ArrayList<DrawerMenuModel> mParent;
    private ExpandableListView accordion;
    public int lastExpandedGroupPosition;
    TwoRecyclerInterface recyclerInterface;
    Context context;

    public DrawerItemCustomAdapter(Context context, ArrayList<DrawerMenuModel> parent, ExpandableListView accordion, TwoRecyclerInterface recyclerInterface) {
        mParent = parent;
        inflater = LayoutInflater.from(context);
        this.accordion = accordion;
        this.context = context;
        this.recyclerInterface = recyclerInterface;
    }


    @Override
    //counts the number of group/parent items so the list knows how many times calls getGroupView() method
    public int getGroupCount() {
        return mParent.size();
    }

    @Override
    //counts the number of children items so the list knows how many times calls getChildView() method
    public int getChildrenCount(int i) {
        return mParent.get(i).getSubMenu().size();
    }

    @Override
    //gets the title of each parent/group
    public Object getGroup(int i) {
        return mParent.get(i).getName();
    }

    @Override
    //gets the name of each item
    public Object getChild(int i, int i1) {
        return mParent.get(i).getSubMenu().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    //in this method you must set the text to see the parent/group on the list
    public View getGroupView(final int i,final boolean isExpanded, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = inflater.inflate(R.layout.row_category_parent_lay, viewGroup, false);
        }
        // set category name as tag so view can be found view later
        view.setTag(getGroup(i).toString());

        LinearLayout parentCategoryNameLin = view.findViewById(R.id.parent_category_name_lin);
        TextView textView = view.findViewById(R.id.txt_parent_category_name);
        ImageView img_navigation = view.findViewById(R.id.img_navigation);

        ImageView img_expand = view.findViewById(R.id.img_expand);

        //"i" is the position of the parent/group in the list
        textView.setText(getGroup(i).toString());
        if (mParent.get(i).isSelected())
        {

            img_navigation.setImageResource(mParent.get(i).getImageSelected());
            //textView.setCompoundDrawablesWithIntrinsicBounds(mParent.get(i).getImageSelected(), 0,0, 0);
            textView.setTextColor(context.getResources().getColor(R.color.purple));
        } else {
            img_navigation.setImageResource(mParent.get(i).getImage());
            //textView.setCompoundDrawablesWithIntrinsicBounds(mParent.get(i).getImage(), 0,0, 0);
            textView.setTextColor(context.getResources().getColor(R.color.purple));
            //textView.setTextColor(context.getResources().getColor(R.color.black_like));
        }

        // If group is expanded then change the text into bold and change the icon
        if (mParent.get(i).getSubMenu().size() > 0)
        {
            img_expand.setVisibility(View.VISIBLE);
        } else {
            img_expand.setVisibility(View.GONE);
        }


        if (isExpanded) {
            //textView.setTypeface(null, Typeface.BOLD);
            //img_expand.setImageResource(R.mipmap.gray_arrow_up);
            img_expand.setImageResource(R.drawable.drop_up);
        } else {
            // If group is not expanded then change the text back into normal
            // and change the icon
            // textView.setTypeface(null, Typeface.NORMAL);
            //img_expand.setImageResource(R.mipmap.gray_arrow_down);
            img_expand.setImageResource(R.drawable.drop_down);
        }

        img_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    accordion.collapseGroup(i);
                } else {
                    accordion.expandGroup(i);
                }
            }
        });

        parentCategoryNameLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("catId", mParent.get(i).getId());
                intent.putExtra("catName", mParent.get(i).getName());
                intent.putExtra("comeFrom", "Category");
                context.startActivity(intent);*/

                if (mParent.get(i).getSubMenu().size() > 0)
                {
                    if (isExpanded)
                    {
                        accordion.collapseGroup(i);
                    } else {
                        accordion.expandGroup(i);
                    }
                }
                else{
                    recyclerInterface.itemClick(i,-1,"parant");
                }



            }
        });

        //return the entire view
        return view;
    }

    @Override
    //in this method you must set the text to see the children on the list
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null)
        {
            view = inflater.inflate(R.layout.row_category_child_lay, viewGroup, false);
        }
        LinearLayout childCategoryNameLin = view.findViewById(R.id.child_category_name_lin);
        TextView textView = view.findViewById(R.id.txt_child_category_name);
        ImageView img_next = view.findViewById(R.id.img_next);
        ImageView img_sub = view.findViewById(R.id.img_sub);

        if (mParent.get(i).getSubMenu().get(i1).getSubMenu().size() > 0)
        {
            img_next.setVisibility(View.VISIBLE);
        }
        else {
            img_next.setVisibility(View.GONE);
        }

        textView.setText(mParent.get(i).getSubMenu().get(i1).getName());
        if (mParent.get(i).getSubMenu().get(i1).isSelected())
        {
            img_sub.setImageResource(mParent.get(i).getSubMenu().get(i1).getImageSelected());
            // textView.setCompoundDrawablesWithIntrinsicBounds(mParent.get(i).getSubMenu().get(i1).getImageSelected(), 0,0, 0);
            textView.setTextColor(context.getResources().getColor(R.color.purple));
        } else {
            img_sub.setImageResource(mParent.get(i).getSubMenu().get(i1).getImage());
            // textView.setCompoundDrawablesWithIntrinsicBounds(mParent.get(i).getSubMenu().get(i1).getImage(), 0,0, 0);
            //textView.setTextColor(context.getResources().getColor(R.color.black_like));
            textView.setTextColor(context.getResources().getColor(R.color.purple));
        }

        childCategoryNameLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mParent.get(i).getSubMenu().size() > 0)
                {
                    recyclerInterface.itemClick(i,i1,"child");
                }else{}
            }
        });

        img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mParent.get(i).getSubMenu().get(i1).getSubMenu().size() > 0)
                {
                    /*Intent intent = new Intent(context, SubCategoriesActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable("ARRAYLIST", (Serializable) mParent.get(i).getSubMenu().get(i1));
                    intent.putExtra("BUNDLE", args);
                    context.startActivity(intent);*/
                }else{

                }
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    /**
     * automatically collapse last expanded group
     * @see http://stackoverflow.com/questions/4314777/programmatically-collapse-a-group-in-expandablelistview
     */
    public void onGroupExpanded(int groupPosition) {

        if (groupPosition != lastExpandedGroupPosition) {
            accordion.collapseGroup(lastExpandedGroupPosition);
        }

        super.onGroupExpanded(groupPosition);

        lastExpandedGroupPosition = groupPosition;
    }

}

