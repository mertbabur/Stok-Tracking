package com.example.stoktakip.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stoktakip.Adapters.ExpenseAdapter;
import com.example.stoktakip.Models.AdditionalExpense;
import com.example.stoktakip.R;
import com.example.stoktakip.Utils.TimeClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdditionalExpenseFragment extends Fragment {

    private Toolbar toolbar_additionalExpense;
    private RecyclerView recyclerView_additionalExpense;
    private FloatingActionButton floatingActionButton_additionalExpense;
    private TextInputEditText textInputEditText_expenseAbout, textInputEditText_expenseQuantity;
    private RadioGroup radioGroup_expense;
    private RadioButton radioButton_fuelExpense, radioButton_rentExpense, radioButton_employeeCost
                        , radioButton_taxExpense, radioButton_other;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private FirebaseAuth mAuth;

    private String USER_UID;

    private ExpenseAdapter adapter;

    private List<AdditionalExpense> expenseList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_additional_expense_design, container, false);

        defineAttributes(rootView);
        actionAttributes();
        getExpenseFromDB();
        defineToolbar();

        return rootView;

    }

    /**
     * Gorsel nesneler burada baglanir .
     * @param rootView
     */
    public void defineAttributes(View rootView){

        toolbar_additionalExpense = rootView.findViewById(R.id.toolbar_additionalExpense);
        recyclerView_additionalExpense = rootView.findViewById(R.id.recyclerView_additionalExpense);
        floatingActionButton_additionalExpense = rootView.findViewById(R.id.floatingActionButton_additionalExpense);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mAuth = FirebaseAuth.getInstance();

        USER_UID = mAuth.getUid();

        expenseList = new ArrayList<>();

    }


    /**
     * Gorsel nesnelerin actionlari burada tetiklenir .
     */
    public void actionAttributes(){

        // ek gider ekleme ..
        floatingActionButton_additionalExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createAlertViewForExpense();

            }
        });

    }

    /**
     * Toolbari tanimlar .
     */
    public void defineToolbar(){

        toolbar_additionalExpense.setTitle("Ek Giderler");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar_additionalExpense);

    }

    /**
     * AlertView olusturur .
     * saveExpenseToDB metodunu cagirir . --->  gideri db ye kaydeder .
     */
    public void createAlertViewForExpense(){

        View desing = getLayoutInflater().inflate(R.layout.alertview_additional_expense_design, null);

        textInputEditText_expenseAbout = desing.findViewById(R.id.textInputEditText_expenseAbout);
        textInputEditText_expenseQuantity = desing.findViewById(R.id.textInputEditText_expenseQuantity);
        radioGroup_expense = desing.findViewById(R.id.radioGroup_expense);
        radioButton_fuelExpense = desing.findViewById(R.id.radioButton_fuelExpense);
        radioButton_rentExpense = desing.findViewById(R.id.radioButton_rentExpense);
        radioButton_employeeCost = desing.findViewById(R.id.radioButton_employeeCost);
        radioButton_taxExpense = desing.findViewById(R.id.radioButton_taxExpense);
        radioButton_other = desing.findViewById(R.id.radioButton_other);

        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(getActivity());

        alertDialogbuilder.setTitle("EK GİDER EKLE");
        alertDialogbuilder.setMessage("Lütfen alınacak miktarı geçmeyecek şekilde tutarı giriniz ." );
        alertDialogbuilder.setIcon(R.drawable.add_icon);

        alertDialogbuilder.setView(desing);

        alertDialogbuilder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (isFilledAttributes()) {
                    saveExpenseToDB();
                    Log.e("sdasdasdas:", "12312" + String.valueOf(isFilledAttributes()));

                }
                else
                    Toast.makeText(getActivity(), "Lütfen ek gider miktarını ve/ veya ne olduğunu giriniz .", Toast.LENGTH_SHORT).show();

            }
        });

        alertDialogbuilder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });


        alertDialogbuilder.create().show();
    }


    /**
     * Radio Group taki secileni dondurur.
     * Bos birakilamaz .
     * @return --> String whichExpense .
     */
    public String setSelected_typeExpense(){

        int selectId = radioGroup_expense.getCheckedRadioButtonId();
        switch (selectId){
            case R.id.radioButton_fuelExpense:
                return "fuelExpense";
            case R.id.radioButton_rentExpense:
                return "rentExpense";
            case R.id.radioButton_employeeCost:
                return "employeeCost";
            case  R.id.radioButton_taxExpense:
                return "taxExpense";
            case R.id.radioButton_other:
                return "other";
            default:
                return null;
        }
    }

    /**
     * Ek gider i UserExpenses DB sine ekler .
     * updateTotalExpense metodunu cagirir. --> totalExpense i gunceller .
     */
    public void saveExpenseToDB(){

        final String expenseKey = UUID.randomUUID().toString();
        String expenseAbout = textInputEditText_expenseAbout.getText().toString();
        final String expenseQuantity = textInputEditText_expenseQuantity.getText().toString();
        String date = TimeClass.getDate();
        String clock = TimeClass.getClock();
        final String expenseType = setSelected_typeExpense();

        if (expenseType == null) { // Eger expense type secilmedi ise . devam etme ...
            Toast.makeText(getActivity(), "Lütfen ek gider türünü seçiniz .", Toast.LENGTH_SHORT).show();
            return;
        }

        AdditionalExpense additionalExpense = new AdditionalExpense(expenseKey, expenseType, expenseAbout, expenseQuantity, date + "/" + clock);

        myRef.child("UserExpenses"). child(USER_UID).child(expenseKey).setValue(additionalExpense).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(getActivity(), "Ek Gider Başarıyla Eklendi .", Toast.LENGTH_SHORT).show();
                    updateTotalExpense("totalExpense", Float.valueOf(expenseQuantity));
                    updateTotalExpense("totalAdditionalExpense", Float.valueOf(expenseQuantity));
                    updateExpenseTypeQuantityToDB(expenseType, Float.valueOf(expenseQuantity));
                }

            }
        });

    }

    /**
     * Gider turune gore toplam gider i guncller .
     * updateTotalExpense metodunu cagirir . --> guncelleme isini yapar .
     * @param expenseType
     * @param expenseQuantity
     */
    public void updateExpenseTypeQuantityToDB(String expenseType, Float expenseQuantity ){

        String whichAtt;
        if (expenseType.equals("taxExpense"))
            whichAtt = "totalTaxExpense";
        else if (expenseType.equals("other"))
            whichAtt = "totalOtherExpense";
        else if (expenseType.equals("rentExpense"))
            whichAtt = "totalRentExpense";
        else if (expenseType.equals("employeeCost"))
            whichAtt = "totalEmployeeCost";
        else
            whichAtt = "totalFuelExpense";

        updateTotalExpense(whichAtt, expenseQuantity);

    }

    /**
     * Yeni ek girildikten sonra CashDesk DB sindeki totalExpense guncellenir .
     * @param whichAttribute
     * @param expenseQuantity
     */
    public void updateTotalExpense(final String whichAttribute, final Float expenseQuantity){

        myRef.child("CashDesk").child(USER_UID).child(whichAttribute).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Float totalExpense = Float.valueOf(snapshot.getValue().toString());
                totalExpense += expenseQuantity;

                myRef.child("CashDesk").child(USER_UID).child(whichAttribute).setValue(String.valueOf(totalExpense));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /**
     * gerekli attributeler dolduruldu mu ?
     * @return --> Doldu ise --> True , Dolmadi ise --> False
     */
    public boolean isFilledAttributes(){

        if (!textInputEditText_expenseAbout.getText().toString().equals("") && !textInputEditText_expenseQuantity.getText().toString().equals(""))
            return true;

        return false;
    }

    /**
     * RecyclerView tanimlar .
     */
    public void defineRecyclerView(){

        recyclerView_additionalExpense.setHasFixedSize(true);
        recyclerView_additionalExpense.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ExpenseAdapter(getActivity(), expenseList, USER_UID);
        recyclerView_additionalExpense.setAdapter(adapter);

    }

    /**
     * UserExpenses DB sinden giderleri expensesListe atar ve defineRecyclerView cagirir .
     */
    public void getExpenseFromDB(){

        myRef.child("UserExpenses").child(USER_UID).orderByChild("expenseDate").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                AdditionalExpense additionalExpense = snapshot.getValue(AdditionalExpense.class);
                expenseList.add(additionalExpense);
                defineRecyclerView();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
