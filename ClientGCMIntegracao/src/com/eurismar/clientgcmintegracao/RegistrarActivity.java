package com.eurismar.clientgcmintegracao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class RegistrarActivity extends Activity {
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "1";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	private static final String TAG = "GCM";
	String SENDER_ID = "494593702108";
	Context context;
	GoogleCloudMessaging gcm;
	String regid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registrar);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.registrar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.menuPrincipal) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			return true;
		}
		if (id == R.id.cancelarRegistro) {
			cancelarRegistroGCM();			
		}
		return super.onOptionsItemSelected(item);
	}

	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_registrar,
					container, false);
			return rootView;
		}
	}

	public void onClick(View v) {		
		if (v.getId() == R.id.btnRegistrar) {
			context = getApplicationContext();			
			if (checkPlayServices()) {				
				gcm = GoogleCloudMessaging.getInstance(this);				
				regid = getRegistrationId(context);				
				if (regid.isEmpty()) {				
					registerInBackground();					
				}
			} else {				
				Toast t = Toast.makeText(getApplicationContext(),
						"Serviço Google Play não encontrado!",
						Toast.LENGTH_LONG);
				t.show();
			}
		}			
		gravarDadosEmTxt(regid);
	}

	public void Mensagem(String titulo, String texto) {
		AlertDialog.Builder mensagem = new AlertDialog.Builder(this);
		mensagem.setTitle(titulo);
		mensagem.setMessage(texto);
		mensagem.setNeutralButton("OK", null);
		mensagem.show();
	}

	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(SENDER_ID);
					Log.d("GCM", "Aplicativo registrado:"+ regid);
					msg = "Dispositivo Registrado, Registro ID=" + regid;
					storeRegistrationId(context, regid);
				} catch (IOException ex) {
					msg = "Erro ao registrar :" + ex.getMessage();					
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {	
				Log.i(TAG, msg);	
				gravarDadosEmTxt(msg);

			}
		}.execute(null, null, null);
	}

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {		
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	private SharedPreferences getGCMPreferences(Context context) {
		return getSharedPreferences(MainActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registro não encontrado");
			return "";
		}
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {			
			return null;
		}
		Log.i(TAG, registrationId);
		return registrationId;
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "Dispositivo não suportado");
				finish();
			}
			return false;
		}
		return true;
	}

	private void cancelarRegistroGCM() {

		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}			
					Log.i(TAG, "AKI VAI CANCELAR O REGISTRO GCM");
					gcm.unregister();
					Log.i(TAG, "ACABOU DE CANCELAR");
				} catch (IOException ex) {
					msg = "Erro" + ex.getMessage();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				limparDados();
			}
		}.execute();
	}
	private void limparDados(){
		final SharedPreferences prefs = getGCMPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.clear();
		editor.commit();			
		Mensagem("Atenção", "DADOS DO REGISTRO FORAM LIMPADOS!");
	}
	public void gravarDadosEmTxt(String txt){
		EditText txtNomeArq = (EditText)findViewById(R.id.edtUSuario);
		String lstrNomeArq;
		File arq;
		byte[] dados;
		try {
			lstrNomeArq = "gcm_config_" + txtNomeArq.getText().toString() + ".txt";

			arq = new File(Environment.getExternalStorageDirectory(),
					lstrNomeArq);
			FileOutputStream fos;
			fos = new FileOutputStream(arq);
			fos.write(txt.toString().getBytes());
			fos.flush();
			fos.close();
			Mensagem("Informação","Dispositivo registrado com sucesso! Leia o texto abaixo");
		    TextView tvInfo = (TextView)findViewById(R.id.txtInfo);
			tvInfo.setText("foi gravado o arquivo:\n" + lstrNomeArq +  "\n" 
		    + " no diretório:\n" + Environment.getExternalStorageDirectory().toString()
		    + "\n"
		    + "\n"
		    + "copie este arquivo e cole em um diretório no seu computador de onde será enviado a mensagem"
		    + "\n"
		    + "Este arquivo contém as informações necessárias para o computador saber qual dispositivo irá receber as mensagens"
					);
			
			//Listar();
		} catch (Exception e) {		
			Mensagem("Erro ao gravar configurações", e.getMessage());
		}		
		
	}
	private String ObterDiretorio() {
		File root = android.os.Environment.getExternalStorageDirectory();
		return root.toString();
	}	
	
}
