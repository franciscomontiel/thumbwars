package com.thumbwars.game.android;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.thumbwars.game.Messages;
import com.thumbwars.game.ThumbWarsGame;

import java.io.IOException;
import java.net.InetAddress;

public class DiscoveryActivity extends Activity {

    private Button btnCrear;
    private Button btnUnirse;
    private TextView txtResultado;
    private Button btnBuscarHost;
    private Button btnSiguiente;
    private EditText txtNombre;
    private EditText txtJugador;
    public InetAddress ipObtenida;
    public Client cliente;
    public Server server;
    public String nombre;
    public Switch equipoSwitch;
    public int equipo =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);
        btnCrear = (Button) findViewById(R.id.btnCrear);
        btnUnirse = (Button) findViewById(R.id.btnUnirse);
        txtResultado = (TextView) findViewById(R.id.txtRespuesta);
        txtNombre = (EditText)findViewById(R.id.txtNombre);
        txtJugador = (EditText)findViewById(R.id.txtJugador);
        equipoSwitch = (Switch)findViewById(R.id.switch1);
        //txtResultado.setText("Listo para conectar");
        btnSiguiente = (Button) findViewById(R.id.btnSiguiente);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiscoveryActivity.this, AndroidLauncher.class);
                Bundle b = new Bundle();
                //b.putSerializable(ThumbWarsGame.IP, DiscoveryActivity.this.ipObtenida);
                b.putBoolean(ThumbWarsGame.ONEPLAYER,true);
                b.putBoolean(ThumbWarsGame.ISSERVER, true);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        equipoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    equipo = 2;
                } else {
                    equipo = 1;
                }

            }
        });

        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        txtResultado.setText("Listo para conectar, Mi IP:" + ip);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btnBuscarHost = (Button) findViewById(R.id.btnBuscarHost);
        btnBuscarHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client client = new Client();
                InetAddress address = null;
                Kryo kryo = client.getKryo();
                //kryo.register(ThumbWarsGame.class);
                client.start();
                address = client.discoverHost(54777, 5000);
                txtResultado.setText("Host encontrado en : " + address);
                //ip del servidor
                ipObtenida = address;
                client.close();
            }
        });


        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResultado.setText("Hosteando partida! Espera a que se una alguien!");
                startGame(ipObtenida, true);
            }
        });

        btnUnirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(ipObtenida, false);
            }
        });}

    public void startGame(InetAddress ipObtenida,boolean server){
        nombre = txtNombre.getText().toString();
        Intent intent = new Intent(DiscoveryActivity.this, AndroidLauncher.class);
        Bundle b = new Bundle();
        b.putSerializable(ThumbWarsGame.IP, ipObtenida);
        b.putBoolean(ThumbWarsGame.ISSERVER, server);
        b.putString(ThumbWarsGame.PLAYERNAME, nombre);
        b.putInt(ThumbWarsGame.TEAM, equipo);
        b.putInt(ThumbWarsGame.NUMBERPLAYER,Integer.parseInt(txtJugador.getText().toString()));
        intent.putExtras(b);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_discovery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}