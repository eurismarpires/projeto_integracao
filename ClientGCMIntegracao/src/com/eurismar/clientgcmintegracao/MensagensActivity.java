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
		Intent intent = getIntent();
		TextView tvAssunto = (TextView) findViewById(R.id.tvAssunto);
		TextView tvAutor = (TextView) findViewById(R.id.tvAutor);
		TextView tvMensagem = (TextView) findViewById(R.id.tvMsg);
		tvAssunto.setText(intent.getStringExtra("assunto"));
		tvAutor.setText(intent.getStringExtra("autor"));
		tvMensagem.setText(intent.getStringExtra("mensagem"));		

	}


}
