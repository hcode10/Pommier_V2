package com.geeko.pommier_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

public class EditActivity extends AppCompatActivity {

    public Button BtnScan;
    public Button BtnUpdateBDD;
    public EditText TextEmp;
    public EditText TextNote;
    public EditText TextQuantite;
    public EditText TextRef;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                this.TextEmp.setText(result.getContents().replaceAll("@", ""));


            });
    public AppDatabase db;
    long uid;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Bundle b = getIntent().getExtras();
        this.uid = -1L;
        if (b != null) {
            this.uid = b.getLong("key");
        }
        this.BtnScan = (Button) findViewById(R.id.buttonScan);
        this.BtnUpdateBDD = (Button) findViewById(R.id.buttonUpdateBDD);
        this.TextEmp = (EditText) findViewById(R.id.editTextEmplacement);
        this.TextRef = (EditText) findViewById(R.id.editTextRef);
        this.TextQuantite = (EditText) findViewById(R.id.editTextQuantite);
        this.TextNote = (EditText) findViewById(R.id.editTextNote);
        AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
        this.db = appDatabase;
        Emplacement emp = appDatabase.bdd().findByUID(Long.valueOf(this.uid));
        this.TextRef.setText(emp.ref);
        this.TextEmp.setText(emp.emplacementPalette);
        this.TextQuantite.setText(emp.quantite);
        this.TextNote.setText(emp.note);
        this.BtnScan.setOnClickListener(new View.OnClickListener() { // from class: com.geeko.pommier.EditActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ScanOptions options = new ScanOptions();
                options.setDesiredBarcodeFormats("CODE_128");
                options.setPrompt("Scan a barcode");
                options.setCameraId(0);
                options.setBeepEnabled(true);
                options.setBarcodeImageEnabled(true);
                options.setOrientationLocked(false);
                EditActivity.this.barcodeLauncher.launch(options);
            }
        });
        this.BtnUpdateBDD.setOnClickListener(new View.OnClickListener() { // from class: com.geeko.pommier.EditActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                EditActivity.this.db.bdd().updateRef(EditActivity.this.TextRef.getText().toString(), Long.valueOf(EditActivity.this.uid));
                EditActivity.this.db.bdd().updateEmplacement(EditActivity.this.TextEmp.getText().toString(), Long.valueOf(EditActivity.this.uid));
                EditActivity.this.db.bdd().updateQuantity(EditActivity.this.TextQuantite.getText().toString(), Long.valueOf(EditActivity.this.uid));
                EditActivity.this.db.bdd().updateNote(EditActivity.this.TextNote.getText().toString(), Long.valueOf(EditActivity.this.uid));
                Toast.makeText(EditActivity.this, "BDD Mise à jour !", Toast.LENGTH_LONG).show();
            }
        });
    }

}