package com.example.sketchapp;
import android.Manifest;


import static java.security.AccessController.getContext;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private CanvasView canvasView;
    private Button colorButton;
    private Button widthButton;
    private Button saveButton;
    private Button clearButton;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        canvasView = findViewById(R.id.canvas_view);
        colorButton = findViewById(R.id.color_button);
        widthButton = findViewById(R.id.width_button);
        saveButton = findViewById(R.id.save_button);
        clearButton = findViewById(R.id.clear_button);

        // Check if WRITE_EXTERNAL_STORAGE permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        }



        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Show a color picker dialog and update the canvas view's color based on the selected color
                        ColorPickerDialog dialog = new ColorPickerDialog(MainActivity.this);
                        dialog.setOnColorSelectedListener(new ColorPickerDialog.OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int color) {
                                canvasView.setPaintColor(color);
                            }
                        });
                        dialog.show();
                    }
                });

            }
        });

        widthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new SeekBarDialog
                SeekBarDialog dialog = new SeekBarDialog(MainActivity.this, "Select stroke width", 1, 50, (int) canvasView.getStrokeWidth());

                // Set a listener to update the stroke width when the user changes the value on the SeekBar
                dialog.setOnSeekBarChangeListener(new SeekBarDialog.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        // Update the stroke width of the canvas view
                        canvasView.setStrokeWidth(progress);
                    }
                });


                // Set a listener to handle the "OK" button
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing - the stroke width has already been updated
                    }
                });

                // Set a listener to handle the "Cancel" button
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Restore the previous stroke width of the canvas view
                        canvasView.setStrokeWidth(canvasView.getPreviousStrokeWidth());
                    }
                });

                // Show the SeekBarDialog
                dialog.show();
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Save Signature");
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setHint("Enter filename");
                builder.setView(input);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Save the signature as a PNG file
                        String filename = input.getText().toString();
                        if (!TextUtils.isEmpty(filename)) {
                            Bitmap bitmap = canvasView.getBitmap();

                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
                            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
                            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            OutputStream fos = null;
                            try {
                                fos = getContentResolver().openOutputStream(uri);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                Toast.makeText(MainActivity.this, "Signature saved to gallery", Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "Error saving signature", Toast.LENGTH_LONG).show();
                            } finally {
                                if (fos != null) {
                                    try {
                                        fos.flush();
                                        fos.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Please enter a filename", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });



        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the canvas view's canvas
                canvasView.clear();
            }
        });
    }

    // Handle the permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, do something
            } else {
                // Permission denied, show a message or disable functionality
            }
        }
    }

}
