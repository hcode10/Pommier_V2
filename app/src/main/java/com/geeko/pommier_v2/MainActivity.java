package com.geeko.pommier_v2;

import android.os.Bundle;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.geeko.pommier_v2.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;
import com.google.android.material.snackbar.Snackbar;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    MyCursorAdapter cA;
    Cursor csr;
    public AppDatabase db;
    ListView lv1;
    private AppBarConfiguration mAppBarConfiguration;
    public boolean scanEmp = false;
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                if (!this.scanEmp) {
                    Emplacement emp = new Emplacement();
                    emp.emplacementPalette = "xxxxx";
                    emp.ref = result.getContents().replace("@", "");
                    emp.quantite = "0";
                    this.db.bdd().insert(emp);
                    setUpOrRefreshListView();
                }


            });

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding inflate = ActivityMainBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView(inflate.getRoot());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.binding.toolbar);
        this.binding.fab.setOnClickListener(new View.OnClickListener() { // from class: com.geeko.pommier.MainActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", BaseTransientBottomBar.LENGTH_LONG).setAnchorView(R.id.fab).setAction("Action", (View.OnClickListener) null).show();
                ScanOptions options = new ScanOptions();
                options.setDesiredBarcodeFormats("CODE_128");
                options.setPrompt("Scan a barcode");
                options.setCameraId(0);
                options.setBeepEnabled(true);
                options.setBarcodeImageEnabled(true);
                options.setOrientationLocked(false);
                MainActivity.this.barcodeLauncher.launch(options);
            }
        });
        this.db = AppDatabase.getInstance(getApplicationContext());
        this.lv1 = (ListView) findViewById(R.id.lv1);
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Tout supprimer ?");
            builder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() { // from class: com.geeko.pommier.MainActivity.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int id2) {
                    MainActivity.this.db.bdd().deleteALL();
                    MainActivity.this.setUpOrRefreshListView();
                }
            });
            builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() { // from class: com.geeko.pommier.MainActivity.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int id2) {
                }
            });
            builder.show();
            return true;
        }
        if (id == R.id.genPDF) {
            Intent i = new Intent(this, TableauActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setUpOrRefreshListView() {
        Cursor cursor = getCursor();
        this.csr = cursor;
        MyCursorAdapter myCursorAdapter = this.cA;
        if (myCursorAdapter == null) {
            this.cA = new MyCursorAdapter(this, this.csr);
            this.lv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { // from class: com.geeko.pommier.MainActivity.4
                @Override // android.widget.AdapterView.OnItemLongClickListener
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(view.getContext(), "You long-clicked with Reference = " + MainActivity.this.csr.getString(MainActivity.this.csr.getColumnIndex("ref")) + " Emplacement = " + MainActivity.this.csr.getString(MainActivity.this.csr.getColumnIndex("emplacement_palette")) + " UID = " + l, 0).show();
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    Bundle b = new Bundle();
                    b.putLong("key", l);
                    intent.putExtras(b);
                    MainActivity.this.startActivity(intent);
                    return true;
                }
            });
        } else {
            myCursorAdapter.swapCursor(cursor);
        }
        this.lv1.setAdapter((ListAdapter) this.cA);
    }

    private Cursor getCursor() {
        MatrixCursor mxcsr = new MatrixCursor(new String[]{"_id", "ref", "quantite", "emplacement_palette"}, 0);
        for (Emplacement p : this.db.bdd().getAll()) {
            mxcsr.addRow(new Object[]{Integer.valueOf(p.uid), p.ref, p.quantite, p.emplacementPalette});
        }
        return mxcsr;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        setUpOrRefreshListView();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.csr.close();
    }


}