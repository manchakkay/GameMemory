package com.example.gamememory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static int selectvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectvalue = 1;
    }

    public void buttonExit()
    {
        System.exit(0);
    }

    public void buttonStart()
    {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("size", selectvalue);
        startActivity(intent);
    }

    public void buttonSettings()
    {
        CustomDialogFragment dialog = new CustomDialogFragment();
        dialog.show(getSupportFragmentManager(), "custom");
    }

    public static class CustomDialogFragment extends DialogFragment {

        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int n = 5; // максимальный размер поля
            int k = 2;
            ArrayList<String> arrayList = new ArrayList<>();
            for (int i = 0; i < n; i++){
                String s = String.valueOf(k) + 'x' + k;
                k += 2;
                arrayList.add(s);
            }
            String[] array = arrayList.toArray(new String[0]);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Выберите размер игрового поля");
            builder.setSingleChoiceItems(array, selectvalue, (dialog, which) -> {
                selectvalue = which;
                Log.i("test", String.valueOf(selectvalue));
            });
            builder.setPositiveButton("OK", (dialog, whichButton) -> {

            });
            return builder.create();
        }

    }

}