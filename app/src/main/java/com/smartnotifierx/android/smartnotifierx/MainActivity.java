package com.smartnotifierx.android.smartnotifierx;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
/**
 * Created by sv300_000 on 11-Oct-17.
 */

public class MainActivity extends AppCompatActivity{

    private String phoneNumber;
    private String callType;
    private String callDate;
    private Date callDateTime;
    StringBuffer sb = new StringBuffer();
    private String contactname;
    private RecyclerView recyclerView;
    private LogAdapter logAdapter;
    private List<ContactDetails> detailsList = new ArrayList<>();
    public static ImageView image;
    private Paint p = new Paint();
    private static final float BITMAP_SCALE = 0.6f;
    private static final float BLUR_RADIUS = 15f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);
        image = (ImageView) findViewById(R.id.imageBlur);
        Bitmap resultBmp = blur(this, BitmapFactory.decodeResource(getResources(), R.mipmap.background));
        image.setImageBitmap(resultBmp);
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CALL_LOG},0);
            return;
        }*/
        permissionCheck(Manifest.permission.READ_CALL_LOG);
        permissionCheck(Manifest.permission.READ_CONTACTS);
        permissionCheck(Manifest.permission.READ_SMS);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        logAdapter = new LogAdapter(detailsList);
        RecyclerView.LayoutManager lLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(logAdapter);
        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        //recyclerView.addItemDecoration(dividerItemDecoration);
        initSwipe();
        getCallDetails();
    }




    @Override
    protected void onPostResume() {
        super.onPostResume();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        logAdapter = new LogAdapter(detailsList);
        RecyclerView.LayoutManager lLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(logAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        initSwipe();
        int size = this.detailsList.size();
        this.detailsList.clear();
        logAdapter.notifyItemRangeRemoved(0,size);
        getCallDetails();
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                ContactDetails log = detailsList.get(position);
                if (direction == ItemTouchHelper.RIGHT) {
                    Intent i = new Intent(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:"+log.getName()));
                    if(ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(MainActivity.this,"Please grant permission", LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
                    }
                    else
                    {
                        startActivity(i);
                    }
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.fromParts("sms",log.getName(),null)));
                }
            }

            public void onChildDraw(Canvas c, RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,float dX,float dY,int actionState,boolean isCurrentlyActive ){
                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height/3;

                    if(dX > 0)
                    {
                        p.setColor(Color.parseColor("#00CC66"));
                        RectF background = new RectF((float) itemView.getLeft(),(float) itemView.getTop(),dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(),R.mipmap.phone);
                        RectF icon_dest = new RectF((float) itemView.getLeft()+width,(float) itemView.getTop()+width,itemView.getLeft()+2*width,(float) itemView.getBottom()-width);
                        c.drawBitmap(icon,null,icon_dest,p);

                    }

                    else {
                        p.setColor(Color.parseColor("#FF9900"));
                        RectF background = new RectF((float) itemView.getRight()+dX,(float) itemView.getTop(),(float) itemView.getRight(),(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(),R.mipmap.mail);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width,(float) itemView.getTop()+width,(float) itemView.getRight()-width,(float) itemView.getBottom()-width);
                        c.drawBitmap(icon,null,icon_dest,p);

                    }
                }
                super.onChildDraw(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void getCallDetails()
    {
        String orderBy = CallLog.Calls.DATE;
        Cursor cursor = managedQuery(CallLog.Calls.CONTENT_URI,null,
                null,
                null,
                orderBy + " DESC LIMIT 5");
        int num = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int name = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        //StringBuffer sb = new StringBuffer();
        while(cursor.moveToNext())
        {
            phoneNumber = cursor.getString(num);
            long seconds = cursor.getLong(date);
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy HH:mm");
            String dateString = format.format(new Date(seconds));
            callDate = cursor.getString(date);
            callDateTime = new Date(Long.valueOf(callDate));
            contactname = cursor.getString(name);
            callType = cursor.getString(type);
            int callCode = Integer.parseInt(callType);
            ContactDetails contactDetails = new ContactDetails(phoneNumber,dateString);
            if(contactname!=null)
                contactDetails.setContactName(contactname);
            if(callCode == CallLog.Calls.MISSED_TYPE)
                detailsList.add(contactDetails);
        }
        logAdapter.notifyDataSetChanged();
        //cursor.close();
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blur(Context context, Bitmap image) {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);

        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        intrinsicBlur.setRadius(BLUR_RADIUS);
        intrinsicBlur.setInput(tmpIn);
        intrinsicBlur.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    public void permissionCheck(String str) {
        if (ActivityCompat.checkSelfPermission(this, str) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{str},0);
            return;
        }
    }
}
