package com.eurismar.clientgcmintegracao;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class MensagensActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mensagens);
		TextView tv = (TextView)findViewById(R.id.txtMensagem);
		Intent it = getIntent();
		Bundle params = it.getExtras();
		String mensagem = "";
		if(params != null)
			mensagem = params.getString("msg");
		else
			mensagem = params.getString("Nenhuma mensagem recebida!");
		tv.setText(mensagem);

	}


}
