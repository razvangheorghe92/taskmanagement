package razvan.gheorghe.taskmanagement.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import razvan.gheorghe.taskmanagement.R;
import razvan.gheorghe.taskmanagement.fragments.ExportFragment;
import razvan.gheorghe.taskmanagement.fragments.ShoppingListFragment;
import razvan.gheorghe.taskmanagement.fragments.TasksFragment;

public class MainActivity extends Activity {
    ActionBar.Tab tab1, tab2, tab3; //taburi meniu
    Context context;

    Fragment tasksFragment = new TasksFragment();
//    Fragment organiserFragment = new OrganiserFragment();
    Fragment organiserFragment = new ShoppingListFragment();
    Fragment exportFragment = new ExportFragment();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //  requestWindowFeature(Window.FEATURE_NO_TITLE);

        tab1 = actionBar.newTab().setText("Task");
        tab2 = actionBar.newTab().setText("Organizator");
        tab3 = actionBar.newTab().setText("Export");

        tab1.setIcon(R.drawable.menu_cautare);
        tab2.setIcon(R.drawable.menu_new_entry);
        tab3.setIcon(R.drawable.menu_info_user);

        tab1.setTabListener(new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                tab1.setIcon(R.drawable.menu_cautare_selectat);
                fragmentTransaction.replace(R.id.fragment_container, tasksFragment);
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                tab1.setIcon(R.drawable.menu_cautare);
                fragmentTransaction.remove(tasksFragment);
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                tab1.setIcon(R.drawable.menu_cautare_selectat);
            }
        });

        tab2.setTabListener(new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                tab2.setIcon(R.drawable.menu_new_entry_selectat);
                fragmentTransaction.replace(R.id.fragment_container, organiserFragment);
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                tab2.setIcon(R.drawable.menu_new_entry);
                fragmentTransaction.remove(organiserFragment);
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                tab2.setIcon(R.drawable.menu_new_entry_selectat);
            }
        });

        tab3.setTabListener(new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                tab3.setIcon(R.drawable.menu_info_user_selectat);
                fragmentTransaction.replace(R.id.fragment_container, exportFragment);
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                tab3.setIcon(R.drawable.menu_info_user);
                fragmentTransaction.remove(exportFragment);
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                tab3.setIcon(R.drawable.menu_info_user_selectat);
            }
        });

        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
        actionBar.addTab(tab3);


    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
        public boolean onCreateOptionsMenu(Menu menu) {

            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_activity_actions, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle presses on the action bar items
            switch (item.getItemId()) {
                case R.id.action_log_out:
                    showPopup(0);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
    }

    public void showPopup(final int type) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_erroare);
        dialog.setCancelable(false);
        TextView textHeader;
        TextView textContent;

        textHeader = (TextView) dialog.findViewById(R.id.text_titlu);
        textContent = (TextView) dialog.findViewById(R.id.text_error);

        textHeader.setText("Atentie !");
        textContent.setText("Iesiti din contul dumneavoastra?");

        if (type == 0) {

        }
        Button btn_nu = (Button) dialog.findViewById(R.id.button_nu);
        if (type == 0) {
            btn_nu.setText("NU");
        }
        btn_nu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button btn_da = (Button) dialog.findViewById(R.id.button_da);
        btn_da.setText("DA");
        btn_da.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (type == 0) {
//                    finish();
//                    new LogSharedPrefs().deletePrefs(MainActivity.this);
//                    Intent signOutIntent = new Intent(MainActivity.this, LoginActivity.class);
//                    startActivity(signOutIntent);
//                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
