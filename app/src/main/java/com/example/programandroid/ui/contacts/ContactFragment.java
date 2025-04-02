package com.example.programandroid.ui.contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.programandroid.R;
import com.example.programandroid.databinding.FragmentContactBinding;

public class ContactFragment extends Fragment {

    private FragmentContactBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ContactViewModel contactViewModel =
                new ViewModelProvider(this).get(ContactViewModel.class);

        binding = FragmentContactBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        contactViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        ImageView imageView = root.findViewById(R.id.imageView3);  // Тук използваме root
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("https://maps.app.goo.gl/VBaia69SwVRvTvae7");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                // Премахнете setPackage и оставете Android да избере подходящото приложение
                if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(requireContext(), "Google Maps не е инсталиран!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        ImageView imageView2 = root.findViewById(R.id.imageView2);  // Тук използваме root
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("https://maps.app.goo.gl/TTaDwHhVudDnxUcCA");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                // Премахнете setPackage и оставете Android да избере подходящото приложение
                if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(requireContext(), "Google Maps не е инсталиран!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    /*public void onClick(View view) {
        Uri gmmIntentUri = Uri.parse("https://maps.app.goo.gl/VBaia69SwVRvTvae7");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(requireContext(), "Google Maps не е инсталиран!", Toast.LENGTH_SHORT).show();
        }
    }
*/

}