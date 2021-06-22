package com.example.attandanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attandanceapp.Auth.SignUpActivity;
import com.example.attandanceapp.Database.Database;
import com.example.attandanceapp.Database.Firebase;
import com.example.attandanceapp.Utils.ExcelUtils;
import com.example.attandanceapp.Utils.GpsTracker;
import com.example.attandanceapp.Utils.Preferences;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 100;
    private static final int REQUEST_STORAGE = 200;
    private File filePath = new File(Environment.getExternalStorageDirectory() + "/Attendance.xls");
    Button inOut;
    LocationManager locationManager;
    String latitude, longitude;
    TextView username;
    String Date, Time, Day;
    DatabaseReference reference, myRef;
    Button generate;
    MaterialCardView cardView;
    String password;
    int status;

    String completeAddress;
    GpsTracker gpsTracker;
    String[] days = new String[]{"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
    List<User> universityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inOut = findViewById(R.id.in_out_btn);
        cardView = findViewById(R.id.cardView);
        username = findViewById(R.id.username_id);
        generate = findViewById(R.id.generate_btn);
        myRef = Firebase.getInstance().getReference("Attendance");
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateXls();
            }
        });
        reference = FirebaseDatabase.getInstance().getReference();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            getLocation();
        } catch (IOException e) {
            e.printStackTrace();
        }

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStoragePermission();
            }


        });


        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                getLocation();
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }


        username.setText("Welcome " + Preferences.getName(MainActivity.this));

        inOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inOut.setTag(1);
                inOut.setText("In");
                status = (Integer) v.getTag();
                if (status == 1) {
                    uploadDataToFirebase();
                    inOut.setText("Out");
                    v.setTag(0);
                } else {
                    inOut.setText("In");
                    v.setTag(1);
                }
            }
        });
    }


    private void getLocation() throws IOException {
        gpsTracker = new GpsTracker(MainActivity.this);
        if (gpsTracker.canGetLocation()) {
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            addresses = geocoder.getFromLocation(Double.parseDouble(String.valueOf(latitude)), Double.parseDouble(String.valueOf(longitude)), 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String country = addresses.get(0).getCountryName();
            completeAddress = address + " " + " " + city + " " + country;

        } else {
            gpsTracker.showSettingsAlert();
        }
    }


    private void uploadDataToFirebase() {
        User user = new User();
        String key = reference.push().getKey();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss a");
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");

        Time = time.format(calendar.getTime());
        Date = date.format(calendar.getTime());
        Day = days[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        if (status == 0) {

            Preferences.setDate(MainActivity.this, Date);
            Preferences.setDay(MainActivity.this, Day);
            Preferences.setLatitude(MainActivity.this, (latitude));
            Preferences.setLongitude(MainActivity.this, (longitude));
            Preferences.setSignInTime(MainActivity.this, Time);
            Preferences.setSignInAddress(MainActivity.this, completeAddress);
            Preferences.setIsIn(MainActivity.this, status);
            Preferences.setKey(MainActivity.this, key);

        } else {
            user.setDate(Date);
            user.setDay(Day);
            user.setPushId(Preferences.getKey(MainActivity.this));
            user.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
            user.setSinginltd(Preferences.getLatitude(MainActivity.this));
            user.setSinginlng(Preferences.getLongitude(MainActivity.this));
            user.setSinginaddress(Preferences.getSignInAddress(MainActivity.this));
            user.setSingtime(Preferences.getSignInTime(MainActivity.this));
            user.setSignoutaddress(completeAddress);
            user.setPushId(Preferences.getKey(MainActivity.this));
            user.setSignouttime(Time);
            user.setSignout_lng(longitude);
            user.setSingout_ltd(latitude);
            Database.setOutAttendance(MainActivity.this, user);
            Database.signOut(MainActivity.this);
            startActivity(new Intent(MainActivity.this, SignUpActivity.class));
        }

    }

    private void generateXls() {
        List<User> universityList = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                universityList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    User model = postSnapshot.getValue(User.class);
                    universityList.add(model);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

       /* try (HSSFWorkbook workBook = new HSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/Attendance.xls")) {

            // Create the Sheet
            HSSFSheet Sheet = workBook.createSheet("Attendance");
            System.out.println("!!!!!!!!!!!!!!!@##############$$$$$$$$$$$$" + fos);
            // Create the first row corresponding to the header
            Row header = Sheet.createRow(0);
            header.createCell(0).setCellValue("Date");
            header.createCell(1).setCellValue("Day");
            header.createCell(2).setCellValue("Emil");
            header.createCell(3).setCellValue("Name");
            header.createCell(4).setCellValue("PushID");
            header.createCell(5).setCellValue("SignOutAddress");
            header.createCell(6).setCellValue("SignOutTime");
            header.createCell(7).setCellValue("SignInAddress");
            header.createCell(8).setCellValue("SignInLongitude");
            header.createCell(9).setCellValue("SignInLatitude");
            header.createCell(10).setCellValue("SignInTime");
            header.createCell(11).setCellValue("SignOutLatitude");
            header.createCell(12).setCellValue("SignOutLongitude");
            header.createCell(13).setCellValue("UID");


            // Iterate over all the list an create the rows of data
            for (int i = 0; i < universityList.size(); i++) {
                // Create the current starting from 1 to al.size()
                HSSFRow row = Sheet.createRow((short) i + 1);
                row.createCell(0).setCellValue("6/22/2021");
                row.createCell(1).setCellValue("6/22/2021");
                row.createCell(2).setCellValue("6/22/2021");
                row.createCell(3).setCellValue("6/22/2021");
                row.createCell(4).setCellValue("6/22/2021");
                row.createCell(5).setCellValue("6/22/2021");
                row.createCell(6).setCellValue("6/22/2021");
                row.createCell(7).setCellValue("6/22/2021");
                row.createCell(8).setCellValue("6/22/2021");
                row.createCell(9).setCellValue("6/22/2021");
                row.createCell(10).setCellValue("6/22/2021");
                row.createCell(11).setCellValue("6/22/2021");
                row.createCell(12).setCellValue("6/22/2021");
                row.createCell(13).setCellValue("6/22/2021");

            }
            // Write the result into the file
            workBook.write(fos);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        /*createxls*/
       /* HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Custom Sheet");

        HSSFRow hssfRow = hssfSheet.createRow(0);
        HSSFCell hssfCell = hssfRow.createCell(0);

        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        data.put("1", new Object[]{"singinaddress", "singtime", "day", "date", "signoutaddress", "signouttime", "uid", "pushId", "name", "email",
                "singinltd", "singinlng", "singout_ltd", "signout_lng"});
        data.put("2", new Object[]{universityList});


        //Iterate over data and write to sheet
        Set<String> keyset = data.keySet();

        int rownum = 0;
        for (String key : keyset) {
            //create a row of excelsheet
            Row row = hssfSheet.createRow(rownum++);

            //get object array of prerticuler key
            Object[] objArr = data.get(key);

            int cellnum = 0;

            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++);
                if (obj instanceof String) {
                    cell.setCellValue((String) obj);
                } else if (obj instanceof Date) {
                    cell.setCellValue((Date) obj);
                }
            }
            try {
                if (!filePath.exists()) {
                    filePath.createNewFile();
                }

                FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                hssfWorkbook.write(fileOutputStream);

                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/


    }


    public void showDialog() {
        reference.child("Password").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                password = snapshot.getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.passprompt, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);


        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setNegativeButton("Go",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String user_text = (userInput.getText()).toString();

                                if (user_text.equals(password)) {
                                    Log.d(user_text, "Password Matched:)");
                                    CreateExcel();
                                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + filePath);
                                    Toast.makeText(MainActivity.this, "File is generated in your local storage" + filePath, Toast.LENGTH_SHORT).show();


                                } else {
                                    Log.d(user_text, "string is empty");
                                    String message = "The password you have entered is incorrect." + " \n \n" + "Please try again!";
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle("Error");
                                    builder.setMessage(message);
                                    builder.setPositiveButton("Cancel", null);
                                    builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            showDialog();
                                        }
                                    });
                                    builder.create().show();

                                }
                            }
                        })
                .setPositiveButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }

                        }

                );

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }


    private void mLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocation();
                }
            case REQUEST_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // createExcel();
                    Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            showDialog();
            CreateExcel();
            Toast.makeText(MainActivity.this, "You have already granted this permission!",
                    Toast.LENGTH_SHORT).show();
        } else {
            requestStoragePermission();
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE);
        }
    }

    public void CreateExcel() {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Custom Sheet");

        HSSFRow hssfRow = hssfSheet.createRow(0);
        HSSFCell hssfCell = hssfRow.createCell(0);

        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        data.put("1", new Object[]{"ID", "NAME", "LASTNAME"});
        data.put("2", new Object[]{2, "Amit", "Shukla"});
        data.put("3", new Object[]{3, "Lokesh", "Gupta"});
        data.put("4", new Object[]{4, "John", "Adwards"});
        data.put("5", new Object[]{5, "Brian", "Schultz"});


        hssfCell.setCellValue(String.valueOf(data));

        //Iterate over data and write to sheet
/*        Set<String> keyset = data.keySet();

        int rownum = 0;
        for (String key : keyset) {
            //create a row of excelsheet
            Row row = hssfSheet.createRow(rownum++);

            //get object array of particular key
           *//* Object[] objArr = data.get(key);

            int cellnum = 0;

            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++);
                if (obj instanceof String) {
                    cell.setCellValue((String) obj);
                } else if (obj instanceof Integer) {
                    cell.setCellValue((Integer) obj);
                }
            }*//*
        }*/
        try {

            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            System.out.println("#####################" + fileOutputStream);
            hssfWorkbook.write(fileOutputStream);

            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
