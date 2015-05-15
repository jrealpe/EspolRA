package org.kokoa.espolra;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.kokoa.espolra.R;
import org.kokoa.voice.VoiceRecognition;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	//id of the dialog used to choose between maps and augmented reality
	static final int CHOOSE_METHOD_DIALOG = 1;
	static final int ABOUT_DIALOG = 2;	
	static int CURRENT_FASTFOOD_RESTAURANT = 0;
	static int CURRENT_FASTFOOD_RESTAURANT_IMAGE = 0;
	
	private IconContextMenu chooseActionContextMenu = null;
	private IconContextMenu tiendas_xmenor = null;
	private IconContextMenu restaurantes_verdes = null;
	private IconContextMenu alojamiento_cercano = null;
	private IconContextMenu reciclaje_general = null;
	private IconContextMenu reciclaje_electronico = null;
	private IconContextMenu alojamiento = null;
	
	private final int MENU_ITEM_1_ACTION_MAPS = 1;
	private final int MENU_ITEM_2_ACTION_AUGMENTED_REALITY = 2;
	
	private String GOOGLE_SEARCH_API_CUSTOM = "q=";
	private String VOICE_RECOGNIZED_WORD = "";
	
	private final String URL_SPACE = "%20";
	
	String arUri;
	String mapUri;
	Intent mapCall;
	Uri geoUri;
	
	String opcion_tiendas;
	String opcion_resta;
	String opcion_reci;

	public LocationManager locManager;
	public LocationListener locListener;
	public double mialt;
	public double milat;
	private VoiceRecognition voiceRecognition;
	public static Location l;
	public static int DimensionRadio;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);              
        final Button tiendas = (Button) findViewById(R.id.btn_MainMenu01);
        final Button restaurantes = (Button) findViewById(R.id.btn_MainMenu02);
        final Button reciclaje = (Button) findViewById(R.id.btn_MainMenu03);
        final Button farmacias = (Button) findViewById(R.id.btn_MainMenu04);
        final Button hoteles = (Button) findViewById(R.id.btn_MainMenu05);
        final Button estaciones = (Button) findViewById(R.id.btn_MainMenu06);       
        
        this.voiceRecognition = new VoiceRecognition(this); 	//to recognize voice
        final GPSLocation loc=new GPSLocation(this);
        loc.writeSignalGPS();
        
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Radio");
		alert.setMessage("Ingrese el radio: ");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		
		alert.setView(input);
		
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		

		public void onClick(DialogInterface dialog, int whichButton) {
			 String valor = input.getText().toString();
			 MainActivity.DimensionRadio = Integer.parseInt(valor);
		  // Do something with value!
		  	  
		  }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
			  MainActivity.DimensionRadio = 100;
		  }
		});

		alert.show();					
        
        final CharSequence[] items = {"Mi Comisariato", "Megamaxi", "Tia"};
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Tiendas con productos verdes").setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialogInterface, int item) {
		            //Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
		        	if(items[item].toString().compareTo("Mi Comisariato")==0){
		        		String mi_com="Mi%20Comisariato";
		    		    opcion_tiendas=mi_com;
		        	}else{
		        	opcion_tiendas=items[item].toString();
		        	}
		        	
		            dialogInterface.dismiss();
		            showDialog(CHOOSE_METHOD_DIALOG);
		        }
		    });
		
		final CharSequence[] items2 = {"El gran brocoli", "Plati-Organico", "EL buen vegetariano"};
		final AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
		builder2.setTitle("Restaurantes Verdes").setSingleChoiceItems(items2, 1, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialogInterface, int item) {
		            //Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
		        	if(items[item].toString().compareTo("El gran brocoli")==0){
		        		String mi_com="El%20gran%20brocoli";
		        		opcion_resta=mi_com;
		        	}else if(items[item].toString().compareTo("EL buen vegetariano")==0){
		        		String mi_com="El%20buen%20vegetariano";
		        		opcion_resta=mi_com;
		        	}else{
			        	opcion_resta=items2[item].toString();			        		
		        	}	        	
		            dialogInterface.dismiss();
		            showDialog(CHOOSE_METHOD_DIALOG);
		        }
		    });
		
		final CharSequence[] items3 = {"Fui Reciclado", "Electro-Reciclado", "Reciclajes-eco"};
		final AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
		builder3.setTitle("Sitios de Reciclaje").setSingleChoiceItems(items3, 1, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialogInterface, int item) {
		            //Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
		        	if(items[item].toString().compareTo("Fui Reciclado")==0){
		        		String mi_com="Fui%20Reciclado";
		        		opcion_reci=mi_com;
		        	}else{
		        		opcion_reci=items[item].toString();
		        	}
		            dialogInterface.dismiss();
		            showDialog(CHOOSE_METHOD_DIALOG);
		        }
		    });
        //------------------------------------------------------------------
        //I N I T    T H E    M E N U    W I T H    I T S    E V E N T S
        //------------------------------------------------------------------
        Resources res = getResources();
		chooseActionContextMenu = new IconContextMenu(this, CHOOSE_METHOD_DIALOG);
        chooseActionContextMenu.addItem(res, R.string.choose_action_maps, R.drawable.action_maps, MENU_ITEM_1_ACTION_MAPS);
        chooseActionContextMenu.addItem(res, R.string.choose_action_augmentedreality, R.drawable.action_ar, MENU_ITEM_2_ACTION_AUGMENTED_REALITY);
        
        //set onclick listener for context menu
        chooseActionContextMenu.setOnClickListener(new IconContextMenu.IconContextMenuOnClickListener() {
			@Override
			public void onClick(int menuId) {
				if (menuId == MENU_ITEM_1_ACTION_MAPS){

					Intent i = new Intent(MainActivity.this, WView.class);
    				//i.putExtra("web", "http://kokoa.espol.edu.ec:9090/map");
					startActivity(i);
					/*switch (CURRENT_FASTFOOD_RESTAURANT)
    	    		{
    	    			case 1: //MC DONALDS
    	    				Intent i = new Intent(getApplicationContext(), WView.class);
    	    				i.putExtra("web", "http://kokoa.espol.edu.ec:9090/map");
    						startActivity(i);
    	    				
    	    				break;
    	    			case 2: //BURGER KING
    	    				check_BurgerKing();
    	    				break;
    	    			case 3: //PIZZA HUT
    	    				check_PizzaHut();
    	    				break;
    	    			case 4: //PANS AND COMPANY
    	    				check_PanAndCompany();
    	    				break;
    	    			case 5: //STARBUCKS
    	    				check_Starbucks();
    	    				break;
    	    			case 6: //DUNKIN DONUTS
    	    				check_DunkinDonuts();
    	    				break;
    	    			case 7: //CUSTOM
    	    				check_Custom();
    	    				break;
    	    		}*/					
				}else if (menuId == MENU_ITEM_2_ACTION_AUGMENTED_REALITY){
					
					// Creamos un AlertDialog y lo mostramos


					Intent arview = new Intent();
					arview.setAction(Intent.ACTION_VIEW);
					//arUri = GOOGLE_SEARCH_API_URL;
					switch (CURRENT_FASTFOOD_RESTAURANT)
    	    		{
    	    			case 1: 
    	    				arUri="http://kokoa.espol.edu.ec:9090/positions?type=1";
    	    				break;
    	    			case 2: 
    	    				arUri="http://kokoa.espol.edu.ec:9090/positions?type=2";
    	    				break;
    	    			case 3: 
    	    				arUri="http://kokoa.espol.edu.ec:9090/positions?type=3";
    	    				break;
    	    			case 4:
    	    				arUri="http://kokoa.espol.edu.ec:9090/positions?type=5";
    	    				break;
    	    			case 5: 
    	    				arUri="http://kokoa.espol.edu.ec:9090/positions?type=7";
    	    				break;
    	    			case 6:
    	    				arUri="http://kokoa.espol.edu.ec:9090/positions?type=8";
    	    				//arUri="http://www.kokoa.espol.edu.ec/pruebajs/ra/paraderos.json";
    	    				break;
    	    			case 7:
    	    				arUri += GOOGLE_SEARCH_API_CUSTOM;
    	    				arUri += VOICE_RECOGNIZED_WORD.replace(" ", URL_SPACE);
    	    				break;
    	    		}						
					Log.i("uri", ""+arUri);
					//arUri += GOOGLE_SEARCH_API_QUERY;
					arview.setDataAndType(Uri.parse(arUri), "application/sergio-json");
					arview.putExtra("imageId", CURRENT_FASTFOOD_RESTAURANT_IMAGE);					
					arview.putExtra("loc", loc.currentLocation);
					startActivity(arview);										
				}
			}
        });
        
        
        
        //------------------------------------------------------------------
        //E V E N T S
        //------------------------------------------------------------------
        tiendas.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				CURRENT_FASTFOOD_RESTAURANT = 1;
				CURRENT_FASTFOOD_RESTAURANT_IMAGE = R.drawable.mcdonalds_icon_ar;
				showDialog(CHOOSE_METHOD_DIALOG);
				//builder.create().show();
			}
		});      
        
        restaurantes.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				CURRENT_FASTFOOD_RESTAURANT = 2;
				CURRENT_FASTFOOD_RESTAURANT_IMAGE = R.drawable.burgerking_icon_ar;
				showDialog(CHOOSE_METHOD_DIALOG);
				//builder2.create().show();
			}
		});       
        
        reciclaje.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				CURRENT_FASTFOOD_RESTAURANT = 3;
				CURRENT_FASTFOOD_RESTAURANT_IMAGE = R.drawable.pizzahut_icon_ar;
				showDialog(CHOOSE_METHOD_DIALOG);
				//builder3.create().show();
			}
		});             
        
        farmacias.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				CURRENT_FASTFOOD_RESTAURANT = 4;
				CURRENT_FASTFOOD_RESTAURANT_IMAGE = R.drawable.pansandcompany_icon_ar;
				showDialog(CHOOSE_METHOD_DIALOG);
			}
		});             
        
        hoteles.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				CURRENT_FASTFOOD_RESTAURANT = 5;
				CURRENT_FASTFOOD_RESTAURANT_IMAGE = R.drawable.starbucks_icon_ar;
				showDialog(CHOOSE_METHOD_DIALOG);
			}
		});              
        
        estaciones.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				CURRENT_FASTFOOD_RESTAURANT = 6;
				CURRENT_FASTFOOD_RESTAURANT_IMAGE = R.drawable.dunkindonuts_icon_ar;
				showDialog(CHOOSE_METHOD_DIALOG);
			}
		});                            
    }
    
 // Asignacion de Nombre Area
    public AlertDialog BuilderDialog_Area() {

        final EditText nombre_area = new EditText(MainActivity.this);
        //nombre_area.setPadding(10, 0, 10, 0);

        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Radio");
        alert.setMessage("Escriba radio a divisar: ");
        //alert.setView(nombre_area);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	//this.DimensionRadio = (int)nombre_area.getText().toString();
            	
            }
        });

        alert.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        nombre_area.setText("");
                        dialog.cancel();
                    }
                });

        return alert.create();

    }
    
    //------------------------------------------------------------------
    //A C T I V I T Y    R E S U L T
    //------------------------------------------------------------------
    /**
     * Handle the results from the recognition activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.voiceRecognition.VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it could have heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            
            if (matches.size()>=0){
            	this.VOICE_RECOGNIZED_WORD = matches.get(0).toString();
            	CURRENT_FASTFOOD_RESTAURANT = 7;
            	CURRENT_FASTFOOD_RESTAURANT_IMAGE = R.drawable.custom_icon_ar1;
            	showDialog(CHOOSE_METHOD_DIALOG);
			}
            else{
            	this.VOICE_RECOGNIZED_WORD = "";
            	Toast.makeText(this, getString(R.string.voice_not_results_msg), Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    //------------------------------------------------------------------
    //P R E P A R I N G    T H E    C H O O S E    M E T H O D    D I A L O G
    //------------------------------------------------------------------
    @Override
    protected Dialog onCreateDialog(int id) {
    	Dialog dialog = null;
    	switch (id) {
    		case CHOOSE_METHOD_DIALOG:
    			return chooseActionContextMenu.createMenu(this.getString(R.string.choose_action_title));    			
    		case ABOUT_DIALOG:
    			dialog = createAboutDialog();
    			break;			
    	    default:
    	    	dialog = null;
    	    	break;
    	}
    	return dialog;
    };
    
    
    
    
    //------------------------------------------------------------------
    //P R E P A R I N G    T H E    D I A L O G S
    //------------------------------------------------------------------    
    private AlertDialog createAboutDialog(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(R.string.license_title)
								.setMessage(R.string.license).setIcon(R.drawable.about)
								.setNeutralButton(R.string.about_button, null);
    	AlertDialog alert = builder.create();
    	return alert;
    }
    
    
    //------------------------------------------------------------------
    //P R E P A R I N G    T H E    M E N U
    //------------------------------------------------------------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);		

		// Return true so that the menu gets displayed.
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// Close the menu after a period of time.
		// Note that this STARTS the timer when the options menu is being
		// prepared, NOT when the menu is made visible.
		Timer timing = new Timer();
		timing.schedule(new TimerTask() {

			@Override
			public void run() {
				closeOptionsMenu();
			}
		}, 5000);
		return super.onPrepareOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
			case R.id.voice:
				this.startVoice();
				break;
			case R.id.about:
				showDialog(ABOUT_DIALOG);
				break;
		}		
		return true;
	}
	
    //------------------------------------------------------------------
    //P R I V A T E     M E T H O D S
    //------------------------------------------------------------------
	
	public double getMialt() {
		return mialt;
	}

	public void setMialt(double mialt) {
		this.mialt = mialt;
	}

	public double getMilat() {
		return milat;
	}

	public void setMilat(double milat) {
		this.milat = milat;
	}

	/*	
 * arUri="http://www.kokoa.espol.edu.ec/pruebajs/administrativos.json";
	arUri="http://www.kokoa.espol.edu.ec/pruebajs/aulasyfacultades.json";
	arUri="http://www.kokoa.espol.edu.ec/pruebajs/comedores.json";
	arUri="http://www.kokoa.espol.edu.ec/pruebajs/instalacionesdeportivas.json";
	arUri="http://www.kokoa.espol.edu.ec/pruebajs/laboratorios.json";
	arUri="http://www.kokoa.espol.edu.ec/pruebajs/paraderos.json";
	*/
	private void check_McDonalds() {	
		Bundle bundle = new Bundle();
		bundle.putString("url", "http://www.kokoa.espol.edu.ec/pruebajs/maps/administrativos.json");
		Intent mapCall = new Intent(getApplicationContext(),Maps.class);	
		startActivity(mapCall.putExtras(bundle));
	}
	
	private void check_BurgerKing() {
		Bundle bundle = new Bundle();
		bundle.putString("url", "http://www.kokoa.espol.edu.ec/pruebajs/maps/aulasyfacultades.json");
		Intent mapCall = new Intent(getApplicationContext(),Maps.class);	
		startActivity(mapCall.putExtras(bundle));
	}
	
	private void check_PizzaHut() {
		Bundle bundle = new Bundle();
		bundle.putString("url", "http://www.kokoa.espol.edu.ec/pruebajs/maps/comedores.json");
		Intent mapCall = new Intent(getApplicationContext(),Maps.class);	
		startActivity(mapCall.putExtras(bundle));
	}
	
	private void check_PanAndCompany() {
		Bundle bundle = new Bundle();
		bundle.putString("url", "http://www.kokoa.espol.edu.ec/pruebajs/maps/instalacionesdeportivas.json");
		Intent mapCall = new Intent(getApplicationContext(),Maps.class);	
		startActivity(mapCall.putExtras(bundle));
	}
	
	private void check_Starbucks() {
		Bundle bundle = new Bundle();
		bundle.putString("url", "http://www.kokoa.espol.edu.ec/pruebajs/maps/laboratorios.json");
		Intent mapCall = new Intent(getApplicationContext(),Maps.class);	
		startActivity(mapCall.putExtras(bundle));
	}
	
	private void check_DunkinDonuts() {
		Bundle bundle = new Bundle();
		bundle.putString("url", "http://www.kokoa.espol.edu.ec/pruebajs/maps/paraderos.json");
		Intent mapCall = new Intent(getApplicationContext(),Maps.class);	
		startActivity(mapCall.putExtras(bundle));
	}
	
	private void check_Custom(){
		mapUri = getResources().getString(R.string.map_location_uri_custom) + this.VOICE_RECOGNIZED_WORD;
		geoUri = Uri.parse(mapUri);
		mapCall = new Intent(Intent.ACTION_VIEW, geoUri);	
		startActivity(mapCall);
	}
	
	private void startVoice(){
		if (this.voiceRecognition != null){
			if (this.voiceRecognition.recognitionAvailable()){
				startActivityForResult(this.voiceRecognition.getVoiceRecognitionIntent(getString(R.string.voice_find_msg)), 
									   this.voiceRecognition.VOICE_RECOGNITION_REQUEST_CODE);
			}else{
				Toast.makeText(this, getString(R.string.voice_notavailable_msg), Toast.LENGTH_LONG).show();
			}
		}			
	}
    //------------------------------------------------------------------
    //P U B L I C     M E T H O D S
    //------------------------------------------------------------------
	
}

