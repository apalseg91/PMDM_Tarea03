package dam.pmdm.practicanavigationyoutube;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * A simple {@link Fragment} subclass.
 * Use the factory method to create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    private ImageView imageDetails;
    private TextView idDetails, nameDetails, typeDetails, heightDetails, weightDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        imageDetails = view.findViewById(R.id.imageDetails);
        idDetails = view.findViewById(R.id.idDetails);
        nameDetails = view.findViewById(R.id.nameDetails);
        typeDetails = view.findViewById(R.id.typeDetails);
        heightDetails = view.findViewById(R.id.heightDetails);
        weightDetails = view.findViewById(R.id.weightDetails);

        Button backButton = view.findViewById(R.id.backToTeamButton);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        loadDetails();

        return view;
    }

    private void loadDetails() {
        if (getArguments() != null) {
            Bundle args = getArguments();
            Log.d("DetailsFragment", "Arguments: " + args);

            int id = args.getInt("pokemon_id", -1);
            String name = args.getString("pokemon_name", "Unknown");
            String imageUrl = args.getString("pokemon_image", "");
            String types = args.getString("pokemon_types", "Unknown");
            int height = args.getInt("pokemon_height", -1);
            int weight = args.getInt("pokemon_weight", -1);

            Log.d("DetailsFragment", "Height del bundle: " + height);
            Log.d("DetailsFragment", "Weight: " + weight);

            idDetails.setText("ID: " + id);
            nameDetails.setText(name);
            typeDetails.setText("Types: " + types);
            heightDetails.setText("Height: "+ height);
            weightDetails.setText("Weight: "+weight);


            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.baseline_error_24)
                    .into(imageDetails);
        }
    }
}
