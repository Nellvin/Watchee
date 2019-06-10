package pl.fl.zegarke;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.util.LruCache;

import java.util.ArrayList;
import java.util.List;

public class MyGridAdapter extends FragmentGridPagerAdapter {

    private  Context context;
    private List<Row> master;
    private ColorDrawable defaultCololr;

    @SuppressLint("ResourceAsColor")
    public MyGridAdapter(Context ctx, FragmentManager fm){
        super(fm);
        this.context=ctx;
        master=new ArrayList<Row>();
        master.add(new Row( cardFragment("","Dodaj: jajka, mąke, wode gazowana,sól")));
        master.add(new Row( cardFragment("Wymieszaj","dokładnie")));
        master.add(new Row( cardFragment("Rozgrzej","patelnie")));
        master.add(new Row( cardFragment("Nałóż","porcje")));
        master.add(new Row( cardFragment("Smaż","")));
        master.add(new Row( cardFragment("Zrób Flipa","")));
        master.add(new Row( cardFragment("Zrób","stos"),cardFragment("Smacznego","")));
        defaultCololr=new ColorDrawable(R.color.blue);
    }

    private Fragment cardFragment(String title, String text){
        Resources resource = context.getResources();
        CardFragment cardFragment= CardFragment.create(title,text);
        cardFragment.setCardMarginBottom(resource.getDimensionPixelSize(R.dimen.card_margin_bottom ));
        return cardFragment;
    }

    @Override
    public int getRowCount() {
        return master.size();
    }

    @Override
    public int getColumnCount(int rowNumber) {
        return master.get(rowNumber).getCoulumnCount();
    }

    @Override
    public Fragment getFragment(int row, int column) {
        Row roww = master.get(row);

        return roww.getColumn(column);
    }
    private class Row{
        final List<Fragment> lift = new ArrayList<Fragment>();
        public Row(Fragment... fragments){
            for (Fragment f: fragments){
                add(f);
            }
        }
        public void add(Fragment fragment){
            lift.add(fragment);
        }

        Fragment getColumn(int i){
            return lift.get(i);
        }

        public int getCoulumnCount(){
            return lift.size();
        }
    }
    static final int [] BG_IMAGES = new int[]{
            R.drawable.a,
            R.drawable.aa,
            R.drawable.aaaaa,
            R.drawable.aaaaaa,
            R.drawable.aaaaaaa,
            R.drawable.aaa,
            R.drawable.aaaa
    };
    int maxMem = (int)(Runtime.getRuntime().maxMemory()/1024);
    int cachSize = maxMem/8;
    int myRow;
    LruCache<Integer, Drawable> lruCache = new LruCache<Integer, Drawable>(cachSize){

        @Override
        protected Drawable create(Integer key) {
            int imagenumer = BG_IMAGES[key % BG_IMAGES.length];
            myRow=key;
            new ImageLoader().execute(imagenumer);
            return defaultCololr;
        }
    };

    class ImageLoader extends AsyncTask<Integer,Void,Drawable>{

        @Override
        protected void onPostExecute(Drawable drawable) {
            TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{defaultCololr,drawable} );
             lruCache.put(myRow, transitionDrawable);
             notifyRowBackgroundChanged(myRow);
             transitionDrawable.startTransition(3000);
        }

        @Override
        protected Drawable doInBackground(Integer... integers) {
            return context.getResources().getDrawable(integers[0]);
        }
    }

    @Override
    public Drawable getBackgroundForRow(int row) {
        return lruCache.get(row);
    }
}
