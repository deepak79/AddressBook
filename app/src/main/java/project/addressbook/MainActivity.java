package project.addressbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static RecyclerView RecyclerViewAddressBook;
    public static RecyclerAdapterAddressBook recyclerAdapterAddressBook;
    public static List<AddressStruct> ListAddress = new ArrayList<AddressStruct>();
    TextView tvNothing;
    Button btnAddContact, btnDeleteAllContacts;
    private DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerViewAddressBook = (RecyclerView) findViewById(R.id.RecyclerViewAddressBook);
        tvNothing = (TextView) findViewById(R.id.tvNothing);
        btnAddContact = (Button) findViewById(R.id.btnAddContact);
        btnDeleteAllContacts = (Button) findViewById(R.id.btnDeleteAllContacts);

        final LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerViewAddressBook.setLayoutManager(llm);

        mydb = new DBHelper(this);

        btnDeleteAllContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ListAddress.size() > 0) {
                    alertView("Are your sure you want to delete all your contacts?");
                } else {
                    Toast.makeText(MainActivity.this, "Nothing to delete!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ListAddress = mydb.getAllContacts();
        if (ListAddress.size() != 0) {

            Toast.makeText(MainActivity.this, "Total " + mydb.numberOfRows() + " Contacts found!", Toast.LENGTH_SHORT).show();

            recyclerAdapterAddressBook = new RecyclerAdapterAddressBook(getApplicationContext(), ListAddress);
            RecyclerViewAddressBook.setAdapter(recyclerAdapterAddressBook);
            recyclerAdapterAddressBook.notifyDataSetChanged();
        } else {
            tvNothing.setVisibility(View.VISIBLE);
        }

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddContact.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

    private void alertView(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

        dialog.setTitle("Confirm Delete")
                .setMessage(message)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        if (mydb.deleteAll() == true) {
                            ListAddress.clear();
                            recyclerAdapterAddressBook.notifyDataSetChanged();
                            tvNothing.setVisibility(View.VISIBLE);
                            Toast.makeText(MainActivity.this, "Successfully deleted all your contacts!", Toast.LENGTH_SHORT).show();

                        }
                    }
                }).show();
    }

}
