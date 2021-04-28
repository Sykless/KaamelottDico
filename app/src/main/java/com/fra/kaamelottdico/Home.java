package com.fra.kaamelottdico;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {

    private static final int DICO_REPLIQUE_PERSONNAGE = 0;
    private static final int DICO_REPLIQUE_LIVRE = 1;
    private static final int DICO_REPLIQUE_EPISODE = 2;
    private static final int DICO_REPLIQUE_REPLIQUE = 3;
    private static final int DICO_REPLIQUE_REPLIQUE_ID = 4;

    private static final int DICO_PERSONNAGE_PERSONNAGE = 0;

    private static final int DICO_EPISODE_LIVRE = 0;
    private static final int DICO_EPISODE_EPISODE = 1;
    private static final int DICO_EPISODE_EPISODE_NAME = 2;

    private static final Map<String,Integer> IMAGE_DICTIONARY = new HashMap<>();

    private String character = "";
    private String replique = "";
    private int livre;
    private int episode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateIMAGE_DICTIONARY();
        this.deleteDatabase("kaamelottreplique.db");

        TestAdapter mDbHelper = new TestAdapter(this);
        mDbHelper.createDatabase();
        mDbHelper.open();

        Cursor cursor = mDbHelper.findRepliqueWithKeyword("pipi");

        while (cursor.moveToNext()) {
            character = cursor.getString(DICO_REPLIQUE_PERSONNAGE);
            replique = cursor.getString(DICO_REPLIQUE_PERSONNAGE);
            livre = cursor.getInt(DICO_REPLIQUE_LIVRE);
            episode = cursor.getInt(DICO_REPLIQUE_EPISODE);

            //LinearLayOut Setup
            LinearLayout linearLayout= new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));

            //ImageView Setup
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(IMAGE_DICTIONARY.get("Flaccus"));

            // textView.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            // setIncludeFontPadding (boolean includepad)

            imageView.setAdjustViewBounds(true);

            //setting image position
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            //adding view to layout
            linearLayout.addView(imageView);

            //make visible to program
            //setContentView(linearLayout);
        }

        mDbHelper.close();
    }

    private void populateIMAGE_DICTIONARY() {
        IMAGE_DICTIONARY.put("Acheflour",R.drawable.acheflour);
        IMAGE_DICTIONARY.put("Aconia",R.drawable.aconia);
        IMAGE_DICTIONARY.put("Aelis",R.drawable.aelis);
        IMAGE_DICTIONARY.put("Angharad",R.drawable.angharad);
        IMAGE_DICTIONARY.put("Anna",R.drawable.anna);
        IMAGE_DICTIONARY.put("Anton",R.drawable.anton);
        IMAGE_DICTIONARY.put("Ao Si'Ka",R.drawable.ao_si_ka);
        IMAGE_DICTIONARY.put("Arthur",R.drawable.arthur);
        IMAGE_DICTIONARY.put("Attila",R.drawable.attila);
        IMAGE_DICTIONARY.put("Aziliz",R.drawable.aziliz);
        IMAGE_DICTIONARY.put("Azénor",R.drawable.azenor);
        IMAGE_DICTIONARY.put("Bateleur",R.drawable.bateleur);
        IMAGE_DICTIONARY.put("Belt",R.drawable.belt);
        IMAGE_DICTIONARY.put("Bergère",R.drawable.bergere);
        IMAGE_DICTIONARY.put("Berlewen",R.drawable.berlewen);
        IMAGE_DICTIONARY.put("Bohort",R.drawable.bohort);
        IMAGE_DICTIONARY.put("Bohort Ier",R.drawable.bohort_ier);
        IMAGE_DICTIONARY.put("Boniface",R.drawable.boniface);
        IMAGE_DICTIONARY.put("Breccan",R.drawable.breccan);
        IMAGE_DICTIONARY.put("Buzit",R.drawable.buzit);
        IMAGE_DICTIONARY.put("Caius",R.drawable.caius);
        IMAGE_DICTIONARY.put("Calogrenant",R.drawable.calogrenant);
        IMAGE_DICTIONARY.put("Chef Maure",R.drawable.chef_maure);
        IMAGE_DICTIONARY.put("Chef Ostrogoth",R.drawable.chef_ostrogoth);
        IMAGE_DICTIONARY.put("Cordius",R.drawable.cordius);
        IMAGE_DICTIONARY.put("Cryda",R.drawable.cryda);
        IMAGE_DICTIONARY.put("César",R.drawable.cesar);
        IMAGE_DICTIONARY.put("Dagonet",R.drawable.dagonet);
        IMAGE_DICTIONARY.put("Dame de la Sauvegarde",R.drawable.dame_de_la_sauvegarde);
        IMAGE_DICTIONARY.put("Dame des Bois",R.drawable.dame_des_bois);
        IMAGE_DICTIONARY.put("Dame des Pierres",R.drawable.dame_des_pierres);
        IMAGE_DICTIONARY.put("Dame du Feu",R.drawable.dame_du_feu);
        IMAGE_DICTIONARY.put("Dame du Lac",R.drawable.dame_du_lac);
        IMAGE_DICTIONARY.put("Demetra",R.drawable.demetra);
        IMAGE_DICTIONARY.put("Desticius",R.drawable.desticius);
        IMAGE_DICTIONARY.put("Drusilla",R.drawable.drusilla);
        IMAGE_DICTIONARY.put("Duc d’Aquitaine",R.drawable.duc_daquitaine);
        IMAGE_DICTIONARY.put("Duchesse d’Aquitaine",R.drawable.duchesse_daquitaine);
        IMAGE_DICTIONARY.put("Elane",R.drawable.elane);
        IMAGE_DICTIONARY.put("Elias",R.drawable.elias);
        IMAGE_DICTIONARY.put("Evaine",R.drawable.evaine);
        IMAGE_DICTIONARY.put("Falerius",R.drawable.falerius);
        IMAGE_DICTIONARY.put("Ferbach",R.drawable.ferbach);
        IMAGE_DICTIONARY.put("Ferghus",R.drawable.ferghus);
        IMAGE_DICTIONARY.put("Flaccus",R.drawable.flaccus);
        IMAGE_DICTIONARY.put("Fée Mère",R.drawable.fee_mere);
        IMAGE_DICTIONARY.put("Galessin",R.drawable.galessin);
        IMAGE_DICTIONARY.put("Gauvain",R.drawable.gauvain);
        IMAGE_DICTIONARY.put("Glaucia",R.drawable.glaucia);
        IMAGE_DICTIONARY.put("Goustan",R.drawable.goustan);
        IMAGE_DICTIONARY.put("Grüdü",R.drawable.grudu);
        IMAGE_DICTIONARY.put("Guenièvre",R.drawable.guenievre);
        IMAGE_DICTIONARY.put("Guethenoc",R.drawable.guethenoc);
        IMAGE_DICTIONARY.put("Hagop d’Arménie",R.drawable.hagop_darmenie);
        IMAGE_DICTIONARY.put("Helvia",R.drawable.helvia);
        IMAGE_DICTIONARY.put("Herveig",R.drawable.herveig);
        IMAGE_DICTIONARY.put("Hervé de Rinel",R.drawable.herve_de_rinel);
        IMAGE_DICTIONARY.put("Hoël",R.drawable.hoel);
        IMAGE_DICTIONARY.put("Interprète",R.drawable.interprete);
        IMAGE_DICTIONARY.put("Jacca",R.drawable.jacca);
        IMAGE_DICTIONARY.put("Joueur de Bonneteau",R.drawable.joueur_de_bonneteau);
        IMAGE_DICTIONARY.put("Julia",R.drawable.julia);
        IMAGE_DICTIONARY.put("Jurisconsulte",R.drawable.jurisconsulte);
        IMAGE_DICTIONARY.put("Kadoc",R.drawable.kadoc);
        IMAGE_DICTIONARY.put("Karadoc",R.drawable.karadoc);
        IMAGE_DICTIONARY.put("Kay",R.drawable.kay);
        IMAGE_DICTIONARY.put("Ketchatar",R.drawable.ketchatar);
        IMAGE_DICTIONARY.put("Keu",R.drawable.keu);
        IMAGE_DICTIONARY.put("Lancelot",R.drawable.lancelot);
        IMAGE_DICTIONARY.put("Licinia",R.drawable.licinia);
        IMAGE_DICTIONARY.put("Lionel",R.drawable.lionel);
        IMAGE_DICTIONARY.put("Loth",R.drawable.loth);
        IMAGE_DICTIONARY.put("Lumeria",R.drawable.lumeria);
        IMAGE_DICTIONARY.put("Lurco",R.drawable.lurco);
        IMAGE_DICTIONARY.put("Luventius",R.drawable.luventius);
        IMAGE_DICTIONARY.put("Léodagan",R.drawable.leodagan);
        IMAGE_DICTIONARY.put("L’Ankou",R.drawable.lankou);
        IMAGE_DICTIONARY.put("Macrinus",R.drawable.macrinus);
        IMAGE_DICTIONARY.put("Madenn",R.drawable.madenn);
        IMAGE_DICTIONARY.put("Manilius",R.drawable.manilius);
        IMAGE_DICTIONARY.put("Maître d’Armes",R.drawable.maitre_darmes);
        IMAGE_DICTIONARY.put("Merlin",R.drawable.merlin);
        IMAGE_DICTIONARY.put("Mevanwi",R.drawable.mevanwi);
        IMAGE_DICTIONARY.put("Ministre Maure",R.drawable.ministre_maure);
        IMAGE_DICTIONARY.put("Morgane",R.drawable.morgane);
        IMAGE_DICTIONARY.put("Méléagant",R.drawable.meleagant);
        IMAGE_DICTIONARY.put("Narses",R.drawable.narses);
        IMAGE_DICTIONARY.put("Nathair",R.drawable.nathair);
        IMAGE_DICTIONARY.put("Nessa",R.drawable.nessa);
        IMAGE_DICTIONARY.put("Nonna",R.drawable.nonna);
        IMAGE_DICTIONARY.put("Papinius",R.drawable.papinius);
        IMAGE_DICTIONARY.put("Pellinor",R.drawable.pellinor);
        IMAGE_DICTIONARY.put("Perceval",R.drawable.perceval);
        IMAGE_DICTIONARY.put("Pisentius",R.drawable.pisentius);
        IMAGE_DICTIONARY.put("Prisca",R.drawable.prisca);
        IMAGE_DICTIONARY.put("Procyon",R.drawable.procyon);
        IMAGE_DICTIONARY.put("Père Blaise",R.drawable.pere_blaise);
        IMAGE_DICTIONARY.put("Père des jumelles",R.drawable.pere_des_jumelles);
        IMAGE_DICTIONARY.put("Robyn",R.drawable.robyn);
        IMAGE_DICTIONARY.put("Roi Burgonde",R.drawable.roi_burgonde);
        IMAGE_DICTIONARY.put("Roparzh",R.drawable.roparzh);
        IMAGE_DICTIONARY.put("Répurgateur",R.drawable.repurgateur);
        IMAGE_DICTIONARY.put("Sallustius",R.drawable.sallustius);
        IMAGE_DICTIONARY.put("Servius",R.drawable.servius);
        IMAGE_DICTIONARY.put("Sven",R.drawable.sven);
        IMAGE_DICTIONARY.put("Séfriane",R.drawable.sefriane);
        IMAGE_DICTIONARY.put("Séli",R.drawable.seli);
        IMAGE_DICTIONARY.put("Tavernier",R.drawable.tavernier);
        IMAGE_DICTIONARY.put("Tegeirian",R.drawable.tegeirian);
        IMAGE_DICTIONARY.put("Torri",R.drawable.torri);
        IMAGE_DICTIONARY.put("Tumet",R.drawable.tumet);
        IMAGE_DICTIONARY.put("Urgan",R.drawable.urgan);
        IMAGE_DICTIONARY.put("Venec",R.drawable.venec);
        IMAGE_DICTIONARY.put("Verinus",R.drawable.verinus);
        IMAGE_DICTIONARY.put("Vouga",R.drawable.vouga);
        IMAGE_DICTIONARY.put("Ygerne",R.drawable.ygerne);
        IMAGE_DICTIONARY.put("Yvain",R.drawable.yvain);
    }
}