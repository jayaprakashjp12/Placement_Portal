package com.group6.placementportal;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Student_Register extends AppCompatActivity {

    private DatabaseReference ref;
    private com.group6.placementportal.DatabasePackage.Student user;

    private EditText full_name_V;
    private EditText dept_V;
    private EditText prog_V;
    private Spinner gender_V;
    private EditText roll_no_V;
    private EditText year_of_graduation_V;
    private EditText contact_V;
    private EditText webmail_V;
    private EditText cpi_V;
    private Button btn_save_V;
    private EditText password_V;

    private String full_name;
    private String dept;
    private String prog;
    private String gender;
    private String roll_no;
    private String year_of_graduation;
    private String contact;
    private String webmail;
    private String cpi;
    private String password;

    public static boolean isNumeric(String str)
    {
        for (char c : str.toCharArray())
        {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(isNetworkAvailable()==false){
            Toast.makeText(Student_Register.this,"NO INTERNET CONNECTION", Toast.LENGTH_LONG).show();
            return;
        }


        //Taking Views From the screen

        full_name_V = findViewById(R.id.first_name_text);
        dept_V = findViewById(R.id.dept_text);
        prog_V = findViewById(R.id.prog_text);
        gender_V = findViewById(R.id.gender_spinner);
        roll_no_V = findViewById(R.id.roll_no_text);
        year_of_graduation_V = findViewById(R.id.year_text);
        contact_V = findViewById(R.id.contact_text);
        webmail_V = findViewById(R.id.webmail_text);
        cpi_V = findViewById(R.id.cpi_text);
        password_V=findViewById(R.id.password_text);
        btn_save_V = findViewById(R.id.btn_savechanges);

        user=new com.group6.placementportal.DatabasePackage.Student();
        Intent intent = getIntent();

        String  fullName= intent.getStringExtra("fullName");
        String Webmail = intent.getStringExtra("Webmail");
        final String rollNo=intent.getStringExtra("rollNo");
        String programme=intent.getStringExtra("programme");
        full_name_V.setText(fullName);
        webmail_V.setText(Webmail);
        roll_no_V.setText(rollNo);
        prog_V.setText(programme);
        full_name_V.setEnabled(false);
        webmail_V.setEnabled(false);
        roll_no_V.setEnabled(false);
        prog_V.setEnabled(false);

        btn_save_V.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                full_name = full_name_V.getText().toString();
                dept = dept_V.getText().toString();
                prog = prog_V.getText().toString();
                gender = gender_V.getSelectedItem().toString();
                roll_no = roll_no_V.getText().toString();
                year_of_graduation = year_of_graduation_V.getText().toString();
                contact = contact_V.getText().toString();
                webmail = webmail_V.getText().toString();
                cpi = cpi_V.getText().toString();


                password=password_V.getText().toString();
                //FULL NAME CHECK
                if (full_name.isEmpty()) {
                    full_name_V.setError("Enter your Full Name");
                    return;
                }
                //PROGRAMME CHECK
                if (prog.isEmpty()) {
                    prog_V.setError("Enter your Programme");
                    return;
                }
                //CONTACT CHECK
                if (contact.isEmpty()) {
                    contact_V.setError("Enter your Contact No");
                    return;
                }
                //DEPT CHECK
                if (dept.isEmpty()) {
                    dept_V.setError("Enter your Department");
                    return;
                }



                //ROLL NO CHECK
                if (roll_no.isEmpty()) {
                    roll_no_V.setError("Enter your Roll No");
                    return;
                }
                if(isNumeric(roll_no)==false){
                    roll_no_V.setError("Roll No. can contain only digits");
                    return;
                }
                if(roll_no.length()!=9){
                    roll_no_V.setError("Roll No. should be of 9 digits");
                    return;
                }

                //YEAR OF GRADUATION CHECK
                if (year_of_graduation.isEmpty()) {
                    year_of_graduation_V.setError("Enter your Year of Graduation");
                    return;
                }
                if(year_of_graduation.length()!=4 || isNumeric(year_of_graduation)==false){
                    year_of_graduation_V.setError("Invalid year");
                    return;
                }


                if(isNumeric(contact)==false){
                    contact_V.setError("Contact No. can contain only digits");
                    return;
                }
                if(contact.length()!=10){
                    contact_V.setError("Contact No. should be of 10 digits");
                    return;
                }


                //CPI CHECKS
                if (cpi.isEmpty()) {
                    cpi_V.setError("Enter your CPI");
                    return;
                }
                if(cpi.matches("\\d*\\.?\\d+")==false && isNumeric(cpi)==false){
                    cpi_V.setError("Invalid CPI");
                    return;
                }
                double cpi_double=Double.parseDouble(cpi);
                if(cpi_double>10 || cpi_double<0){
                    cpi_V.setError("Invalid CPI");
                    return;
                }
                //PASSWORD CHECK
                if (password.isEmpty()) {
                    password_V.setError("Enter your Password");
                    return;
                }
                //CONVERT YEAR TO INT
                int year_int=Integer.parseInt(year_of_graduation);

                //FILL STUDENT OBJECT WITH DATA
                user.setFullName(full_name);
                user.setDepartment(dept);
                user.setWebmailID(webmail);
                user.setContact(contact);
                user.setRollNo(roll_no);
                user.setGender(gender);
                user.setCPI(cpi_double);
                user.setProgramme(prog);
                user.setYearOfGraduation(year_int);
                user.setPassword(password);
                //INSERT STUDENT DATA TO FIREBASE
                ref=FirebaseDatabase.getInstance().getReference("Student");
                ref.child(webmail).setValue(user);
                //NEW ACTIVITY
                Intent I = new Intent(Student_Register.this, Student_Dashboard.class);
                I.putExtra("user",user);
                startActivity(I);
            }
        });

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}