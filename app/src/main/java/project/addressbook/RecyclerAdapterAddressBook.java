package project.addressbook;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by deepakgavkar on 15/04/16.
 */
public class RecyclerAdapterAddressBook extends RecyclerView.Adapter<RecyclerAdapterAddressBook.AddressBookHolder> {
    private List<AddressStruct> addressStructs;
    private Context context;
    View.OnClickListener LinearTouch = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AddressBookHolder holder = (AddressBookHolder) view.getTag();
            int position = holder.getAdapterPosition();

            Intent i = new Intent(context, AddContact.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("id", addressStructs.get(position).getId());
            i.putExtra("flag", "1");
            context.startActivity(i);
        }
    };
    private DBHelper mydb;
    View.OnLongClickListener LinearTouchLong = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            AddressBookHolder holder = (AddressBookHolder) view.getTag();
            final int position = holder.getAdapterPosition();

            final AddressStruct addressStruct = addressStructs.get(position);

            final CharSequence colors[] = new CharSequence[]{"Edit", "Copy Contact", "Delete"};

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Select an action");
            builder.setItems(colors, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        Intent i = new Intent(context, AddContact.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("id", addressStruct.getId());
                        i.putExtra("flag", "0");
                        context.startActivity(i);
                    } else if (which == 1) {
                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Contact number", addressStruct.getMobileno());
                        clipboard.setPrimaryClip(clip);

                        Toast.makeText(context, "Contact number copied to clipboard", Toast.LENGTH_SHORT).show();
                    } else if (which == 2) {
                        alertView("Are you sure you want to delete " + addressStruct.getName() + "from your contacts", Integer.parseInt(addressStruct.getId()), position);
                    }
                }
            });
            builder.show();
            return false;
        }
    };

    public RecyclerAdapterAddressBook(Context context, List<AddressStruct> addressStructs) {
        this.addressStructs = addressStructs;
        this.context = context;
    }

    private void alertView(String message, final int id, final int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setTitle("Confirm Delete")
                .setMessage(message)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        mydb.deleteContact(id);
                        MainActivity.ListAddress.remove(position);
                        MainActivity.recyclerAdapterAddressBook.notifyDataSetChanged();
                        Toast.makeText(context, "Contact deleted successfully!", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    @Override
    public AddressBookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row, null);
        AddressBookHolder fp = new AddressBookHolder(v);
        context = parent.getContext();
        mydb = new DBHelper(context);
        return fp;
    }

    @Override
    public void onBindViewHolder(AddressBookHolder holder, int position) {
        try {
            try {
                AddressStruct addressStruct = addressStructs.get(position);

                holder.LinearTouch.setOnClickListener(LinearTouch);
                holder.LinearTouch.setOnLongClickListener(LinearTouchLong);
                holder.LinearTouch.setTag(holder);

                byte[] decodedString = Base64.decode(addressStruct.getProfile(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.imgProfile.setImageBitmap(decodedByte);

                holder.tvName.setText(addressStruct.getName());
                holder.tvMobileNo.setText(addressStruct.getMobileno());
                holder.tvAddress.setText(addressStruct.getAddress());
                holder.tvEmail.setText(addressStruct.getEmail());

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != addressStructs ? addressStructs.size() : 0);
    }

    public class AddressBookHolder extends RecyclerView.ViewHolder {

        public Context context;
        public TextView tvName, tvMobileNo, tvAddress, tvEmail;
        public CircleImageView imgProfile;
        public LinearLayout LinearTouch;

        public AddressBookHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            this.LinearTouch = (LinearLayout) itemView.findViewById(R.id.LinearTouch);
            this.imgProfile = (CircleImageView) itemView.findViewById(R.id.imgProfile);
            this.tvName = (TextView) itemView.findViewById(R.id.tvName);
            this.tvMobileNo = (TextView) itemView.findViewById(R.id.tvMobileNo);
            this.tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            this.tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
        }
    }

}
