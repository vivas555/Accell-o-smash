package com.sensors.philippe.sensorstest.Vue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sensors.philippe.sensorstest.Modele.Collision;
import com.sensors.philippe.sensorstest.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.sensors.philippe.sensorstest.R.layout.list_view;

public class CollisionHistory extends AppCompatActivity {

    private static final String COLLISIONS ="COLLISIONS" ;
    public static final String SAVED_COLLISIONS_LIST = "COLLISIONS_LIST";
    public static final String ACCOUNT = "ACCOUNT";

    private String accountAsString;

    private List<Collision> collisionsList;
    private List<String> collisionsListFormated = null;
    private ArrayAdapter<String> adapter;
    private ListView listViewToHoldCollision;
//    FragmentManager fragmentManager = getFragmentManager();
//    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collision_history);

        Bundle extras = getIntent().getExtras();
        extras.getString(ACCOUNT);


        if(savedInstanceState == null) {

            if (extras != null) {
                ObjectMapper mapper = new ObjectMapper();
                String collisionsAsString = extras.getString(COLLISIONS);
                accountAsString = extras.getString(ACCOUNT);
                try {
                    collisionsList = mapper.readValue(collisionsAsString,
                            mapper.getTypeFactory().constructCollectionType(ArrayList.class, Collision.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            collisionsListFormated = new ArrayList<>();

            for (int i = 0; i < collisionsList.size(); i++) {
                collisionsListFormated.add(collisionsList.get(i).toString(getResources()));
            }
        }
        else {
            collisionsListFormated = savedInstanceState.getStringArrayList(SAVED_COLLISIONS_LIST);
        }

        if (collisionsListFormated != null) {
            adapter = new ArrayAdapter<String>(this, list_view, collisionsListFormated);

            listViewToHoldCollision = (ListView) findViewById(R.id.collision_list);

            if (listViewToHoldCollision != null) {
                listViewToHoldCollision.setAdapter(adapter);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<String> castedList = (ArrayList<String>) collisionsListFormated;
        outState.putStringArrayList(SAVED_COLLISIONS_LIST, castedList);

    }

    public void onClickBtnBack(View view) {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);

        if (accountAsString != null) {
            Bundle bundle = new Bundle();
            bundle.putString("ACCOUNT", accountAsString);
            intent.putExtras(bundle);

        }

        startActivity(intent);
    }
}
