package com.geeko.pommier_v2;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.geeko.pommier_v2.databinding.ActivityTableauBinding;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.rajat.pdfviewer.PdfViewerActivity;
import java.io.File;
import java.util.List;

public class TableauActivity extends AppCompatActivity {
    public static final String DEST = "results/tables/simple_table.pdf";
    private AppBarConfiguration appBarConfiguration;
    private ActivityTableauBinding binding;
    File pdfFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTableauBinding inflate = ActivityTableauBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView(inflate.getRoot());
        setSupportActionBar(this.binding.toolbar);
        this.binding.fab.setOnClickListener(new View.OnClickListener() { // from class: com.geeko.pommier.TableauActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", 0).setAnchorView(R.id.fab).setAction("Action", (View.OnClickListener) null).show();
            }
        });
        try {
            manipulatePdf();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    protected void manipulatePdf() throws Exception {
        File outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File outputFile = File.createTempFile("table", ".pdf", outputDir);
        outputFile.getParentFile().mkdirs();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(outputFile));
        Document doc = new Document(pdfDoc);
        Table table = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
        table.addCell("Code Article");
        table.addCell("Emplacement");
        table.addCell("Quantité");
        table.addCell("Commentaire");
        doc.add((IBlockElement) table);
        Table table2 = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        List<Emplacement> data = db.bdd().getAll();
        for (Emplacement e : data) {
            table2.addCell(e.ref);
            table2.addCell(e.emplacementPalette);
            table2.addCell(e.quantite);
            table2.addCell(e.note);
        }
        doc.add((IBlockElement) table2);
        doc.close();
        Toast.makeText(this, "Path = " + outputFile.getPath().toString(), 1).show();
        TextView t = (TextView) findViewById(R.id.textViewPath);
        t.setText(outputFile.getPath().toString());
        startActivity(PdfViewerActivity.Companion.launchPdfFromPath(this, outputFile.getPath(), "Expedition Mondial", outputFile.getParent().toString(), true, false));
    }

}