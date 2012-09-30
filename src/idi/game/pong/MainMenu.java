package idi.game.pong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.ImageView;

public class MainMenu extends Activity
{
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      ImageView title = (ImageView) this.findViewById(R.id.title);
      title.setImageResource(R.drawable.title);
      
      Button newGameButton = (Button)this.findViewById(R.id.newGameButton);
      newGameButton.setOnClickListener(new OnClickListener(){
        public void onClick(View arg0) {
          Intent i = new Intent(MainMenu.this, GameActivity.class);
          startActivityForResult(i, 0x1337);          
        }
      });
      
      Button quitButton = (Button)this.findViewById(R.id.quitButton);
      quitButton.setOnClickListener(new OnClickListener(){
        public void onClick(View arg0) {
          MainMenu.this.finish();  
        }
      });
  }
}
