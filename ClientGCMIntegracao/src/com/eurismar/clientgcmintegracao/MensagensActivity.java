package com.eurismar.clientgcmintegracao;



import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MensagensActivity extends Activity {
	Button btnAtualizar;
	Button btnFechar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mensagens);				
		setTitle("Integração UFG");
		atualizaDadosMensagem();
		btnFechar = (Button)findViewById(R.id.btnFechar);
				
		btnFechar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
					finish();					
				
			}
		});
		
		btnAtualizar = (Button)findViewById(R.id.btnAtualizar);
		btnAtualizar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				atualizaDadosMensagem();
				
			}
		});
	}
	
	@Override
	protected void onStart() {	   
		super.onStart();
		atualizaDadosMensagem();
	}

	@Override
	protected void onRestart() {		
		super.onRestart();
		atualizaDadosMensagem();
	}

	public void atualizaDadosMensagem(){
		Intent intent = getIntent();
		TextView tvAssunto = (TextView) findViewById(R.id.tvAssunto);
		TextView tvAutor = (TextView) findViewById(R.id.tvAutor);
		TextView tvMensagem = (TextView) findViewById(R.id.tvMsg);
		tvAssunto.setText(intent.getStringExtra("assunto"));
		tvAutor.setText(intent.getStringExtra("autor"));
		tvMensagem.setText(intent.getStringExtra("mensagem"));				
				
	}

}
