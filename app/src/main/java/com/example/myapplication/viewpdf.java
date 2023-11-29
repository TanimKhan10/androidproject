package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class viewpdf extends AppCompatActivity {



    private String title;
    private String url;
    private boolean isFavorite;

    private DatabaseReference favoritesRef;


    WebView pdfview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpdf);
        favoritesRef = FirebaseDatabase.getInstance().getReference().child("favorites");

        Button btnAddToFavorites = findViewById(R.id.btnAddToFavorites);
        Button btnHighlight = findViewById(R.id.btnHighlight);
        Button btnRemoveHighlight = findViewById(R.id.btnRemoveHighlight);

        pdfview = (WebView) findViewById(R.id.viewpdf);

        pdfview.getSettings().setJavaScriptEnabled(true);

        title=getIntent().getStringExtra("title");
        url=getIntent().getStringExtra("url");
        checkFavoriteStatus();




        ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle((title));
        pd.setMessage("Openning...");


        btnHighlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightText();
            }
        });

        btnRemoveHighlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeHighlighting();
            }
        });

        btnAddToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    // If already in favorites, remove from favorites
                    removeFromFavorites();
                } else {
                    // If not in favorites, add to favorites
                    addToFavorites(title, url);
                }
            }
        });

        pdfview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String Urrl, Bitmap favicon) {
                super.onPageStarted(view, Urrl, favicon);
                pd.show();
            }

            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                // Log or handle JavaScript console messages here
                Log.d("WebView Console", consoleMessage.message() + " -- From line "
                        + consoleMessage.lineNumber() + " of " + consoleMessage.sourceId());
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String Urrl) {
                super.onPageFinished(view, Urrl);

                pd.dismiss();

                // Delay before injecting JavaScript (adjust the delay time as needed)
                pdfview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String javascriptCode = "function highlightText() { " +
                                "   var range = window.getSelection().getRangeAt(0); " +
                                "   var span = document.createElement('span'); " +
                                "   span.style.backgroundColor = 'yellow'; " +
                                "   range.surroundContents(span); " +
                                "}";

                        // Inject JavaScript code
                        pdfview.evaluateJavascript(javascriptCode, new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                // JavaScript code has been injected, now call the highlightText function
                                pdfview.evaluateJavascript("highlightText();", null);
                            }
                        });
                    }
                }, 1000); // Delay for 1 second (adjust as needed)
            }
        });


        String Urrl="";
        try {
            Urrl= URLEncoder.encode(url,"UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();

            // Show a user-friendly message (e.g., using a Toast)
            Toast.makeText(this, "Error encoding URL", Toast.LENGTH_SHORT).show();
        }


        pdfview.loadUrl("https://docs.google.com/viewerng/viewer?embedded=true&url="+Urrl);

    }

    private void checkFavoriteStatus() {
        // Check if the PDF is already in favorites
        favoritesRef.orderByChild("url").equalTo(url).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    isFavorite = true;
                    // Change the text of the button accordingly
                    updateButtonText();
                } else {
                    isFavorite = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void updateButtonText() {
        Button btnAddToFavorites = findViewById(R.id.btnAddToFavorites);
        if (isFavorite) {
            btnAddToFavorites.setText("Remove from Favorites");
        } else {
            btnAddToFavorites.setText("Add to Favorites");
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (pdfview.canGoBack()) {
            pdfview.goBack();
        } else {
            // Handle going back to the previous activity
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }


    private void addToFavorites(String title, String url) {
        PdfModel pdfModel = new PdfModel(title, url);
        favoritesRef.push().setValue(pdfModel);
        isFavorite = true;
        updateButtonText();
        Toast.makeText(viewpdf.this, "Added to Favorites", Toast.LENGTH_SHORT).show();
    }

    private void removeFromFavorites() {
        // Find the entry in favorites corresponding to the current PDF and remove it
        favoritesRef.orderByChild("url").equalTo(url).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataSnapshot.getRef().removeValue();
                    isFavorite = false;
                    updateButtonText();
                    Toast.makeText(viewpdf.this, "Removed from Favorites", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    // Java method to highlight text in the WebView
    private void highlightText() {
        // JavaScript code to highlight text
        String javascriptCode = "function highlightText() { " +
                "   var range = window.getSelection().getRangeAt(0); " +
                "   var span = document.createElement('span'); " +
                "   span.style.backgroundColor = 'yellow'; " +
                "   range.surroundContents(span); " +
                "}";

        // Inject JavaScript code
        pdfview.evaluateJavascript(javascriptCode, null);

        // Call the highlightText function
        pdfview.evaluateJavascript("highlightText();", null);
    }

    // Java method to remove highlighting from the WebView
    private void removeHighlighting() {
        // JavaScript code to remove highlighting
        String javascriptCode = "function removeHighlighting() { " +
                "   var highlightedElements = document.querySelectorAll('span[style=\"background-color: yellow;\"]'); " +
                "   highlightedElements.forEach(function (element) { " +
                "       var parent = element.parentNode; " +
                "       while (element.firstChild) { " +
                "           parent.insertBefore(element.firstChild, element); " +
                "       } " +
                "       parent.removeChild(element); " +
                "   }); " +
                "}";

        // Inject JavaScript code
        pdfview.evaluateJavascript(javascriptCode, null);

        // Call the removeHighlighting function
        pdfview.evaluateJavascript("removeHighlighting();", null);
    }


}
