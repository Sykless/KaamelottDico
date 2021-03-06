package com.fra.kaamelottdico;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.text.HtmlCompat;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD;

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

    private int ONE_DP;
    private int FOUR_DP;
    private int EIGHT_DP;
    private int TWENTY_DP;

    private static final Map<String,Integer> IMAGE_DICTIONARY = new HashMap<>();
    private static final String[] romanDigit = {"0","I","II","III","IV","V","VI"};

    private List<String> characterList = new ArrayList<>();
    private List<List<String>> episodeList = new ArrayList<>();
    private List<String> livreList = new ArrayList<>();

    private List<String> characterFilter = new ArrayList<>();
    private List<String> episodeFilter = new ArrayList<>();
    private List<String> livreFilter = new ArrayList<>();

    private TestAdapter mDbHelper;
    private LinearLayout mainLayout;
    private LinearLayout biggerLayout;
    private ConstraintLayout characterFilterLayout;
    private CustomArrayAdapter characterArrayAdapter;
    private FlexboxLayout flexboxLayout;
    private AutoCompleteTextView characterInput;

    private Button searchButton;
    private Button characterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.deleteDatabase("DICO_KAAMELOTT.db");

        populateImageDictionary();
        populateCharacterList();

        EditText repliqueInput = findViewById(R.id.repliqueInput);
        characterFilterLayout = findViewById(R.id.characterFilterLayout);
        characterInput = findViewById(R.id.characterInput);
        flexboxLayout = findViewById(R.id.characterNamesFilter);
        mainLayout = findViewById(R.id.mainLayout);
        biggerLayout = findViewById(R.id.biggerLayout);
        searchButton = findViewById(R.id.searchButton);
        characterButton = findViewById(R.id.characterButton);

        searchButton.setOnClickListener(view -> refreshMainLayout(repliqueInput.getText().toString()));

        characterButton.setOnClickListener(view -> {
            if (characterFilterLayout.getVisibility() == View.GONE) {
                characterFilterLayout.setVisibility(View.VISIBLE);
            } else {
                characterFilterLayout.setVisibility(View.GONE);
            }
        });

        // Search for repliques on any change in EditText
        repliqueInput.addTextChangedListener(new TextWatcher() {
                 @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
                 @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                 @Override
                 public void afterTextChanged(final Editable input) {
                     if (input.length() >= 3) {
                         searchButton.setAlpha(1);
                         searchButton.setClickable(true);
                     } else {
                         searchButton.setAlpha(0.5f);
                         searchButton.setClickable(false);
                     }
                 }
             }
        );

        // Setup characters list in character filter
        characterArrayAdapter = new CustomArrayAdapter(this,android.R.layout.simple_list_item_1,characterList);
        characterInput.setThreshold(2);
        characterInput.setAdapter(characterArrayAdapter);
        characterInput.setOnItemClickListener((parent, view, pos, id) -> {
            String characterName = characterArrayAdapter.getItem(pos).toString();

            flexboxLayout.addView(createCharaterButton(characterName));
            characterInput.setText("");

            characterList.remove(characterName);
            updateCharacterArray();
        });

        ONE_DP = convertDpToPx(1);
        FOUR_DP = convertDpToPx(4);
        EIGHT_DP = convertDpToPx(8);
        TWENTY_DP = convertDpToPx(20);
    }

    private void refreshMainLayout(String keyword) {
        mDbHelper = new TestAdapter(this);
        mDbHelper.createDatabase();
        mDbHelper.open();

        mainLayout.removeAllViews();

        populateFilters();
        Cursor repliqueCursor = mDbHelper.findRepliqueWithFilters(keyword, characterFilter, episodeFilter, livreFilter);

        while (repliqueCursor.moveToNext()) {
            // Create ConstraintLayout
            ConstraintLayout repliqueLayout = writeRepliqueInfo(repliqueCursor, keyword);

            // Add the info layout to the main layout
            mainLayout.addView(repliqueLayout);
        }

        mDbHelper.close();
    }

    private ConstraintLayout createCharaterButton(String characterName) {

        ONE_DP = convertDpToPx(1);
        FOUR_DP = convertDpToPx(4);
        EIGHT_DP = convertDpToPx(8);
        TWENTY_DP = convertDpToPx(20);

        // Create ConstraintLayout to display character name
        ConstraintLayout characterButton = new ConstraintLayout(this);
        FlexboxLayout.LayoutParams characterButtonParams = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        characterButtonParams.setMargins(0,0,EIGHT_DP,EIGHT_DP);
        characterButton.setLayoutParams(characterButtonParams);
        characterButton.setBackground(getDrawable(R.drawable.rectangle_shape));

        // characterNameText Setup
        TextView characterNameText = new TextView(this);
        characterNameText.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
        characterNameText.setId(View.generateViewId());
        characterNameText.setIncludeFontPadding(false);
        characterNameText.setSingleLine(true);
        characterNameText.setText(characterName);

        // characterNameButton Setup
        ImageButton characterNameButton = new ImageButton(this);
        characterNameButton.setLayoutParams(new ConstraintLayout.LayoutParams(TWENTY_DP, TWENTY_DP));
        characterNameButton.setId(View.generateViewId());
        characterNameButton.setBackground(getDrawable(R.drawable.ic_baseline_cancel_24));

        // Remove button on click
        characterNameButton.setOnClickListener(view -> {
            ViewGroup button = (ViewGroup) view.getParent();
            String characterNameString = ((TextView) button.getChildAt(0)).getText().toString();

            // Remove parent view
            ((ViewGroup) button.getParent()).removeView(button);

            characterList.add(characterNameString);
            updateCharacterArray();
        });

        // Adding view to layout
        characterButton.addView(characterNameText);
        characterButton.addView(characterNameButton);

        // Add constraints between children
        ConstraintSet characterNameConstraints = new ConstraintSet();
        characterNameConstraints.clone(characterButton);

        characterNameConstraints.connect(characterNameText.getId(),ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START,EIGHT_DP);
        characterNameConstraints.connect(characterNameText.getId(),ConstraintSet.END,characterNameButton.getId(),ConstraintSet.START,FOUR_DP);
        characterNameConstraints.connect(characterNameText.getId(),ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM,0);
        characterNameConstraints.connect(characterNameText.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,0);

        characterNameConstraints.connect(characterNameButton.getId(),ConstraintSet.START,characterNameText.getId(),ConstraintSet.END,0);
        characterNameConstraints.connect(characterNameButton.getId(),ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END,ONE_DP);
        characterNameConstraints.connect(characterNameButton.getId(),ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM,ONE_DP);
        characterNameConstraints.connect(characterNameButton.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,ONE_DP);

        characterNameConstraints.applyTo(characterButton);

        return characterButton;
    }

    private ConstraintLayout writeRepliqueInfo(Cursor repliqueCursor, String keyword) {

        String replique = repliqueCursor.getString(DICO_REPLIQUE_REPLIQUE);
        String character = repliqueCursor.getString(DICO_REPLIQUE_PERSONNAGE);
        int livre = repliqueCursor.getInt(DICO_REPLIQUE_LIVRE);
        int episode = repliqueCursor.getInt(DICO_REPLIQUE_EPISODE);

        // Create ConstraintLayout to display every info
        ConstraintLayout infosLayout = new ConstraintLayout(this);
        LinearLayout.LayoutParams infosLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        infosLayoutParams.setMargins(0,FOUR_DP,0,FOUR_DP);
        infosLayout.setLayoutParams(infosLayoutParams);
        infosLayout.setBackgroundColor(getColor(R.color.grey));

        // CharacterImage Setup
        ImageView characterImage = new ImageView(this);

        if (characterExists(character)) {
            characterImage.setImageResource(IMAGE_DICTIONARY.get(character));
        } else {
            characterImage.setImageResource(R.drawable.joueur_de_bonneteau);
        }

        characterImage.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
        characterImage.setId(View.generateViewId());
        characterImage.setAdjustViewBounds(true);

        // RepliqueView Setup
        TextView repliqueView = new TextView(this);
        repliqueView.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        repliqueView.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
        repliqueView.setId(View.generateViewId());
        repliqueView.setIncludeFontPadding(false);

        if (keyword == null) {
            repliqueView.setText(replique);
        } else {
            repliqueView.setText(HtmlCompat.fromHtml(getFormattedReplique(replique,keyword), HtmlCompat.FROM_HTML_MODE_COMPACT));
        }


        // RepliqueView Setup
        TextView repliqueInfosView = new TextView(this);
        repliqueInfosView.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        repliqueInfosView.setTypeface(null, Typeface.ITALIC);
        repliqueInfosView.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
        repliqueInfosView.setId(View.generateViewId());
        repliqueInfosView.setIncludeFontPadding(false);
        repliqueInfosView.setText(character + " - Livre " + romanDigit[livre] + " - Episode " + episode + " : " + getEpisodeName(livre,episode));

        // Adding view to layout
        infosLayout.addView(characterImage);
        infosLayout.addView(repliqueView);
        infosLayout.addView(repliqueInfosView);

        // Add constraints between children
        ConstraintSet characterImageConstraints = new ConstraintSet();
        characterImageConstraints.clone(infosLayout);

        characterImageConstraints.connect(characterImage.getId(),ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START,FOUR_DP);
        characterImageConstraints.connect(characterImage.getId(),ConstraintSet.END,repliqueView.getId(),ConstraintSet.START,0);
        characterImageConstraints.connect(characterImage.getId(),ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM,FOUR_DP);
        characterImageConstraints.connect(characterImage.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,FOUR_DP);
        characterImageConstraints.setVerticalBias(characterImage.getId(),0);
        characterImageConstraints.constrainPercentWidth(characterImage.getId(),0.15F);

        characterImageConstraints.connect(repliqueView.getId(),ConstraintSet.START,characterImage.getId(),ConstraintSet.END,EIGHT_DP);
        characterImageConstraints.connect(repliqueView.getId(),ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END,EIGHT_DP);
        characterImageConstraints.connect(repliqueView.getId(),ConstraintSet.BOTTOM,repliqueInfosView.getId(),ConstraintSet.TOP,0);
        characterImageConstraints.connect(repliqueView.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,FOUR_DP);

        characterImageConstraints.connect(repliqueInfosView.getId(),ConstraintSet.START,characterImage.getId(),ConstraintSet.END,EIGHT_DP);
        characterImageConstraints.connect(repliqueInfosView.getId(),ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END,EIGHT_DP);
        characterImageConstraints.connect(repliqueInfosView.getId(),ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM,FOUR_DP);
        characterImageConstraints.connect(repliqueInfosView.getId(),ConstraintSet.TOP,repliqueView.getId(),ConstraintSet.BOTTOM,FOUR_DP);

        characterImageConstraints.applyTo(infosLayout);

        return infosLayout;
    }

    private String getFormattedReplique(String replique, String keyword) {
        String completeReplique = "";
        String[] separatedReplique = replique.split("(?i)" + keyword);
        List<String> originalKeywords = new ArrayList<>();

        // Find all words matching keyword case insensitive
        for (int i = 0 ; i < replique.length() - keyword.length() + 1 ; i++) {
            String stringToCompare = replique.substring(i,i+keyword.length());

            if (stringToCompare.equalsIgnoreCase(keyword)) {
                originalKeywords.add(stringToCompare);
                i += keyword.length() - 1;
            }
        }

        // Concatenate splitted segmeents with bold keywords
        for (int i = 0 ; i < separatedReplique.length ; i++) {
            completeReplique += separatedReplique[i];

            if (i < separatedReplique.length - 1 || separatedReplique.length == originalKeywords.size()) {
                    completeReplique += "<b>" + originalKeywords.get(i) + "</b>";
            }
        }

        return completeReplique;
    }

    private boolean characterExists(String character) {
        Cursor characterCursor = mDbHelper.findCharacter(character);
        return characterCursor.moveToNext();
    }

    private String getEpisodeName(int livre, int episode) {
        Cursor episodeCursor = mDbHelper.findEpisode(livre, episode);
        return episodeCursor.getString(DICO_EPISODE_EPISODE_NAME);
    }

    private void updateCharacterArray() {
        characterArrayAdapter.clear();
        characterArrayAdapter.addAll(characterList);
        characterArrayAdapter.notifyDataSetChanged();
    }

    private int convertDpToPx(int dpValue) {
        Resources r = this.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, r.getDisplayMetrics());
    }

    private void populateFilters() {
        characterFilter.clear();

        for (int i = 0 ; i < flexboxLayout.getChildCount() ; i++) {
            ViewGroup characterButtonView = (ViewGroup) flexboxLayout.getChildAt(i);
            TextView characterNameView = (TextView) characterButtonView.getChildAt(0);

            characterFilter.add(characterNameView.getText().toString());
        }
    }

    private void populateCharacterList() {
        mDbHelper = new TestAdapter(this);
        mDbHelper.createDatabase();
        mDbHelper.open();

        Cursor cursor = mDbHelper.findAllCharacters();

        while (cursor.moveToNext()) {
            characterList.add(cursor.getString(DICO_PERSONNAGE_PERSONNAGE));
        }

        mDbHelper.close();
    }

    private void populateImageDictionary() {
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
        IMAGE_DICTIONARY.put("Az??nor",R.drawable.azenor);
        IMAGE_DICTIONARY.put("Bateleur",R.drawable.bateleur);
        IMAGE_DICTIONARY.put("Belt",R.drawable.belt);
        IMAGE_DICTIONARY.put("Berg??re",R.drawable.bergere);
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
        IMAGE_DICTIONARY.put("C??sar",R.drawable.cesar);
        IMAGE_DICTIONARY.put("Dagonet",R.drawable.dagonet);
        IMAGE_DICTIONARY.put("Dame de la Sauvegarde",R.drawable.dame_de_la_sauvegarde);
        IMAGE_DICTIONARY.put("Dame des Bois",R.drawable.dame_des_bois);
        IMAGE_DICTIONARY.put("Dame des Pierres",R.drawable.dame_des_pierres);
        IMAGE_DICTIONARY.put("Dame du Feu",R.drawable.dame_du_feu);
        IMAGE_DICTIONARY.put("Dame du Lac",R.drawable.dame_du_lac);
        IMAGE_DICTIONARY.put("Demetra",R.drawable.demetra);
        IMAGE_DICTIONARY.put("Desticius",R.drawable.desticius);
        IMAGE_DICTIONARY.put("Drusilla",R.drawable.drusilla);
        IMAGE_DICTIONARY.put("Duc d???Aquitaine",R.drawable.duc_daquitaine);
        IMAGE_DICTIONARY.put("Duchesse d???Aquitaine",R.drawable.duchesse_daquitaine);
        IMAGE_DICTIONARY.put("Elane",R.drawable.elane);
        IMAGE_DICTIONARY.put("Elias",R.drawable.elias);
        IMAGE_DICTIONARY.put("Evaine",R.drawable.evaine);
        IMAGE_DICTIONARY.put("Falerius",R.drawable.falerius);
        IMAGE_DICTIONARY.put("Ferbach",R.drawable.ferbach);
        IMAGE_DICTIONARY.put("Ferghus",R.drawable.ferghus);
        IMAGE_DICTIONARY.put("Flaccus",R.drawable.flaccus);
        IMAGE_DICTIONARY.put("F??e M??re",R.drawable.fee_mere);
        IMAGE_DICTIONARY.put("Galessin",R.drawable.galessin);
        IMAGE_DICTIONARY.put("Gauvain",R.drawable.gauvain);
        IMAGE_DICTIONARY.put("Glaucia",R.drawable.glaucia);
        IMAGE_DICTIONARY.put("Goustan",R.drawable.goustan);
        IMAGE_DICTIONARY.put("Gr??d??",R.drawable.grudu);
        IMAGE_DICTIONARY.put("Gueni??vre",R.drawable.guenievre);
        IMAGE_DICTIONARY.put("Guethenoc",R.drawable.guethenoc);
        IMAGE_DICTIONARY.put("Hagop d???Arm??nie",R.drawable.hagop_darmenie);
        IMAGE_DICTIONARY.put("Helvia",R.drawable.helvia);
        IMAGE_DICTIONARY.put("Herveig",R.drawable.herveig);
        IMAGE_DICTIONARY.put("Herv?? de Rinel",R.drawable.herve_de_rinel);
        IMAGE_DICTIONARY.put("Ho??l",R.drawable.hoel);
        IMAGE_DICTIONARY.put("Interpr??te",R.drawable.interprete);
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
        IMAGE_DICTIONARY.put("L??odagan",R.drawable.leodagan);
        IMAGE_DICTIONARY.put("L???Ankou",R.drawable.lankou);
        IMAGE_DICTIONARY.put("Macrinus",R.drawable.macrinus);
        IMAGE_DICTIONARY.put("Madenn",R.drawable.madenn);
        IMAGE_DICTIONARY.put("Manilius",R.drawable.manilius);
        IMAGE_DICTIONARY.put("Ma??tre d???Armes",R.drawable.maitre_darmes);
        IMAGE_DICTIONARY.put("Merlin",R.drawable.merlin);
        IMAGE_DICTIONARY.put("Mevanwi",R.drawable.mevanwi);
        IMAGE_DICTIONARY.put("Ministre Maure",R.drawable.ministre_maure);
        IMAGE_DICTIONARY.put("Morgane",R.drawable.morgane);
        IMAGE_DICTIONARY.put("M??l??agant",R.drawable.meleagant);
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
        IMAGE_DICTIONARY.put("P??re Blaise",R.drawable.pere_blaise);
        IMAGE_DICTIONARY.put("P??re des jumelles",R.drawable.pere_des_jumelles);
        IMAGE_DICTIONARY.put("Robyn",R.drawable.robyn);
        IMAGE_DICTIONARY.put("Roi Burgonde",R.drawable.roi_burgonde);
        IMAGE_DICTIONARY.put("Roparzh",R.drawable.roparzh);
        IMAGE_DICTIONARY.put("R??purgateur",R.drawable.repurgateur);
        IMAGE_DICTIONARY.put("Sallustius",R.drawable.sallustius);
        IMAGE_DICTIONARY.put("Servius",R.drawable.servius);
        IMAGE_DICTIONARY.put("Sven",R.drawable.sven);
        IMAGE_DICTIONARY.put("S??friane",R.drawable.sefriane);
        IMAGE_DICTIONARY.put("S??li",R.drawable.seli);
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